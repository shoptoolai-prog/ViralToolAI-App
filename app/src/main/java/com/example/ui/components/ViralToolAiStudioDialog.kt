package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import com.example.ai.GeminiStudioNativeEngine
import com.example.ai.ImageGenerationResult
import com.example.ai.VeoVideoResult
import com.example.ui.theme.*
import kotlinx.coroutines.launch

enum class StudioCapability(
    val title: String,
    val subtitle: String,
    val iconName: String
) {
    CREATE_EDIT_IMAGE("Create & Edit Image", "Text & image guided creation", "image"),
    ANIMATE_IMAGE_TO_VIDEO("Animate Image to Video", "Veo photo motion synthesis", "movie"),
    GENERATE_VIDEO_FROM_TEXT("Text to Video (Veo)", "AI cinematic video creation", "video_spark"),
    AI_IMAGE_GEN("AI Image Generation", "1K/2K/4K with custom aspect ratios", "palette"),
    AI_IMAGE_EDIT("AI Image Editing", "Style transfer & background edit", "edit"),
    AI_VIDEO_GEN("AI Video Generation", "1080p Veo rendering engine", "videocam"),
    AI_VIDEO_ENHANCE("AI Video Enhancement", "Script, hook & audio enhancer", "auto_awesome"),
    AI_CONTENT_CREATION("AI Content Creation", "Thinking, Grounding & Low-Latency", "psychology")
}

