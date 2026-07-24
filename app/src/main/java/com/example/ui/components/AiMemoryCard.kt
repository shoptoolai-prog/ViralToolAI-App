package com.example.ui.components

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.creatoracademy.CreatorSetupData
import com.example.creatoracademy.ViralLevel
import com.example.creatoracademy.ViralMemoryEngine
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 16 — AI Memory Card
 * Premium Apple Glass Card inside Creator Academy displaying:
 * Current Goal, Current Level, Current XP, Today's Task, Learning Streak,
 * Completed Lessons & Next Unlock.
 */
@Composable
fun AiMemoryCard(
    xpPoints: Int,
    streakDays: Int,
    setupData: CreatorSetupData,
    completedTasksCount: Int,
    onXpGained: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val viralLevel = remember(xpPoints) { ViralLevel.getLevelForXp(xpPoints) }
    var todayTask by remember { mutableStateOf(ViralMemoryEngine.getTodayDynamicTask(context)) }

    val taskInteraction = remember { MutableInteractionSource() }
    val isTaskPressed by taskInteraction.collectIsPressedAsState()
    val taskScale by animateFloatAsState(
        targetValue = if (isTaskPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "taskScale"
    )

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF0F141C),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                listOf(EmeraldPrimary.copy(alpha = 0.7f), Color(0x22FFFFFF))
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: AI Memory Badge & Daily Motivation Quote
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0x2210B981))
                            .border(BorderStroke(1.dp, EmeraldPrimary), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Memory Engine",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI MEMORY ENGINE™",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldPrimary,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1A10B981))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "$streakDays Day Streak",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Level & Goal Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Level ${viralLevel.levelNumber} • ${viralLevel.name}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Text(
                        text = "Goal: ${setupData.primaryGoal} (${setupData.niche})",
                        fontSize = 11.5.sp,
                        color = TextWhite.copy(alpha = 0.6f)
                    )
                }

                Text(
                    text = "$xpPoints / ${viralLevel.maxXp} XP",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // XP Progress Bar
            val progress = remember(xpPoints, viralLevel) {
                val currentInLevel = xpPoints - viralLevel.minXp
                val totalInLevel = (viralLevel.maxXp - viralLevel.minXp).coerceAtLeast(1)
                (currentInLevel.toFloat() / totalInLevel.toFloat()).coerceIn(0f, 1f)
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = EmeraldPrimary,
                trackColor = Color(0x33FFFFFF)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Today's Dynamic Task Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = taskScale
                        scaleY = taskScale
                    }
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x18FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
                    .clickable(
                        interactionSource = taskInteraction,
                        indication = null,
                        onClick = {
                            if (!todayTask.isCompleted) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                val success = ViralMemoryEngine.markDynamicTaskCompleted(context, todayTask.id)
                                if (success) {
                                    todayTask = todayTask.copy(isCompleted = true)
                                    onXpGained()
                                    Toast.makeText(context, "⚡ +50 XP Gained! Dynamic Task Completed!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TODAY'S TASK",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${todayTask.xpReward} XP",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = todayTask.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = todayTask.description,
                            fontSize = 11.sp,
                            color = TextWhite.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (todayTask.isCompleted) EmeraldPrimary else Color(0x22FFFFFF))
                            .border(
                                BorderStroke(1.dp, if (todayTask.isCompleted) EmeraldPrimary else Color(0x44FFFFFF)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (todayTask.isCompleted) Icons.Default.Check else Icons.Default.AutoAwesome,
                            contentDescription = "Task State",
                            tint = if (todayTask.isCompleted) AmoledBlack else TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer Stats Row: Completed Lessons & Next Unlock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Lessons",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$completedTasksCount Lessons Completed",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = "Unlock",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Next: Level ${viralLevel.levelNumber + 1} Tools",
                        fontSize = 11.sp,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
