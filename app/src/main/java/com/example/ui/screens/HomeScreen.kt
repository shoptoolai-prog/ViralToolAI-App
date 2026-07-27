package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.ShoppingItem
import coil.compose.SubcomposeAsyncImage
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.example.data.MerchantDetector
import com.example.data.MerchantRegistry
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cloud.LiveCloudManager
import com.example.cloud.ToolLockedDialog
import com.example.cloud.ToolStatus
import com.example.data.MerchantInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.scale
import com.example.vision.*

// Top-level thread-safe persistent maps for Master Phase 2B Single Scan Engine
private val sessionCache = androidx.compose.runtime.mutableStateMapOf<String, Boolean>()
private val activeProgressCache = androidx.compose.runtime.mutableStateMapOf<String, Int>()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    historyList: List<ShoppingItem>,
    onAddHistoryItem: (ShoppingItem) -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToAnalysis: (String) -> Unit,
    onNavigateToCreatorAcademy: (() -> Unit)? = null,
    initialSharedUrl: String? = null
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    
    var linkInput by remember { mutableStateOf(initialSharedUrl ?: "") }

    LaunchedEffect(initialSharedUrl) {
        if (!initialSharedUrl.isNullOrBlank()) {
            linkInput = initialSharedUrl
        }
    }
    var showSuccessDialog by remember { mutableStateOf<ShoppingItem?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var showInstagramBottomSheet by remember { mutableStateOf(false) }
    var showCreatorProfileBottomSheet by remember { mutableStateOf(false) }
    var showCreatorProfileScreen by remember { mutableStateOf(false) }
    var invalidUrlPopupResult by remember { mutableStateOf<com.example.data.ShoppingValidationResult?>(null) }
    var selectedPremiumTool by remember { mutableStateOf<com.example.ui.components.PremiumToolData?>(null) }
    var showBrandCollabDialog by remember { mutableStateOf(false) }
    var showInstaAutoDmDialog by remember { mutableStateOf(false) }
    var showMeeshoCreatorDialog by remember { mutableStateOf(false) }
    var showSmartRedirectionDialog by remember { mutableStateOf(false) }
    var lockedToolInfo by remember { mutableStateOf<Pair<String, ToolStatus>?>(null) }

    fun checkAndLaunchTool(toolKey: String, toolName: String, onLaunch: () -> Unit) {
        val status = LiveCloudManager.getToolStatus(toolKey)
        if (status == ToolStatus.ENABLED) {
            LiveCloudManager.logToolOpen(toolKey, toolName)
            onLaunch()
        } else {
            lockedToolInfo = Pair(toolName, status)
        }
    }
    
    var isGalleryImageSelected by remember { mutableStateOf(false) }
    var isCameraImageCaptured by remember { mutableStateOf(false) }
    var showVisionScanDialog by remember { mutableStateOf<VisionSource?>(null) }
    
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2000)
            toastMessage = null
        }
    }
    
    var isInputFocused by remember { mutableStateOf(false) }
    
    val placeholders = remember {
        listOf(
            "Paste any official shopping product link...",
            "Paste Amazon, Flipkart, Meesho, Myntra or AJIO product link...",
            "Paste Nykaa, Snitch, Nike, Apple or Croma product link..."
        )
    }
    var placeholderIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            placeholderIndex = (placeholderIndex + 1) % placeholders.size
        }
    }

    // Screen Layout
    if (showCreatorProfileScreen) {
        CreatorProfileAiScreen(
            onBackClick = { showCreatorProfileScreen = false }
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==================================================
            // FLAGSHIP HERO BANNER (APP HEADER PREMIUM REDESIGN V1)
            // ==================================================
            // One-time entrance animation sequence (Logo scale -> pulse -> title fade -> tagline fade)
            val headerAnimProgress = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                headerAnimProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing)
                )
            }

            val animVal = headerAnimProgress.value
            val logoScale = 0.75f + (0.25f * (animVal / 0.4f).coerceIn(0f, 1f))
            val logoPulseGlow = if (animVal in 0.3f..0.7f) ((1f - Math.abs(animVal - 0.5f) / 0.2f) * 0.5f) else 0f
            val titleAlpha = ((animVal - 0.2f) / 0.4f).coerceIn(0f, 1f)
            val taglineAlpha = ((animVal - 0.4f) / 0.4f).coerceIn(0f, 1f)

            // Continuous subtle micro-animations for 60 FPS polish
            val headerInfiniteTransition = rememberInfiniteTransition(label = "headerMicroAnims")
            val logoBreathingAlpha by headerInfiniteTransition.animateFloat(
                initialValue = 0.35f,
                targetValue = 0.85f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "logoBreathingAlpha"
            )
            val headerShimmerOffset by headerInfiniteTransition.animateFloat(
                initialValue = -300f,
                targetValue = 900f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3800, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "headerShimmerOffset"
            )
            val logoFloatY by headerInfiniteTransition.animateFloat(
                initialValue = -1.5f,
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2600, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "logoFloatY"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
                    .shadow(
                        elevation = 14.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = EmeraldPrimary.copy(alpha = 0.3f),
                        ambientColor = Color.Black
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF131A16), Color(0xFF0A0F0D))
                        )
                    )
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    EmeraldPrimary.copy(alpha = logoBreathingAlpha),
                                    ElectricPurple.copy(alpha = 0.45f),
                                    EmeraldGlow.copy(alpha = logoBreathingAlpha)
                                ),
                                start = Offset(headerShimmerOffset, 0f),
                                end = Offset(headerShimmerOffset + 400f, 250f)
                            )
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                // Glass shimmer sweep line across container
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    val sweepX = headerShimmerOffset
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.03f),
                                Color.White.copy(alpha = 0.12f),
                                Color.White.copy(alpha = 0.03f),
                                Color.Transparent
                            )
                        ),
                        start = Offset(sweepX, 0f),
                        end = Offset(sweepX + 180f, size.height),
                        strokeWidth = 30.dp.toPx()
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // APP LOGO (EXACT OFFICIAL LAUNCHER ICON LOGO)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.graphicsLayer {
                                translationY = logoFloatY.dp.toPx()
                            }
                        ) {
                            // Soft Outer Glow Ring
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                EmeraldPrimary.copy(alpha = (logoBreathingAlpha * 0.4f) + logoPulseGlow),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            // Official Icon Container Box
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .graphicsLayer {
                                        scaleX = logoScale
                                        scaleY = logoScale
                                    }
                                    .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = EmeraldPrimary)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF1B2820), Color(0xFF0D1610))
                                        )
                                    )
                                    .border(
                                        BorderStroke(
                                            1.2.dp,
                                            Brush.linearGradient(
                                                listOf(
                                                    EmeraldPrimary.copy(alpha = logoBreathingAlpha),
                                                    EmeraldGlow
                                                )
                                            )
                                        ),
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.foundation.Image(
                                    painter = painterResource(id = com.example.R.drawable.ic_viraltool_icon),
                                    contentDescription = "ViralToolAI Logo",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // APP TITLE & TAGLINE
                        Column {
                            Text(
                                text = "ViralToolAI",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                style = androidx.compose.ui.text.TextStyle(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            TextWhite,
                                            Color(0xFFE2F3EB),
                                            EmeraldGlow
                                        )
                                    )
                                ),
                                letterSpacing = (-0.3).sp,
                                modifier = Modifier.graphicsLayer {
                                    alpha = titleAlpha
                                }
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = com.example.core.TaglineEngine.getTagline(com.example.core.AppModule.SHOPPING),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldPrimary,
                                letterSpacing = 1.6.sp,
                                modifier = Modifier.graphicsLayer {
                                    alpha = taglineAlpha
                                }
                            )
                        }
                    }

                    // CREATED BY ASIT BADGE (POLISHED GLASS PILL)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        EmeraldPrimary.copy(alpha = 0.18f),
                                        Color(0x1AFFFFFF)
                                    )
                                )
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    Brush.horizontalGradient(
                                        listOf(
                                            EmeraldPrimary.copy(alpha = logoBreathingAlpha),
                                            EmeraldGlow.copy(alpha = 0.6f),
                                            Color.White.copy(alpha = 0.2f)
                                        )
                                    )
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Created by Asit",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite.copy(alpha = 0.95f),
                            letterSpacing = 0.6.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // ==================================================
            // FLIPKART SHOPPING ANALYZER (HERO GLASS CARD)
            // ==================================================
            val flipkartShineTransition = rememberInfiniteTransition(label = "flipkartShine")
            val flipkartShineOffset by flipkartShineTransition.animateFloat(
                initialValue = -0.3f,
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "flipkartShineOffset"
            )
            val flipkartGlowPulse by flipkartShineTransition.animateFloat(
                initialValue = 0.45f,
                targetValue = 0.95f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "flipkartGlowPulse"
            )

            val flipkartCardShape = RoundedCornerShape(22.dp)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 16.dp,
                        shape = flipkartCardShape,
                        ambientColor = Color(0xFF2874F0),
                        spotColor = Color(0xFFFFE11B)
                    ),
                shape = flipkartCardShape,
                color = Color(0xFF091424),
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2874F0).copy(alpha = flipkartGlowPulse),
                            Color(0xFFFFE11B).copy(alpha = flipkartGlowPulse),
                            Color(0xFF2874F0).copy(alpha = flipkartGlowPulse)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(800f * flipkartShineOffset, 800f * flipkartShineOffset)
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF0E1F38),
                                    Color(0xFF07101C)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header inside card with Official Flipkart Logo, Title, Subtitle & Badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                OfficialLogo(
                                    name = "flipkart",
                                    modifier = Modifier
                                        .size(28.dp)
                                        .shadow(6.dp, CircleShape, spotColor = Color(0xFFFFE11B))
                                )
                                Column {
                                    Text(
                                        text = "Flipkart Shopping Analyzer",
                                        fontSize = 16.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        letterSpacing = 0.3.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Analyze Flipkart products with AI-powered insights, price details and smart shopping reports.",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFB8D3F8),
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF2874F0), Color(0xFFFFE11B))
                                        )
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "FLIPKART AI",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AmoledBlack,
                                    letterSpacing = 0.6.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Link Input Box Polish
                        val inputGlowElevation by animateDpAsState(
                            targetValue = if (isInputFocused) 10.dp else 2.dp,
                            label = "InputGlow"
                        )
                        
                        OutlinedTextField(
                            value = linkInput,
                            onValueChange = { linkInput = it },
                            placeholder = {
                                Text(
                                    text = "Paste Flipkart Product URL...",
                                    color = Color.White.copy(alpha = 0.45f),
                                    fontSize = 13.5.sp
                                )
                            },
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFE11B),
                                unfocusedBorderColor = Color(0xFF2874F0).copy(alpha = 0.4f),
                                focusedLabelColor = Color(0xFFFFE11B),
                                cursorColor = Color(0xFFFFE11B),
                                focusedContainerColor = Color(0xFF081220),
                                unfocusedContainerColor = Color(0xFF0B172B)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .onFocusChanged { isInputFocused = it.isFocused }
                                .shadow(
                                    elevation = inputGlowElevation,
                                    shape = RoundedCornerShape(16.dp),
                                    clip = false,
                                    ambientColor = Color(0xFF2874F0),
                                    spotColor = Color(0xFFFFE11B)
                                ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = "Link Icon",
                                    tint = if (isInputFocused || linkInput.isNotEmpty()) Color(0xFFFFE11B) else Color(0xFF2874F0),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    if (linkInput.isNotEmpty()) {
                                        IconButton(
                                            onClick = { linkInput = "" },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear",
                                                tint = TextGray,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else {
                                        // Quick Paste Button inside text field
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(
                                                    Brush.horizontalGradient(
                                                        listOf(Color(0xFF2874F0), Color(0xFF1B53B4))
                                                    )
                                                )
                                                .clickable {
                                                    val clipText = clipboardManager.getText()?.text
                                                    if (!clipText.isNullOrBlank()) {
                                                        linkInput = clipText
                                                        val detectedName = detectMerchant(clipText).name
                                                        toastMessage = "✔ $detectedName Link Pasted"
                                                    } else {
                                                        toastMessage = "Clipboard is empty"
                                                    }
                                                }
                                                .padding(horizontal = 9.dp, vertical = 5.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentPaste,
                                                    contentDescription = "Paste",
                                                    tint = Color(0xFFFFE11B),
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = "Paste",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        )

                        AnimatedVisibility(
                            visible = isGalleryImageSelected || isCameraImageCaptured,
                            enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                            exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                        ) {
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0x1A2C2C2C), RoundedCornerShape(12.dp))
                                        .border(BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.3f)), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isGalleryImageSelected) Icons.Default.PhotoLibrary else Icons.Default.PhotoCamera,
                                        contentDescription = "Visual Input",
                                        tint = CrimsonLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isGalleryImageSelected) "Gallery Image Loaded" else "Camera Image Captured",
                                            color = TextWhite,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            isGalleryImageSelected = false
                                            isCameraImageCaptured = false
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove",
                                            tint = TextGray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (linkInput.isNotBlank()) {
                            val merchant = remember(linkInput) { detectMerchant(linkInput) }
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(animationSpec = tween(350)) + expandVertically(animationSpec = tween(350)),
                                exit = fadeOut(animationSpec = tween(250)) + shrinkVertically(animationSpec = tween(250))
                            ) {
                                Column {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    MiniatureMerchantCard(merchant = merchant)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val isInputProvided = linkInput.isNotBlank()

                        // Flagship Compact CTA Button (Perplexity / AI Search style)
                        val haptic = LocalHapticFeedback.current
                        var ctaState by remember { mutableStateOf("IDLE") } // "IDLE", "ANALYZING"
                        val analyzeInteractionSource = remember { MutableInteractionSource() }
                        val isAnalyzePressed by analyzeInteractionSource.collectIsPressedAsState()
                        
                        val analyzeScale by animateFloatAsState(
                            targetValue = if (isAnalyzePressed) 0.96f else 1.0f,
                            animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                            label = "AnalyzeScaleSpring"
                        )

                        val buttonBrush = if (isInputProvided) {
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF2874F0), Color(0xFF1A52B7), Color(0xFF0F3B8C))
                            )
                        } else {
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF2874F0).copy(alpha = 0.35f), Color(0xFFFFE11B).copy(alpha = 0.25f))
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .graphicsLayer {
                                    scaleX = analyzeScale
                                    scaleY = analyzeScale
                                }
                                .shadow(
                                    elevation = if (isInputProvided) 12.dp else 0.dp,
                                    shape = RoundedCornerShape(14.dp),
                                    clip = false,
                                    ambientColor = Color(0xFF2874F0),
                                    spotColor = Color(0xFFFFE11B)
                                )
                                .background(buttonBrush, RoundedCornerShape(14.dp))
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isInputProvided) Color(0xFFFFE11B) else Color(0x22FFFFFF)
                                    ),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable(
                                    enabled = isInputProvided && ctaState == "IDLE",
                                    interactionSource = analyzeInteractionSource,
                                    indication = if (isInputProvided) androidx.compose.foundation.LocalIndication.current else null
                                ) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    coroutineScope.launch {
                                        val targetInput = linkInput.trim()
                                        val lowerInput = targetInput.lowercase()

                                        val isInstagramProfile = lowerInput.contains("instagram.com/") && 
                                            (lowerInput.contains("/profile") || (!lowerInput.contains("/p/") && !lowerInput.contains("/reel/"))) ||
                                            (lowerInput.startsWith("@") && lowerInput.length > 1)

                                        if (isInstagramProfile) {
                                            showCreatorProfileScreen = true
                                            ctaState = "IDLE"
                                        } else if (isGalleryImageSelected) {
                                            showVisionScanDialog = VisionSource.GALLERY
                                            ctaState = "IDLE"
                                        } else if (isCameraImageCaptured) {
                                            showVisionScanDialog = VisionSource.CAMERA
                                            ctaState = "IDLE"
                                        } else {
                                            val validation = com.example.data.ShoppingUrlValidator.validate(targetInput)
                                            if (!validation.isValid) {
                                                invalidUrlPopupResult = validation
                                                ctaState = "IDLE"
                                            } else {
                                                ctaState = "ANALYZING"
                                                focusManager.clearFocus()
                                                onNavigateToAnalysis(targetInput)
                                                ctaState = "IDLE"
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                if (ctaState == "ANALYZING") {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = Color(0xFFFFE11B),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Launching Flipkart AI Analyzer...",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "AI Analyze",
                                        tint = if (isInputProvided) Color(0xFFFFE11B) else TextGray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isInputProvided) "Analyze Flipkart Link" else "Paste Flipkart Link to Analyze",
                                        color = if (isInputProvided) Color.White else TextGray,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==================================================
            // 2. ⭐ PREMIUM CREATOR & AFFILIATE TOOLS MARKETPLACE
            // ==================================================
            com.example.ui.components.PremiumCreatorToolsSection(
                onToolSelected = { tool ->
                    if (tool.id == "brand_collab_ai") {
                        checkAndLaunchTool("tool_brand_collaboration", "Brand Collaboration AI") {
                            showBrandCollabDialog = true
                        }
                    } else {
                        checkAndLaunchTool(tool.id, tool.title) {
                            selectedPremiumTool = tool
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==================================================
            // 3. MEESHO CREATOR AI CARD
            // ==================================================
            com.example.ui.components.MeeshoCreatorAiCard(
                onComingSoonClick = {
                    checkAndLaunchTool("tool_shopping_ai", "Meesho Creator AI") {
                        showMeeshoCreatorDialog = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==================================================
            // 4. INSTAGRAM SHOPPING AI
            // ==================================================
            PremiumFeatureComingSoonCard(
                onComingSoonClick = {
                    checkAndLaunchTool("tool_instagram", "Instagram Shopping AI") {
                        showInstagramBottomSheet = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==================================================
            // 5. INSTA AUTO DM AI CARD
            // ==================================================
            com.example.ui.components.InstaAutoDmAiCard(
                onComingSoonClick = {
                    checkAndLaunchTool("tool_instagram", "Insta Auto DM AI") {
                        showInstaAutoDmDialog = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. Sliding Premium Bottom Sheet for Instagram Shopping AI
        AnimatedVisibility(
            visible = showInstagramBottomSheet,
            enter = fadeIn(animationSpec = tween(250)),
            exit = fadeOut(animationSpec = tween(250))
        ) {
            val sheetScrollState = rememberScrollState()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xB3000000))
                    .clickable { showInstagramBottomSheet = false }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                        .background(
                            color = Color(0xF90A0A0A),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .border(
                            BorderStroke(1.2.dp, Color(0x33FF2E44)),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp)
                        .clickable(enabled = false) { }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(sheetScrollState)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(44.dp)
                                .height(4.dp)
                                .background(Color(0x33FFFFFF), RoundedCornerShape(2.dp))
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF833AB4),
                                            Color(0xFFFD1D1D),
                                            Color(0xFFFCB045)
                                        )
                                    ),
                                    CircleShape
                                )
                                .shadow(elevation = 12.dp, shape = CircleShape, spotColor = Color(0xFFFD1D1D)),
                            contentAlignment = Alignment.Center
                        ) {
                            OfficialLogo(name = "instagram", modifier = Modifier.size(36.dp))
                        }
                        
                        Spacer(modifier = Modifier.height(18.dp))
                        
                        Text(
                            text = "Instagram Shopping AI",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "UNDER DEVELOPMENT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonLight,
                            letterSpacing = 1.5.sp
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Instagram Shopping AI is currently under development.\n\nIt will be available in a future update with AI Reel Analysis, automatic product detection and instant shopping comparison.\n\nThank you for your patience.",
                            fontSize = 13.sp,
                            color = TextGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(28.dp))
                        
                        val sheetBtnInteractionSource = remember { MutableInteractionSource() }
                        val isSheetBtnPressed by sheetBtnInteractionSource.collectIsPressedAsState()
                        val sheetBtnScale by animateFloatAsState(
                            targetValue = if (isSheetBtnPressed) 0.95f else 1.0f,
                            label = "SheetBtnScale"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .graphicsLayer {
                                    scaleX = sheetBtnScale
                                    scaleY = sheetBtnScale
                                }
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    clip = false,
                                    ambientColor = CrimsonRed,
                                    spotColor = CrimsonLight
                                )
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(CrimsonRed, Color(0xFF990000))
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(12.dp))
                                .clickable(
                                    interactionSource = sheetBtnInteractionSource,
                                    indication = androidx.compose.foundation.LocalIndication.current
                                ) {
                                    showInstagramBottomSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Got it",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        // 5. Creator Profile AI Flagship LIVE Overlay Screen (Phase 6D)
        AnimatedVisibility(
            visible = showCreatorProfileScreen,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.zIndex(200f)
        ) {
            CreatorProfileAiScreen(
                onBackClick = { showCreatorProfileScreen = false }
            )
        }
        AnimatedVisibility(
            visible = showCreatorProfileBottomSheet,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.zIndex(100f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.82f))
                    .clickable { showCreatorProfileBottomSheet = false },
                contentAlignment = Alignment.BottomCenter
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable(enabled = false) {},
                    borderColor = Color(0xFFFFD700).copy(alpha = 0.4f),
                    backgroundColor = Color(0xFF140D2B)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header Orb
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF8E2DE2),
                                            Color(0xFFFF007F),
                                            Color(0xFFFFD700)
                                        )
                                    )
                                )
                                .shadow(elevation = 16.dp, shape = CircleShape, spotColor = Color(0xFFFF007F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Creator Profile AI",
                                tint = TextWhite,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Creator Profile AI",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x33FFD700))
                                .border(BorderStroke(1.dp, Color(0xFFFFD700)), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "⭐ FLAGSHIP AI • UNDER DEVELOPMENT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFD700),
                                letterSpacing = 1.2.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Features List in Sheet
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x18FFFFFF))
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CreatorSheetFeatureRow("📷", "Upload Profile Screenshot", "Just upload your Instagram profile screenshot.")
                            CreatorSheetFeatureRow("🧠", "Complete AI Review", "Bio, username, content style & layout analysis.")
                            CreatorSheetFeatureRow("🚀", "Growth & Hashtag Engine", "Trending hashtags, best posting time & caption ideas.")
                            CreatorSheetFeatureRow("🤝", "Brand Collaboration Score", "Evaluate your readiness for brand deals.")
                            CreatorSheetFeatureRow("💬", "Simple Hinglish Reports", "Clear, actionable advice with zero jargon.")
                            CreatorSheetFeatureRow("🔒", "100% Privacy First", "No login, no password. Analyzes only your screenshots.")
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        val creatorSheetBtnSource = remember { MutableInteractionSource() }
                        val isCreatorSheetBtnPressed by creatorSheetBtnSource.collectIsPressedAsState()
                        val creatorSheetBtnScale by animateFloatAsState(
                            targetValue = if (isCreatorSheetBtnPressed) 0.95f else 1.0f,
                            label = "CreatorSheetBtnScale"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .graphicsLayer {
                                    scaleX = creatorSheetBtnScale
                                    scaleY = creatorSheetBtnScale
                                }
                                .shadow(elevation = 12.dp, shape = RoundedCornerShape(25.dp), spotColor = Color(0xFFFF007F))
                                .clip(RoundedCornerShape(25.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF8E2DE2),
                                            Color(0xFFFF007F)
                                        )
                                    )
                                )
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)), RoundedCornerShape(25.dp))
                                .clickable(
                                    interactionSource = creatorSheetBtnSource,
                                    indication = androidx.compose.foundation.LocalIndication.current
                                ) {
                                    showCreatorProfileBottomSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Got it",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

    // ==================================================
    // POPUPS AND DIALOGS (100% Stable, No Fake Behavior)
    // ==================================================
    
    // AI Vision Scanner Dialog (Phase 5D)
    showVisionScanDialog?.let { source ->
        VisionScanDialog(
            source = source,
            imageUriOrPath = "sample_product.jpg",
            onDismiss = { showVisionScanDialog = null },
            onScanComplete = { targetLink ->
                showVisionScanDialog = null
                onNavigateToAnalysis(targetLink)
            }
        )
    }

    // Invalid Link Popup
    invalidUrlPopupResult?.let { result ->
        PremiumInvalidLinkPopup(
            result = result,
            onDismiss = { invalidUrlPopupResult = null }
        )
    }

    // Common Premium Tool Popup
    selectedPremiumTool?.let { tool ->
        if (tool.id == "brand_collab_ai") {
            com.example.creatoracademy.BrandCollaborationAiDialog(
                onDismiss = { selectedPremiumTool = null }
            )
        } else {
            com.example.ui.components.CommonPremiumToolPopupDialog(
                tool = tool,
                onDismiss = { selectedPremiumTool = null }
            )
        }
    }

    // Brand Collaboration AI Mentor Dialog (MASTER PHASE V3)
    if (showBrandCollabDialog) {
        com.example.creatoracademy.BrandCollaborationAiDialog(
            onDismiss = { showBrandCollabDialog = false }
        )
    }

    // Insta Auto DM AI Dialog (MASTER PHASE 15E)
    if (showInstaAutoDmDialog) {
        com.example.ui.components.InstaAutoDmAiDialog(
            onDismiss = { showInstaAutoDmDialog = false }
        )
    }

    // Meesho Creator AI Dialog (MEESHO CREATOR AI V3 - ZERO TO HERO)
    if (showMeeshoCreatorDialog) {
        com.example.ui.components.MeeshoCreatorAiDialog(
            onDismiss = { showMeeshoCreatorDialog = false },
            onNavigateToBrandCollab = {
                showMeeshoCreatorDialog = false
                showBrandCollabDialog = true
            }
        )
    }

    // Tool Locked Dialog
    lockedToolInfo?.let { (toolName, status) ->
        ToolLockedDialog(
            toolName = toolName,
            status = status,
            onDismiss = { lockedToolInfo = null }
        )
    }

    // Smart Workspace Redirection Dialog (MASTER PHASE 15F)
    if (showSmartRedirectionDialog) {
        com.example.ui.components.SmartRedirectionDialog(
            onDismiss = { showSmartRedirectionDialog = false },
            onSwitchWorkspace = {
                showSmartRedirectionDialog = false
                onNavigateToCreatorAcademy?.invoke()
            }
        )
    }
    
    // 1. Success Dialog (After analyzing a link)
    showSuccessDialog?.let { item ->
        PremiumDialog(
            title = "Analysis Success",
            onDismiss = { showSuccessDialog = null }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "LINK SUCCESSFULLY PROCESSED",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = CrimsonRed,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Details Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x1F2C2C2C), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Source:", color = TextGray, fontSize = 12.sp, modifier = Modifier.width(60.dp))
                            Text(item.platform, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("URL:", color = TextGray, fontSize = 12.sp, modifier = Modifier.width(60.dp))
                            Text(
                                item.url, 
                                color = TextWhite, 
                                fontSize = 11.sp, 
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("Date:", color = TextGray, fontSize = 12.sp, modifier = Modifier.width(60.dp))
                            Text(item.timestamp, color = TextWhite, fontSize = 11.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { showSuccessDialog = null },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Dismiss", fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            showSuccessDialog = null
                            onNavigateToHistory()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Text("View History", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Premium Animated Slide Toast (Left-to-Right, Glassmorphic, Crimson blur halo, Auto-disappears in 2s)
    AnimatedVisibility(
        visible = toastMessage != null,
        enter = fadeIn(animationSpec = tween(350)) + slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(350, easing = EaseOutCubic)),
        exit = fadeOut(animationSpec = tween(250)) + slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(250, easing = EaseInCubic)),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(horizontal = 24.dp).padding(bottom = 96.dp) // Float above bottom bar with healthy padding
            .zIndex(100f)
    ) {
        toastMessage?.let { msg ->
            val isSuccess = msg.contains("✔") || msg.lowercase().contains("success") || msg.lowercase().contains("complete") || msg.lowercase().contains("detected")
            val isWarning = msg.lowercase().contains("empty") || msg.lowercase().contains("unsupported") || msg.lowercase().contains("fail") || msg.lowercase().contains("error") || msg.lowercase().contains("please")
            
            val icon = when {
                isSuccess -> Icons.Default.CheckCircle
                isWarning -> Icons.Default.Warning
                else -> Icons.Default.Info
            }
            
            val iconColor = when {
                isSuccess -> Color(0xFF00FFCC) // Glowing High-Contrast Mint Green
                isWarning -> Color(0xFFFFCC00) // Glowing Warm Amber/Yellow
                else -> CrimsonLight // Brands Crimson Red
            }

            // Beautiful Glassmorphic Toast Container
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(20.dp),
                        clip = false,
                        ambientColor = CrimsonRed.copy(alpha = 0.5f),
                        spotColor = CrimsonRed
                    )
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xDC121215), Color(0xC808080A))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.horizontalGradient(
                                colors = listOf(
                                    CrimsonRed.copy(alpha = 0.65f),
                                    Color(0x1F9C27B0),
                                    Color(0x0F2196F3)
                                )
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Status Icon",
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Text(
                        text = msg,
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }
    }
}
}

// Reusable Premium Dialog Composable
@Composable
fun PremiumDialog(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .background(AmoledBlack)
                .border(BorderStroke(1.2.dp, Color(0x33FF2E44)), RoundedCornerShape(22.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                content()
            }
        }
    }
}

// ==================================================
// OFFICIAL WEBSITE LOGO DRAWERS (Bespoke Vector Art)
// ==================================================
@Composable
fun OfficialLogo(name: String, modifier: Modifier = Modifier) {
    val cleanName = name.trim().lowercase()
    val merchant = remember(name) { detectMerchant(name) }
    
    val knownCanvasBrands = listOf("amazon", "flipkart", "meesho", "myntra", "ajio", "nykaa", "snapdeal", "tatacliq", "jiomart", "croma", "reliancedigital", "reliance", "firstcry", "nike", "adidas", "puma", "apple", "samsung", "zara", "hm", "snitch", "allensolly", "vijaysales", "instagram", "google", "youtube", "capcut", "vn", "snapchat", "telegram", "whatsapp", "chrome", "chatgpt", "gemini")
    val hasCanvas = knownCanvasBrands.any { cleanName.contains(it) }
    
    if (hasCanvas) {
        Canvas(modifier = modifier) {
            val width = size.width
            val height = size.height
            val center = androidx.compose.ui.geometry.Offset(width / 2f, height / 2f)
            val radius = width / 2f
            
            when {
                cleanName.contains("youtube") -> {
                    drawCircle(color = Color(0xFFFF0000), radius = radius)
                    val playPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.40f, height * 0.32f)
                        lineTo(width * 0.68f, height * 0.50f)
                        lineTo(width * 0.40f, height * 0.68f)
                        close()
                    }
                    drawPath(path = playPath, color = Color.White)
                }
                cleanName.contains("instagram") -> {
                    drawCircle(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045))
                        ),
                        radius = radius
                    )
                    drawRoundRect(
                        color = Color.White,
                        topLeft = androidx.compose.ui.geometry.Offset(width * 0.3f, height * 0.3f),
                        size = androidx.compose.ui.geometry.Size(width * 0.4f, height * 0.4f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                    drawCircle(
                        color = Color.White,
                        radius = width * 0.1f,
                        center = center,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                    drawCircle(
                        color = Color.White,
                        radius = width * 0.025f,
                        center = androidx.compose.ui.geometry.Offset(width * 0.62f, height * 0.38f)
                    )
                }
                cleanName.contains("capcut") -> {
                    drawCircle(color = Color(0xFF111111), radius = radius)
                    val leftPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.28f, height * 0.28f)
                        lineTo(width * 0.48f, height * 0.50f)
                        lineTo(width * 0.28f, height * 0.72f)
                    }
                    drawPath(
                        path = leftPath,
                        color = Color.White,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx())
                    )
                    val rightPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.72f, height * 0.28f)
                        lineTo(width * 0.52f, height * 0.50f)
                        lineTo(width * 0.72f, height * 0.72f)
                    }
                    drawPath(
                        path = rightPath,
                        color = Color.White,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx())
                    )
                    drawLine(
                        color = Color.White,
                        start = androidx.compose.ui.geometry.Offset(width * 0.38f, height * 0.50f),
                        end = androidx.compose.ui.geometry.Offset(width * 0.62f, height * 0.50f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
                cleanName.contains("vn") -> {
                    drawCircle(color = Color(0xFF00B2FF), radius = radius)
                    val vPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.25f, height * 0.32f)
                        lineTo(width * 0.42f, height * 0.68f)
                        lineTo(width * 0.55f, height * 0.32f)
                    }
                    drawPath(
                        path = vPath,
                        color = Color.White,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx())
                    )
                    val nPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.60f, height * 0.68f)
                        lineTo(width * 0.60f, height * 0.32f)
                        lineTo(width * 0.78f, height * 0.68f)
                        lineTo(width * 0.78f, height * 0.32f)
                    }
                    drawPath(
                        path = nPath,
                        color = Color.White,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx())
                    )
                }
                cleanName.contains("snapchat") -> {
                    drawCircle(color = Color(0xFFFFFC00), radius = radius)
                    drawCircle(
                        color = Color.Black,
                        radius = radius * 0.45f,
                        center = center,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                }
                cleanName.contains("telegram") -> {
                    drawCircle(color = Color(0xFF229ED9), radius = radius)
                    val paperPlane = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.25f, height * 0.50f)
                        lineTo(width * 0.75f, height * 0.28f)
                        lineTo(width * 0.60f, height * 0.72f)
                        lineTo(width * 0.48f, height * 0.58f)
                        close()
                    }
                    drawPath(path = paperPlane, color = Color.White)
                }
                cleanName.contains("whatsapp") -> {
                    drawCircle(color = Color(0xFF25D366), radius = radius)
                    drawCircle(
                        color = Color.White,
                        radius = radius * 0.45f,
                        center = center,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx())
                    )
                }
                cleanName.contains("chrome") -> {
                    drawCircle(color = Color(0xFF4285F4), radius = radius)
                    drawCircle(color = Color.White, radius = radius * 0.45f, center = center)
                    drawCircle(color = Color(0xFF34A853), radius = radius * 0.25f, center = center)
                }
                cleanName.contains("chatgpt") -> {
                    drawCircle(color = Color(0xFF10A37F), radius = radius)
                    drawCircle(
                        color = Color.White,
                        radius = radius * 0.45f,
                        center = center,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                }
                cleanName.contains("gemini") -> {
                    drawCircle(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF1E88E5), Color(0xFF8E24AA), Color(0xFF00ACC1))
                        ),
                        radius = radius
                    )
                    val starPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.5f, height * 0.25f)
                        quadraticTo(width * 0.5f, height * 0.5f, width * 0.75f, height * 0.5f)
                        quadraticTo(width * 0.5f, height * 0.5f, width * 0.5f, height * 0.75f)
                        quadraticTo(width * 0.5f, height * 0.5f, width * 0.25f, height * 0.5f)
                        quadraticTo(width * 0.5f, height * 0.5f, width * 0.5f, height * 0.25f)
                    }
                    drawPath(path = starPath, color = Color.White)
                }
                cleanName.contains("google") -> {
                    drawCircle(color = Color(0xFF4285F4), radius = radius)
                    drawCircle(
                        color = Color.White,
                        radius = radius * 0.45f,
                        center = center,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                    drawLine(
                        color = Color.White,
                        start = center,
                        end = androidx.compose.ui.geometry.Offset(width * 0.72f, height * 0.5f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
                cleanName.contains("amazon") -> {
                    drawCircle(color = Color(0xFF131921), radius = radius)
                    val smilePath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.25f, height * 0.55f)
                        quadraticTo(
                            width * 0.5f, height * 0.75f,
                            width * 0.75f, height * 0.55f
                        )
                    }
                    drawPath(
                        path = smilePath,
                        color = Color(0xFFFF9900),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 2.5.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    )
                    val arrowPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.71f, height * 0.52f)
                        lineTo(width * 0.82f, height * 0.51f)
                        lineTo(width * 0.78f, height * 0.62f)
                        close()
                    }
                    drawPath(path = arrowPath, color = Color(0xFFFF9900))
                }
                cleanName.contains("flipkart") -> {
                    drawCircle(color = Color(0xFFF9D80B), radius = radius)
                    val bagColor = Color(0xFF2874F0)
                    val bagPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.35f, height * 0.4f)
                        lineTo(width * 0.65f, height * 0.4f)
                        lineTo(width * 0.7f, height * 0.75f)
                        lineTo(width * 0.3f, height * 0.75f)
                        close()
                    }
                    drawPath(path = bagPath, color = bagColor)
                    
                    val handlePath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.42f, height * 0.4f)
                        quadraticTo(width * 0.5f, height * 0.22f, width * 0.58f, height * 0.4f)
                    }
                    drawPath(
                        path = handlePath,
                        color = bagColor,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 2.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    )
                    drawCircle(color = Color.White, radius = 2.dp.toPx(), center = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.58f))
                }
                cleanName.contains("nike") -> {
                    drawCircle(color = Color(0xFF111111), radius = radius)
                    val swoosh = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.2f, height * 0.55f)
                        quadraticTo(width * 0.45f, height * 0.68f, width * 0.8f, height * 0.3f)
                        quadraticTo(width * 0.5f, height * 0.65f, width * 0.28f, height * 0.58f)
                        close()
                    }
                    drawPath(path = swoosh, color = Color.White)
                }
                cleanName.contains("adidas") -> {
                    drawCircle(color = Color(0xFF000000), radius = radius)
                    val bar1 = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.28f, height * 0.68f)
                        lineTo(width * 0.38f, height * 0.55f)
                        lineTo(width * 0.45f, height * 0.55f)
                        lineTo(width * 0.35f, height * 0.68f)
                        close()
                    }
                    val bar2 = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.42f, height * 0.68f)
                        lineTo(width * 0.56f, height * 0.45f)
                        lineTo(width * 0.63f, height * 0.45f)
                        lineTo(width * 0.49f, height * 0.68f)
                        close()
                    }
                    val bar3 = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.56f, height * 0.68f)
                        lineTo(width * 0.74f, height * 0.35f)
                        lineTo(width * 0.81f, height * 0.35f)
                        lineTo(width * 0.63f, height * 0.68f)
                        close()
                    }
                    drawPath(path = bar1, color = Color.White)
                    drawPath(path = bar2, color = Color.White)
                    drawPath(path = bar3, color = Color.White)
                }
                cleanName.contains("puma") -> {
                    drawCircle(color = Color(0xFFBA0C2F), radius = radius)
                    drawCircle(color = Color.White, radius = radius * 0.45f, center = center)
                    drawCircle(color = Color(0xFFBA0C2F), radius = radius * 0.32f, center = center)
                }
                cleanName.contains("apple") -> {
                    drawCircle(color = Color(0xFF222222), radius = radius)
                    drawCircle(color = Color.White, radius = radius * 0.38f, center = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.54f))
                    drawCircle(color = Color.White, radius = radius * 0.12f, center = androidx.compose.ui.geometry.Offset(width * 0.55f, height * 0.32f))
                }
                cleanName.contains("samsung") -> {
                    drawCircle(color = Color(0xFF1428A0), radius = radius)
                    drawCircle(color = Color.White, radius = radius * 0.42f, center = center, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
                }
                cleanName.contains("snitch") -> {
                    drawCircle(color = Color(0xFF111111), radius = radius)
                    val lightning = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.55f, height * 0.25f)
                        lineTo(width * 0.38f, height * 0.52f)
                        lineTo(width * 0.52f, height * 0.52f)
                        lineTo(width * 0.45f, height * 0.75f)
                        lineTo(width * 0.62f, height * 0.48f)
                        lineTo(width * 0.48f, height * 0.48f)
                        close()
                    }
                    drawPath(path = lightning, color = Color(0xFFFFCC00))
                }
                cleanName.contains("meesho") -> {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFF43397), Color(0xFFFF6EAE))
                        ),
                        radius = radius
                    )
                    val heartPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.5f, height * 0.68f)
                        cubicTo(width * 0.2f, height * 0.45f, width * 0.25f, height * 0.28f, width * 0.5f, height * 0.42f)
                        cubicTo(width * 0.75f, height * 0.28f, width * 0.8f, height * 0.45f, width * 0.5f, height * 0.68f)
                    }
                    drawPath(path = heartPath, color = Color.White)
                }
                cleanName.contains("myntra") -> {
                    drawCircle(color = Color(0xFF1E1E1E), radius = radius)
                    val path1 = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.3f, height * 0.65f)
                        lineTo(width * 0.42f, height * 0.45f)
                        lineTo(width * 0.5f, height * 0.55f)
                        lineTo(width * 0.58f, height * 0.45f)
                        lineTo(width * 0.7f, height * 0.65f)
                        lineTo(width * 0.62f, height * 0.65f)
                        lineTo(width * 0.5f, height * 0.55f)
                        lineTo(width * 0.38f, height * 0.65f)
                        close()
                    }
                    drawPath(
                        path = path1,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFC2779), Color(0xFFFF527B), Color(0xFFFF9233))
                        )
                    )
                }
                cleanName.contains("ajio") -> {
                    drawCircle(color = Color(0xFF2C3E50), radius = radius)
                    drawRect(
                        color = Color.White,
                        topLeft = androidx.compose.ui.geometry.Offset(width * 0.35f, height * 0.42f),
                        size = androidx.compose.ui.geometry.Size(width * 0.3f, height * 0.3f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                    val handlePath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.42f, height * 0.42f)
                        quadraticTo(width * 0.5f, height * 0.28f, width * 0.58f, height * 0.42f)
                    }
                    drawPath(
                        path = handlePath,
                        color = Color.White,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                }
                cleanName.contains("nykaa") -> {
                    drawCircle(color = Color(0xFFFC2779), radius = radius)
                    val starPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.5f, height * 0.3f)
                        quadraticTo(width * 0.5f, height * 0.5f, width * 0.7f, height * 0.5f)
                        quadraticTo(width * 0.5f, height * 0.5f, width * 0.5f, height * 0.7f)
                        quadraticTo(width * 0.5f, height * 0.5f, width * 0.3f, height * 0.5f)
                        quadraticTo(width * 0.5f, height * 0.5f, width * 0.5f, height * 0.3f)
                    }
                    drawPath(path = starPath, color = Color.White)
                }
                cleanName.contains("reliancedigital") || cleanName.contains("reliance") -> {
                    drawCircle(color = Color(0xFFE42528), radius = radius)
                    val boltPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.55f, height * 0.28f)
                        lineTo(width * 0.35f, height * 0.52f)
                        lineTo(width * 0.52f, height * 0.52f)
                        lineTo(width * 0.45f, height * 0.72f)
                        lineTo(width * 0.65f, height * 0.48f)
                        lineTo(width * 0.48f, height * 0.48f)
                        close()
                    }
                    drawPath(path = boltPath, color = Color.White)
                }
                cleanName.contains("croma") -> {
                    drawCircle(color = Color(0xFF00E5D2), radius = radius)
                    val leafPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.5f, height * 0.3f)
                        quadraticTo(width * 0.7f, height * 0.5f, width * 0.5f, height * 0.7f)
                        quadraticTo(width * 0.3f, height * 0.5f, width * 0.5f, height * 0.3f)
                    }
                    drawPath(path = leafPath, color = Color.White)
                    drawLine(
                        color = Color.White,
                        start = androidx.compose.ui.geometry.Offset(width * 0.35f, height * 0.65f),
                        end = androidx.compose.ui.geometry.Offset(width * 0.65f, height * 0.35f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
                cleanName.contains("tatacliq") || cleanName.contains("tata cliq") -> {
                    drawCircle(color = Color(0xFF222222), radius = radius)
                    val checkPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.35f, height * 0.5f)
                        lineTo(width * 0.48f, height * 0.63f)
                        lineTo(width * 0.68f, height * 0.38f)
                    }
                    drawPath(
                        path = checkPath,
                        color = Color(0xFFE40046),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 3.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round,
                            join = androidx.compose.ui.graphics.StrokeJoin.Round
                        )
                    )
                    drawCircle(color = Color(0xFFE40046), radius = 2.dp.toPx(), center = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.22f))
                }
                cleanName.contains("snapdeal") -> {
                    drawCircle(color = Color(0xFFE40046), radius = radius)
                    val boxPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.5f, height * 0.4f)
                        lineTo(width * 0.32f, height * 0.48f)
                        lineTo(width * 0.32f, height * 0.66f)
                        lineTo(width * 0.5f, height * 0.74f)
                        lineTo(width * 0.68f, height * 0.66f)
                        lineTo(width * 0.68f, height * 0.48f)
                        close()
                    }
                    drawPath(path = boxPath, color = Color.White, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
                    drawLine(color = Color.White, start = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.4f), end = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.74f), strokeWidth = 1.dp.toPx())
                    drawLine(color = Color.White, start = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.58f), end = androidx.compose.ui.geometry.Offset(width * 0.32f, height * 0.48f), strokeWidth = 1.dp.toPx())
                    drawLine(color = Color.White, start = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.58f), end = androidx.compose.ui.geometry.Offset(width * 0.68f, height * 0.48f), strokeWidth = 1.dp.toPx())
                }
                cleanName.contains("jiomart") -> {
                    drawCircle(color = Color(0xFF003399), radius = radius)
                    val basketPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.32f, height * 0.45f)
                        lineTo(width * 0.68f, height * 0.45f)
                        lineTo(width * 0.63f, height * 0.72f)
                        lineTo(width * 0.37f, height * 0.72f)
                        close()
                    }
                    drawPath(path = basketPath, color = Color.White, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
                    val handlePath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.4f, height * 0.45f)
                        quadraticTo(width * 0.5f, height * 0.28f, width * 0.6f, height * 0.45f)
                    }
                    drawPath(path = handlePath, color = Color.White, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
                }
                cleanName.contains("firstcry") -> {
                    drawCircle(color = Color(0xFFFF7000), radius = radius)
                    drawCircle(
                        color = Color.White,
                        radius = 8.dp.toPx(),
                        center = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.62f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                    val shieldPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width * 0.32f, height * 0.44f)
                        quadraticTo(width * 0.5f, height * 0.34f, width * 0.68f, height * 0.44f)
                        quadraticTo(width * 0.65f, height * 0.54f, width * 0.5f, height * 0.54f)
                        quadraticTo(width * 0.35f, height * 0.54f, width * 0.32f, height * 0.44f)
                        close()
                    }
                    drawPath(path = shieldPath, color = Color.White)
                }
                else -> {
                    drawCircle(color = Color(0xFF1E1E1E), radius = radius)
                }
            }
        }
    } else {
        val merchantInfo = remember(name) { MerchantRegistry.findMerchant(name) }
        val primaryColor = Color(merchantInfo.primaryColor)
        val secondaryColor = Color(merchantInfo.secondaryColor)
        val rawBadge = merchantInfo.brandBadgeText.ifBlank { merchantInfo.merchantName.take(5).uppercase(Locale.getDefault()) }
        val badgeText = if (rawBadge.length > 1) rawBadge else merchantInfo.merchantName.take(5).ifBlank { "STORE" }.uppercase(Locale.getDefault())

        Box(
            modifier = modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.5f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryColor, secondaryColor)
                    ),
                    shape = CircleShape
                )
                .border(BorderStroke(1.2.dp, Color(0x99FFFFFF)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = badgeText,
                color = Color.White,
                fontSize = if (badgeText.length > 5) 8.sp else if (badgeText.length > 3) 9.sp else 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==================================================
// IMMERSIVE ANALYSIS SCREEN (Premium Material Motion)
// ==================================================
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnalysisScreen(
    analyzedLink: String,
    onAddHistoryItem: (ShoppingItem) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToResult: () -> Unit
) {
    val scrollState = rememberScrollState()
    val merchant = remember(analyzedLink) { detectMerchant(analyzedLink) }
    val resultData = remember(analyzedLink) { com.example.data.generateResultData(analyzedLink) }
    
    val isInstagram = remember(analyzedLink) { analyzedLink.contains("instagram.com") || analyzedLink.contains("instagr.am") }
    val isCached = remember(analyzedLink) { sessionCache.containsKey(analyzedLink) }
    var showCachedOpeningMessage by remember { mutableStateOf(isCached) }
    
    var progressPercentage by remember { mutableStateOf(activeProgressCache[analyzedLink] ?: 0) }
    var analysisFinished by remember { mutableStateOf(progressPercentage >= 100) }
    var showSuccessOverlay by remember { mutableStateOf(false) }
    var isScanningStarted by remember { mutableStateOf(false) }
    
    val currentStatus = when {
        isInstagram -> {
            when {
                progressPercentage < 14 -> "Loading Reel..."
                progressPercentage < 28 -> "Downloading Preview..."
                progressPercentage < 42 -> "Extracting Video Frames..."
                progressPercentage < 56 -> "Detecting Products..."
                progressPercentage < 70 -> "Matching Shopping Catalog..."
                progressPercentage < 85 -> "Comparing Prices..."
                progressPercentage < 100 -> "Generating Smart Shopping Report..."
                else -> "Scan Complete"
            }
        }
        else -> {
            when {
                progressPercentage < 12 -> "Detecting Store..."
                progressPercentage < 25 -> "Extracting Product Page..."
                progressPercentage < 38 -> "Gemini AI Verification..."
                progressPercentage < 50 -> "Multi-Store Catalog Match..."
                progressPercentage < 62 -> "Comparing Live Prices..."
                progressPercentage < 75 -> "Analyzing Price History..."
                progressPercentage < 88 -> "Calculating Delivery & Trust..."
                progressPercentage < 100 -> "Synthesizing AI Buying Advice..."
                else -> "Analysis Complete"
            }
        }
    }
    
    LaunchedEffect(analyzedLink) {
        if (analyzedLink.isNotBlank()) {
            withContext(Dispatchers.IO) {
                com.example.data.integration.RealDataIntegrationLayer.getProductData(analyzedLink)
            }
        }
    }

    LaunchedEffect(analyzedLink, isScanningStarted) {
        if (isInstagram && !isScanningStarted) {
            return@LaunchedEffect
        }
        if (isCached && !isInstagram) {
            delay(600)
            showCachedOpeningMessage = false
            onNavigateToResult()
            return@LaunchedEffect
        }
        
        val steps = if (isInstagram) {
            listOf(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
        } else {
            listOf(0, 12, 25, 38, 50, 62, 75, 88, 100)
        }
        
        for (i in 0 until steps.size) {
            val targetProgress = steps[i]
            progressPercentage = targetProgress
            activeProgressCache[analyzedLink] = targetProgress
            
            if (targetProgress < 100) {
                val delayMs = if (isInstagram) 350L else 300L
                delay(delayMs)
            }
        }
        
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val now = sdf.format(Date())
        val newItem = ShoppingItem(
            url = if (analyzedLink.isNotBlank()) analyzedLink else "https://amazon.com/product/mock_link",
            platform = merchant.name,
            timestamp = now
        )
        onAddHistoryItem(newItem)
        sessionCache[analyzedLink] = true
        analysisFinished = true
        
        if (isInstagram) {
            showSuccessOverlay = true
            delay(2200)
        } else {
            delay(300)
        }
        onNavigateToResult()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        if (isInstagram) {
            InstagramAnalysisContent(
                analyzedLink = analyzedLink,
                isScanningStarted = isScanningStarted,
                showSuccessScreen = showSuccessOverlay,
                progressPercentage = progressPercentage,
                currentStatus = currentStatus,
                onStartScan = { isScanningStarted = true },
                onBackClick = onBackClick
            )
        } else {
            if (showSuccessOverlay) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SuccessOverlay()
                }
            } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                
                Box(
                    modifier = Modifier
                        .background(
                            if (isInstagram) Color(0x1FE1306C) else CrimsonRed.copy(alpha = 0.1f), 
                            RoundedCornerShape(20.dp)
                        )
                        .border(
                            BorderStroke(1.dp, if (isInstagram) Color(0x33E1306C) else CrimsonRed.copy(alpha = 0.25f)), 
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(if (isInstagram) Color(0xFFE1306C) else CrimsonLight, CircleShape)
                        )
                        Text(
                            text = if (isInstagram) "INSTAGRAM SHOPPING AI ENGINE" else "AI SCAN CO-PILOT ENGINE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isInstagram) Color(0xFFE1306C) else CrimsonLight,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(30.dp))
                
                if (isInstagram) {
                    CinematicInstagramScanner(
                        progressPercentage = progressPercentage,
                        currentStatus = currentStatus,
                        isFinished = analysisFinished
                    )
                } else {
                    LuxuryScanner(
                        progressPercentage = progressPercentage,
                        currentStatus = currentStatus,
                        merchant = merchant,
                        isFinished = analysisFinished,
                        resultData = resultData
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = if (isInstagram) "CREATOR VIDEO REEL" else resultData.brand.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isInstagram) Color(0xFFE1306C) else CrimsonLight,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isInstagram) "Scanning multi-products from video feed" else resultData.productName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                GlassCard(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    borderColor = Color(0x18FFFFFF),
                    backgroundColor = Color(0x0AFFFFFF)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (progressPercentage < 100) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.5.dp,
                                    color = if (isInstagram) Color(0xFFE1306C) else CrimsonLight,
                                    trackColor = Color(0x1AFFFFFF)
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Complete",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                            }

                            AnimatedContent(
                                targetState = currentStatus,
                                transitionSpec = {
                                    (fadeIn(animationSpec = tween(220)) + slideInVertically(initialOffsetY = { 16 })) with
                                    (fadeOut(animationSpec = tween(180)) + slideOutVertically(targetOffsetY = { -16 }))
                                },
                                label = "CompactStatusText"
                            ) { status ->
                                Text(
                                    text = status,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
}

@Composable
fun LuxuryScanner(
    progressPercentage: Int,
    currentStatus: String,
    merchant: DetectedMerchant,
    isFinished: Boolean,
    resultData: com.example.data.ShoppingResult
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LuxuryScannerAnims")
    
    val outerRotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OuterRotation"
    )
    
    val innerRotationAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "InnerRotation"
    )
    
    val laserYProgress by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LaserYProgress"
    )
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .graphicsLayer {
                    scaleX = if (isFinished) 1.0f else pulseScale
                    scaleY = if (isFinished) 1.0f else pulseScale
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(195.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                CrimsonRed.copy(alpha = 0.18f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            CircularProgressIndicator(
                progress = progressPercentage / 100f,
                modifier = Modifier.size(180.dp),
                color = CrimsonRed,
                strokeWidth = 3.dp,
                trackColor = Color(0x12FFFFFF)
            )
            
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer { rotationZ = outerRotationAngle }
            ) {
                val strokeWidth = 1.5.dp.toPx()
                val dashEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                val stroke = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = strokeWidth,
                    pathEffect = dashEffect
                )
                drawArc(
                    color = CrimsonLight.copy(alpha = 0.45f),
                    startAngle = 0f,
                    sweepAngle = 45f,
                    useCenter = false,
                    style = stroke
                )
                drawArc(
                    color = CrimsonLight.copy(alpha = 0.45f),
                    startAngle = 90f,
                    sweepAngle = 45f,
                    useCenter = false,
                    style = stroke
                )
                drawArc(
                    color = CrimsonLight.copy(alpha = 0.45f),
                    startAngle = 180f,
                    sweepAngle = 45f,
                    useCenter = false,
                    style = stroke
                )
                drawArc(
                    color = CrimsonLight.copy(alpha = 0.45f),
                    startAngle = 270f,
                    sweepAngle = 45f,
                    useCenter = false,
                    style = stroke
                )
            }
            
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .graphicsLayer { rotationZ = innerRotationAngle }
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.sweepGradient(
                                listOf(CrimsonRed, Color.Transparent, CrimsonLight, Color.Transparent)
                            )
                        ),
                        CircleShape
                    )
            )
            
            Box(
                modifier = Modifier
                    .size(134.dp)
                    .background(Color(0xFF0F0F0F), CircleShape)
                    .border(BorderStroke(1.2.dp, Color(0x1F2C2C2C)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Dynamic aura glow matching merchant accent
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .scale(pulseScale)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    merchant.accentColor.copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )
                
                // Dynamic brand logo or shopping globe
                val isKnownMerchant = merchant.name.lowercase() != "shopping website" && merchant.name.lowercase() != "supported store"
                if (isKnownMerchant) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .scale(pulseScale)
                            .shadow(12.dp, CircleShape, ambientColor = merchant.accentColor, spotColor = merchant.accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(
                            name = merchant.name,
                            modifier = Modifier.size(68.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .scale(pulseScale)
                            .background(
                                Brush.linearGradient(
                                    listOf(CrimsonRed.copy(alpha = 0.2f), CrimsonLight.copy(alpha = 0.1f))
                                ),
                                CircleShape
                            )
                            .border(BorderStroke(1.dp, CrimsonLight.copy(alpha = 0.4f)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Animated Globe Scanner",
                            tint = CrimsonLight,
                            modifier = Modifier
                                .size(36.dp)
                                .graphicsLayer { rotationZ = innerRotationAngle }
                        )
                    }
                }

                if (!isFinished) {
                    Canvas(modifier = Modifier.size(120.dp)) {
                        val y = laserYProgress * this.size.height
                        drawLine(
                            color = CrimsonRed,
                            start = androidx.compose.ui.geometry.Offset(10f, y),
                            end = androidx.compose.ui.geometry.Offset(this.size.width - 10f, y),
                            strokeWidth = 3.5f
                        )
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    CrimsonRed.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            ),
                            topLeft = androidx.compose.ui.geometry.Offset(10f, y - 8.dp.toPx()),
                            size = androidx.compose.ui.geometry.Size(this.size.width - 20f, 16.dp.toPx())
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(18.dp))
        
        Text(
            text = "$progressPercentage%",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            letterSpacing = (-0.5).sp
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = if (isFinished) "ANALYSIS COMPLETE" else currentStatus.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = if (isFinished) Color(0xFF4CAF50) else CrimsonLight,
            letterSpacing = 1.5.sp
        )
    }
}

data class ToolItem(
    val title: String,
    val icon: ImageVector,
    val dialogText: String
)

// ==================================================
// SMART URL DETECTOR & MERCHANT MODELS
// ==================================================
enum class UrlPageType {
    ProductPage,
    CategoryPage,
    HomePage,
    Cart,
    Checkout,
    Unknown
}

fun detectPageType(url: String): UrlPageType {
    val cleanUrl = url.trim().lowercase()
    
    // Check for Cart/Checkout
    if (cleanUrl.contains("cart") || cleanUrl.contains("checkout") || cleanUrl.contains("basket") || cleanUrl.contains("bag") || cleanUrl.contains("pay")) {
        return UrlPageType.Cart
    }
    
    val uri = try {
        java.net.URI(if (!cleanUrl.startsWith("http")) "https://$cleanUrl" else cleanUrl)
    } catch (e: Exception) {
        null
    }
    
    val path = uri?.path ?: ""
    val query = uri?.query ?: ""
    
    // Check for Home Page
    if (path.isEmpty() || path == "/") {
        return UrlPageType.HomePage
    }
    
    // Check for Category/Search
    if (path.contains("category") || path.contains("collection") || path.contains("search") || path.contains("list") || path.contains("catalog") || path.contains("shop") || query.contains("category") || query.contains("search")) {
        return UrlPageType.CategoryPage
    }
    
    // If it contains product-specific indicators
    if (path.contains("product") || path.contains("/p/") || path.contains("/dp/") || path.contains("/gp/") || path.contains("item") || path.contains("detail") || path.contains("prod")) {
        return UrlPageType.ProductPage
    }
    
    // Or if path has multiple segments and ends with an ID or alphanumeric slug
    val segments = path.split("/").filter { it.isNotEmpty() }
    if (segments.size >= 2) {
        return UrlPageType.ProductPage
    }
    
    return UrlPageType.ProductPage // Default for deep URLs
}

data class DetectedMerchant(
    val name: String,
    val domain: String,
    val logoChar: Char,
    val accentColor: Color,
    val accentColorLong: Long,
    val isSupported: Boolean,
    val country: String,
    val isSecure: Boolean,
    val isProductPage: Boolean
)

fun detectMerchant(url: String): DetectedMerchant {
    val result = MerchantDetector.analyzeUrl(url)
    val info = result.merchantInfo
    val logoChar = if (info.brandBadgeText.isNotEmpty()) info.brandBadgeText[0] else 'S'
    val isSecure = !url.lowercase(Locale.getDefault()).startsWith("http://")
    
    return DetectedMerchant(
        name = info.merchantName,
        domain = info.domain,
        logoChar = logoChar,
        accentColor = Color(info.primaryColor),
        accentColorLong = info.primaryColor,
        isSupported = info.supported,
        country = info.country,
        isSecure = isSecure,
        isProductPage = result.isProductPage
    )
}

fun hslToLong(h: Float, s: Float, l: Float): Long {
    val c = (1f - Math.abs(2f * l - 1f)) * s
    val x = c * (1f - Math.abs((h / 60f) % 2f - 1f))
    val m = l - c / 2f
    val (r, g, b) = when {
        h < 60 -> Triple(c, x, 0f)
        h < 120 -> Triple(x, c, 0f)
        h < 180 -> Triple(0f, c, x)
        h < 240 -> Triple(0f, x, c)
        h < 300 -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }
    val ri = ((r + m) * 255).toInt().coerceIn(0, 255)
    val gi = ((g + m) * 255).toInt().coerceIn(0, 255)
    val bi = ((b + m) * 255).toInt().coerceIn(0, 255)
    return (0xFF000000L or (ri.toLong() shl 16) or (gi.toLong() shl 8) or bi.toLong())
}

@Composable
fun MiniatureMerchantCard(merchant: DetectedMerchant) {
    val isFlipkart = merchant.name.lowercase().contains("flipkart")
    val accentCol = if (isFlipkart) Color(0xFF2874F0) else merchant.accentColor
    val highlightCol = if (isFlipkart) Color(0xFFFFE11B) else Color(0xFF00FFCC)

    // Green/Yellow pulse animation for verified badge & link detection
    val infinitePulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infinitePulse.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infinitePulse.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    val sweepProgress by infinitePulse.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweepProgress"
    )

    var isGlowActive by remember(merchant.name) { mutableStateOf(true) }
    LaunchedEffect(merchant.name) {
        isGlowActive = true
        delay(350)
        isGlowActive = false
    }

    val animatedGlowAlpha by animateFloatAsState(
        targetValue = if (isGlowActive) 0.75f else 0.25f,
        animationSpec = tween(300),
        label = "glowAlpha"
    )

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF0C1A2B),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                listOf(
                    accentCol.copy(alpha = animatedGlowAlpha + 0.3f),
                    highlightCol.copy(alpha = animatedGlowAlpha + 0.3f),
                    accentCol.copy(alpha = animatedGlowAlpha + 0.3f)
                )
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = accentCol)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Tiny progress sweep indicator line at top of card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                highlightCol.copy(alpha = 0.9f),
                                accentCol,
                                Color.Transparent
                            ),
                            startX = sweepProgress * 600f - 200f,
                            endX = sweepProgress * 600f + 200f
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OfficialLogo(
                    name = merchant.name,
                    modifier = Modifier
                        .size(42.dp)
                        .border(BorderStroke(1.2.dp, accentCol.copy(alpha = 0.7f)), CircleShape)
                        .shadow(4.dp, CircleShape, spotColor = highlightCol)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = if (isFlipkart) "Flipkart Product Verified" else merchant.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        if (merchant.isSupported) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Merchant",
                                tint = if (isFlipkart) Color(0xFFFFE11B) else Color(0xFF4CAF50),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .scale(pulseScale)
                                .background(highlightCol.copy(alpha = pulseAlpha), CircleShape)
                        )
                        Text(
                            text = if (merchant.isProductPage) "Flipkart Product Link Detected" else "Flipkart Store Page Detected",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = highlightCol
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(
                            if (merchant.isSupported) accentCol.copy(alpha = 0.25f) else Color(0x1FFF5252),
                            RoundedCornerShape(12.dp)
                        )
                        .border(
                            BorderStroke(
                                1.dp,
                                if (merchant.isSupported) highlightCol.copy(alpha = 0.6f) else Color(0x33FF5252)
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (merchant.isSupported) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = "Status",
                            tint = if (merchant.isSupported) highlightCol else Color(0xFFFF5252),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (merchant.isSupported) "Verified Link" else "Unsupported",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = if (merchant.isSupported) highlightCol else Color(0xFFFF5252),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// FUTURE READY HIDDEN PLACEHOLDERS (Phase 3B / Phase 4 Architectural Modules)
// =========================================================================
@Composable
fun HiddenFeaturePlaceholders() {
    /* 
       Architectural placeholders kept hidden until future phases.
       - AI Price Prediction
       - Coupon Finder
       - Cashback Finder
       - Price Drop Alert
       - Wishlist Sync
       - Recently Viewed
       - Product Collections
       - Seller Trust Score
       - Price History Graph
       - AI Review Summary
       - Barcode Scanner
       - QR Scanner
       - Voice Search
       - Reel Product Detection
       - Video Product Detection
       - Shopping Assistant Chat
       - Nearby Store Availability
    */
}

fun extractInstagramUsername(url: String): String {
    val clean = url.trim().lowercase().removePrefix("https://").removePrefix("http://").removePrefix("www.").removePrefix("m.")
    val segments = clean.split("/").filter { it.isNotEmpty() }
    if (segments.size > 1 && segments[0] == "instagram.com" && segments[1] != "p" && segments[1] != "reel" && segments[1] != "tv") {
        return "@" + segments[1]
    }
    return "@luxe.trends.ai"
}

@Composable
fun InstagramReelPreviewCard(url: String) {
    val creator = remember(url) { extractInstagramUsername(url) }
    
    val instagramGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF833AB4), // Purple
            Color(0xFFFD1D1D), // Red
            Color(0xFFF56040), // Orange
            Color(0xFFFFDC80)  // Yellow
        )
    )
    
    // Pulse animation for the "Ready For AI Scan" dot
    val infiniteTransition = rememberInfiniteTransition(label = "ReadyDot")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DotAlpha"
    )

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.2.dp, instagramGradient), RoundedCornerShape(16.dp)),
        borderColor = Color.Transparent,
        backgroundColor = Color(0x120F0F0F)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail: Tall 9:16 aspect ratio placeholder
            Box(
                modifier = Modifier
                    .width(85.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF4C1D95), Color(0xFFDB2777), Color(0xFFF59E0B))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Diagonal accent overlay to make it look like "Reel Premium Art"
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.08f),
                        radius = size.minDimension / 1.5f,
                        center = androidx.compose.ui.geometry.Offset(size.width, 0f)
                    )
                }
                
                // Centered translucent play button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.25f), CircleShape)
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Reel",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                // HD Badge at top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "HD 60FPS",
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                // Duration at bottom right
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "0:24",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Metadata info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Instagram Header row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Instagram gradient icon
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(instagramGradient, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Instagram",
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                    Text(
                        text = "INSTAGRAM REEL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFF56040),
                        letterSpacing = 1.2.sp
                    )
                }
                
                // Creator name with Verified Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = creator,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    // Verified Badge: blue circle with a check
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(Color(0xFF3897F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Verified",
                            tint = Color.White,
                            modifier = Modifier.size(9.dp)
                        )
                    }
                }
                
                Text(
                    text = "Awaiting AI Product Scan",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // Ready badge
                Box(
                    modifier = Modifier
                        .background(Color(0x1A00FFCC), RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, Color(0x3300FFCC)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .graphicsLayer { alpha = dotAlpha }
                                .background(Color(0xFF00FFCC), CircleShape)
                        )
                        Text(
                            text = "READY FOR AI SCAN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF00FFCC),
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CinematicInstagramScanner(
    progressPercentage: Int,
    currentStatus: String,
    isFinished: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "InstagramScannerAnims")
    
    // Core Rotation Angle
    val coreRotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CoreRotation"
    )
    
    // Laser vertical progress
    val laserYProgress by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LaserProgress"
    )
    
    // Pulse scale for ring glow
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )
    
    // Rotating dash angle
    val outerDashAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OuterDash"
    )

    val instagramGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF833AB4), // Purple
            Color(0xFFFD1D1D), // Red
            Color(0xFFF56040), // Orange
            Color(0xFFFFDC80)  // Yellow
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            // 1. Ambient Glow Ring (behind everything)
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .graphicsLayer {
                        scaleX = pulseScale
                        scaleY = pulseScale
                    }
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0x22E1306C),
                                Color(0x06833AB4),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            // 2. Translucent Pulse Ring 1 (Expanding)
            val ringScale1 by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "RingScale1"
            )
            val ringAlpha1 by infiniteTransition.animateFloat(
                initialValue = 0.35f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "RingAlpha1"
            )
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .graphicsLayer {
                        scaleX = ringScale1
                        scaleY = ringScale1
                        alpha = ringAlpha1
                    }
                    .border(BorderStroke(1.2.dp, instagramGradient), CircleShape)
            )

            // 3. Rotating Dash Ring (ChatGPT styling)
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer { rotationZ = outerDashAngle }
            ) {
                val strokeWidth = 1.5.dp.toPx()
                val dashEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(25f, 25f), 0f)
                drawArc(
                    brush = instagramGradient,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = strokeWidth,
                        pathEffect = dashEffect
                    )
                )
            }

            // 4. Rotating AI Core Circle
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .graphicsLayer { rotationZ = coreRotationAngle }
                    .border(
                        BorderStroke(
                            1.5.dp,
                            Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFF833AB4),
                                    Color(0xFFFD1D1D),
                                    Color(0xFFF56040),
                                    Color.Transparent,
                                    Color(0xFF833AB4)
                                )
                            )
                        ),
                        CircleShape
                    )
            )

            // 5. Center Core (Nothing OS styling)
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0A0A0A))
                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Barcode scanning effect drawn under the laser
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val lines = 16
                    val spacing = size.width / (lines + 1)
                    for (i in 1..lines) {
                        val x = spacing * i
                        val opacity = if (i % 3 == 0) 0.12f else if (i % 2 == 0) 0.05f else 0.08f
                        drawLine(
                            color = Color.White.copy(alpha = opacity),
                            start = androidx.compose.ui.geometry.Offset(x, 20f),
                            end = androidx.compose.ui.geometry.Offset(x, size.height - 20f),
                            strokeWidth = if (i % 4 == 0) 3.dp.toPx() else 1.dp.toPx()
                        )
                    }
                }

                // AI Particle system (orbiting background)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val angleRad = (coreRotationAngle * Math.PI / 180f)
                    val radius = 45.dp.toPx()
                    val particleX1 = (size.width / 2) + Math.cos(angleRad) * radius
                    val particleY1 = (size.height / 2) + Math.sin(angleRad) * radius
                    
                    val particleX2 = (size.width / 2) - Math.cos(angleRad + 1.2) * radius
                    val particleY2 = (size.height / 2) - Math.sin(angleRad + 1.2) * radius
                    
                    drawCircle(Color(0xFF00FFCC), 3.dp.toPx(), androidx.compose.ui.geometry.Offset(particleX1.toFloat(), particleY1.toFloat()))
                    drawCircle(Color(0xFFFFDC80), 2.5.dp.toPx(), androidx.compose.ui.geometry.Offset(particleX2.toFloat(), particleY2.toFloat()))
                }

                // Inner Instagram Logo (PhotoCamera as placeholder camera logo)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0x33833AB4), Color(0x33FD1D1D), Color(0x33F56040))
                            ),
                            CircleShape
                        )
                        .border(BorderStroke(1.dp, Color(0x44FFFFFF)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Instagram Core",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // 6. Moving AI Laser Scanner
                if (!isFinished) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val y = laserYProgress * size.height
                        // Draw glowing gradient band around laser line
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFFFD1D1D).copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            topLeft = androidx.compose.ui.geometry.Offset(0f, y - 8.dp.toPx()),
                            size = androidx.compose.ui.geometry.Size(size.width, 16.dp.toPx())
                        )
                        // Laser line itself
                        drawLine(
                            brush = instagramGradient,
                            start = androidx.compose.ui.geometry.Offset(10f, y),
                            end = androidx.compose.ui.geometry.Offset(size.width - 10f, y),
                            strokeWidth = 3f
                        )
                    }
                }
            }

            // Animated progress circle around core
            CircularProgressIndicator(
                progress = progressPercentage / 100f,
                modifier = Modifier.size(225.dp),
                color = Color(0xFFE1306C),
                strokeWidth = 2.dp,
                trackColor = Color(0x0AFFFFFF)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "$progressPercentage%",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (isFinished) "ANALYSIS COMPLETE" else currentStatus.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = if (isFinished) Color(0xFF00FFCC) else Color(0xFFE1306C),
            letterSpacing = 1.5.sp
        )
    }
}

