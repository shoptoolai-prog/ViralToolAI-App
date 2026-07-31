package com.example.ui.components

import android.content.Context
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.ViralMemoryEngine
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 16 — Welcome Back Experience Dialog
 * Premium Apple-style Glass Dialog shown when returning to ViralToolAi
 * displaying last completed lesson & today's next lesson.
 */
@Composable
fun WelcomeBackDialog(
    onContinue: () -> Unit,
    onStartFresh: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val lastCompleted = remember { ViralMemoryEngine.getLastCompletedLesson(context) }
    val nextLesson = remember { ViralMemoryEngine.getNextLesson(context) }
    var rememberChoice by remember { mutableStateOf(ViralMemoryEngine.isRememberWelcomeChoice(context)) }

    val continueInteraction = remember { MutableInteractionSource() }
    val isContinuePressed by continueInteraction.collectIsPressedAsState()
    val continueScale by animateFloatAsState(
        targetValue = if (isContinuePressed) 0.95f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "continueScale"
    )

    val freshInteraction = remember { MutableInteractionSource() }
    val isFreshPressed by freshInteraction.collectIsPressedAsState()
    val freshScale by animateFloatAsState(
        targetValue = if (isFreshPressed) 0.95f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "freshScale"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.80f))
                .clickable(onClick = onDismiss)
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF0F1A14),
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(EmeraldGlow, ElectricPurple.copy(alpha = 0.6f), EmeraldPrimary)
                    )
                ),
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.92f)
                    .shadow(24.dp, RoundedCornerShape(28.dp), spotColor = EmeraldGlow)
                    .clickable(enabled = false) {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Wave Icon
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0x2210B981))
                            .border(BorderStroke(1.2.dp, EmeraldPrimary), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👋",
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (lastCompleted != null) "Welcome Back" else "Welcome to Creator Academy",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (lastCompleted != null) "Continue your Creator Journey?" else "Start your first lesson",
                        fontSize = 13.sp,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Progress Overview Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x12FFFFFF))
                            .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (lastCompleted != null) {
                                // Completed Previously
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Completed",
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Previously completed:",
                                            fontSize = 11.sp,
                                            color = TextWhite.copy(alpha = 0.6f)
                                        )
                                        Text(
                                            text = lastCompleted,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                    }
                                }
                            } else {
                                // New Creator Status
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Status",
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Learning Status:",
                                            fontSize = 11.sp,
                                            color = TextWhite.copy(alpha = 0.6f)
                                        )
                                        Text(
                                            text = "Ready to start first lesson",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                    }
                                }
                            }

                            // Today's Lesson
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Next Lesson",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Column {
                                    Text(
                                        text = if (lastCompleted != null) "Today's lesson:" else "Your First Lesson:",
                                        fontSize = 11.sp,
                                        color = TextWhite.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = nextLesson,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Remember My Choice Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                rememberChoice = !rememberChoice
                                ViralMemoryEngine.setRememberWelcomeChoice(context, rememberChoice)
                            }
                            .padding(vertical = 4.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (rememberChoice) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                            contentDescription = "Remember choice",
                            tint = if (rememberChoice) EmeraldPrimary else TextWhite.copy(alpha = 0.5f),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Remember my choice",
                            fontSize = 12.sp,
                            color = TextWhite.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Start Fresh Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .graphicsLayer {
                                    scaleX = freshScale
                                    scaleY = freshScale
                                }
                                .clip(RoundedCornerShape(23.dp))
                                .background(Color(0x1AFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(23.dp))
                                .clickable(
                                    interactionSource = freshInteraction,
                                    indication = null,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onStartFresh()
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Start Fresh",
                                    tint = TextWhite.copy(alpha = 0.9f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Start Fresh",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite.copy(alpha = 0.9f)
                                )
                            }
                        }

                        // Continue Button
                        Box(
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp)
                                .graphicsLayer {
                                    scaleX = continueScale
                                    scaleY = continueScale
                                }
                                .clip(RoundedCornerShape(23.dp))
                                .background(EmeraldPrimary)
                                .clickable(
                                    interactionSource = continueInteraction,
                                    indication = null,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onContinue()
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Continue",
                                    tint = AmoledBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (lastCompleted != null) "Continue" else "Start First Lesson",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AmoledBlack
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
