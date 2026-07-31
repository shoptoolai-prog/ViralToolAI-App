package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class QuickToolType {
    PROMPT_HERO,
    REMOVE_BG,
    INSTAGRAM_DOWNLOADER,
    YOUTUBE_DOWNLOADER,
    GEMINI_AI,
    VIDEO_ENHANCER
}

data class QuickAccessTool(
    val id: String,
    val name: String,
    val url: String,
    val gradientColors: List<Color>,
    val toolType: QuickToolType
)

val QUICK_ACCESS_TOOLS_ROW1 = listOf(
    QuickAccessTool(
        id = "prompt_hero",
        name = "PromptHero",
        url = "https://prompthero.com/",
        gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF6366F1)),
        toolType = QuickToolType.PROMPT_HERO
    ),
    QuickAccessTool(
        id = "remove_bg",
        name = "Remove BG",
        url = "https://www.remove.bg/",
        gradientColors = listOf(Color(0xFF00C9FF), Color(0xFF92FE9D)),
        toolType = QuickToolType.REMOVE_BG
    ),
    QuickAccessTool(
        id = "insta_tools",
        name = "Instagram Tools",
        url = "https://indown.io/en2",
        gradientColors = listOf(Color(0xFF833AB4), Color(0xFFE1306C), Color(0xFFFD1D1D)),
        toolType = QuickToolType.INSTAGRAM_DOWNLOADER
    )
)

val QUICK_ACCESS_TOOLS_ROW2 = listOf(
    QuickAccessTool(
        id = "yt_tools",
        name = "YouTube Tools",
        url = "https://convertytmp3.org/",
        gradientColors = listOf(Color(0xFFFF0000), Color(0xFFD32F2F)),
        toolType = QuickToolType.YOUTUBE_DOWNLOADER
    ),
    QuickAccessTool(
        id = "gemini_ai",
        name = "Gemini",
        url = "https://gemini.google.com/",
        gradientColors = listOf(Color(0xFF1A73E8), Color(0xFF8E24AA), Color(0xFF00C9FF)),
        toolType = QuickToolType.GEMINI_AI
    ),
    QuickAccessTool(
        id = "video_enhancer",
        name = "Video Enhancer",
        url = "https://vmake.ai/video-enhancer",
        gradientColors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)),
        toolType = QuickToolType.VIDEO_ENHANCER
    )
)

