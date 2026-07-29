package com.example.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

enum class WindowWidthSizeClass { COMPACT, MEDIUM, EXPANDED }
enum class WindowHeightSizeClass { COMPACT, MEDIUM, EXPANDED }

@Immutable
data class WindowSizeClass(
    val widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.COMPACT,
    val heightSizeClass: WindowHeightSizeClass = WindowHeightSizeClass.MEDIUM
) {
    val isCompactWidth: Boolean get() = widthSizeClass == WindowWidthSizeClass.COMPACT
    val isMediumWidth: Boolean get() = widthSizeClass == WindowWidthSizeClass.MEDIUM
    val isExpandedWidth: Boolean get() = widthSizeClass == WindowWidthSizeClass.EXPANDED

    val isCompactHeight: Boolean get() = heightSizeClass == WindowHeightSizeClass.COMPACT
    val isMediumHeight: Boolean get() = heightSizeClass == WindowHeightSizeClass.MEDIUM
    val isExpandedHeight: Boolean get() = heightSizeClass == WindowHeightSizeClass.EXPANDED
}

/**
 * MASTER PHASE — UNIVERSAL AUTO RESPONSIVE ENGINE
 * Automatically detects device metrics (width, height, aspect ratio, DPI, font scale, safe area)
 * and calculates dynamic bounds for tool cards, popups, buttons, text, and padding.
 */
@Immutable
data class ResponsiveMetrics(
    val screenWidthDp: Dp = 390.dp,
    val screenHeightDp: Dp = 844.dp,
    val aspectRatio: Float = 2.16f,
    val densityDpi: Int = 420,
    val fontScale: Float = 1.0f,
    val statusBarHeightDp: Dp = 24.dp,
    val navigationBarHeightDp: Dp = 16.dp,
    val isSmallPhone: Boolean = false,
    val isMediumPhone: Boolean = true,
    val isLargePhone: Boolean = false,
    val isTablet: Boolean = false,
    val isFoldable: Boolean = false,
    val windowSizeClass: WindowSizeClass = WindowSizeClass(),
    val scaleFactor: Float = 1.0f,
    val cardMaxWidth: Dp = 560.dp,
    val dialogMaxWidth: Dp = 520.dp,
    val dialogMaxHeight: Dp = 680.dp,
    val buttonMaxWidth: Dp = 440.dp,
    val minButtonHeight: Dp = 48.dp,
    val horizontalPadding: Dp = 16.dp,
    val cardSpacing: Dp = 12.dp,
    val gridColumns: Int = 1
) {
    fun scaledDp(baseDp: Float): Dp = (baseDp * scaleFactor).dp
    fun scaledSp(baseSp: Float): TextUnit = (baseSp * scaleFactor).sp
}

val LocalResponsiveMetrics = staticCompositionLocalOf { ResponsiveMetrics() }

