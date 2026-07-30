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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class QuickToolItem(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val gradientColors: List<Color>,
    val toolType: QuickToolType
)

enum class QuickToolType {
    REMOVE_BG,
    INSTAGRAM_DOWNLOADER,
    YOUTUBE_DOWNLOADER,
    GOOGLE_FLOW,
    PROMPT_HERO,
    GEMINI_AI,
    VIDEO_ENHANCER
}

val CREATOR_QUICK_TOOLS = listOf(
    QuickToolItem(
        id = "remove_bg",
        title = "Remove Background",
        description = "Remove image backgrounds instantly.",
        url = "https://www.remove.bg/",
        gradientColors = listOf(Color(0xFF00C9FF), Color(0xFF92FE9D)),
        toolType = QuickToolType.REMOVE_BG
    ),
    QuickToolItem(
        id = "insta_downloader",
        title = "Instagram Downloader",
        description = "Download Reels, Posts, Photos, Stories, Profile Pictures and Audio.",
        url = "https://indown.io/en2",
        gradientColors = listOf(Color(0xFF833AB4), Color(0xFFE1306C), Color(0xFFFD1D1D)),
        toolType = QuickToolType.INSTAGRAM_DOWNLOADER
    ),
    QuickToolItem(
        id = "yt_downloader",
        title = "YouTube Downloader",
        description = "Download YouTube Videos, Shorts or MP3.",
        url = "https://convertytmp3.org/",
        gradientColors = listOf(Color(0xFFFF0000), Color(0xFFD32F2F)),
        toolType = QuickToolType.YOUTUBE_DOWNLOADER
    ),
    QuickToolItem(
        id = "google_flow",
        title = "Google Flow",
        description = "Create AI videos using Google's Flow.",
        url = "https://labs.google/fx/tools/flow",
        gradientColors = listOf(Color(0xFF4285F4), Color(0xFFEA4335), Color(0xFF34A853)),
        toolType = QuickToolType.GOOGLE_FLOW
    ),
    QuickToolItem(
        id = "prompt_hero",
        title = "PromptHero",
        description = "Discover professional AI image prompts.",
        url = "https://prompthero.com/",
        gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF6366F1)),
        toolType = QuickToolType.PROMPT_HERO
    ),
    QuickToolItem(
        id = "gemini_ai",
        title = "Gemini AI",
        description = "Generate images, text and creative ideas.",
        url = "https://gemini.google.com/",
        gradientColors = listOf(Color(0xFF1A73E8), Color(0xFF8E24AA), Color(0xFF00C9FF)),
        toolType = QuickToolType.GEMINI_AI
    ),
    QuickToolItem(
        id = "video_enhancer",
        title = "AI Video Enhancer",
        description = "Improve and upscale video quality.",
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
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚡ Creator Quick Tools",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Instant access to useful creator websites.",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid/List of Creator Quick Tool Cards
        CREATOR_QUICK_TOOLS.forEach { tool ->
            QuickToolCard(
                tool = tool,
                onToolClick = {
                    launchQuickToolWebsite(context, tool.url)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun QuickToolCard(
    tool: QuickToolItem,
    onToolClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "pressScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scaleAnim)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = tool.gradientColors.first().copy(alpha = 0.25f),
                spotColor = tool.gradientColors.first().copy(alpha = 0.35f)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF13151D))
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.horizontalGradient(
                        colors = listOf(
                            tool.gradientColors.first().copy(alpha = 0.6f),
                            tool.gradientColors.last().copy(alpha = 0.25f),
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
                        delay(350)
                        isPressed = false
                        onToolClick()
                        delay(200)
                        isLoading = false
                    }
                }
            }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Official Logo Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = tool.gradientColors.map { it.copy(alpha = 0.2f) }
                        )
                    )
                    .border(
                        BorderStroke(1.2.dp, tool.gradientColors.first().copy(alpha = 0.7f)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                QuickToolIcon(type = tool.toolType, primaryColor = tool.gradientColors.first())
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Middle: Title & Description
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = tool.title,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Small External Link Indicator
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "External Link",
                        tint = tool.gradientColors.first().copy(alpha = 0.85f),
                        modifier = Modifier.size(15.dp)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = tool.description,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right: Launch Arrow or Loading Indicator
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(tool.gradientColors.first().copy(alpha = 0.15f))
                    .border(BorderStroke(1.dp, tool.gradientColors.first().copy(alpha = 0.4f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = tool.gradientColors.first(),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Website",
                        tint = tool.gradientColors.first(),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
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
            Canvas(modifier = Modifier.size(24.dp)) {
                // Background removal grid symbol
                drawRect(
                    color = primaryColor.copy(alpha = 0.3f),
                    topLeft = Offset(2f, 2f),
                    size = Size(10f, 10f)
                )
                drawRect(
                    color = primaryColor.copy(alpha = 0.3f),
                    topLeft = Offset(12f, 12f),
                    size = Size(10f, 10f)
                )
                // Scissors overlay
                drawCircle(color = primaryColor, radius = 3.5f, center = Offset(7f, 17f), style = Stroke(width = 2f))
                drawCircle(color = primaryColor, radius = 3.5f, center = Offset(17f, 17f), style = Stroke(width = 2f))
                drawLine(color = primaryColor, start = Offset(7f, 14f), end = Offset(17f, 6f), strokeWidth = 2f)
                drawLine(color = primaryColor, start = Offset(17f, 14f), end = Offset(7f, 6f), strokeWidth = 2f)
            }
        }
        QuickToolType.INSTAGRAM_DOWNLOADER -> {
            Canvas(modifier = Modifier.size(24.dp)) {
                // Insta camera box
                drawRoundRect(
                    brush = Brush.linearGradient(listOf(Color(0xFF833AB4), Color(0xFFE1306C))),
                    topLeft = Offset(2f, 2f),
                    size = Size(20f, 20f),
                    cornerRadius = CornerRadius(5f),
                    style = Stroke(width = 2f)
                )
                // Down Arrow
                drawLine(color = Color.White, start = Offset(12f, 7f), end = Offset(12f, 15f), strokeWidth = 2f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(8f, 12f), end = Offset(12f, 15f), strokeWidth = 2f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(16f, 12f), end = Offset(12f, 15f), strokeWidth = 2f, cap = StrokeCap.Round)
            }
        }
        QuickToolType.YOUTUBE_DOWNLOADER -> {
            Canvas(modifier = Modifier.size(24.dp)) {
                // YouTube play shape
                drawRoundRect(
                    color = Color(0xFFFF0000),
                    topLeft = Offset(2f, 4f),
                    size = Size(20f, 16f),
                    cornerRadius = CornerRadius(4f)
                )
                // Down arrow white
                drawLine(color = Color.White, start = Offset(12f, 7f), end = Offset(12f, 15f), strokeWidth = 2f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(8f, 12f), end = Offset(12f, 15f), strokeWidth = 2f, cap = StrokeCap.Round)
                drawLine(color = Color.White, start = Offset(16f, 12f), end = Offset(12f, 15f), strokeWidth = 2f, cap = StrokeCap.Round)
            }
        }
        QuickToolType.GOOGLE_FLOW -> {
            Canvas(modifier = Modifier.size(24.dp)) {
                // Google FX quadrupled color wave flow
                val path = Path().apply {
                    moveTo(2f, 12f)
                    cubicTo(6f, 4f, 10f, 20f, 14f, 12f)
                    cubicTo(18f, 4f, 22f, 20f, 22f, 12f)
                }
                drawPath(path = path, color = Color(0xFF4285F4), style = Stroke(width = 3f, cap = StrokeCap.Round))
                drawCircle(color = Color(0xFFEA4335), radius = 3f, center = Offset(6f, 8f))
                drawCircle(color = Color(0xFF34A853), radius = 3f, center = Offset(14f, 16f))
            }
        }
        QuickToolType.PROMPT_HERO -> {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "PromptHero",
                tint = primaryColor,
                modifier = Modifier.size(22.dp)
            )
        }
        QuickToolType.GEMINI_AI -> {
            Canvas(modifier = Modifier.size(24.dp)) {
                // Gemini 4-point glowing star
                val starPath = Path().apply {
                    moveTo(12f, 2f)
                    cubicTo(12f, 8f, 16f, 12f, 22f, 12f)
                    cubicTo(16f, 12f, 12f, 16f, 12f, 22f)
                    cubicTo(12f, 16f, 8f, 12f, 2f, 12f)
                    cubicTo(8f, 12f, 12f, 8f, 12f, 2f)
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
                contentDescription = "AI Video Enhancer",
                tint = primaryColor,
                modifier = Modifier.size(24.dp)
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
            Toast.makeText(context, "Unable to open the website. Please try again later.", Toast.LENGTH_SHORT).show()
        }
    }
}