@Composable
fun CreatorQuickToolsSection(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Section Header - "Quick Access" with NO subtitle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "⚡ Quick Access",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.3.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Row 1 (3 columns)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QUICK_ACCESS_TOOLS_ROW1.forEach { tool ->
                Box(modifier = Modifier.weight(1f)) {
                    QuickAccessCard(
                        tool = tool,
                        onToolClick = {
                            launchQuickToolWebsite(context, tool.url)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Row 2 (3 columns)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QUICK_ACCESS_TOOLS_ROW2.forEach { tool ->
                Box(modifier = Modifier.weight(1f)) {
                    QuickAccessCard(
                        tool = tool,
                        onToolClick = {
                            launchQuickToolWebsite(context, tool.url)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickAccessCard(
    tool: QuickAccessTool,
    onToolClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "pressScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Square layout
            .scale(scaleAnim)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = tool.gradientColors.first().copy(alpha = 0.25f),
                spotColor = tool.gradientColors.first().copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x30222636),
                        Color(0x20161924)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            tool.gradientColors.first().copy(alpha = 0.75f),
                            tool.gradientColors.last().copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.15f)
                        )
                    )
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = tool.gradientColors.first())
            ) {
                if (!isLoading) {
                    scope.launch {
                        isPressed = true
                        isLoading = true
                        delay(200)
                        isPressed = false
                        onToolClick()
                        delay(150)
                        isLoading = false
                    }
                }
            }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Large Icon Box
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = tool.gradientColors.map { it.copy(alpha = 0.25f) }
                        )
                    )
                    .border(
                        BorderStroke(1.2.dp, tool.gradientColors.first().copy(alpha = 0.8f)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = tool.gradientColors.first(),
                        strokeWidth = 2.dp
                    )
                } else {
                    QuickToolIcon(type = tool.toolType, primaryColor = tool.gradientColors.first())
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tool Name ONLY (No subtitle / description)
            Text(
                text = tool.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun QuickToolIcon(
    type: QuickToolType,
    primaryColor: Color
) {
    when (type) {
        QuickToolType.REMOVE_BG -> {
            Canvas(modifier = Modifier.size(28.dp)) {
                val w = size.width
                val h = size.height

                drawRect(
                    color = primaryColor.copy(alpha = 0.4f),
                    topLeft = Offset(w * 0.12f, h * 0.12f),
                    size = Size(w * 0.35f, h * 0.35f)
                )
                drawRect(
                    color = primaryColor.copy(alpha = 0.4f),
                    topLeft = Offset(w * 0.52f, h * 0.52f),
                    size = Size(w * 0.35f, h * 0.35f)
                )

                drawCircle(color = Color.White, radius = w * 0.12f, center = Offset(w * 0.32f, h * 0.72f), style = Stroke(width = w * 0.06f))
                drawCircle(color = Color.White, radius = w * 0.12f, center = Offset(w * 0.68f, h * 0.72f), style = Stroke(width = w * 0.06f))
                drawLine(color = Color.White, start = Offset(w * 0.32f, h * 0.62f), end = Offset(w * 0.68f, h * 0.25f), strokeWidth = w * 0.06f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(w * 0.68f, h * 0.62f), end = Offset(w * 0.32f, h * 0.25f), strokeWidth = w * 0.06f, cap = StrokeCap.Round)
            }
        }
        QuickToolType.INSTAGRAM_DOWNLOADER -> {
            Canvas(modifier = Modifier.size(28.dp)) {
                val w = size.width
                val h = size.height

                drawRoundRect(
                    brush = Brush.linearGradient(listOf(Color(0xFF833AB4), Color(0xFFE1306C), Color(0xFFFD1D1D))),
                    topLeft = Offset(w * 0.08f, h * 0.08f),
                    size = Size(w * 0.84f, h * 0.84f),
                    cornerRadius = CornerRadius(w * 0.22f),
                    style = Stroke(width = w * 0.07f)
                )
                drawCircle(
                    color = Color.White,
                    radius = w * 0.18f,
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = w * 0.06f)
                )
                drawLine(color = Color.White, start = Offset(w * 0.5f, h * 0.32f), end = Offset(w * 0.5f, h * 0.68f), strokeWidth = w * 0.08f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(w * 0.35f, h * 0.54f), end = Offset(w * 0.5f, h * 0.68f), strokeWidth = w * 0.08f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(w * 0.65f, h * 0.54f), end = Offset(w * 0.5f, h * 0.68f), strokeWidth = w * 0.08f, cap = StrokeCap.Round)
            }
        }
        QuickToolType.YOUTUBE_DOWNLOADER -> {
            Canvas(modifier = Modifier.size(28.dp)) {
                val w = size.width
                val h = size.height

                drawRoundRect(
                    color = Color(0xFFFF0000),
                    topLeft = Offset(w * 0.06f, h * 0.16f),
                    size = Size(w * 0.88f, h * 0.68f),
                    cornerRadius = CornerRadius(w * 0.18f)
                )
                drawLine(color = Color.White, start = Offset(w * 0.5f, h * 0.30f), end = Offset(w * 0.5f, h * 0.68f), strokeWidth = w * 0.08f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(w * 0.35f, h * 0.52f), end = Offset(w * 0.5f, h * 0.68f), strokeWidth = w * 0.08f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(w * 0.65f, h * 0.52f), end = Offset(w * 0.5f, h * 0.68f), strokeWidth = w * 0.08f, cap = StrokeCap.Round)
            }
        }
        QuickToolType.PROMPT_HERO -> {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "PromptHero",
                tint = primaryColor,
                modifier = Modifier.size(28.dp)
            )
        }
        QuickToolType.GEMINI_AI -> {
            Canvas(modifier = Modifier.size(28.dp)) {
                val w = size.width
                val h = size.height

                val starPath = Path().apply {
                    moveTo(w * 0.5f, h * 0.06f)
                    cubicTo(w * 0.5f, h * 0.35f, w * 0.65f, h * 0.5f, w * 0.94f, h * 0.5f)
                    cubicTo(w * 0.65f, h * 0.5f, w * 0.5f, h * 0.65f, w * 0.5f, h * 0.94f)
                    cubicTo(w * 0.5f, h * 0.65f, w * 0.35f, h * 0.5f, w * 0.06f, h * 0.5f)
                    cubicTo(w * 0.35f, h * 0.5f, w * 0.5f, h * 0.35f, w * 0.5f, h * 0.06f)
                    close()
                }
                drawPath(
                    path = starPath,
                    brush = Brush.linearGradient(listOf(Color(0xFF1A73E8), Color(0xFF8E24AA), Color(0xFF00C9FF)))
                )
            }
        }
        QuickToolType.VIDEO_ENHANCER -> {
            Icon(
                imageVector = Icons.Default.Hd,
                contentDescription = "Video Enhancer",
                tint = primaryColor,
                modifier = Modifier.size(28.dp)
            )
        }
        else -> {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Tool",
                tint = primaryColor,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

private fun launchQuickToolWebsite(context: Context, url: String) {
    try {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetwork = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
        val isConnected = capabilities != null && (
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        )

        if (!isConnected) {
            Toast.makeText(context, "Internet connection required.", Toast.LENGTH_SHORT).show()
            return
        }

        val customTabsIntent = CustomTabsIntent.Builder().apply {
            setShowTitle(true)
        }.build()

        customTabsIntent.launchUrl(context, Uri.parse(url))
    } catch (e: Exception) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(context, "Unable to open website. Please try again later.", Toast.LENGTH_SHORT).show()
        }
    }
}
