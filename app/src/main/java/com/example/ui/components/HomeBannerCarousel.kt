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
    autoScrollDelayMillis: Long = 3000L
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val itemCount = bannerUrls.size

    val initialPage = remember(itemCount) {
        if (itemCount > 0) {
            val middle = Int.MAX_VALUE / 2
            middle - (middle % itemCount)
        } else 0
    }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { if (itemCount > 0) Int.MAX_VALUE else 0 }
    )

    // Track user dragging state via DragInteraction
    var isUserDragging by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.interactionSource) {
        pagerState.interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is DragInteraction.Start -> {
                    isUserDragging = true
                }
                is DragInteraction.Stop, is DragInteraction.Cancel -> {
                    isUserDragging = false
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

    // Smooth Infinite Forward Auto-Slide Loop (1 -> 2 -> 3 -> 1 -> 2 -> 3...)
    // Always scrolls forward, never reverses, never stops automatically
    LaunchedEffect(pagerState, isUserDragging, isResumed, itemCount) {
        if (!isResumed || itemCount <= 0) return@LaunchedEffect

        while (true) {
            delay(autoScrollDelayMillis)

            if (!isUserDragging && isResumed && itemCount > 0) {
                val targetNextPage = pagerState.currentPage + 1
                pagerState.animateScrollToPage(
                    page = targetNextPage,
                    animationSpec = tween(
                        durationMillis = 450,
                        easing = FastOutSlowInEasing
                    )
                )
            }
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
                modifier = Modifier.fillMaxSize(),
                pageSpacing = 0.dp,
                userScrollEnabled = true
            ) { page ->
                val realIndex = if (itemCount > 0) page % itemCount else 0
                val imageUrl = bannerUrls.getOrElse(realIndex) { "" }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .crossfade(300)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = "ViralToolAI Banner ${realIndex + 1}",
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
        val currentRealIndex = if (itemCount > 0) pagerState.currentPage % itemCount else 0

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            repeat(itemCount) { index ->
                val isSelected = currentRealIndex == index

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
                            coroutineScope.launch {
                                val currentReal = pagerState.currentPage % itemCount
                                val delta = index - currentReal
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage + delta,
                                    animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
                                )
                            }
                        }
                )
            }
        }
    }
}