@Composable
fun SuccessOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "SuccessAnims")
    
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "RingScale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glowing Ring
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .graphicsLayer {
                        scaleX = ringScale
                        scaleY = ringScale
                    }
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0x222ECC71),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
                    .border(BorderStroke(2.dp, Color(0xFF2ECC71)), CircleShape)
            )

            // Green AI Tick Inside
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = Color(0xFF2ECC71),
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AI SCAN SUCCESSFUL",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF2ECC71),
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Instagram Shopping Report Compiled",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextWhite.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Vibrations and sound mock notes
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(Color(0x1F2ECC71), RoundedCornerShape(8.dp))
                .border(BorderStroke(1.dp, Color(0x332ECC71)), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Audio feedback",
                tint = Color(0xFF2ECC71),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Success chime & haptic feedback triggered",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2ECC71),
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun InstagramAnalysisContent(
    analyzedLink: String,
    isScanningStarted: Boolean,
    showSuccessScreen: Boolean,
    progressPercentage: Int,
    currentStatus: String,
    onStartScan: () -> Unit,
    onBackClick: () -> Unit
) {
    val metadata = remember(analyzedLink) { com.example.data.getInstagramReelMetadata(analyzedLink) }
    
    Box(
        modifier = Modifier.fillMaxSize().background(AmoledBlack),
        contentAlignment = Alignment.Center
    ) {
        if (showSuccessScreen) {
            InstagramSuccessScreen()
        } else if (isScanningStarted) {
            InstagramCinematicScanning(progressPercentage, currentStatus)
        } else {
            InstagramReelPreviewCard(metadata, onStartScan, onBackClick)
        }
    }
}

@Composable
fun InstagramReelPreviewCard(
    metadata: com.example.data.InstagramReelMetadata,
    onStartScan: () -> Unit,
    onBackClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_button")
    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Button & Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0x12FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Instagram Shopping AI",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Premium Preview Card Container
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0x0AFFFFFF))
                .border(BorderStroke(1.5.dp, Color(0x1AFFFFFF)), RoundedCornerShape(24.dp))
        ) {
            // Thumbnail / Fallback Artwork
            if (metadata.thumbnailUrl != null) {
                SubcomposeAsyncImage(
                    model = metadata.thumbnailUrl,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x0DFFFFFF))
                        )
                    },
                    error = {
                        InstagramPlaceholderArtwork()
                    },
                    contentDescription = "Reel Thumbnail",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                InstagramPlaceholderArtwork()
            }

            // Dark semi-transparent gradient overlay for readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Badges in top right/left
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Instagram Gradient Icon & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFF91880), Color(0xFFF77737), Color(0xFFFFDD55))
                            ),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Instagram",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "REEL PREVIEW",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                // HD Badge & Duration
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0x66000000), RoundedCornerShape(4.dp))
                            .border(BorderStroke(0.5.dp, Color(0x33FFFFFF)), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "HD",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0x66000000), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = metadata.duration,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bottom Info Overlay inside Card
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Creator Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF833AB4), Color(0xFFF77737))
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (metadata.username.length > 1) metadata.username[1].uppercase() else "I",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Text(
                        text = metadata.username,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (metadata.isVerified) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified Creator",
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Caption
                Text(
                    text = metadata.caption,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Ready for AI Scan Status Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .background(Color(0x1A00FFCC), RoundedCornerShape(12.dp))
                        .border(BorderStroke(1.dp, Color(0x4000FFCC)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFF00FFCC), CircleShape)
                    )
                    Text(
                        text = "Ready For AI Scan",
                        color = Color(0xFF00FFCC),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Big Scan Button
        Button(
            onClick = onStartScan,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .graphicsLayer {
                    scaleX = scalePulse
                    scaleY = scalePulse
                }
                .shadow(16.dp, RoundedCornerShape(16.dp), ambientColor = Color(0xFFE1306C), spotColor = Color(0xFF833AB4)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFCAF45), Color(0xFFE1306C), Color(0xFF833AB4))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Bolt",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "START AI SCAN",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InstagramPlaceholderArtwork() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0x4D833AB4), Color(0xFF0D0D0D)),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0x12FFFFFF), CircleShape)
                    .border(
                        BorderStroke(
                            1.5.dp,
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFCAF45), Color(0xFFE1306C), Color(0xFF833AB4))
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Movie Preview",
                    tint = Color(0xFFE1306C),
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "AI STREAM LINK ACTIVE",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Instagram Shopping AI scan protocol ready.",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InstagramCinematicScanning(progress: Int, statusText: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "cinematic_scanner")
    
    val laserSweep by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laserSweep"
    )

    val coreRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "coreRotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CINEMATIC VISION SCAN ACTIVE",
            color = Color(0xFF00FFCC),
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0x05FFFFFF))
                .border(BorderStroke(1.5.dp, Color(0x14FFFFFF)), RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = size.width / 12f
                for (i in 1..11) {
                    val alpha = if (i % 3 == 0) 0.12f else 0.04f
                    drawLine(
                        color = Color.White.copy(alpha = alpha),
                        start = Offset(i * step, 0f),
                        end = Offset(i * step, size.height),
                        strokeWidth = if (i % 3 == 0) 2f else 1f
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .graphicsLayer {
                        scaleX = pulseScale
                        scaleY = pulseScale
                        alpha = pulseAlpha
                    }
                    .background(Color.Transparent, CircleShape)
                    .border(BorderStroke(1.5.dp, Color(0xFF00FFCC)), CircleShape)
            )

            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF00FFCC),
                    strokeWidth = 3.dp,
                    trackColor = Color(0x14FFFFFF),
                )

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .graphicsLayer { rotationZ = coreRotation }
                        .background(Color(0x1200FFCC), CircleShape)
                        .border(
                            BorderStroke(
                                1.5.dp,
                                Brush.sweepGradient(
                                    colors = listOf(Color(0xFFE1306C), Color(0xFF833AB4), Color(0xFF00FFCC), Color(0xFFE1306C))
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI Scanner Core",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .graphicsLayer { rotationZ = -coreRotation }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        translationY = 250.dp.value * laserSweep * 2.5f
                    }
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF00FFCC), Color(0xFFE1306C), Color(0xFF00FFCC), Color.Transparent)
                        )
                    )
                    .shadow(elevation = 8.dp, spotColor = Color(0xFF00FFCC), ambientColor = Color(0xFFE1306C))
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val t = (System.currentTimeMillis() % 3000) / 3000f
                val points = listOf(
                    Offset(size.width * 0.25f, size.height * (0.8f - 0.4f * t)),
                    Offset(size.width * 0.75f, size.height * (0.2f + 0.5f * t)),
                    Offset(size.width * 0.4f, size.height * (0.3f + 0.3f * t)),
                    Offset(size.width * 0.6f, size.height * (0.7f - 0.4f * t))
                )
                points.forEach { point ->
                    drawCircle(
                        color = Color(0xFF00FFCC).copy(alpha = 0.35f * (1f - t)),
                        radius = 8f,
                        center = point
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "$progress%",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = statusText,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Please wait, extracting shoppable fashion keys...",
            color = TextGray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun InstagramSuccessScreen() {
    val context = LocalContext.current
    val infiniteTransition = rememberInfiniteTransition(label = "success_screen")
    
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringScale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringAlpha"
    )

    LaunchedEffect(Unit) {
        try {
            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
            if (vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(android.os.VibrationEffect.createOneShot(80, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(80)
                }
            }
        } catch (e: Exception) {
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer {
                        scaleX = ringScale
                        scaleY = ringScale
                        alpha = ringAlpha
                    }
                    .background(Color.Transparent, CircleShape)
                    .border(BorderStroke(2.dp, Color(0xFF00FFCC)), CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color(0x1A00FFCC), CircleShape)
                    .border(BorderStroke(3.dp, Color(0xFF00FFCC)), CircleShape)
                    .shadow(24.dp, CircleShape, ambientColor = Color(0xFF00FFCC), spotColor = Color(0xFF00FFCC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success Tick",
                    tint = Color(0xFF00FFCC),
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "AI ANALYSED SUCCESSFULLY",
            color = Color(0xFF00FFCC),
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Shopping Report Ready!",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(Color(0x0DFFFFFF), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Vibration,
                    contentDescription = "Haptic Pulse Active",
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "HAPTIC FEEDBACK",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(Color.White.copy(alpha = 0.3f), CircleShape)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Audio Playback Played",
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "🎵 AI CHIME PLAYED",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

// ==================================================
// REUSABLE PREMIUM FEATURE HERO CARD (MASTER PATCH 4G.3)
// ==================================================
@Composable
fun PremiumFeatureComingSoonCard(
    onComingSoonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val infiniteTransition = rememberInfiniteTransition(label = "LockAndShimmerTransition")

    // Floating Lock y-offset (-4dp to +4dp)
    val lockFloatingY by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lockFloatingY"
    )

    // Lock Rotation (0 deg to -12 deg for unlock 10% effect)
    val lockRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3500
                0f at 0
                0f at 1500
                -12f at 2000
                -12f at 2200
                0f at 2700
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "lockRotation"
    )

    // Lock Glow Pulse
    val lockGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lockGlowAlpha"
    )

    // Button Shimmer Offset
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = GlassCardBorder,
        backgroundColor = GlassCardBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            // Header Row: Title + Subtitle + Animated Lock & COMING SOON Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Animated Lock Container with Glowing Backdrop
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .graphicsLayer {
                                translationY = lockFloatingY.dp.toPx()
                            }
                    ) {
                        // Breathing Glow Canvas Behind Lock
                        Canvas(modifier = Modifier.size(50.dp)) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFE1306C).copy(alpha = lockGlowAlpha),
                                        Color(0xFF833AB4).copy(alpha = lockGlowAlpha * 0.5f),
                                        Color.Transparent
                                    )
                                )
                            )
                        }

                        // Glass Pill Container for Lock Icon
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF833AB4).copy(alpha = 0.35f),
                                            Color(0xFFFD1D1D).copy(alpha = 0.35f),
                                            Color(0xFFFCB045).copy(alpha = 0.35f)
                                        )
                                    )
                                )
                                .border(
                                    BorderStroke(
                                        1.2.dp,
                                        Brush.horizontalGradient(
                                            listOf(Color.White.copy(alpha = 0.7f), Color(0xFFFF80AB))
                                        )
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            OfficialLogo(name = "instagram", modifier = Modifier.size(26.dp))
                        }

                        // Sparkle Particle overlay
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val alphaVal = lockGlowAlpha
                            drawCircle(
                                color = Color.White.copy(alpha = alphaVal * 0.9f),
                                radius = 1.5.dp.toPx(),
                                center = Offset(size.width * 0.8f, size.height * 0.2f)
                            )
                            drawCircle(
                                color = Color(0xFFFFD700).copy(alpha = alphaVal * 0.9f),
                                radius = 2.dp.toPx(),
                                center = Offset(size.width * 0.15f, size.height * 0.85f)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Instagram Shopping AI",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Analyze any Instagram Reel & instantly find every product.",
                            fontSize = 12.sp,
                            color = TextGray,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // COMING SOON Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0x33FF2E44),
                                    Color(0x339C27B0)
                                )
                            )
                        )
                        .border(
                            BorderStroke(
                                1.dp,
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFFF2E44), Color(0xFFFF80AB))
                                )
                            ),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "COMING SOON",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = CrimsonLight,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PREVIEW CONTENT ROWS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x0CFFFFFF))
                    .border(BorderStroke(1.dp, Color(0x15FFFFFF)), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                PreviewFeatureRow(emoji = "✓", text = "Paste Instagram Reel Link")
                Text(
                    text = "↓",
                    fontSize = 11.sp,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp)
                )
                PreviewFeatureRow(emoji = "✓", text = "AI detects every product")
                Text(
                    text = "↓",
                    fontSize = 11.sp,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp)
                )
                PreviewFeatureRow(emoji = "✓", text = "Compare prices instantly")
                Text(
                    text = "↓",
                    fontSize = 11.sp,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp)
                )
                PreviewFeatureRow(emoji = "✓", text = "Buy from trusted stores")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // COMING SOON SHIMMER BUTTON
            val buttonInteractionSource = remember { MutableInteractionSource() }
            val isPressed by buttonInteractionSource.collectIsPressedAsState()
            val buttonScale by animateFloatAsState(
                targetValue = if (isPressed) 0.96f else 1f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                label = "btnScale"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .graphicsLayer {
                        scaleX = buttonScale
                        scaleY = buttonScale
                    }
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp), spotColor = Color(0xFFE1306C))
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF833AB4).copy(alpha = 0.85f),
                                Color(0xFFFD1D1D).copy(alpha = 0.85f),
                                Color(0xFFFCB045).copy(alpha = 0.85f)
                            ),
                            start = Offset(shimmerOffset % 600f, 0f),
                            end = Offset((shimmerOffset % 600f) + 400f, 150f)
                        )
                    )
                    .border(
                        BorderStroke(1.2.dp, Color.White.copy(alpha = 0.4f)),
                        RoundedCornerShape(24.dp)
                    )
                    .clickable(
                        interactionSource = buttonInteractionSource,
                        indication = androidx.compose.foundation.LocalIndication.current,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onComingSoonClick()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = TextWhite,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Coming Soon",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PreviewFeatureRow(emoji: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (emoji == "✓") {
                Text(text = emoji, fontSize = 14.sp, color = EmeraldPrimary, fontWeight = FontWeight.Black)
            } else {
                Text(text = emoji, fontSize = 14.sp)
            }
            Text(
                text = text,
                fontSize = 12.sp,
                color = TextWhite,
                fontWeight = FontWeight.Medium
            )
        }
        
        // Animated Check Icon
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color(0x222ECC71)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Check",
                tint = Color(0xFF2ECC71),
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

