package com.example.vision

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ocr.OcrScanStage
import com.example.ui.components.OcrScannerOverlay
import com.example.ui.theme.*
import kotlinx.coroutines.delay

/**
 * SHOPTOOLAI Phase 8C — Premium AI Vision Scan Dialog with Universal OCR
 */
@Composable
fun VisionScanDialog(
    source: VisionSource,
    imageUriOrPath: String,
    onDismiss: () -> Unit,
    onScanComplete: (String) -> Unit
) {
    var currentStage by remember { mutableStateOf(VisionScanStage.PREPARING) }
    var isLowQualityTest by remember { mutableStateOf(false) }

    val mappedOcrStage = when (currentStage) {
        VisionScanStage.UPLOADING_IMAGE, VisionScanStage.PREPARING -> OcrScanStage.PREPARING_IMAGE
        VisionScanStage.CONNECTING_GEMINI, VisionScanStage.ENHANCING -> OcrScanStage.READING_TEXT
        VisionScanStage.READING_SCREENSHOT, VisionScanStage.AI_RUNNING -> OcrScanStage.DETECTING_LAYOUT
        VisionScanStage.GENERATING_INSIGHTS, VisionScanStage.DETECTING_PRODUCT, VisionScanStage.MATCHING_MERCHANT -> OcrScanStage.EXTRACTING_INFO
        VisionScanStage.PREPARING_REPORT, VisionScanStage.CREATING_REPORT -> OcrScanStage.GENERATING_REPORT
        VisionScanStage.COMPLETED -> OcrScanStage.COMPLETED
        VisionScanStage.ERROR_LOW_QUALITY -> OcrScanStage.ERROR
    }

    LaunchedEffect(source, imageUriOrPath, isLowQualityTest) {
        val testPath = if (isLowQualityTest) "blur_error_sample.jpg" else imageUriOrPath
        AiVisionEngine.processImagePipeline(
            source = source,
            imageUriOrPath = testPath,
            onStageUpdate = { stage ->
                currentStage = stage
            }
        )
        if (currentStage == VisionScanStage.COMPLETED) {
            delay(200)
            onScanComplete("https://www.amazon.in/dp/B0CX234P5D")
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xEE000000))
                .statusBarsPadding()
                .imePadding()
                .navigationBarsPadding()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        BorderStroke(1.2.dp, Brush.linearGradient(listOf(CrimsonRed, CrimsonLight, Color(0x33FFFFFF)))),
                        RoundedCornerShape(24.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = AmoledBlack)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Row with Close
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(CrimsonRed.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CenterFocusWeak,
                                    contentDescription = "AI Vision",
                                    tint = CrimsonLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "Universal Vision & OCR Scanner",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Luxury OcrScannerOverlay with laser, particles, matrix grid
                    OcrScannerOverlay(
                        currentStage = mappedOcrStage,
                        errorMessage = if (currentStage == VisionScanStage.ERROR_LOW_QUALITY) "Image resolution is too low or blurred. Please upload a clearer screenshot." else null
                    )

                    if (currentStage == VisionScanStage.ERROR_LOW_QUALITY) {
                        Button(
                            onClick = { isLowQualityTest = false },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Upload Clearer Image", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Test Mode Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isLowQualityTest = !isLowQualityTest },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isLowQualityTest) "Switch to Clear Image Mode" else "Simulate Low Quality Image Check",
                            fontSize = 10.sp,
                            color = TextGray.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
