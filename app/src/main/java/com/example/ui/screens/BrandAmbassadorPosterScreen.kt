package com.example.ui.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cloud.LiveCloudManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private const val TAG = "BrandAmbassadorPoster"
private const val POSTER_IMAGE_URL =
    "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/a7996a261d91d703ea1e41a90cba30233d85b80a/Picsart_26-07-26_21-11-24-122.jpg"

object BrandAmbassadorPrefs {
    private const val PREF_NAME = "viraltoolai_launch_prefs"
    private const val KEY_FIRST_TIME = "has_seen_first_launch_welcome"
    private const val KEY_LAST_MSG = "last_launch_msg_index"

    fun isFirstTimeLaunch(context: Context): Boolean {
        return try {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            !prefs.getBoolean(KEY_FIRST_TIME, false)
        } catch (e: Exception) {
            Log.e(TAG, "Error checking first time launch status", e)
            true
        }
    }

    fun setFirstTimeLaunchCompleted(context: Context) {
        try {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_FIRST_TIME, true).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error setting first time launch completed", e)
        }
    }

    fun getRandomStatusMessage(context: Context): String {
        val defaultMessages = listOf(
            "Create. Inspire. Repeat.",
            "Your Creative Studio is Ready.",
            "Edit Better. Grow Faster.",
            "Premium Creator Mode Enabled.",
            "Your Next Viral Reel Starts Here.",
            "AI Workspace Ready.",
            "Learn Something New Today.",
            "Great Creators Never Stop Learning.",
            "Build. Learn. Earn.",
            "Preparing Your Creative Tools.",
            "Success Starts With One Video.",
            "Lights On. Creativity Begins.",
            "Every Reel Can Change Your Future.",
            "Let's Create Something Amazing."
        )
        return try {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val lastIdx = prefs.getInt(KEY_LAST_MSG, -1)
            var newIdx = (0 until defaultMessages.size).random()
            if (defaultMessages.size > 1 && newIdx == lastIdx) {
                newIdx = (newIdx + 1) % defaultMessages.size
            }
            prefs.edit().putInt(KEY_LAST_MSG, newIdx).apply()
            defaultMessages[newIdx]
        } catch (e: Exception) {
            Log.e(TAG, "Error getting random status message", e)
            defaultMessages.random()
        }
    }
}

@Composable
fun BrandAmbassadorPosterScreen(
    onDismiss: () -> Unit,
    onExploreClicked: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val isPreviewMode = LocalInspectionMode.current

    val baConfig by LiveCloudManager.brandAmbassadorConfig.collectAsStateWithLifecycle()

    LaunchedEffect(baConfig.enabled) {
        if (!baConfig.enabled) {
            onDismiss()
        }
    }

    val isFirstTime = remember {
        try {
            if (isPreviewMode) true else BrandAmbassadorPrefs.isFirstTimeLaunch(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error in BrandAmbassadorPosterScreen initial state", e)
            true
        }
    }

    var isExiting by remember { mutableStateOf(false) }

    val handleExit = remember {
        {
            if (!isExiting) {
                isExiting = true
                try {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                } catch (_: Exception) {}

                try {
                    if (isFirstTime) {
                        BrandAmbassadorPrefs.setFirstTimeLaunchCompleted(context)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error setting completion flag", e)
                }

                try {
                    if (onExploreClicked != null) {
                        onExploreClicked()
                    } else {
                        onDismiss()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error invoking exit callback", e)
                    onDismiss()
                }
            }
        }
    }

    val animatedExitAlpha by animateFloatAsState(
        targetValue = if (isExiting) 0f else 1f,
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "exitAlpha"
    )

    val animatedExitScale by animateFloatAsState(
        targetValue = if (isExiting) 0.97f else 1f,
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "exitScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = animatedExitAlpha
                scaleX = animatedExitScale
                scaleY = animatedExitScale
            }
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        if (isFirstTime) {
            FirstTimeWelcomeView(
                onFinish = handleExit,
                isPreviewMode = isPreviewMode
            )
        } else {
            ReturningUserStatusView(
                onFinish = handleExit,
                isPreviewMode = isPreviewMode
            )
        }
    }
}

@Composable
private fun PosterHeroImage(
    zoomScale: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(POSTER_IMAGE_URL)
                .crossfade(true)
                .build(),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
            },
            contentDescription = "ViralToolAI Brand Ambassador Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = zoomScale
                    scaleY = zoomScale
                }
        )
    }
}

