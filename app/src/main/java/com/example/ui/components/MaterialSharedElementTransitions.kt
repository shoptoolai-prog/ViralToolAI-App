package com.example.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * Material 3 Shared Element Container Provider
 * Wraps top-level screen switching (e.g. Main Tool List <-> Detailed Analysis View)
 * allowing elements (tool cards, headers, logos, badges) to morph smoothly.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Material3SharedElementContainer(
    content: @Composable SharedTransitionScope.() -> Unit
) {
    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            content()
        }
    }
}

/**
 * Material 3 Shared Element Modifier for Tool Cards & Analysis Detail Views.
 * Morphs tool list items into detailed analysis screens smoothly.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
fun Modifier.materialSharedElement(key: Any): Modifier = composed {
    val sharedScope = LocalSharedTransitionScope.current
    val animatedScope = LocalAnimatedVisibilityScope.current

    if (sharedScope != null && animatedScope != null) {
        with(sharedScope) {
            this@composed.sharedElement(
                rememberSharedContentState(key = key),
                animatedVisibilityScope = animatedScope,
                boundsTransform = { _, _ ->
                    tween(durationMillis = 380, easing = FastOutSlowInEasing)
                }
            )
        }
    } else {
        this
    }
}

/**
 * Material 3 Shared Bounds Modifier for Card Containers & Headers.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
fun Modifier.materialSharedBounds(key: Any): Modifier = composed {
    val sharedScope = LocalSharedTransitionScope.current
    val animatedScope = LocalAnimatedVisibilityScope.current

    if (sharedScope != null && animatedScope != null) {
        with(sharedScope) {
            this@composed.sharedBounds(
                rememberSharedContentState(key = key),
                animatedVisibilityScope = animatedScope,
                boundsTransform = { _, _ ->
                    tween(durationMillis = 380, easing = FastOutSlowInEasing)
                }
            )
        }
    } else {
        this
    }
}
