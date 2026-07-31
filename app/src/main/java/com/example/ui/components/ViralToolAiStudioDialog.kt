package com.example.ui.components

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ai.AiPromptExtractorEngine
import com.example.ai.PromptExtractorResult
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ViralToolAiStudioDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()
    val responsiveMetrics = LocalResponsiveMetrics.current

    // Media input state
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var promptInput by remember { mutableStateOf("") }

    // Analysis output state
    var isAnalyzing by remember { mutableStateOf(false) }
    var extractionResult by remember { mutableStateOf<PromptExtractorResult?>(null) }

    // Creator Academy Guide Modal State
    var showCreatorAcademyGuide by remember { mutableStateOf(false) }

    // Auto-analysis trigger upon picking image
    fun triggerAutoVisionAnalysis(bmp: Bitmap?, textNotes: String?) {
        if (bmp == null && textNotes.isNullOrBlank()) return
        isAnalyzing = true
        extractionResult = null
        coroutineScope.launch {
            try {
                val res = AiPromptExtractorEngine.extractPromptFromImage(
                    bitmap = bmp,
                    userNotes = textNotes
                )
                extractionResult = res
            } catch (e: Throwable) {
                e.printStackTrace()
                Toast.makeText(context, "Vision analysis fallback applied: ${e.localizedMessage ?: "Done"}", Toast.LENGTH_SHORT).show()
            } finally {
                isAnalyzing = false
            }
        }
    }

    // Launcher for image pick
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bytes = stream.readBytes()
                    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                    var sampleSize = 1
                    while (options.outWidth / sampleSize > 1024 || options.outHeight / sampleSize > 1024) {
                        sampleSize *= 2
                    }
                    val decodeOpts = BitmapFactory.Options().apply { inSampleSize = sampleSize }
                    val loadedBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOpts)
                    selectedImageBitmap = loadedBmp

                    // Automatically analyze immediately after upload!
                    triggerAutoVisionAnalysis(loadedBmp, promptInput)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to load image: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val isSmallPhone = responsiveMetrics.isSmallPhone
    val dialogPadding = if (isSmallPhone) 8.dp else 14.dp

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
                .background(Color.Black.copy(alpha = 0.88f))
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
                                        Color(0xFFA78BFA),
                                        ElectricPurple,
                                        Color(0xFF8B5CF6)
                                    )
                                )
                            ),
                            RoundedCornerShape(26.dp)
                        ),
                    color = Color(0xFF100B1E),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 14.dp)
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
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x338B5CF6))
                                        .border(BorderStroke(1.dp, Color(0xFF8B5CF6)), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Prompt Extractor Icon",
                                        tint = Color(0xFFA78BFA),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "AI Prompt Extractor",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Language Badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x338B5CF6))
                                        .border(BorderStroke(0.8.dp, Color(0xFF8B5CF6)), RoundedCornerShape(10.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "🌍 Multi-Lang",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFA78BFA)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onDismiss()
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.08f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = TextWhite,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // LANGUAGE SUPPORT BANNER
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF1B142E), Color(0xFF18152B))
                                    )
                                )
                                .border(
                                    BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.4f)),
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "🌍 Write in any language.",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Hindi, English, Hinglish, Urdu, Bengali, Tamil, Telugu, Marathi, Gujarati, Punjabi and many more are supported.",
                                    fontSize = 10.sp,
                                    color = TextWhite.copy(alpha = 0.85f),
                                    lineHeight = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Apni baat kisi bhi language mein likhiye. AI automatically samajh lega.",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFA78BFA)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // IMPORTANT NOTICE NOTE
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF1A1724))
                                .border(
                                    BorderStroke(1.dp, ElectricPurple.copy(alpha = 0.35f)),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = ElectricPurple,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "This tool creates the best possible recreation prompt based on AI analysis. Results may vary depending on the AI model.",
                                    fontSize = 9.5.sp,
                                    color = TextWhite.copy(alpha = 0.8f),
                                    lineHeight = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // MAIN SCROLLABLE CONTROLS & RESULTS
                        Column(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // IMAGE UPLOAD SECTION
                            Text(
                                text = "1. Upload AI Image or Screenshot",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA78BFA)
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF161026))
                                    .border(
                                        BorderStroke(
                                            1.2.dp,
                                            Brush.linearGradient(
                                                listOf(
                                                    Color(0xFF8B5CF6),
                                                    ElectricPurple
                                                )
                                            )
                                        ),
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
                                        contentDescription = "Uploaded AI Photo",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(6.dp)
                                            .clip(CircleShape)
                                            .background(Color.Black.copy(alpha = 0.75f))
                                            .clickable {
                                                selectedImageBitmap = null
                                                extractionResult = null
                                            }
                                            .padding(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove photo",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = "Upload Photo",
                                            tint = Color(0xFFA78BFA),
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Tap to upload AI image or screenshot from gallery",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "AI vision will automatically analyze immediately upon upload",
                                            fontSize = 9.5.sp,
                                            color = TextWhite.copy(alpha = 0.65f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // OPTIONAL USER TEXT IDEA FIELD
                            Text(
                                text = "2. Additional Idea or Notes (Optional)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA78BFA)
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = promptInput,
                                onValueChange = { promptInput = it },
                                placeholder = {
                                    Text(
                                        text = "Describe your idea in any language...",
                                        fontSize = 11.sp,
                                        color = TextWhite.copy(alpha = 0.45f)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(72.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF1B142E),
                                    unfocusedContainerColor = Color(0xFF161026),
                                    focusedBorderColor = Color(0xFFA78BFA),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // EXTRACT ACTION BUTTON
                            Button(
                                onClick = {
                                    if (selectedImageBitmap == null && promptInput.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Please upload an image or describe your idea",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    triggerAutoVisionAnalysis(selectedImageBitmap, promptInput)
                                },
                                enabled = !isAnalyzing,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFFA78BFA)),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF8B5CF6),
                                    disabledContainerColor = Color.Gray
                                )
                            ) {
                                if (isAnalyzing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Analyzing Vision & Generating Prompt...",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Extract Prompt",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (extractionResult != null) "Re-Analyze & Extract Prompt ✦" else "Extract Recreation Prompt ✦",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // EXTRACTION RESULTS VIEW
                            val result = extractionResult
                            if (result != null) {
                                Column(modifier = Modifier.fillMaxWidth()) {

                                    // FINAL USER MESSAGE BANNER
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(Color(0xFF8B5CF6), ElectricPurple)
                                                )
                                            )
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Now copy this prompt and generate your image using Gemini or ChatGPT.",
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.White,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // RECREATION PROMPT CARD
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF1B122E))
                                            .border(
                                                BorderStroke(1.dp, Color(0xFFA78BFA)),
                                                RoundedCornerShape(16.dp)
                                            )
                                            .padding(14.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.ContentCopy,
                                                        contentDescription = null,
                                                        tint = Color(0xFFA78BFA),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "Detailed Recreation Prompt",
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFA78BFA)
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color(0xFF8B5CF6))
                                                        .clickable {
                                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            clipboardManager.setText(AnnotatedString(result.recreationPrompt))
                                                            Toast.makeText(
                                                                context,
                                                                "Recreation Prompt Copied!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = "Copy Prompt",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = result.recreationPrompt,
                                                fontSize = 11.5.sp,
                                                color = TextWhite,
                                                lineHeight = 17.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // NEGATIVE PROMPT CARD
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0xFF22161A))
                                            .border(
                                                BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f)),
                                                RoundedCornerShape(14.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Block,
                                                        contentDescription = null,
                                                        tint = Color(0xFFFF5252),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "Negative Prompt",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFFF5252)
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color(0xFFD32F2F))
                                                        .clickable {
                                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            clipboardManager.setText(AnnotatedString(result.negativePrompt))
                                                            Toast.makeText(
                                                                context,
                                                                "Negative Prompt Copied!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = "Copy Negative",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(
                                                text = result.negativePrompt,
                                                fontSize = 10.5.sp,
                                                color = TextWhite.copy(alpha = 0.9f),
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // KEY METRICS & STYLE KEYWORDS
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF161E23))
                                                .border(
                                                    BorderStroke(1.dp, ElectricPurple.copy(alpha = 0.4f)),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(10.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Best AI Model",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = ElectricPurple
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = result.recommendedModel,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite
                                                )
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF1B142E))
                                                .border(
                                                    BorderStroke(1.dp, Color(0xFFA78BFA).copy(alpha = 0.4f)),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(10.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Aspect Ratio",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFA78BFA)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = result.recommendedAspectRatio,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // STYLE CHIPS
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(result.styleKeywords) { kw ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0x338B5CF6))
                                                    .border(
                                                        BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.3f)),
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "#$kw",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color(0xFFA78BFA)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // DETECTED DETAILS BREAKDOWN CARD
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0xFF18122B))
                                            .border(
                                                BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
                                                RoundedCornerShape(14.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Visibility,
                                                    contentDescription = null,
                                                    tint = Color(0xFFA78BFA),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "AI Vision Detected Breakdown",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            val det = result.detectedDetails
                                            val itemsList = listOf(
                                                "• Main Subject" to det.mainSubject,
                                                "• Composition" to det.composition,
                                                "• Camera Angle" to det.cameraAngle,
                                                "• Pose" to det.pose,
                                                "• Facial Expression" to det.facialExpression,
                                                "• Lighting" to det.lighting,
                                                "• Colors" to det.colors,
                                                "• Background" to det.background,
                                                "• Mood" to det.mood,
                                                "• Style" to det.style,
                                                "• Materials" to det.materials,
                                                "• Rendering Quality" to det.renderingQuality,
                                                "• Lens/Camera Style" to det.lensCameraStyle,
                                                "• Fine Details" to det.fineArtisticDetails
                                            )

                                            itemsList.forEach { (label, value) ->
                                                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                                    Text(
                                                        text = "$label: ",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFA78BFA)
                                                    )
                                                    Text(
                                                        text = value,
                                                        fontSize = 10.sp,
                                                        color = TextWhite.copy(alpha = 0.85f)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // ACTION BUTTONS ROW
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    clipboardManager.setText(AnnotatedString(result.recreationPrompt))
                                                    Toast.makeText(
                                                        context,
                                                        "Prompt Copied to Clipboard!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(42.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Copy Prompt",
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    clipboardManager.setText(AnnotatedString(result.negativePrompt))
                                                    Toast.makeText(
                                                        context,
                                                        "Negative Prompt Copied!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(42.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Block,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Copy Negative",
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    try {
                                                        val sendIntent: Intent = Intent().apply {
                                                            action = Intent.ACTION_SEND
                                                            putExtra(
                                                                Intent.EXTRA_TEXT,
                                                                "🔥 Extracted AI Recreation Prompt via ViralToolAi:\n\n${result.recreationPrompt}\n\nNegative Prompt:\n${result.negativePrompt}"
                                                            )
                                                            type = "text/plain"
                                                        }
                                                        val shareIntent = Intent.createChooser(sendIntent, "Share Prompt")
                                                        context.startActivity(shareIntent)
                                                    } catch (_: Exception) {
                                                        Toast.makeText(context, "Sharing unavailable", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(42.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Share,
                                                    contentDescription = null,
                                                    tint = TextWhite,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Share",
                                                    fontSize = 11.5.sp,
                                                    color = TextWhite,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    showCreatorAcademyGuide = true
                                                },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(42.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.School,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Creator Academy",
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold
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
    }

    // CREATOR ACADEMY GUIDE MODAL
    if (showCreatorAcademyGuide) {
        Dialog(
            onDismissRequest = { showCreatorAcademyGuide = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .clip(RoundedCornerShape(24.dp))
                        .border(
                            BorderStroke(1.5.dp, Brush.linearGradient(listOf(ElectricPurple, Color(0xFFA78BFA)))),
                            RoundedCornerShape(24.dp)
                        ),
                    color = Color(0xFF141022)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = ElectricPurple,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Creator Academy Guide",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                            }

                            IconButton(
                                onClick = { showCreatorAcademyGuide = false },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            GuideTopicCard(
                                title = "1. Which AI Model to Use",
                                icon = Icons.Default.PrecisionManufacturing,
                                content = "• Midjourney v6.1: Best for hyper-realistic photos, 3D character art & complex textures.\n" +
                                        "• Flux.1 Schnell / Dev: Best open-source model with crisp text rendering and photorealism.\n" +
                                        "• DALL-E 3 (ChatGPT Plus): Best for vibrant creative compositions, posters & surreal art.\n" +
                                        "• Gemini Imagen 3: Fast, realistic photography with natural lighting."
                            )

                            GuideTopicCard(
                                title = "2. Which Generation Mode to Select",
                                icon = Icons.Default.Tune,
                                content = "• Raw Mode (--style raw): Disables default AI smooth filters for camera grain realism.\n" +
                                        "• Stylize (--stylize 250 - 750): Increases artistic flair and dramatic lighting.\n" +
                                        "• Quality (--quality 2): Max rendering steps for razor-sharp micro details."
                            )

                            GuideTopicCard(
                                title = "3. Which Aspect Ratio to Choose",
                                icon = Icons.Default.AspectRatio,
                                content = "• 16:9 (--ar 16:9): Perfect for YouTube Thumbnails, Banners & Desktop Wallpapers.\n" +
                                        "• 9:16 (--ar 9:16): Essential for Instagram Reels, TikTok & YouTube Shorts.\n" +
                                        "• 1:1 or 4:5 (--ar 1:1): Ideal for Instagram Posts & Profile Feeds."
                            )

                            GuideTopicCard(
                                title = "4. Where to Paste the Generated Prompt",
                                icon = Icons.Default.ContentPaste,
                                content = "1. Copy the Recreation Prompt using the 'Copy Prompt' button above.\n" +
                                        "2. Open ChatGPT (DALL-E 3), Midjourney (/imagine prompt: ...), or Flux.1 UI.\n" +
                                        "3. Paste the prompt directly into the input box.\n" +
                                        "4. Paste the Negative Prompt into the negative/exclude box."
                            )

                            GuideTopicCard(
                                title = "5. How to Get a Very Similar Result",
                                icon = Icons.Default.AutoAwesome,
                                content = "• Keep the Negative Prompt active to remove blurry artifacts or distorted hands.\n" +
                                        "• If using Midjourney, attach your screenshot image as an Image Weight prompt (--iw 2.0).\n" +
                                        "• Maintain camera lens specifications (e.g. 85mm f/1.4 prime lens) in the prompt."
                            )

                            GuideTopicCard(
                                title = "6. Quality Improvement Tips",
                                icon = Icons.Default.Star,
                                content = "• Include lighting cues: 'volumetric rim light', 'dual-tone key lighting', 'softbox studio'.\n" +
                                        "• Include render engines: 'Octane Render', 'Unreal Engine 5', '8K Ray Tracing'.\n" +
                                        "• Specify material details: 'subsurface scattering', 'brushed aluminum', 'soft silk texture'."
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { showCreatorAcademyGuide = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
                        ) {
                            Text(
                                text = "Got it! Back to AI Prompt Extractor",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GuideTopicCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1A132B))
            .border(BorderStroke(1.dp, Color(0xFFA78BFA).copy(alpha = 0.35f)), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFA78BFA),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA78BFA)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content,
                fontSize = 11.sp,
                color = TextWhite,
                lineHeight = 16.sp
            )
        }
    }
}