@Composable
private fun FirstTimeWelcomeView(
    onFinish: () -> Unit,
    isPreviewMode: Boolean
) {
    val posterZoomScale = remember { Animatable(1.0f) }
    val contentAlpha = remember { Animatable(0f) }
    val signatureProgress = remember { Animatable(0f) }
    val shineProgress = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "welcomeBgAnims")
    val ambientPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambientPulse"
    )

    val reflectionOffset by infiniteTransition.animateFloat(
        initialValue = -500f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "reflectionOffset"
    )

    LaunchedEffect(Unit) {
        if (isPreviewMode) {
            contentAlpha.snapTo(1f)
            signatureProgress.snapTo(1f)
            shineProgress.snapTo(0.5f)
            return@LaunchedEffect
        }

        try {
            // Camera Zoom: 100% to 102% over 5 seconds
            launch {
                try {
                    posterZoomScale.animateTo(
                        targetValue = 1.02f,
                        animationSpec = tween(5000, easing = LinearEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Zoom animation error", e)
                }
            }

            // Fade in text block
            launch {
                try {
                    contentAlpha.animateTo(1f, tween(500, easing = FastOutSlowInEasing))
                } catch (e: Exception) {
                    Log.e(TAG, "Content alpha animation error", e)
                }
            }

            // Animated Handwritten Signature starts after "Thanks For Downloading" (delay 800ms)
            // Complete full handwriting animation within 1 second (1000ms)
            delay(800)
            launch {
                try {
                    signatureProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(1000, easing = FastOutSlowInEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Signature animation error", e)
                }
            }

            // Immediately after signature finishes (~1.8s mark), small shine passes once
            delay(1000)
            launch {
                try {
                    shineProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(900, easing = FastOutSlowInEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Shine animation error", e)
                }
            }

            // Total screen duration: exactly 5 seconds
            delay(2200)
            onFinish()
        } catch (e: Exception) {
            Log.e(TAG, "Fatal LaunchedEffect exception in FirstTimeWelcomeView", e)
            onFinish()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hero Poster Image with subtle camera zoom
        PosterHeroImage(zoomScale = posterZoomScale.value)

        // Glass Reflection Light Sweep
        if (!isPreviewMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                try {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.06f),
                                Color.Transparent
                            ),
                            start = Offset(reflectionOffset, 0f),
                            end = Offset(reflectionOffset + 250f, size.height)
                        )
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Canvas reflection sweep error", e)
                }
            }
        }

        // Ambient Floating Particles
        if (!isPreviewMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                try {
                    val numParticles = 16
                    for (i in 0 until numParticles) {
                        val px = (sin((i * 1.6f + ambientPulse * 2.5f)) * 0.48f + 0.5f) * size.width
                        val py = (cos((i * 2.2f + ambientPulse * 2.0f)) * 0.48f + 0.5f) * size.height
                        val radius = (1.5f + (i % 3) * 1.5f).dp.toPx()
                        drawCircle(
                            color = EmeraldGlow.copy(alpha = (0.20f + 0.25f * sin(i + ambientPulse))),
                            radius = radius,
                            center = Offset(px, py)
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Canvas ambient particles error", e)
                }
            }
        }

        // Lower Third Vignette Gradient (keeps face completely untouched and 100% bright)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0x66000000),
                            Color(0xCC000000),
                            Color(0xFA000000)
                        )
                    )
                )
        )

        // Lower Third Typography & Signature
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .graphicsLayer { alpha = contentAlpha.value },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "WELCOME TO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite.copy(alpha = 0.70f),
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "VIRALTOOLAI",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Build Your Creator Journey",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = EmeraldGlow,
                letterSpacing = 0.6.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Thanks For Downloading ❤️",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Fast Real Ink Animated Signature "Asit Yadav"
            AnimatedRealInkSignature(
                progress = signatureProgress.value,
                shineProgress = shineProgress.value,
                modifier = Modifier
                    .width(230.dp)
                    .height(75.dp)
            )
        }
    }
}

