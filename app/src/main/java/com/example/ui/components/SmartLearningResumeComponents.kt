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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * Universal Smart Welcome Back Dialog for All Learning Courses
 */
@Composable
fun SmartWelcomeBackDialog(
    courseTitle: String,
    currentStep: Int,
    totalSteps: Int,
    lastLessonName: String = "Lesson $currentStep of $totalSteps",
    onContinue: () -> Unit,
    onRestart: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val progressPercent = ((currentStep.toFloat() / totalSteps.coerceAtLeast(1).toFloat()) * 100).toInt().coerceIn(0, 100)

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
                .background(Color.Black.copy(alpha = 0.82f))
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
                    // Header Emoji Icon
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
                        text = "Welcome Back 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = courseTitle,
                        fontSize = 14.sp,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Lesson Progress Glass Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0x18FFFFFF))
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "You're currently on",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "$progressPercent% Complete",
                                    fontSize = 12.sp,
                                    color = EmeraldGlow,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "Lesson $currentStep of $totalSteps",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )

                            // Progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0x33FFFFFF))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(fraction = (currentStep.toFloat() / totalSteps.coerceAtLeast(1).toFloat()).coerceIn(0.05f, 1f))
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(EmeraldPrimary, EmeraldGlow, ElectricPurple)
                                            )
                                        )
                                )
                            }

                            Text(
                                text = "Would you like to continue where you left off?",
                                fontSize = 12.sp,
                                color = TextWhite.copy(alpha = 0.8f),
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Buttons Row
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Continue Learning Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .graphicsLayer {
                                    scaleX = continueScale
                                    scaleY = continueScale
                                }
                                .clip(RoundedCornerShape(25.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(EmeraldPrimary, EmeraldGlow)
                                    )
                                )
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
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Continue Learning",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AmoledBlack
                                )
                            }
                        }

                        // Restart Course Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
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
                                        onRestart()
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
                                    contentDescription = "Restart Course",
                                    tint = TextWhite.copy(alpha = 0.9f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Restart Course",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Universal Restart Course Confirmation Dialog
 */
@Composable
fun RestartCourseConfirmDialog(
    courseTitle: String,
    onConfirmRestart: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF140D0D),
                border = BorderStroke(1.2.dp, Color(0xFFEF4444).copy(alpha = 0.6f)),
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth(0.92f)
                    .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFFEF4444))
                    .clickable(enabled = false) {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0x22EF4444))
                            .border(BorderStroke(1.dp, Color(0xFFEF4444)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Restart",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Restart this course?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your saved progress for \"$courseTitle\" will be removed and you will start again from Lesson 1.",
                        fontSize = 13.sp,
                        color = TextWhite.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Cancel
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0x1AFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(22.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        // Restart
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFFEF4444))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onConfirmRestart()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Restart",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Embedded Learning Progress Card for Course Screens
 */
@Composable
fun LearningProgressIndicatorCard(
    currentStep: Int,
    totalSteps: Int,
    stepTitle: String = "Lesson $currentStep of $totalSteps",
    onResetClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val progressFraction = (currentStep.toFloat() / totalSteps.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val progressPercent = (progressFraction * 100).toInt()

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0x180F2015),
        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f)),
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = EmeraldGlow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                        contentDescription = "Progress",
                        tint = EmeraldGlow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Learning Progress",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite.copy(alpha = 0.9f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$progressPercent% Complete",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldGlow
                    )

                    if (onResetClick != null) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x22FFFFFF))
                                .clickable { onResetClick() }
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset Learning",
                                tint = TextWhite.copy(alpha = 0.8f),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Reset",
                                fontSize = 10.sp,
                                color = TextWhite.copy(alpha = 0.8f),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Custom Styled Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0x33FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(fraction = progressFraction.coerceAtLeast(0.04f))
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(EmeraldPrimary, EmeraldGlow)
                            )
                        )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current: $stepTitle",
                    fontSize = 11.5.sp,
                    color = TextWhite.copy(alpha = 0.75f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Universal Course Completed Dialog
 */
@Composable
fun CourseCompletedDialog(
    courseTitle: String,
    onReviewAgain: () -> Unit,
    onResetCourse: () -> Unit,
    onNextCourse: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = Color(0xFF0D1B12),
                border = BorderStroke(1.5.dp, EmeraldGlow),
                modifier = Modifier
                    .widthIn(max = 450.dp)
                    .fillMaxWidth(0.92f)
                    .shadow(24.dp, RoundedCornerShape(26.dp), spotColor = EmeraldGlow)
                    .clickable(enabled = false) {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0x2210B981))
                            .border(BorderStroke(1.2.dp, EmeraldPrimary), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Completed",
                            tint = EmeraldGlow,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "🎉 Congratulations!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "You have completed $courseTitle!",
                        fontSize = 14.sp,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Review Again
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(23.dp))
                                .background(EmeraldPrimary)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onReviewAgain()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Review Course",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = AmoledBlack
                            )
                        }

                        // Reset Course
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0x1AFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(22.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onResetCourse()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Reset Course",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        // Go to Next Course
                        TextButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNextCourse()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Done / Next Course",
                                fontSize = 13.sp,
                                color = TextWhite.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}
