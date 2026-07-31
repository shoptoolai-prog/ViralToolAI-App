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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ToolStatus {
    AVAILABLE,
    NEW,
    OFFLINE,
    UPDATE
}

data class UtilityToolItem(
    val id: String,
    val shortTitle: String,
    val category: String, // "AI & Prompts", "Media & Video", "Social Tools", "Utilities"
    val externalUrl: String,
    val keywords: List<String>,
    val icon: ImageVector,
    val accentColor: Color,
    val gradientColors: List<Color>,
    val status: ToolStatus = ToolStatus.AVAILABLE,
    val isFuturePlaceholder: Boolean = false
)

private val GoldPrimary = Color(0xFFFFD700)

/**
 * TOOLS PAGE COMPLETE REDESIGN — PHASE 3
 * Creator Utility Hub: Fast access to trusted creator utilities.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoEditingScreen(
    onNavigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // State
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var showCategoryFilterMenu by remember { mutableStateOf(false) }
    var showFutureTools by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }

    // External Website Confirmation Dialog State
    var pendingToolToOpen by remember { mutableStateOf<UtilityToolItem?>(null) }
    var dontShowAgainThisSession by rememberSaveable { mutableStateOf(false) }
    var rememberChoiceChecked by remember { mutableStateOf(false) }

    // Entrance Animation
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        )
        delay(300)
        isLoading = false
    }

    // Refresh simulation
    fun triggerRefresh() {
        scope.launch {
            isRefreshing = true
            isLoading = true
            delay(400)
            isLoading = false
            isRefreshing = false
            Toast.makeText(context, "Utility Hub Refreshed", Toast.LENGTH_SHORT).show()
        }
    }

    // List of Utility Tools
    val allTools = remember {
        listOf(
            // PRIMARY QUICK UTILITIES
            UtilityToolItem(
                id = "tool_prompt_hero",
                shortTitle = "PromptHero",
                category = "AI & Prompts",
                externalUrl = "https://prompthero.com",
                keywords = listOf("prompt", "prompthero", "ai", "chat", "gpt", "images", "text"),
                icon = Icons.Default.AutoAwesome,
                accentColor = Color(0xFF00F2FE),
                gradientColors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)),
                status = ToolStatus.AVAILABLE
            ),
            UtilityToolItem(
                id = "tool_remove_bg",
                shortTitle = "Remove BG",
                category = "Media & Video",
                externalUrl = "https://www.remove.bg",
                keywords = listOf("background", "remove", "bg", "transparent", "image", "photo", "cutout"),
                icon = Icons.Default.ContentCut,
                accentColor = EmeraldGlow,
                gradientColors = listOf(EmeraldPrimary, EmeraldGlow),
                status = ToolStatus.AVAILABLE
            ),
            UtilityToolItem(
                id = "tool_instagram",
                shortTitle = "Instagram Tools",
                category = "Social Tools",
                externalUrl = "https://www.instagram.com",
                keywords = listOf("instagram", "ig", "reels", "social", "media", "downloader", "post"),
                icon = Icons.Default.CameraAlt,
                accentColor = Color(0xFFE1306C),
                gradientColors = listOf(Color(0xFF833AB4), Color(0xFFE1306C), Color(0xFFFD1D1D)),
                status = ToolStatus.AVAILABLE
            ),
            UtilityToolItem(
                id = "tool_youtube",
                shortTitle = "YouTube Tools",
                category = "Social Tools",
                externalUrl = "https://studio.youtube.com",
                keywords = listOf("youtube", "yt", "shorts", "downloader", "studio", "video"),
                icon = Icons.Default.PlayCircle,
                accentColor = Color(0xFFFF0000),
                gradientColors = listOf(Color(0xFFFF0000), Color(0xFFB71C1C)),
                status = ToolStatus.AVAILABLE
            ),
            UtilityToolItem(
                id = "tool_gemini",
                shortTitle = "Gemini",
                category = "AI & Prompts",
                externalUrl = "https://gemini.google.com",
                keywords = listOf("gemini", "ai", "google", "chat", "prompt", "writing"),
                icon = Icons.Default.Psychology,
                accentColor = ElectricPurple,
                gradientColors = listOf(ElectricPurple, Color(0xFF8B5CF6)),
                status = ToolStatus.AVAILABLE
            ),
            UtilityToolItem(
                id = "tool_video_enhancer",
                shortTitle = "Video Enhancer",
                category = "Media & Video",
                externalUrl = "https://viesus.com",
                keywords = listOf("video", "enhancer", "hd", "4k", "upscale", "quality", "editing"),
                icon = Icons.Default.HighQuality,
                accentColor = GoldPrimary,
                gradientColors = listOf(GoldPrimary, Color(0xFFFF8C00)),
                status = ToolStatus.NEW
            ),

            // FUTURE-READY PLACEHOLDERS
            UtilityToolItem(
                id = "tool_ai_upscaler",
                shortTitle = "AI Upscaler",
                category = "Utilities",
                externalUrl = "https://viesus.com",
                keywords = listOf("upscaler", "ai", "image", "hd", "resolution"),
                icon = Icons.Default.Transform,
                accentColor = Color(0xFF38BDF8),
                gradientColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7)),
                status = ToolStatus.UPDATE,
                isFuturePlaceholder = true
            ),
            UtilityToolItem(
                id = "tool_ai_audio_cleaner",
                shortTitle = "AI Audio Cleaner",
                category = "Utilities",
                externalUrl = "https://podcast.adobe.com/enhance",
                keywords = listOf("audio", "cleaner", "noise", "voice", "sound", "podcast"),
                icon = Icons.Default.GraphicEq,
                accentColor = Color(0xFFA855F7),
                gradientColors = listOf(Color(0xFFA855F7), Color(0xFF7E22CE)),
                status = ToolStatus.UPDATE,
                isFuturePlaceholder = true
            ),
            UtilityToolItem(
                id = "tool_image_compressor",
                shortTitle = "Image Compressor",
                category = "Utilities",
                externalUrl = "https://tinypng.com",
                keywords = listOf("image", "compressor", "compress", "photo", "size", "jpg", "png"),
                icon = Icons.Default.Compress,
                accentColor = Color(0xFF34D399),
                gradientColors = listOf(Color(0xFF34D399), Color(0xFF059669)),
                status = ToolStatus.AVAILABLE,
                isFuturePlaceholder = true
            ),
            UtilityToolItem(
                id = "tool_video_compressor",
                shortTitle = "Video Compressor",
                category = "Utilities",
                externalUrl = "https://freeconvert.com/video-compressor",
                keywords = listOf("video", "compressor", "compress", "mp4", "size"),
                icon = Icons.Default.VideoSettings,
                accentColor = Color(0xFFF43F5E),
                gradientColors = listOf(Color(0xFFF43F5E), Color(0xFFE11D48)),
                status = ToolStatus.AVAILABLE,
                isFuturePlaceholder = true
            ),
            UtilityToolItem(
                id = "tool_png_converter",
                shortTitle = "PNG Converter",
                category = "Utilities",
                externalUrl = "https://cloudconvert.com/png-converter",
                keywords = listOf("png", "converter", "convert", "format", "image"),
                icon = Icons.Default.Image,
                accentColor = Color(0xFFF59E0B),
                gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFD97706)),
                status = ToolStatus.AVAILABLE,
                isFuturePlaceholder = true
            ),
            UtilityToolItem(
                id = "tool_pdf_tools",
                shortTitle = "PDF Tools",
                category = "Utilities",
                externalUrl = "https://ilovepdf.com",
                keywords = listOf("pdf", "tools", "merge", "convert", "document"),
                icon = Icons.Default.PictureAsPdf,
                accentColor = Color(0xFFEF4444),
                gradientColors = listOf(Color(0xFFEF4444), Color(0xFFB91C1C)),
                status = ToolStatus.AVAILABLE,
                isFuturePlaceholder = true
            )
        )
    }

    // Filter tools based on Search Query, Category, and Future Placeholders Rule
    val queryTrimmed = searchQuery.trim().lowercase()
    val filteredTools = remember(queryTrimmed, selectedCategory, showFutureTools, allTools) {
        allTools.filter { tool ->
            // Hide future placeholders unless searched, or future tools toggle is active, or category is Utilities
            val matchesFutureRule = !tool.isFuturePlaceholder || showFutureTools || queryTrimmed.isNotEmpty() || selectedCategory == "Utilities"

            val matchesCategory = selectedCategory == "All" || tool.category.equals(selectedCategory, ignoreCase = true)

            val matchesSearch = if (queryTrimmed.isEmpty()) {
                true
            } else {
                tool.shortTitle.lowercase().contains(queryTrimmed) ||
                tool.category.lowercase().contains(queryTrimmed) ||
                tool.keywords.any { it.contains(queryTrimmed) }
            }

            matchesFutureRule && matchesCategory && matchesSearch
        }
    }

    // Function to safely launch external URL
    fun openExternalTool(tool: UtilityToolItem) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tool.externalUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open browser for ${tool.shortTitle}", Toast.LENGTH_SHORT).show()
        }
    }

    // Function called when a tool card is clicked
    fun onToolClick(tool: UtilityToolItem) {
        if (dontShowAgainThisSession) {
            openExternalTool(tool)
        } else {
            rememberChoiceChecked = false
            pendingToolToOpen = tool
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val gridColumns = if (maxWidth > 600.dp) 3 else 2

            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
            ) {
                // ==================================================
                // HEADER SECTION (Spans full width)
                // ==================================================
                item(span = { GridItemSpan(gridColumns) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                alpha = animProgress.value
                                translationY = (1f - animProgress.value) * 20f
                            }
                    ) {
                        // Header Top Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(ElectricPurple.copy(alpha = 0.3f), EmeraldGlow.copy(alpha = 0.15f))
                                            )
                                        )
                                        .border(BorderStroke(1.2.dp, ElectricPurple.copy(alpha = 0.6f)), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Build,
                                        contentDescription = "Creator Utility Hub",
                                        tint = ElectricPurple,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Creator Utility Hub",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextWhite,
                                        letterSpacing = (-0.3).sp
                                    )
                                    Text(
                                        text = "Fast access to trusted utilities",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextGray
                                    )
                                }
                            }

                            // Header Actions (Filter & Refresh)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Category Filter Button
                                Box {
                                    IconButton(
                                        onClick = { showCategoryFilterMenu = true },
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (selectedCategory != "All") ElectricPurple.copy(alpha = 0.25f) else Color(0xFF181D2B)
                                            )
                                            .border(
                                                BorderStroke(
                                                    1.dp,
                                                    if (selectedCategory != "All") ElectricPurple else Color.White.copy(alpha = 0.12f)
                                                ),
                                                CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FilterList,
                                            contentDescription = "Filter",
                                            tint = if (selectedCategory != "All") ElectricPurple else TextWhite,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showCategoryFilterMenu,
                                        onDismissRequest = { showCategoryFilterMenu = false },
                                        modifier = Modifier.background(Color(0xFF161B26))
                                    ) {
                                        listOf("All", "AI & Prompts", "Media & Video", "Social Tools", "Utilities").forEach { category ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = category,
                                                        color = if (selectedCategory == category) EmeraldGlow else TextWhite,
                                                        fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                },
                                                onClick = {
                                                    selectedCategory = category
                                                    showCategoryFilterMenu = false
                                                }
                                            )
                                        }
                                    }
                                }

                                // Refresh Button
                                IconButton(
                                    onClick = { triggerRefresh() },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF181D2B))
                                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        tint = EmeraldGlow,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Search Bar
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = ElectricPurple),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF141824),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Tools",
                                    tint = ElectricPurple,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = {
                                        Text(
                                            text = "Search Prompt, Remove BG, Gemini, Video...",
                                            fontSize = 13.sp,
                                            color = TextGray
                                        )
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite,
                                        cursorColor = ElectricPurple
                                    ),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                                    modifier = Modifier.weight(1f)
                                )
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { searchQuery = "" },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = TextGray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Category Filter Chips Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val categories = listOf("All", "AI & Prompts", "Media & Video", "Social Tools")
                            categories.forEach { cat ->
                                val isSelected = selectedCategory == cat
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCategory = cat },
                                    label = {
                                        Text(
                                            text = cat,
                                            fontSize = 11.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ElectricPurple.copy(alpha = 0.25f),
                                        selectedLabelColor = TextWhite,
                                        containerColor = Color(0xFF141824),
                                        labelColor = TextGray
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        selectedBorderColor = ElectricPurple,
                                        borderColor = Color.White.copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // ==================================================
                // LOADING / SKELETON STATE
                // ==================================================
                if (isLoading) {
                    items(6) {
                        SkeletonToolCard()
                    }
                } else if (filteredTools.isEmpty()) {
                    // ==================================================
                    // EMPTY STATE (Spans full width)
                    // ==================================================
                    item(span = { GridItemSpan(gridColumns) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(ElectricPurple.copy(alpha = 0.15f))
                                        .border(BorderStroke(1.dp, ElectricPurple.copy(alpha = 0.4f)), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SearchOff,
                                        contentDescription = "No tools",
                                        tint = ElectricPurple,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "No tools found.",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Try searching for 'Prompt', 'Remove BG', 'Instagram', or 'Gemini'.",
                                    fontSize = 12.sp,
                                    color = TextGray,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Button(
                                    onClick = {
                                        searchQuery = ""
                                        selectedCategory = "All"
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Reset Filters", fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                            }
                        }
                    }
                } else {
                    // ==================================================
                    // TOOL CARDS GRID
                    // ==================================================
                    items(filteredTools, key = { it.id }) { tool ->
                        ToolGridCard(
                            tool = tool,
                            onClick = { onToolClick(tool) }
                        )
                    }

                    // Optional Future Tools Toggle Card
                    if (!showFutureTools && queryTrimmed.isEmpty() && selectedCategory == "All") {
                        item(span = { GridItemSpan(gridColumns) }) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { showFutureTools = true },
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF121622),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddCircleOutline,
                                            contentDescription = "More Tools",
                                            tint = EmeraldGlow,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Show More Utilities (Upscaler, PDF, Compressors)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expand",
                                        tint = TextGray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==================================================
        // EXTERNAL WEBSITE CONFIRMATION DIALOG
        // ==================================================
        pendingToolToOpen?.let { tool ->
            Dialog(onDismissRequest = { pendingToolToOpen = null }) {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = Color(0xFF161B26),
                    border = BorderStroke(1.5.dp, ElectricPurple.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(tool.accentColor.copy(alpha = 0.2f))
                                .border(BorderStroke(1.dp, tool.accentColor.copy(alpha = 0.6f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "External Link",
                                tint = tool.accentColor,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Leaving ViralToolAi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You are about to open an external website:\n${tool.externalUrl}",
                            fontSize = 12.5.sp,
                            color = TextGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Session Preference Checkbox
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { rememberChoiceChecked = !rememberChoiceChecked }
                                .padding(4.dp)
                        ) {
                            Checkbox(
                                checked = rememberChoiceChecked,
                                onCheckedChange = { rememberChoiceChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = ElectricPurple,
                                    uncheckedColor = TextGray
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Remember choice for current session",
                                fontSize = 11.5.sp,
                                color = TextWhite.copy(alpha = 0.9f)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { pendingToolToOpen = null },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel", color = TextWhite, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    if (rememberChoiceChecked) {
                                        dontShowAgainThisSession = true
                                    }
                                    openExternalTool(tool)
                                    pendingToolToOpen = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Continue", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Large Square Glass Tool Card
 */
