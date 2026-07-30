package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val HOME_BANNER_IMAGES = listOf(
    "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/assets/brand-ambassadors/Picsart_26-07-29_23-45-35-887.jpg",
    "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/assets/brand-ambassadors/Picsart_26-07-29_23-46-04-094.jpg",
    "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/assets/brand-ambassadors/Picsart_26-07-29_23-46-40-738.jpg"
)

@Composable
fun HomeBannerCarousel(
    modifier: Modifier = Modifier,
    bannerUrls: List<String> = HOME_BANNER_IMAGES,
    autoScrollDelayMillis: Long = 3500L
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { bannerUrls.size })

    // Track user dragging state via DragInteraction
    var isUserDragging by remember { mutableStateOf(false) }
    var lastUserInteractionTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(pagerState.interactionSource) {
        pagerState.interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is DragInteraction.Start -> {
                    isUserDragging = true
                    lastUserInteractionTime = System.currentTimeMillis()
                }
                is DragInteraction.Stop, is DragInteraction.Cancel -> {
                    isUserDragging = false
                    lastUserInteractionTime = System.currentTimeMillis()
                }
            }
        }
    }

    // Lifecycle-aware state to pause auto-scroll when app is in background or screen is not visible
    val lifecycleOwner = LocalLifecycleOwner.current
    var isResumed by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> isResumed = true
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> isResumed = false
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Preload banner images for instant rendering & zero lag
    LaunchedEffect(bannerUrls) {
        bannerUrls.forEach { url ->
            try {
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build()
                context.imageLoader.enqueue(request)
            } catch (_: Exception) {
                // Ignore preloading error
            }
        }
    }

    // Infinite loop auto-slide (Banner 1 -> Banner 2 -> Banner 3 -> Banner 1...)
    // Pauses for 5 seconds on manual swipe, auto-scrolls every 3.5s otherwise
    LaunchedEffect(pagerState.currentPage, isUserDragging, isResumed, bannerUrls.size) {
        if (!isResumed || isUserDragging || bannerUrls.isEmpty()) return@LaunchedEffect

        val timeSinceSwipe = System.currentTimeMillis() - lastUserInteractionTime
        val currentDelay = if (timeSinceSwipe < 5000L) {
            5000L - timeSinceSwipe
        } else {
            autoScrollDelayMillis
        }

        delay(currentDelay.coerceAtLeast(100L))

        if (!isUserDragging && isResumed && bannerUrls.isNotEmpty()) {
            val nextPage = (pagerState.currentPage + 1) % bannerUrls.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 16:9 Glassmorphism Banner Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = EmeraldPrimary.copy(alpha = 0.35f),
                    ambientColor = Color.Black
                )
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF141E18),
                            Color(0xFF0D1410),
                            Color(0xFF080C0A)
                        )
                    )
                )
                .border(
                    BorderStroke(
                        width = 1.2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                EmeraldPrimary.copy(alpha = 0.55f),
                                Color.White.copy(alpha = 0.15f),
                                EmeraldGlow.copy(alpha = 0.35f)
                            )
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val imageUrl = bannerUrls[page]

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .crossfade(400)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = "ViralToolAI Banner ${page + 1}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF101713)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(28.dp),
                                    color = EmeraldPrimary,
                                    strokeWidth = 2.5.dp
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF101713))
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Page Indicator Dots
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            repeat(bannerUrls.size) { index ->
                val isSelected = pagerState.currentPage == index

                // Animated dot width and color
                val width by animateDpAsState(
                    targetValue = if (isSelected) 22.dp else 7.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "dotWidth"
                )

                val color by animateColorAsState(
                    targetValue = if (isSelected) EmeraldPrimary else Color.White.copy(alpha = 0.25f),
                    animationSpec = tween(durationMillis = 300),
                    label = "dotColor"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.5.dp)
                        .height(7.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            lastUserInteractionTime = System.currentTimeMillis()
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = index,
                                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                                )
                            }
                        }
                )
            }
        }
    }
}
