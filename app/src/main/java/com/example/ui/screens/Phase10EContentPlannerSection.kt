package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ContentPlannerStorageManager
import com.example.data.ShoppingResult
import com.example.engine.AiContentPlannerEngine
import com.example.ui.theme.*

/**
 * SHOPTOOLAI Master Phase 10E — AI Content Planner Section
 * Features Content Planner Home, 30-Day Content Roadmap (4 Weeks),
 * Daily Content Ideas Swipe Cards (7 types), Reel Planner (Mon-Sun),
 * Content Balance Animated Meter (AI Estimate), Creator Goal Checklists,
 * Smart Reminders ("Coming Soon"), and Local Plan Saver.
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Phase10EContentPlannerSection(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(true) }

    val plannerPackage = remember(resultData) { AiContentPlannerEngine.generatePlannerPackage(resultData) }
    var isPlanSaved by remember(resultData.url) { mutableStateOf(ContentPlannerStorageManager.isPlanSaved(resultData.url)) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0x228E24AA),
                        Color(0x0DFFFFFF)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFAB47BC), Color(0x33FFFFFF)))
                ),
                RoundedCornerShape(24.dp)
            )
            .animateContentSize(animationSpec = tween(350, easing = EaseOutCubic))
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // HEADER BAR: AI CONTENT PLANNER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.radialGradient(listOf(Color(0xFF8E24AA), Color(0xFF4A148C))),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Planner AI",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "AI CONTENT PLANNER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFCE93D8),
                                letterSpacing = 1.2.sp
                            )
                            Box(
                                modifier = Modifier
                                    .background(Brush.horizontalGradient(listOf(Color(0xFF8E24AA), Color(0xFF4A148C))), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "30-DAY PLAN",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                            }
                        }
                        Text(
                            text = "Roadmap, Reel Schedule & Creator Goal Checklists",
                            fontSize = 11.sp,
                            color = TextGray
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = TextWhite,
                    modifier = Modifier.size(22.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {

                    HorizontalDivider(color = Color(0x15FFFFFF), thickness = 1.dp)

                    // 1. CONTENT BALANCE ESTIMATE
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.PieChart, contentDescription = null, tint = Color(0xFFCE93D8), modifier = Modifier.size(16.dp))
                                Text("CONTENT BALANCE RATIO", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("AI Estimate", fontSize = 8.sp, color = TextGray)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    BalancePill("Reviews", "${plannerPackage.contentBalance.reviewContentPercent}%", CrimsonRed)
                                    BalancePill("Education", "${plannerPackage.contentBalance.educationalPercent}%", Color(0xFF3498DB))
                                    BalancePill("Lifestyle", "${plannerPackage.contentBalance.lifestylePercent}%", Color(0xFF2ECC71))
                                    BalancePill("Entertain", "${plannerPackage.contentBalance.entertainmentPercent}%", Color(0xFFFFB74D))
                                }

                                Text(
                                    text = "💡 ${plannerPackage.contentBalance.note}",
                                    fontSize = 10.sp,
                                    color = TextGray,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    // 2. 30-DAY CONTENT ROADMAP (4 WEEKS)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Timeline, contentDescription = null, tint = Color(0xFFCE93D8), modifier = Modifier.size(16.dp))
                                Text("30-DAY CONTENT ROADMAP", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("AI Suggested Plan", fontSize = 8.sp, color = Color(0xFFCE93D8), fontWeight = FontWeight.Bold)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                plannerPackage.roadmapWeeks.forEachIndexed { idx, week ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(Color(0x22AB47BC))
                                                .border(BorderStroke(1.dp, Color(0xFFAB47BC)), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("W${week.weekNumber}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                        }

                                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(week.weekTitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                                Box(
                                                    modifier = Modifier
                                                        .background(Color(0x18FFFFFF), RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(week.focusArea, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = CrimsonLight)
                                                }
                                            }
                                            Text(week.keyDeliverable, fontSize = 10.sp, color = TextGray)
                                            Text("Format: ${week.primaryFormat}", fontSize = 9.sp, color = Color(0xFFCE93D8), fontWeight = FontWeight.SemiBold)
                                        }
                                    }

                                    if (idx < plannerPackage.roadmapWeeks.size - 1) {
                                        HorizontalDivider(color = Color(0x0CFFFFFF), thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }

                    // 3. DAILY CONTENT IDEAS (SWIPE CARDS)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFCE93D8), modifier = Modifier.size(16.dp))
                            Text("DAILY CONTENT IDEAS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(plannerPackage.dailyIdeas, key = { it.title }) { idea ->
                                var saved by remember(idea.title) { mutableStateOf(ContentPlannerStorageManager.isIdeaSaved(idea.title)) }

                                Box(
                                    modifier = Modifier
                                        .width(220.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0x12FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
                                        .padding(14.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .background(Color(0x228E24AA), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(idea.ideaType, fontSize = 8.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                            }

                                            IconButton(
                                                onClick = {
                                                    val nowSaved = ContentPlannerStorageManager.toggleSaveIdea(idea.title)
                                                    saved = nowSaved
                                                    if (nowSaved) onShowToast("✔ Idea Saved!") else onShowToast("Removed Idea")
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (saved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                                    contentDescription = null,
                                                    tint = if (saved) Color(0xFFCE93D8) else TextGray,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = idea.title,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            lineHeight = 15.sp
                                        )

                                        Text(
                                            text = "Hook: \"${idea.hookSuggestion}\"",
                                            fontSize = 10.sp,
                                            color = TextGray,
                                            lineHeight = 14.sp
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("Duration: ${idea.estimatedDuration}", fontSize = 9.sp, color = Color(0xFFCE93D8), fontWeight = FontWeight.Bold)
                                            Text(
                                                text = "Copy Idea",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite,
                                                modifier = Modifier.clickable {
                                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                    val clip = android.content.ClipData.newPlainText("Idea", "${idea.title}\nHook: ${idea.hookSuggestion}")
                                                    clipboard.setPrimaryClip(clip)
                                                    onShowToast("✔ Idea Copied to Clipboard!")
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 4. REEL PLANNER (WEEKLY SCHEDULE)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.VideoCameraBack, contentDescription = null, tint = Color(0xFFCE93D8), modifier = Modifier.size(16.dp))
                                Text("REEL PLANNER (7-DAY SCHEDULE)", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("AI Suggested Posting Plan", fontSize = 8.sp, color = TextGray)
                            }
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(plannerPackage.reelSchedule, key = { it.dayName }) { day ->
                                Box(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x12FFFFFF))
                                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                                    .padding(12.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(day.dayName, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFCE93D8))
                                            Text(day.bestPostingSlot, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                                        }
                                        Text(day.contentType, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(day.actionNote, fontSize = 9.sp, color = TextGray, lineHeight = 13.sp)
                                    }
                                }
                            }
                        }
                    }

                    // 5. CREATOR GOALS (INTERACTIVE CHECKLIST)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = Color(0xFFCE93D8), modifier = Modifier.size(16.dp))
                            Text("CREATOR WEEKLY GOALS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                plannerPackage.goals.forEach { goal ->
                                    var completed by remember(goal.id) { mutableStateOf(ContentPlannerStorageManager.isGoalCompleted(goal.id)) }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                val newStatus = ContentPlannerStorageManager.toggleGoal(goal.id)
                                                completed = newStatus
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Checkbox(
                                            checked = completed,
                                            onCheckedChange = {
                                                val newStatus = ContentPlannerStorageManager.toggleGoal(goal.id)
                                                completed = newStatus
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Color(0xFFAB47BC),
                                                uncheckedColor = TextGray,
                                                checkmarkColor = TextWhite
                                            ),
                                            modifier = Modifier.size(20.dp)
                                        )

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = goal.title,
                                                fontSize = 11.sp,
                                                fontWeight = if (completed) FontWeight.Bold else FontWeight.Medium,
                                                color = if (completed) Color(0xFFCE93D8) else TextWhite
                                            )
                                            Text(goal.category, fontSize = 8.sp, color = TextGray)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 6. SMART REMINDERS (COMING SOON)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color(0xFFCE93D8), modifier = Modifier.size(16.dp))
                            Text("SMART REMINDERS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                plannerPackage.reminders.forEach { stub ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(stub.label, fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(stub.status, fontSize = 8.sp, color = TextGray, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 7. SAVE CONTENT PLAN BUTTON
                    Button(
                        onClick = {
                            ContentPlannerStorageManager.savePlan(
                                productUrl = resultData.url,
                                productName = resultData.productName,
                                category = plannerPackage.category,
                                weeklyFocus = plannerPackage.roadmapWeeks.firstOrNull()?.focusArea ?: "Reviews",
                                totalIdeasCount = plannerPackage.dailyIdeas.size
                            )
                            isPlanSaved = true
                            onShowToast("✔ Full 30-Day Content Plan Saved Locally!")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPlanSaved) Color(0xFF2ECC71) else Color(0xFF8E24AA)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isPlanSaved) Icons.Default.CheckCircle else Icons.Default.Save,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isPlanSaved) "CONTENT PLAN SAVED LOCALLY" else "SAVE 30-DAY CONTENT PLAN LOCALLY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BalancePill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Box(
            modifier = Modifier
                .background(color.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                .border(BorderStroke(1.dp, color), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Black, color = color)
        }
        Text(label, fontSize = 8.sp, color = TextGray, fontWeight = FontWeight.Bold)
    }
}
