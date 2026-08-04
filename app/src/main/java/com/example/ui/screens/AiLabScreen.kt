package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.MerchantDetector
import com.example.data.ShoppingItem
import com.example.ui.components.*
import com.example.ui.theme.*

enum class ToolStatus {
    AVAILABLE,
    NEW,
    OFFLINE,
    UPDATE
}

data class UtilityToolItem(
    val id: String,
    val shortTitle: String,
    val category: String,
    val externalUrl: String,
    val keywords: List<String>,
    val icon: ImageVector,
    val accentColor: Color,
    val gradientColors: List<Color>,
    val status: ToolStatus = ToolStatus.AVAILABLE,
    val isFuturePlaceholder: Boolean = false
)

/**
 * AI LAB SCREEN
 * Centralized hub for Creator AI Intelligence, Shopping Insights, Creator Tips,
 * Growth Challenges, Spotlight, What's New, and External Utility Tools.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiLabScreen(
    onNavigateToHistory: () -> Unit = {},
    onNavigateToAnalysis: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current
    val haptic = LocalHapticFeedback.current
    val responsiveMetrics = LocalResponsiveMetrics.current

    var linkInput by remember { mutableStateOf("") }
    var pendingToolToOpen by remember { mutableStateOf<UtilityToolItem?>(null) }
    var selectedCategory by remember { mutableStateOf("All") }

    // External Utility Tools
    val utilityTools = remember {
        listOf(
            UtilityToolItem(
                id = "tool_prompt_hero",
                shortTitle = "PromptHero",
                category = "AI & Prompts",
                externalUrl = "https://prompthero.com",
                keywords = listOf("prompt", "ai", "images"),
                icon = Icons.Default.AutoAwesome,
                accentColor = Color(0xFF00F2FE),
                gradientColors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))
            ),
            UtilityToolItem(
                id = "tool_remove_bg",
                shortTitle = "Remove BG",
                category = "Media & Video",
                externalUrl = "https://www.remove.bg",
                keywords = listOf("background", "remove", "transparent"),
                icon = Icons.Default.ContentCut,
                accentColor = EmeraldGlow,
                gradientColors = listOf(EmeraldPrimary, EmeraldGlow)
            ),
            UtilityToolItem(
                id = "tool_instagram",
                shortTitle = "Instagram Tools",
                category = "Social Tools",
                externalUrl = "https://www.instagram.com",
                keywords = listOf("instagram", "reels", "social"),
                icon = Icons.Default.CameraAlt,
                accentColor = Color(0xFFE1306C),
                gradientColors = listOf(Color(0xFF833AB4), Color(0xFFE1306C))
            ),
            UtilityToolItem(
                id = "tool_youtube",
                shortTitle = "YouTube Studio",
                category = "Social Tools",
                externalUrl = "https://studio.youtube.com",
                keywords = listOf("youtube", "shorts", "studio"),
                icon = Icons.Default.PlayCircle,
                accentColor = Color(0xFFFF0000),
                gradientColors = listOf(Color(0xFFFF0000), Color(0xFFB71C1C))
            ),
            UtilityToolItem(
                id = "tool_gemini",
                shortTitle = "Gemini AI",
                category = "AI & Prompts",
                externalUrl = "https://gemini.google.com",
                keywords = listOf("gemini", "ai", "chat"),
                icon = Icons.Default.Psychology,
                accentColor = ElectricPurple,
                gradientColors = listOf(ElectricPurple, Color(0xFF8B5CF6))
            ),
            UtilityToolItem(
                id = "tool_video_enhancer",
                shortTitle = "Video Enhancer",
                category = "Media & Video",
                externalUrl = "https://viesus.com",
                keywords = listOf("video", "enhancer", "upscale"),
                icon = Icons.Default.HighQuality,
                accentColor = Color(0xFFFFD700),
                gradientColors = listOf(Color(0xFFFFD700), Color(0xFFFF8C00))
            )
        )
    }

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
                .verticalScroll(scrollState)
                .padding(horizontal = responsiveMetrics.horizontalPadding)
                .padding(top = 12.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HEADER BAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF1B0B33), Color(0xFF0F0620))
                        )
                    )
                    .border(
                        BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(VioletPrimary, ElectricPurple))),
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
                            .background(Brush.linearGradient(listOf(ElectricPurple, VioletGlow))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = "AI Lab",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "AI Lab",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "Creator Intelligence & Utilities Hub",
                            fontSize = 11.5.sp,
                            color = VioletGlow,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // SHOPPING INSIGHTS & LINK ANALYZER
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF0C1424),
                border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(Color(0xFF2874F0), ElectricPurple)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Shopping Insights",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Shopping Insights & Product Research",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Paste any product link (Amazon, Flipkart, Meesho, Myntra) to analyze sentiment, price history & affiliate tips.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = linkInput,
                        onValueChange = { linkInput = it },
                        placeholder = { Text("Paste product link here...", fontSize = 13.sp, color = TextGray) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00E5FF),
                            unfocusedBorderColor = Color.White.copy(0.2f),
                            focusedContainerColor = Color(0xFF080D18),
                            unfocusedContainerColor = Color(0xFF080D18),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        trailingIcon = {
                            if (linkInput.isNotBlank()) {
                                IconButton(onClick = {
                                    onNavigateToAnalysis(linkInput.trim())
                                }) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = "Analyze", tint = Color(0xFF00E5FF))
                                }
                            } else {
                                IconButton(onClick = {
                                    val clip = clipboardManager.getText()?.text
                                    if (!clip.isNullOrBlank()) {
                                        linkInput = clip
                                    }
                                }) {
                                    Icon(Icons.Default.ContentPaste, contentDescription = "Paste", tint = Color.White.copy(0.6f))
                                }
                            }
                        }
                    )
                }
            }

            // CREATOR SPOTLIGHT
            CreatorSpotlightSection()

            // TODAY'S CREATOR TIPS
            TodaysCreatorTipsSection()

            // CONTENT INSPIRATION
            ContentInspirationSection()

            // WEEKLY GROWTH CHALLENGE
            WeeklyGrowthChallengeSection()

            // EXTERNAL UTILITY TOOLS GRID
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🛠️ External Creator Utilities",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    utilityTools.take(3).forEach { tool ->
                        Box(modifier = Modifier.weight(1f)) {
                            AiLabToolCard(tool = tool) {
                                pendingToolToOpen = tool
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    utilityTools.drop(3).take(3).forEach { tool ->
                        Box(modifier = Modifier.weight(1f)) {
                            AiLabToolCard(tool = tool) {
                                pendingToolToOpen = tool
                            }
                        }
                    }
                }
            }

            // TOP PROMOTERS (LEADERBOARD)
            TopPromotersSection()

            // WHAT'S NEW
            WhatsNewSection()
        }

        // CONFIRMATION DIALOG FOR EXTERNAL UTILITY TOOLS
        pendingToolToOpen?.let { tool ->
            Dialog(onDismissRequest = { pendingToolToOpen = null }) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161B26),
                    border = BorderStroke(1.dp, ElectricPurple),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "External Tool",
                            tint = tool.accentColor,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Open ${tool.shortTitle}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "You are opening ${tool.externalUrl} in your browser.",
                            fontSize = 12.sp,
                            color = TextGray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { pendingToolToOpen = null },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Cancel", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tool.externalUrl)).apply {
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
                                    }
                                    pendingToolToOpen = null
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Continue", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AiLabToolCard(
    tool: UtilityToolItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF121624),
        border = BorderStroke(1.dp, tool.accentColor.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(tool.gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = tool.shortTitle,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = tool.shortTitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
