package com.example.ui.screens

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cloud.LiveCloudManager
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private const val TAG = "BrandAmbassadorPoster"
private const val POSTER_IMAGE_URL =
    "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAi-App/main/assets/brand-ambassadors/1785321241752.png"

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
        val motivationalMessages = listOf(
            "Create. Inspire. Grow.",
            "Your next viral moment starts today.",
            "Keep creating, success follows consistency.",
            "Consistency builds legends.",
            "Transform your ideas into viral reality.",
            "Consistency beats talent when talent doesn't work hard.",
            "Every great creator started with zero views.",
            "Your creativity has no limits.",
            "Dream big. Build fast. Go viral.",
            "Craft stories that resonate worldwide.",
            "Turn passion into high-impact content.",
            "Master the algorithm with authentic storytelling."
        )
        return try {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val lastIdx = prefs.getInt(KEY_LAST_MSG, -1)
            var newIdx = (0 until motivationalMessages.size).random()
            if (motivationalMessages.size > 1 && newIdx == lastIdx) {
                newIdx = (newIdx + 1) % motivationalMessages.size
            }
            prefs.edit().putInt(KEY_LAST_MSG, newIdx).apply()
            motivationalMessages[newIdx]
        } catch (e: Exception) {
            Log.e(TAG, "Error getting random status message", e)
            motivationalMessages.random()
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

    // Suppress back button gesture during launch screen
    BackHandler(enabled = true) {
        // Disabled back gesture during launch screen sequence
    }

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
            // CRITICAL REQUIREMENT: Intercept & consume all touch events during startup
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        event.changes.forEach { it.consume() }
                    }
                }
            }
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
    val baConfig by LiveCloudManager.brandAmbassadorConfig.collectAsStateWithLifecycle()

    val rawUrl = baConfig.image.ifBlank { POSTER_IMAGE_URL }
    val imageUrl = if (rawUrl.contains("Picsart") || rawUrl.contains("a7996a261d91d703ea1e41a90cba30233d85b80a") || rawUrl.contains("unsplash")) {
        POSTER_IMAGE_URL
    } else {
        rawUrl
    }

    // Preload image with high priority
    LaunchedEffect(imageUrl) {
        try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
            coil.Coil.imageLoader(context).enqueue(request)
        } catch (e: Exception) {
            Log.e(TAG, "Error preloading image in PosterHeroImage", e)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0E1C16),
                        Color(0xFF0A1410),
                        Color(0xFF040806)
                    )
                )
            )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            contentDescription = "ViralToolAi Welcome Banner",
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
    val titleAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(40f) }
    val subAlpha = remember { Animatable(0f) }
    val subOffsetY = remember { Animatable(30f) }
    val progressAnim = remember { Animatable(0f) }

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
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "reflectionOffset"
    )

    LaunchedEffect(Unit) {
        if (isPreviewMode) {
            titleAlpha.snapTo(1f)
            titleOffsetY.snapTo(0f)
            subAlpha.snapTo(1f)
            subOffsetY.snapTo(0f)
            progressAnim.snapTo(1f)
            return@LaunchedEffect
        }

        try {
            // Camera Zoom: 100% to 102.5% over 6 seconds
            launch {
                try {
                    posterZoomScale.animateTo(
                        targetValue = 1.025f,
                        animationSpec = tween(6000, easing = LinearEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Zoom animation error", e)
                }
            }

            // Smooth progress bar filling over 6 seconds
            launch {
                try {
                    progressAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(6000, easing = LinearEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Progress animation error", e)
                }
            }

            // iOS-style text reveal: Main Title
            launch {
                try {
                    titleAlpha.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
                } catch (e: Exception) {
                    Log.e(TAG, "Title alpha error", e)
                }
            }
            launch {
                try {
                    titleOffsetY.animateTo(0f, tween(900, easing = FastOutSlowInEasing))
                } catch (e: Exception) {
                    Log.e(TAG, "Title offset error", e)
                }
            }

            // iOS-style text reveal: Subtitle message after 400ms delay
            delay(400)
            launch {
                try {
                    subAlpha.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
                } catch (e: Exception) {
                    Log.e(TAG, "Sub alpha error", e)
                }
            }
            launch {
                try {
                    subOffsetY.animateTo(0f, tween(900, easing = FastOutSlowInEasing))
                } catch (e: Exception) {
                    Log.e(TAG, "Sub offset error", e)
                }
            }

            // Total screen duration: 6 seconds (6000ms)
            delay(5600) // remaining delay after the initial 400ms
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

        // Lower Third Vignette Gradient
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.48f)
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

        // Lower Third iOS-Style Typography Layout
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Welcome Title with animated slide and fade
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    alpha = titleAlpha.value
                    translationY = titleOffsetY.value
                }
            ) {
                Text(
                    text = "WELCOME TO VIRALTOOLAI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGlow,
                    letterSpacing = 2.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Thanks For Downloading ViralToolAi App",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite,
                    letterSpacing = 0.3.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle / Additional Message with separate delay reveal
            Text(
                text = "Support us and follow ViralToolAi on Instagram",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite.copy(alpha = 0.90f),
                letterSpacing = 0.4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer {
                    alpha = subAlpha.value
                    translationY = subOffsetY.value
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Fast Animated Loading Line (6s duration)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(3.dp)
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
    val contentAlpha = remember { Animatable(0f) }
    val contentOffsetY = remember { Animatable(30f) }
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
            contentAlpha.snapTo(1f)
            contentOffsetY.snapTo(0f)
            progressAnim.snapTo(1f)
            return@LaunchedEffect
        }

        try {
            // Light cinematic zoom (100% -> 102%) over 3.5 seconds
            launch {
                try {
                    posterZoomScale.animateTo(
                        targetValue = 1.02f,
                        animationSpec = tween(3500, easing = LinearEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Zoom animation error", e)
                }
            }

            // Fill progress bar smoothly over 3.5 seconds
            launch {
                try {
                    progressAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(3500, easing = LinearEasing)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Progress animation error", e)
                }
            }

            // Smooth content reveal animation
            launch {
                try {
                    contentAlpha.animateTo(1f, tween(700, easing = FastOutSlowInEasing))
                } catch (e: Exception) {
                    Log.e(TAG, "Content alpha error", e)
                }
            }
            launch {
                try {
                    contentOffsetY.animateTo(0f, tween(700, easing = FastOutSlowInEasing))
                } catch (e: Exception) {
                    Log.e(TAG, "Content offset error", e)
                }
            }

            delay(3500)
            onFinish()
        } catch (e: Exception) {
            Log.e(TAG, "Fatal LaunchedEffect exception in ReturningUserStatusView", e)
            onFinish()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hero Poster Image with 100% brightness
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

        // Soft gradient at lower third only
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

        // Apple-style Lower Third Layout
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp)
                .graphicsLayer {
                    alpha = contentAlpha.value
                    translationY = contentOffsetY.value
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✨ CREATOR MODE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldGlow,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dynamic motivational creator line
            Text(
                text = statusMessage,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center,
                letterSpacing = 0.3.sp,
                lineHeight = 25.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Fast Progress Line (3.5s duration)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(2.5.dp)
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