@Composable
private fun ToolGridCard(
    tool: UtilityToolItem,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "cardScale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(10.dp, shape = RoundedCornerShape(20.dp), spotColor = tool.accentColor)
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = tool.accentColor)
            ) {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF141824),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                listOf(
                    tool.accentColor.copy(alpha = 0.6f),
                    Color.White.copy(alpha = 0.1f)
                )
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            // Status Chip (Top End) if relevant
            if (tool.status != ToolStatus.AVAILABLE) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(6.dp),
                    color = when (tool.status) {
                        ToolStatus.NEW -> EmeraldGlow.copy(alpha = 0.25f)
                        ToolStatus.UPDATE -> ElectricPurple.copy(alpha = 0.25f)
                        ToolStatus.OFFLINE -> Color.Red.copy(alpha = 0.25f)
                        else -> tool.accentColor.copy(alpha = 0.2f)
                    },
                    border = BorderStroke(
                        0.8.dp,
                        when (tool.status) {
                            ToolStatus.NEW -> EmeraldGlow
                            ToolStatus.UPDATE -> ElectricPurple
                            ToolStatus.OFFLINE -> Color.Red
                            else -> tool.accentColor
                        }
                    )
                ) {
                    Text(
                        text = tool.status.name,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Centered Large Icon & Short Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(tool.gradientColors)
                        )
                        .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = tool.accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = tool.icon,
                        contentDescription = tool.shortTitle,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = tool.shortTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Skeleton Loader Card
 */
@Composable
private fun SkeletonToolCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF141824).copy(alpha = alphaAnim),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {}
}