// ==================================================
// CREATOR PROFILE AI FLAGSHIP HERO CARD (MASTER PATCH 4G.4)
// ==================================================
@Composable
fun FloatingMostLovedBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFFD700), Color(0xFFFF007F))
                )
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "🔥 MOST LOVED CREATOR AI", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }
    }
}

@Composable
fun CreatorProfileAiComingSoonCard(
    onComingSoonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val infiniteTransition = rememberInfiniteTransition(label = "CreatorProfileAiAnims")

    // Continuous flowing gradient border offset
    val borderOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "borderOffset"
    )

    // Rotating AI Brain icon (0 to 360 degrees slow)
    val brainRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "brainRotation"
    )

    // Vertical Camera scanning beam progress (0f to 1f)
    val scanBeamProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanBeamProgress"
    )

    // Golden Breathing Lock every 5 seconds (keyframes)
    val lockScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
                1.0f at 0
                1.0f at 3600
                1.18f at 4100
                1.18f at 4300
                1.0f at 4800
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "lockScale"
    )

    val lockShake by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
                0f at 0
                0f at 3800
                -10f at 4000
                10f at 4150
                -8f at 4300
                0f at 4500
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "lockShake"
    )

    val goldParticleExplosion by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
                0f at 0
                0f at 3800
                1f at 4300
                0f at 4700
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "goldParticleExplosion"
    )

    val borderBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFD700),
            Color(0xFFFF007F),
            Color(0xFF00E5FF),
            Color(0xFF8E2DE2),
            Color(0xFFFFD700)
        ),
        start = Offset(borderOffset % 800f, 0f),
        end = Offset((borderOffset % 800f) + 500f, 300f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color(0xFFFF007F),
                ambientColor = Color(0xFF8E2DE2)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1F0D3D),
                        Color(0xFF0F0824),
                        Color(0xFF1B001F)
                    )
                )
            )
            .border(BorderStroke(1.8.dp, borderBrush), RoundedCornerShape(28.dp))
            .padding(18.dp)
    ) {
        // Scanning Beam Overlay
        Canvas(modifier = Modifier.matchParentSize()) {
            val beamY = size.height * scanBeamProgress
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF00E5FF).copy(alpha = 0.6f),
                        Color(0xFFFF007F).copy(alpha = 0.8f),
                        Color(0xFFFFD700).copy(alpha = 0.6f),
                        Color.Transparent
                    )
                ),
                start = Offset(0f, beamY),
                end = Offset(size.width, beamY),
                strokeWidth = 2.dp.toPx()
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Icon + Title/Subtitle + Badges
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
                    // Rotating AI Brain orb
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(52.dp)
                    ) {
                        // Background glow
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color(0xFFFF007F).copy(alpha = 0.5f), Color.Transparent)
                                    )
                                )
                        )

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF8E2DE2),
                                            Color(0xFFFF007F)
                                        )
                                    )
                                )
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "AI Brain",
                                tint = TextWhite,
                                modifier = Modifier
                                    .size(24.dp)
                                    .graphicsLayer { rotationZ = brainRotation }
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Creator Profile AI",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "Turn Your Instagram Profile into a Growth Machine.",
                            fontSize = 12.sp,
                            color = TextWhite.copy(alpha = 0.75f),
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Badges Column
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // ⭐ FLAGSHIP AI Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFFFD700), Color(0xFFFFA000))
                                )
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "⭐ FLAGSHIP AI",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black,
                            letterSpacing = 0.8.sp
                        )
                    }

                    // FREE FOR EVERYONE Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x3300FFCC))
                            .border(BorderStroke(1.dp, Color(0xFF00FFCC)), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "FREE FOR EVERYONE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF00FFCC),
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PREVIEW FEATURE CHIPS (Grid of 2 columns)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x1A000000))
                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                val chips = listOf(
                    "📷 Upload Profile Screenshot",
                    "🤖 AI Profile Review",
                    "📝 Bio Optimization",
                    "🎯 Username Suggestions",
                    "🔥 Trending Hashtags",
                    "✍️ Caption Generator",
                    "📈 Growth Strategy",
                    "🎥 Reel Improvement Tips",
                    "🤝 Brand Collab Score",
                    "🚀 Viral Profile Roadmap"
                )

                chips.chunked(2).forEach { rowChips ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowChips.forEach { chipText ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x1FFFFFFF))
                                    .border(BorderStroke(0.8.dp, Color(0x33FFFFFF)), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = chipText,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        if (rowChips.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "✨ Explained in simple Hinglish • No Login Required",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SHIMMERING BUTTON WITH GOLDEN LOCK PULSE
            val btnInteractionSource = remember { MutableInteractionSource() }
            val isPressed by btnInteractionSource.collectIsPressedAsState()
            val btnScale by animateFloatAsState(
                targetValue = if (isPressed) 0.95f else 1f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                label = "btnScale"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .graphicsLayer {
                        scaleX = btnScale
                        scaleY = btnScale
                    }
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(25.dp), spotColor = Color(0xFFFFD700))
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF8E2DE2),
                                Color(0xFFFF007F),
                                Color(0xFFFFD700)
                            )
                        )
                    )
                    .border(BorderStroke(1.2.dp, Color.White.copy(alpha = 0.5f)), RoundedCornerShape(25.dp))
                    .clickable(
                        interactionSource = btnInteractionSource,
                        indication = androidx.compose.foundation.LocalIndication.current,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onComingSoonClick()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (goldParticleExplosion > 0f) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val pRadius = size.height * goldParticleExplosion * 0.8f
                        drawCircle(
                            color = Color(0xFFFFD700).copy(alpha = (1f - goldParticleExplosion) * 0.8f),
                            radius = pRadius,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = lockScale
                                scaleY = lockScale
                                rotationZ = lockShake
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Sparkle",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "✨ Analyze Instagram Profile",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CreatorSheetFeatureRow(emoji: String, title: String, desc: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = desc,
                fontSize = 11.sp,
                color = TextGray
            )
        }
    }
}