@Composable
fun ViralToolAiStudioDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val responsiveMetrics = LocalResponsiveMetrics.current

    var activeCapability by remember { mutableStateOf(StudioCapability.CREATE_EDIT_IMAGE) }

    // Controls
    var promptInput by remember { mutableStateOf("") }
    var selectedAspectRatio by remember { mutableStateOf("1:1") }
    var selectedResolution by remember { mutableStateOf("1K") }
    var selectedModelMode by remember { mutableStateOf("HIGH_THINKING") } // HIGH_THINKING, LOW_LATENCY, SEARCH_GROUNDED

    // Media input state
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher for image pick
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bytes = stream.readBytes()
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                    var sampleSize = 1
                    while (options.outWidth / sampleSize > 1024 || options.outHeight / sampleSize > 1024) {
                        sampleSize *= 2
                    }
                    val decodeOpts = BitmapFactory.Options().apply {
                        inSampleSize = sampleSize
                    }
                    selectedImageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOpts)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                Toast.makeText(context, "Image load error: ${e.localizedMessage ?: "Failed"}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Output states
    var isGenerating by remember { mutableStateOf(false) }
    var textOutputResult by remember { mutableStateOf<String?>(null) }
    var imageOutputResult by remember { mutableStateOf<ImageGenerationResult?>(null) }
    var videoOutputResult by remember { mutableStateOf<VeoVideoResult?>(null) }

    val aspectRatios = listOf("1:1", "4:5", "3:4", "9:16", "16:9", "2:3", "21:9")
    val resolutions = listOf("1K", "2K", "4K")

    val isSmallPhone = responsiveMetrics.isSmallPhone
    val dialogPadding = if (isSmallPhone) 10.dp else 16.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(dialogPadding),
            contentAlignment = Alignment.Center
        ) {
            CommonPopupAnimation(visible = true) {
                Surface(
                    modifier = Modifier
                        .responsiveDialogBounds(responsiveMetrics)
                        .clip(RoundedCornerShape(26.dp))
                        .border(
                            BorderStroke(
                                1.5.dp,
                                Brush.linearGradient(
                                    listOf(
                                        EmeraldGlow,
                                        ElectricPurple,
                                        EmeraldPrimary
                                    )
                                )
                            ),
                            RoundedCornerShape(26.dp)
                        ),
                    color = Color(0xFF0F1412),
                    shape = RoundedCornerShape(26.dp)
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    // HEADER ROW
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(EmeraldPrimary, ElectricPurple)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Studio Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ViralToolAI Studio",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Flagship Native Gemini & Veo Intelligence",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldGlow
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDismiss()
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CAPABILITY TAB SELECTOR
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(StudioCapability.entries.toTypedArray()) { capability ->
                            val isSelected = capability == activeCapability
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) Brush.horizontalGradient(
                                            listOf(EmeraldPrimary, ElectricPurple)
                                        ) else Brush.horizontalGradient(
                                            listOf(Color(0xFF1B2320), Color(0xFF141A17))
                                        )
                                    )
                                    .border(
                                        BorderStroke(
                                            1.dp,
                                            if (isSelected) EmeraldGlow else Color.White.copy(alpha = 0.12f)
                                        ),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        activeCapability = capability
                                        textOutputResult = null
                                        imageOutputResult = null
                                        videoOutputResult = null
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = when (capability) {
                                            StudioCapability.CREATE_EDIT_IMAGE -> Icons.Default.Image
                                            StudioCapability.ANIMATE_IMAGE_TO_VIDEO -> Icons.Default.Movie
                                            StudioCapability.GENERATE_VIDEO_FROM_TEXT -> Icons.Default.VideoCall
                                            StudioCapability.AI_IMAGE_GEN -> Icons.Default.Palette
                                            StudioCapability.AI_IMAGE_EDIT -> Icons.Default.Edit
                                            StudioCapability.AI_VIDEO_GEN -> Icons.Default.Videocam
                                            StudioCapability.AI_VIDEO_ENHANCE -> Icons.Default.AutoAwesome
                                            StudioCapability.AI_CONTENT_CREATION -> Icons.Default.Psychology
                                        },
                                        contentDescription = capability.title,
                                        tint = if (isSelected) Color.White else TextWhite.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = capability.title,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else TextWhite.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // MAIN SCROLLABLE CONTROLS & WORKSPACE
                    Column(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // CAPABILITY DESCRIPTION BANNER
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF151D1A))
                                .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)), RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = activeCapability.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = activeCapability.subtitle,
                                        fontSize = 10.5.sp,
                                        color = TextWhite.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // IMAGE INPUT SECTION FOR EDIT / ANIMATE
                        if (activeCapability == StudioCapability.CREATE_EDIT_IMAGE ||
                            activeCapability == StudioCapability.ANIMATE_IMAGE_TO_VIDEO ||
                            activeCapability == StudioCapability.AI_IMAGE_EDIT
                        ) {
                            Text(
                                text = "Input Image / Photo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGlow
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF141C19))
                                    .border(
                                        BorderStroke(1.dp, Brush.linearGradient(listOf(EmeraldPrimary, ElectricPurple))),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        imagePickerLauncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                val bmp = selectedImageBitmap
                                if (bmp != null && !bmp.isRecycled) {
                                    Image(
                                        bitmap = bmp.asImageBitmap(),
                                        contentDescription = "Selected Photo",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(6.dp)
                                            .clip(CircleShape)
                                            .background(Color.Black.copy(alpha = 0.7f))
                                            .clickable { selectedImageBitmap = null }
                                            .padding(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove photo",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = "Upload Photo",
                                            tint = EmeraldGlow,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Tap to choose photo from gallery",
                                            fontSize = 11.sp,
                                            color = TextWhite.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // ASPECT RATIO SELECTOR (For Image & Video tools)
                        if (activeCapability != StudioCapability.AI_CONTENT_CREATION &&
                            activeCapability != StudioCapability.AI_VIDEO_ENHANCE
                        ) {
                            Text(
                                text = "Select Aspect Ratio",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGlow
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(aspectRatios) { ratio ->
                                    val isSelected = ratio == selectedAspectRatio
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) EmeraldPrimary else Color(0xFF1B2320))
                                            .border(
                                                BorderStroke(1.dp, if (isSelected) EmeraldGlow else Color.White.copy(alpha = 0.1f)),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                selectedAspectRatio = ratio
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = ratio,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else TextWhite.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // RESOLUTION SELECTOR (For AI Image Gen)
                        if (activeCapability == StudioCapability.AI_IMAGE_GEN) {
                            Text(
                                text = "Resolution Quality",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGlow
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                resolutions.forEach { res ->
                                    val isSelected = res == selectedResolution
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) ElectricPurple else Color(0xFF1B2320))
                                            .border(
                                                BorderStroke(1.dp, if (isSelected) Color.White else Color.White.copy(alpha = 0.1f)),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                selectedResolution = res
                                            }
                                            .padding(horizontal = 16.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = res,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // MODEL / INTELLIGENCE MODE SELECTOR (For AI Content Creation)
                        if (activeCapability == StudioCapability.AI_CONTENT_CREATION) {
                            Text(
                                text = "Gemini Intelligence Engine Mode",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGlow
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(
                                    Triple("HIGH_THINKING", "🧠 High Thinking Mode", "gemini-3.1-pro-preview"),
                                    Triple("SEARCH_GROUNDED", "🌐 Search Grounding", "gemini-3.5-flash"),
                                    Triple("LOW_LATENCY", "⚡ Low Latency", "gemini-3.1-flash-lite")
                                ).forEach { (modeKey, modeLabel, _) ->
                                    val isSelected = modeKey == selectedModelMode
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) EmeraldPrimary else Color(0xFF1B2320))
                                            .border(
                                                BorderStroke(1.dp, if (isSelected) EmeraldGlow else Color.White.copy(alpha = 0.1f)),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                selectedModelMode = modeKey
                                            }
                                            .padding(vertical = 8.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = modeLabel,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else TextWhite.copy(alpha = 0.8f),
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // PROMPT TEXT FIELD
                        Text(
                            text = "AI Prompt Instruction",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGlow
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = promptInput,
                            onValueChange = { promptInput = it },
                            placeholder = {
                                Text(
                                    text = when (activeCapability) {
                                        StudioCapability.CREATE_EDIT_IMAGE -> "Describe what to generate or edit..."
                                        StudioCapability.ANIMATE_IMAGE_TO_VIDEO -> "Describe video motion (e.g., slow cinematic camera sweep)..."
                                        StudioCapability.GENERATE_VIDEO_FROM_TEXT -> "Describe the video scene (e.g., cyber futuristic city in rain)..."
                                        StudioCapability.AI_IMAGE_GEN -> "Enter detailed image prompt..."
                                        StudioCapability.AI_IMAGE_EDIT -> "Describe photo edit changes..."
                                        StudioCapability.AI_VIDEO_GEN -> "Describe Veo 3D video sequence..."
                                        StudioCapability.AI_VIDEO_ENHANCE -> "Paste video script or title to enhance..."
                                        StudioCapability.AI_CONTENT_CREATION -> "Ask Gemini anything or describe content to create..."
                                    },
                                    fontSize = 11.5.sp,
                                    color = TextWhite.copy(alpha = 0.4f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(95.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF141A17),
                                unfocusedContainerColor = Color(0xFF101513),
                                focusedBorderColor = EmeraldGlow,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // EXECUTE ACTION BUTTON
                        Button(
                            onClick = {
                                if (promptInput.isBlank()) {
                                    Toast.makeText(context, "Please enter an AI prompt instruction", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isGenerating = true
                                textOutputResult = null
                                imageOutputResult = null
                                videoOutputResult = null

                                coroutineScope.launch {
                                    try {
                                        when (activeCapability) {
                                            StudioCapability.CREATE_EDIT_IMAGE,
                                            StudioCapability.AI_IMAGE_EDIT -> {
                                                val res = GeminiStudioNativeEngine.editImage(
                                                    prompt = promptInput,
                                                    inputBitmap = selectedImageBitmap,
                                                    aspectRatio = selectedAspectRatio
                                                )
                                                imageOutputResult = res
                                            }

                                            StudioCapability.AI_IMAGE_GEN -> {
                                                val res = GeminiStudioNativeEngine.generateImage(
                                                    prompt = promptInput,
                                                    aspectRatio = selectedAspectRatio,
                                                    imageSize = selectedResolution
                                                )
                                                imageOutputResult = res
                                            }

                                            StudioCapability.ANIMATE_IMAGE_TO_VIDEO,
                                            StudioCapability.GENERATE_VIDEO_FROM_TEXT,
                                            StudioCapability.AI_VIDEO_GEN -> {
                                                val res = GeminiStudioNativeEngine.generateVeoVideo(
                                                    prompt = promptInput,
                                                    aspectRatio = if (selectedAspectRatio in listOf("16:9", "9:16")) selectedAspectRatio else "16:9",
                                                    resolution = "1080p",
                                                    inputImageBitmap = if (activeCapability == StudioCapability.ANIMATE_IMAGE_TO_VIDEO) selectedImageBitmap else null
                                                )
                                                videoOutputResult = res
                                            }

                                            StudioCapability.AI_VIDEO_ENHANCE -> {
                                                val res = com.example.creatoracademy.ViralAiMentorEngine.generateIntegratedMentorResponse(
                                                    domain = com.example.creatoracademy.MentorToolDomain.AI_VIDEO_IMAGE_GENERATOR,
                                                    userQuery = "Enhance video script, hooks & audio directions for: $promptInput",
                                                    userContext = "ViralToolAI Video Studio Enhancement",
                                                    language = "English"
                                                )
                                                textOutputResult = res
                                            }

                                            StudioCapability.AI_CONTENT_CREATION -> {
                                                val res = com.example.creatoracademy.ViralAiMentorEngine.generateIntegratedMentorResponse(
                                                    domain = com.example.creatoracademy.MentorToolDomain.AI_VIDEO_IMAGE_GENERATOR,
                                                    userQuery = promptInput,
                                                    userContext = "ViralToolAI Studio Content Creation - Mode: $selectedModelMode",
                                                    language = "English"
                                                )
                                                textOutputResult = res
                                            }
                                        }
                                    } catch (e: Throwable) {
                                        e.printStackTrace()
                                        textOutputResult = "⚠️ AI Studio Notice:\n${e.localizedMessage ?: "Processing completed with offline AI guidance fallback."}"
                                    } finally {
                                        isGenerating = false
                                    }
                                }
                            },
                            enabled = !isGenerating,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = EmeraldGlow),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPrimary,
                                disabledContainerColor = Color.Gray
                            )
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Processing AI Request...",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Generate",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Generate with ViralToolAI Studio",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // OUTPUT RESULTS DISPLAY AREA
                        if (textOutputResult != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF141E1A))
                                    .border(BorderStroke(1.dp, EmeraldGlow), RoundedCornerShape(16.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = EmeraldGlow,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Gemini Intelligence Response",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGlow
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = textOutputResult!!,
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }

                        if (imageOutputResult != null) {
                            val imgRes = imageOutputResult!!
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF141E1A))
                                    .border(BorderStroke(1.dp, ElectricPurple), RoundedCornerShape(16.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Palette,
                                            contentDescription = null,
                                            tint = ElectricPurple,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "AI Image Generation Result (${imgRes.aspectRatio} | ${imgRes.imageSize})",
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ElectricPurple
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = imgRes.summaryText,
                                        fontSize = 11.5.sp,
                                        color = TextWhite
                                    )
                                }
                            }
                        }

                        if (videoOutputResult != null) {
                            val vidRes = videoOutputResult!!
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF191324))
                                    .border(BorderStroke(1.dp, ElectricPurple), RoundedCornerShape(16.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Movie,
                                            contentDescription = null,
                                            tint = ElectricPurple,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Veo 3.1 Fast Video Output (${vidRes.aspectRatio} | ${vidRes.resolution})",
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ElectricPurple
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = vidRes.videoSummary,
                                        fontSize = 11.5.sp,
                                        color = TextWhite
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
}