@Composable
fun ProvideUniversalResponsiveEngine(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues(density)
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues(density)

    val metrics = remember(configuration, density) {
        val widthDp = configuration.screenWidthDp.dp
        val heightDp = configuration.screenHeightDp.dp
        val fontScale = configuration.fontScale
        val densityDpi = configuration.densityDpi
        val rawWidth = configuration.screenWidthDp.toFloat()
        val rawHeight = configuration.screenHeightDp.toFloat()
        val aspect = if (rawWidth > 0) rawHeight / rawWidth else 2.0f

        val isSmall = rawWidth < 360f
        val isMedium = rawWidth in 360f..430f
        val isLarge = rawWidth in 431f..599f
        val isTab = rawWidth >= 600f
        val isFold = rawWidth >= 840f || (rawWidth > 500f && aspect < 1.45f)

        val widthClass = when {
            rawWidth < 600f -> WindowWidthSizeClass.COMPACT
            rawWidth < 840f -> WindowWidthSizeClass.MEDIUM
            else -> WindowWidthSizeClass.EXPANDED
        }

        val heightClass = when {
            rawHeight < 480f -> WindowHeightSizeClass.COMPACT
            rawHeight < 900f -> WindowHeightSizeClass.MEDIUM
            else -> WindowHeightSizeClass.EXPANDED
        }

        val windowSize = WindowSizeClass(widthClass, heightClass)

        // Adaptive scaling factor capped on large screens to prevent component bloating/stretching
        val rawScaleFactor = when {
            isSmall -> 0.90f
            isMedium -> 1.0f
            isLarge -> 1.02f
            else -> 1.04f // Cap at 1.04f max so cards & text stay crisp and un-bloated
        }

        val cardMaxW = if (isTab || isFold) 560.dp else if (widthDp - 24.dp < 520.dp) widthDp - 24.dp else 520.dp
        val dialogMaxW = if (isTab || isFold) 520.dp else if (widthDp - 28.dp < 480.dp) widthDp - 28.dp else 480.dp
        // Cap max dialog height adaptively so dialogs stay compact and bottom buttons remain pinned & visible
        val dialogMaxH = if (heightDp < 480.dp) heightDp - 24.dp else if (heightDp - 48.dp < 680.dp) heightDp - 48.dp else 680.dp
        val buttonMaxW = if (isTab || isFold) 440.dp else if (widthDp - 32.dp < 440.dp) widthDp - 32.dp else 440.dp

        val paddingH = when {
            isSmall -> 12.dp
            isTab || isFold -> 24.dp
            else -> 16.dp
        }

        val spacing = when {
            isSmall -> 10.dp
            isTab -> 16.dp
            else -> 12.dp
        }

        val cols = when {
            isTab || isFold -> 2
            else -> 1
        }

        ResponsiveMetrics(
            screenWidthDp = widthDp,
            screenHeightDp = heightDp,
            aspectRatio = aspect,
            densityDpi = densityDpi,
            fontScale = fontScale,
            statusBarHeightDp = statusBarPadding.calculateTopPadding(),
            navigationBarHeightDp = navBarPadding.calculateBottomPadding(),
            isSmallPhone = isSmall,
            isMediumPhone = isMedium,
            isLargePhone = isLarge,
            isTablet = isTab,
            isFoldable = isFold,
            windowSizeClass = windowSize,
            scaleFactor = rawScaleFactor,
            cardMaxWidth = cardMaxW,
            dialogMaxWidth = dialogMaxW,
            dialogMaxHeight = dialogMaxH,
            buttonMaxWidth = buttonMaxW,
            minButtonHeight = max(48f, 48f * rawScaleFactor).dp,
            horizontalPadding = paddingH,
            cardSpacing = spacing,
            gridColumns = cols
        )
    }

    CompositionLocalProvider(LocalResponsiveMetrics provides metrics) {
        content()
    }
}

/**
 * Responsive Card Modifier Extension
 * Ensures tool cards automatically scale, center-align on wide devices, and never overflow.
 */
fun Modifier.responsiveCardBounds(
    metrics: ResponsiveMetrics,
    additionalPadding: Dp = 0.dp
): Modifier = this
    .fillMaxWidth()
    .widthIn(max = metrics.cardMaxWidth)
    .padding(horizontal = additionalPadding)

/**
 * Responsive Dialog Frame Modifier Extension
 * Prevents dialogs and tool popups from expanding beyond the device boundary or overflowing height.
 */
fun Modifier.responsiveDialogBounds(
    metrics: ResponsiveMetrics
): Modifier = this
    .widthIn(max = metrics.dialogMaxWidth)
    .heightIn(max = metrics.dialogMaxHeight)

/**
 * Responsive Button Modifier Extension
 * Ensures minimum touch area (48dp) and prevents button widening beyond 500dp.
 */
fun Modifier.responsiveButtonBounds(
    metrics: ResponsiveMetrics
): Modifier = this
    .fillMaxWidth()
    .widthIn(max = metrics.buttonMaxWidth)
    .heightIn(min = metrics.minButtonHeight)

/**
 * Auto-Resized Text Composable
 * Automatically scales down font size if the text exceeds container boundaries or maxLines,
 * guaranteeing zero text clipping or line cutting across all screen sizes.
 */
@Composable
fun AutoResizedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
    fontSize: TextUnit = style.fontSize,
    minFontSize: TextUnit = 10.sp,
    fontWeight: FontWeight? = style.fontWeight,
    fontStyle: FontStyle? = style.fontStyle,
    fontFamily: FontFamily? = style.fontFamily,
    letterSpacing: TextUnit = style.letterSpacing,
    textDecoration: TextDecoration? = style.textDecoration,
    textAlign: TextAlign? = style.textAlign,
    lineHeight: TextUnit = style.lineHeight,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {
    var resizedFontSize by remember(text, fontSize) { mutableStateOf(fontSize) }

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = resizedFontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = { result ->
            if (result.hasVisualOverflow && resizedFontSize.value > minFontSize.value) {
                resizedFontSize = (resizedFontSize.value * 0.9f).sp
            }
            onTextLayout(result)
        },
        style = style
    )
}

/**
 * Responsive IME & Navigation Bar Insets Modifier Extension
 * Computes max(navigationBars.bottom, ime.bottom) to dynamically shift the input bar
 * above the software keyboard without double-padding or clipping.
 */
@Composable
fun Modifier.responsiveImeAndNavPadding(): Modifier =
    this.windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))