@Composable
fun PremiumInvalidLinkPopup(
    result: com.example.data.ShoppingValidationResult,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.78f))
                .clickable(onClick = onDismiss)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1E1418), Color(0xFF0F0B0E))
                        )
                    )
                    .border(
                        BorderStroke(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFFFF2E44).copy(alpha = 0.8f),
                                    Color(0xFFFF9900).copy(alpha = 0.4f),
                                    Color(0x33FFFFFF)
                                )
                            )
                        ),
                        RoundedCornerShape(28.dp)
                    )
                    .clickable(enabled = false) {}
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFFF2E44).copy(alpha = 0.35f), Color(0x11FF2E44))
                                )
                            )
                            .border(BorderStroke(1.5.dp, Color(0xFFFF2E44)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Warning",
                            tint = Color(0xFFFF4D4D),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = result.errorTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.2.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = result.errorSubtitle,
                        fontSize = 13.sp,
                        color = TextWhite.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "SUPPORTS ALL ONLINE STORES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = CrimsonLight,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val supported = listOf("Amazon", "Flipkart", "Meesho", "Myntra", "AJIO", "Nykaa", "All E-Commerce Sites")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(supported, key = { it }) { name ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x1AFFFFFF))
                                    .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFFF2E44), Color(0xFFFF6B00))
                                )
                            )
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Got it",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BrandCollaborationAiCard(
    onComingSoonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isPressed by buttonInteractionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "brandCollabBtnScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = EmeraldPrimary,
                ambientColor = Color(0x22000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF14141E))
            .border(
                BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary.copy(alpha = 0.5f), Color(0x22FFFFFF)))),
                RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0x1110B981))
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Brand Collaboration AI",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Brand Collaboration AI",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            letterSpacing = 0.3.sp
                        )
                        Text(
                            text = "Pitch, negotiate & close deal emails with top brands.",
                            fontSize = 11.5.sp,
                            color = TextWhite.copy(alpha = 0.7f)
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
                        text = "FREE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grid of Preview Features
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x0CFFFFFF))
                    .border(BorderStroke(1.dp, Color(0x15FFFFFF)), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val features = listOf(
                    "💼 Brand Pitch Generator" to "Generate tailored pitch decks",
                    "🔍 Collaboration Finder" to "Discover high-paying brands",
                    "✉️ Brand Email Generator" to "Professional outreach templates",
                    "💰 Pricing Guide" to "Real market rate calculator",
                    "🤝 Negotiation Tips" to "Secure higher campaign rates",
                    "📊 Campaign Tracker" to "Track deal pipelines & payouts"
                )

                features.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { (title, desc) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x0AFFFFFF))
                                    .border(BorderStroke(0.8.dp, Color(0x1AFFFFFF)), RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = desc,
                                        fontSize = 9.5.sp,
                                        color = TextWhite.copy(alpha = 0.6f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .graphicsLayer {
                        scaleX = buttonScale
                        scaleY = buttonScale
                    }
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(23.dp), spotColor = EmeraldPrimary)
                    .clip(RoundedCornerShape(23.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF10B981), Color(0xFF059669))
                        )
                    )
                    .clickable(
                        interactionSource = buttonInteractionSource,
                        indication = androidx.compose.foundation.LocalIndication.current,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onComingSoonClick()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Open AI Mentor",
                        tint = TextWhite,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Launch AI Mentor 🚀",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}




