package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.theme.*

data class ProjectDraftItem(
    val id: String,
    val title: String,
    val type: String, // "Video Edit", "Photo Edit", "Thumbnail"
    val lastModified: String,
    val durationOrSize: String,
    val progressPercent: Int
)

/**
 * PROJECTS SCREEN
 * Manages Editing Progress, Saved Projects, Drafts, and Resume Course Progress.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToAcademy: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val responsiveMetrics = LocalResponsiveMetrics.current

    var selectedTab by remember { mutableStateOf("Drafts") }

    // Saved drafts list state
    var draftsList by remember {
        mutableStateOf(
            listOf(
                ProjectDraftItem("d1", "Instagram Reel Hook #1", "Video Edit", "2 mins ago", "0:30", 75),
                ProjectDraftItem("d2", "Product Review Video", "Video Edit", "1 hour ago", "1:15", 40),
                ProjectDraftItem("d3", "YouTube Cover Art", "Thumbnail", "Yesterday", "1080p", 90)
            )
        )
    }

    var savedProjectsList by remember {
        mutableStateOf(
            listOf(
                ProjectDraftItem("s1", "Fashion Lookbook Final", "Exported MP4", "3 days ago", "1080p • 60fps", 100),
                ProjectDraftItem("s2", "Tech Review Reel", "Exported MP4", "5 days ago", "4K • 30fps", 100)
            )
        )
    }

    // Academy progress
    val currentAcademyLevel = remember { CreatorAcademyPrefs.getWishlinkStepIndex(context) }
    val isAcademyCompleted = remember { CreatorAcademyPrefs.isWishlinkLevel15Completed(context) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = responsiveMetrics.cardMaxWidth)
                .statusBarsPadding()
                .padding(horizontal = responsiveMetrics.horizontalPadding)
                .padding(top = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF170E2E), Color(0xFF0C071C))
                        )
                    )
                    .border(
                        BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(ElectricPurple, VioletGlow))),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(ElectricPurple, EmeraldGlow))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderSpecial,
                            contentDescription = "Projects",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Projects & Workspace",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "Editing Progress, Drafts & Learning Status",
                            fontSize = 11.5.sp,
                            color = EmeraldGlow,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CATEGORY TABS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val tabs = listOf("Drafts", "Saved", "Learning")
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(ElectricPurple, EmeraldPrimary))
                                else Brush.horizontalGradient(listOf(Color(0xFF121622), Color(0xFF121622)))
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldGlow else Color.White.copy(0.1f)
                                ),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CONTENT BASED ON TAB
            when (selectedTab) {
                "Drafts" -> {
                    if (draftsList.isEmpty()) {
                        EmptyProjectsView(
                            title = "No recent drafts.",
                            subtitle = "Start a new project from Home to begin editing."
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f)
                        ) {
                            items(draftsList, key = { it.id }) { item ->
                                ProjectItemCard(
                                    item = item,
                                    actionText = "Continue Editing",
                                    actionIcon = Icons.Default.Edit,
                                    onAction = {
                                        Toast.makeText(context, "Resuming ${item.title}", Toast.LENGTH_SHORT).show()
                                        onNavigateToHome()
                                    },
                                    onDelete = {
                                        draftsList = draftsList.filterNot { it.id == item.id }
                                    }
                                )
                            }
                        }
                    }
                }

                "Saved" -> {
                    if (savedProjectsList.isEmpty()) {
                        EmptyProjectsView(
                            title = "No saved projects.",
                            subtitle = "Completed exports will appear here."
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f)
                        ) {
                            items(savedProjectsList, key = { it.id }) { item ->
                                ProjectItemCard(
                                    item = item,
                                    actionText = "Open / Share",
                                    actionIcon = Icons.Default.Share,
                                    onAction = {
                                        Toast.makeText(context, "Opening ${item.title}", Toast.LENGTH_SHORT).show()
                                    },
                                    onDelete = {
                                        savedProjectsList = savedProjectsList.filterNot { it.id == item.id }
                                    }
                                )
                            }
                        }
                    }
                }

                "Learning" -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF0F1524),
                            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = "Learning",
                                        tint = EmeraldGlow,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Creator Learning Hub Progress",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = if (isAcademyCompleted) "Level 15 Completed (100%)" else "Currently on Level $currentAcademyLevel / 15",
                                            fontSize = 12.sp,
                                            color = EmeraldGlow
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                val progressFraction = if (isAcademyCompleted) 1.0f else (currentAcademyLevel / 15f)
                                LinearProgressIndicator(
                                    progress = { progressFraction },
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                    color = EmeraldPrimary,
                                    trackColor = Color.White.copy(0.1f)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = { onNavigateToAcademy() },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = "Resume", tint = Color.White)
                                        Text("Resume Creator Academy", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectItemCard(
    item: ProjectDraftItem,
    actionText: String,
    actionIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onAction: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF121624),
        border = BorderStroke(1.dp, Color.White.copy(0.12f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(ElectricPurple, VioletGlow))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.type.contains("Video")) Icons.Default.Videocam else Icons.Default.Image,
                    contentDescription = item.type,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.type} • ${item.durationOrSize} • ${item.lastModified}",
                    fontSize = 11.5.sp,
                    color = TextGray
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Red.copy(0.7f))
            }

            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(actionIcon, contentDescription = actionText, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun EmptyProjectsView(
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.FolderOpen, contentDescription = "Empty", tint = TextGray, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 12.sp, color = TextGray, textAlign = TextAlign.Center)
        }
    }
}
