package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val SolidWhite = Color(0xFFFFFFFF)
private val CyanAccent = Color(0xFF20D9E8)

/**
 * Official ViralToolAI Geometric Monogram Logo (V + T merged symbol).
 * Minimal, luxury, technology, creator AI symbol.
 */
@Composable
fun ViralToolAiLogo(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    logoColor: Color = SolidWhite,
    accentColor: Color = CyanAccent,
    showAccentDot: Boolean = true,
    highlightProgress: Float = -1f
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        val strokeWidth = w * 0.08f

        // Key Monogram Coordinates:
        // Top T Bar
        val tLeft = Offset(w * 0.16f, h * 0.22f)
        val tRight = Offset(w * 0.84f, h * 0.22f)

        // V Diagonals
        val vBottom = Offset(w * 0.50f, h * 0.80f)

        // T Stem
        val tStemTop = Offset(w * 0.50f, h * 0.22f)
        val tStemBottom = Offset(w * 0.50f, h * 0.58f)

        // 1. Top T Horizontal Bar
        drawLine(
            color = logoColor,
            start = tLeft,
            end = tRight,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 2. V Left Diagonal
        drawLine(
            color = logoColor,
            start = tLeft,
            end = vBottom,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 3. V Right Diagonal
        drawLine(
            color = logoColor,
            start = vBottom,
            end = tRight,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 4. T Vertical Stem
        drawLine(
            color = logoColor,
            start = tStemTop,
            end = tStemBottom,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 5. Small Cyan Accent Highlight
        if (highlightProgress in 0.01f..0.99f) {
            val highlightPos = when {
                highlightProgress < 0.35f -> {
                    val p = highlightProgress / 0.35f
                    Offset(
                        x = tLeft.x + (tRight.x - tLeft.x) * p,
                        y = tLeft.y
                    )
                }
                highlightProgress < 0.70f -> {
                    val p = (highlightProgress - 0.35f) / 0.35f
                    Offset(
                        x = tLeft.x + (vBottom.x - tLeft.x) * p,
                        y = tLeft.y + (vBottom.y - tLeft.y) * p
                    )
                }
                else -> {
                    val p = (highlightProgress - 0.70f) / 0.30f
                    Offset(
                        x = vBottom.x + (tRight.x - vBottom.x) * p,
                        y = vBottom.y + (tRight.y - vBottom.y) * p
                    )
                }
            }

            drawCircle(
                color = accentColor,
                radius = w * 0.05f,
                center = highlightPos
            )
            drawCircle(
                color = accentColor.copy(alpha = 0.35f),
                radius = w * 0.10f,
                center = highlightPos
            )
        } else if (showAccentDot) {
            val dotCenter = Offset(w * 0.84f, h * 0.22f)
            drawCircle(
                color = accentColor,
                radius = w * 0.055f,
                center = dotCenter
            )
        }
    }
}