@Composable
private fun AnimatedRealInkSignature(
    progress: Float,
    shineProgress: Float,
    modifier: Modifier = Modifier
) {
    val signaturePath = remember {
        Path().apply {
            // 'A'
            moveTo(20f, 60f)
            cubicTo(15f, 38f, 32f, 15f, 42f, 15f)
            cubicTo(48f, 15f, 45f, 48f, 52f, 60f)
            cubicTo(28f, 40f, 56f, 40f, 64f, 38f)

            // 's'
            cubicTo(70f, 30f, 74f, 28f, 76f, 32f)
            cubicTo(78f, 42f, 68f, 50f, 80f, 46f)

            // 'i'
            cubicTo(84f, 35f, 88f, 34f, 88f, 48f)

            // 't'
            cubicTo(92f, 18f, 96f, 15f, 96f, 48f)
            cubicTo(98f, 48f, 106f, 46f, 110f, 44f)

            // Space & 'Y'
            moveTo(128f, 20f)
            cubicTo(122f, 32f, 134f, 48f, 142f, 44f)
            cubicTo(148f, 38f, 155f, 18f, 155f, 18f)
            cubicTo(155f, 32f, 144f, 75f, 136f, 78f)
            cubicTo(130f, 80f, 126f, 68f, 145f, 48f)

            // 'a'
            cubicTo(152f, 36f, 160f, 34f, 162f, 42f)
            cubicTo(164f, 50f, 158f, 50f, 168f, 46f)

            // 'd'
            cubicTo(172f, 36f, 178f, 18f, 178f, 48f)
            cubicTo(180f, 48f, 184f, 42f, 187f, 46f)

            // 'a'
            cubicTo(190f, 38f, 196f, 36f, 197f, 44f)
            cubicTo(198f, 50f, 194f, 50f, 204f, 42f)

            // 'v'
            cubicTo(207f, 48f, 212f, 50f, 216f, 35f)

            // Sweeping underline flourish
            cubicTo(218f, 30f, 220f, 55f, 202f, 62f)
            cubicTo(155f, 70f, 65f, 68f, 28f, 64f)
        }
    }

    val iDotPath = remember {
        Path().apply {
            addOval(Rect(86f, 20f, 90f, 24f))
        }
    }

    val tCrossPath = remember {
        Path().apply {
            moveTo(88f, 28f)
            lineTo(104f, 26f)
        }
    }

    Canvas(modifier = modifier) {
        try {
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(signaturePath, false)
            val totalLength = pathMeasure.length

            val currentDrawLength = (totalLength * progress.coerceIn(0f, 1f)).coerceAtLeast(0f)
            val animatedPath = Path()
            if (currentDrawLength > 0f) {
                pathMeasure.getSegment(0f, currentDrawLength, animatedPath, true)
            }

            // Subtle depth stroke (Real Ink effect)
            drawPath(
                path = animatedPath,
                color = Color.Black.copy(alpha = 0.40f),
                style = Stroke(width = 3.0.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Main White Real Ink Autograph Stroke
            drawPath(
                path = animatedPath,
                color = TextWhite,
                style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            if (progress > 0.40f) {
                drawPath(
                    path = iDotPath,
                    color = TextWhite,
                    style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
                )
                drawPath(
                    path = tCrossPath,
                    color = TextWhite,
                    style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Small Light Shine Pass
            if (shineProgress > 0f && shineProgress < 1f) {
                val shineX = size.width * (shineProgress * 1.4f - 0.2f)
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.70f),
                            Color.Transparent
                        ),
                        startX = shineX - 30f,
                        endX = shineX + 30f
                    ),
                    blendMode = BlendMode.Screen
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error drawing signature canvas", e)
        }
    }
}

@Composable
private fun ReturningUserStatusView(
    onFinish: () -> Unit,
    isPreviewMode: Boolean
) {
    val context = LocalContext.current

    val statusMessage = remember {
        BrandAmbassadorPrefs.getRandomStatusMessage(context)
    }

    val posterZoomScale = remember { Animatable(1.0f) }
    val progressAnim = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "statusAnims")
    val reflectionOffset by infiniteTransition.animateFloat(
        initialValue = -400f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "reflectionOffset"
    )

    LaunchedEffect(Unit) {
        if (isPreviewMode) {
            progressAnim.snapTo(1f)
            return@LaunchedEffect
        }

        try {
            // Light cinematic zoom (100% -> 102%)
            launch {
                try {
                    posterZoomScale.animateTo(
                        targetValue = 1.02f,
                        animationSpec = tween(3000, easing = LinearEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Zoom animation error", e)
                }
            }

            // Fill progress bar smoothly over 3 seconds
            launch {
                try {
                    progressAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(3000, easing = LinearEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Progress animation error", e)
                }
            }

            delay(3000)
            onFinish()
        } catch (e: Exception) {
            Log.e(TAG, "Fatal LaunchedEffect exception in ReturningUserStatusView", e)
            onFinish()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hero Poster Image with 100% brightness & perfect face clarity
        PosterHeroImage(zoomScale = posterZoomScale.value)

        // Glass Reflection Sweep
        if (!isPreviewMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                try {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.05f),
                                Color.Transparent
                            ),
                            start = Offset(reflectionOffset, 0f),
                            end = Offset(reflectionOffset + 220f, size.height)
                        )
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Canvas reflection sweep error", e)
                }
            }
        }

        // Soft gradient at LOWER THIRD ONLY (face and body remain 100% visible and bright)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.38f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x77000000),
                            Color(0xD9000000),
                            Color(0xFA000000)
                        )
                    )
                )
        )

        // Minimal Apple-style Lower Third Layout
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiny Emerald Accent Sparkle
            Text(
                text = "✨",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Minimal, elegant SF Pro style status message
            Text(
                text = statusMessage,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite,
                textAlign = TextAlign.Center,
                letterSpacing = 0.3.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Very Thin Glass Loading Line (3s duration)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(2.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressAnim.value)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    EmeraldPrimary,
                                    EmeraldGlow,
                                    Color.White
                                )
                            )
                        )
                )
            }
        }
    }
}
