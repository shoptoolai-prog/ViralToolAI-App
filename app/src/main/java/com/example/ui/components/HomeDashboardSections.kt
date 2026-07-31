package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cloud.LiveCloudManager
import com.example.ui.theme.*

// ==================================================
// SECTION 4: CREATOR SPOTLIGHT
// ==================================================
@Composable
fun CreatorSpotlightSection(
    modifier: Modifier = Modifier,
    onExploreClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spotlightGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "spotlightAlpha"
    )

    val cardShape = RoundedCornerShape(22.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 14.dp,
                shape = cardShape,
                ambientColor = ElectricPurple,
                spotColor = EmeraldGlow
            ),
        shape = cardShape,
        color = Color(0xFF0F0C20),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                colors = listOf(
                    ElectricPurple.copy(alpha = glowAlpha),
                    EmeraldPrimary.copy(alpha = glowAlpha),
                    Color.White.copy(alpha = 0.2f)
                )
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF181232), Color(0xFF0C091A))
                    )
                )
                .padding(18.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Tag
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
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(ElectricPurple, Color(0xFFE1306C)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = "Creator Spotlight",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "Creator Spotlight",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33A78BFA))
                            .border(BorderStroke(1.dp, Color(0x66A78BFA)), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "FEATURED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA78BFA)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Content Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // AI-Generated Visual Card Placeholder
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = ElectricPurple)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF2A1B4E), Color(0xFF130D2B))
                                )
                            )
                            .border(BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.6f)), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Artwork",
                                tint = Color(0xFFC084FC),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "100K+ Reels",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGlow
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Master High-Retention Hooks",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Top creators grab attention in the first 1.5 seconds using dynamic visual motion and text overlays.",
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            lineHeight = 16.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// ==================================================
// SECTION 5: TODAY'S CREATOR TIPS
// ==================================================
data class CreatorTip(
    val id: String,
    val category: String,
    val icon: ImageVector,
    val title: String,
    val description: String,
    val color: Color
)

val TODAY_CREATOR_TIPS = listOf(
    CreatorTip(
        id = "tip_1",
        category = "VIRALITY",
        icon = Icons.Default.TrendingUp,
        title = "The 3-Second Rule",
        description = "Always start short videos with immediate action or a bold question to hook 80%+ of viewers.",
        color = Color(0xFFFF007F)
    ),
    CreatorTip(
        id = "tip_2",
        category = "LIGHTING",
        icon = Icons.Default.WbSunny,
        title = "Facing Natural Light",
        description = "Position your camera facing a window for soft, natural face lighting without expensive gear.",
        color = Color(0xFFFFB703)
    ),
    CreatorTip(
        id = "tip_3",
        category = "CONSISTENCY",
        icon = Icons.Default.Schedule,
        title = "Batch Content Days",
        description = "Film 4-5 video concepts in a single day to maintain a steady posting schedule stress-free.",
        color = Color(0xFF00E5FF)
    ),
    CreatorTip(
        id = "tip_4",
        category = "ENGAGEMENT",
        icon = Icons.Default.QuestionAnswer,
        title = "Open-Ended Call-to-Action",
        description = "End your video asking for specific opinions in comments to boost organic algorithm push.",
        color = Color(0xFF8B5CF6)
    )
)

@Composable
fun TodaysCreatorTipsSection(
    modifier: Modifier = Modifier
) {
    var selectedTipIndex by remember { mutableIntStateOf(0) }
    val currentTip = TODAY_CREATOR_TIPS.getOrElse(selectedTipIndex) { TODAY_CREATOR_TIPS[0] }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💡 Today's Creator Tips",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "${selectedTipIndex + 1} of ${TODAY_CREATOR_TIPS.size}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldGlow
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tip Card Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(20.dp), spotColor = currentTip.color)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF151926), Color(0xFF0D101A))
                    )
                )
                .border(
                    BorderStroke(1.dp, currentTip.color.copy(alpha = 0.5f)),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
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
                                .clip(CircleShape)
                                .background(currentTip.color.copy(alpha = 0.2f))
                                .border(BorderStroke(1.dp, currentTip.color), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = currentTip.icon,
                                contentDescription = currentTip.category,
                                tint = currentTip.color,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = currentTip.category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = currentTip.color,
                            letterSpacing = 1.sp
                        )
                    }

                    // Tip switch navigation buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        IconButton(
                            onClick = {
                                selectedTipIndex = if (selectedTipIndex > 0) selectedTipIndex - 1 else TODAY_CREATOR_TIPS.size - 1
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIos,
                                contentDescription = "Previous Tip",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(12.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                selectedTipIndex = (selectedTipIndex + 1) % TODAY_CREATOR_TIPS.size
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = "Next Tip",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = currentTip.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = currentTip.description,
                    fontSize = 12.5.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ==================================================
// SECTION 6: CONTENT INSPIRATION
// ==================================================
data class InspirationIdea(
    val category: String,
    val hook: String,
    val format: String,
    val scriptOutline: String
)

val CONTENT_INSPIRATION_IDEAS = listOf(
    InspirationIdea(
        category = "Fashion",
        hook = "Stop buying expensive clothes until you know these 3 style hacks!",
        format = "GRWM / Outfit Transition Reel",
        scriptOutline = "1. Show budget outfit baseline.\n2. Apply layering & accessories.\n3. Show final elevated aesthetic look."
    ),
    InspirationIdea(
        category = "Tech",
        hook = "Top 5 Secret Smartphone Features you had NO idea existed!",
        format = "Screen-Record + Voiceover",
        scriptOutline = "1. Fast zoom into setting screen.\n2. Show hidden toggle.\n3. Demonstrate real utility value."
    ),
    InspirationIdea(
        category = "Food",
        hook = "10-Minute High Protein Recipe that tastes like restaurant luxury!",
        format = "ASMR Quick Cook Video",
        scriptOutline = "1. Crisp sizzling audio hook.\n2. Quick ingredient drop montage.\n3. Macro countdown & tasting."
    ),
    InspirationIdea(
        category = "Education",
        hook = "How to learn ANY difficult skill 3X faster according to science.",
        format = "Talking Head + Text Overlays",
        scriptOutline = "1. Bold claim hook.\n2. Point 1: Active Recall.\n3. Point 2: Spaced Repetition.\n4. CTA to save reel."
    ),
    InspirationIdea(
        category = "Travel",
        hook = "Unexplored budget paradise you need to visit before it gets crowded!",
        format = "Cinematic Drone / POV Reel",
        scriptOutline = "1. Breathtaking landscape visual.\n2. Budget breakdown text.\n3. Location drop in caption."
    ),
    InspirationIdea(
        category = "Lifestyle",
        hook = "My 5 AM Mindset Routine that doubled my daily productivity.",
        format = "Aesthetic Vlog Montage",
        scriptOutline = "1. Quiet sunrise shot.\n2. Hydration & journaling.\n3. Top 3 priority focus tasks."
    ),
    InspirationIdea(
        category = "Festival",
        hook = "Creative Festive Content & Outfit Ideas for this season!",
        format = "High Energy Transition Video",
        scriptOutline = "1. Traditional outfit reveal.\n2. Festive lighting setup.\n3. Wish everyone and ask for favorite look."
    ),
    InspirationIdea(
        category = "Business",
        hook = "3 Zero-Cost Digital Business Models you can start this week.",
        format = "Whiteboard / Carousel Guide",
        scriptOutline = "1. Problem definition.\n2. Model 1, 2 & 3 simplified.\n3. Step-by-step action roadmap."
    )
)

@Composable
fun ContentInspirationSection(
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Fashion") }
    val currentIdea = CONTENT_INSPIRATION_IDEAS.firstOrNull { it.category == selectedCategory }
        ?: CONTENT_INSPIRATION_IDEAS[0]

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "✨ Content Inspiration",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Selection Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CONTENT_INSPIRATION_IDEAS) { idea ->
                val isSelected = idea.category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) Brush.horizontalGradient(listOf(ElectricPurple, EmeraldPrimary))
                            else SolidColor(Color(0x22FFFFFF))
                        )
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isSelected) EmeraldGlow else Color.White.copy(alpha = 0.2f)
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedCategory = idea.category }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = idea.category,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Idea Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(20.dp), spotColor = ElectricPurple)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF130E26), Color(0xFF0A0716))
                    )
                )
                .border(BorderStroke(1.2.dp, ElectricPurple.copy(alpha = 0.6f)), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FORMAT: ${currentIdea.format}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldGlow,
                        letterSpacing = 0.8.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x228B5CF6))
                            .clickable {
                                clipboardManager.setText(AnnotatedString("Hook: ${currentIdea.hook}\n\nOutline:\n${currentIdea.scriptOutline}"))
                                Toast.makeText(context, "Content hook & outline copied!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Hook",
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "Copy Concept",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Viral Hook:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Text(
                    text = "\"${currentIdea.hook}\"",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Script Outline:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Text(
                    text = currentIdea.scriptOutline,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 17.sp
                )
            }
        }
    }
}

// ==================================================
// SECTION 7: WEEKLY GROWTH CHALLENGE
// ==================================================
@Composable
fun WeeklyGrowthChallengeSection(
    modifier: Modifier = Modifier
) {
    var task1 by rememberSaveable { mutableStateOf(false) }
    var task2 by rememberSaveable { mutableStateOf(false) }
    var task3 by rememberSaveable { mutableStateOf(false) }
    var task4 by rememberSaveable { mutableStateOf(false) }

    val completedCount = listOf(task1, task2, task3, task4).count { it }
    val progressFraction = completedCount / 4f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = EmeraldPrimary)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF0F1A15), Color(0xFF09100C))
                )
            )
            .border(BorderStroke(1.2.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
                            .clip(RoundedCornerShape(10.dp))
                            .background(EmeraldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Growth Challenge",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Weekly Growth Challenge",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$completedCount of 4 Tasks Completed",
                            fontSize = 11.sp,
                            color = EmeraldGlow
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x3310B981))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${(progressFraction * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldGlow
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = EmeraldPrimary,
                trackColor = Color(0x33FFFFFF)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tasks List
            ChallengeTaskRow(
                title = "Finish 1 Learning Guide in Creator Academy",
                isChecked = task1,
                onCheckedChange = { task1 = it }
            )
            ChallengeTaskRow(
                title = "Publish 3 Reels / Shorts this week",
                isChecked = task2,
                onCheckedChange = { task2 = it }
            )
            ChallengeTaskRow(
                title = "Use 1 Creator AI Tool for prompt/script generation",
                isChecked = task3,
                onCheckedChange = { task3 = it }
            )
            ChallengeTaskRow(
                title = "Explore Shopping Insights for product research",
                isChecked = task4,
                onCheckedChange = { task4 = it }
            )
        }
    }
}

@Composable
private fun ChallengeTaskRow(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = EmeraldPrimary,
                uncheckedColor = Color.White.copy(alpha = 0.5f),
                checkmarkColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 12.5.sp,
            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isChecked) EmeraldGlow else Color.White.copy(alpha = 0.85f)
        )
    }
}

// ==================================================
// SECTION 8: TOP PROMOTERS (LEADERBOARD)
// Play Store Compliance: Only display real data.
// If no backend connected, display "Leaderboard coming soon."
// ==================================================
@Composable
fun TopPromotersSection(
    modifier: Modifier = Modifier
) {
    // Check if real live leaderboard data is present in Cloud Manager
    val realPromoters: List<com.example.cloud.LiveLeaderboardItem> = remember { LiveCloudManager.getLiveLeaderboard() }
    val hasRealData = realPromoters.isNotEmpty()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = Color(0xFFFFD700))
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF1A140B), Color(0xFF0F0B06))
                )
            )
            .border(BorderStroke(1.2.dp, Color(0xFFFFD700).copy(alpha = 0.5f)), RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFD700)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Leaderboard,
                            contentDescription = "Top Promoters",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = "Top Promoters",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x33FFD700))
                        .border(BorderStroke(1.dp, Color(0xFFFFD700)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "VERIFIED COMMUNITY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!hasRealData) {
                // Play Store Compliant Placeholder when backend is disconnected
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x18FFFFFF))
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassEmpty,
                            contentDescription = "Coming Soon",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Leaderboard coming soon.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Verified community leaderboard rankings will automatically populate when cloud sync is connected.",
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            } else {
                // Real Live Leaderboard Data
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    realPromoters.take(10).forEachIndexed { index, promoter ->
                        PromoterRowItem(
                            rank = index + 1,
                            name = promoter.name,
                            points = promoter.points,
                            badge = promoter.badge,
                            imageUrl = promoter.imageUrl
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PromoterRowItem(
    rank: Int,
    name: String,
    points: String,
    badge: String,
    imageUrl: String
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x22FFFFFF))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Rank Number
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(
                        when (rank) {
                            1 -> Color(0xFFFFD700)
                            2 -> Color(0xFFC0C0C0)
                            3 -> Color(0xFFCD7F32)
                            else -> Color(0x33FFFFFF)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$rank",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = if (rank <= 3) Color.Black else Color.White
                )
            }

            // Avatar
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = name,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
            )

            Column {
                Text(
                    text = name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    color = Color(0xFFFFD700)
                )
            }
        }

        Text(
            text = "$points pts",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = EmeraldGlow
        )
    }
}

// ==================================================
// SECTION 9: WHAT'S NEW
// Real features, updates, learning guides & announcements
// ==================================================
data class UpdateItem(
    val title: String,
    val description: String,
    val tag: String,
    val color: Color
)

val WHATS_NEW_UPDATES = listOf(
    UpdateItem(
        title = "AI Prompt Studio",
        description = "Analyze AI-generated artwork to receive custom recreation prompts, style breakdowns & negative prompt recommendations.",
        tag = "NEW TOOL",
        color = ElectricPurple
    ),
    UpdateItem(
        title = "Shopping Insights Engine",
        description = "Educational product analysis, review sentiment breakdown & smart buying tips across top stores.",
        tag = "FEATURE",
        color = Color(0xFF00E5FF)
    ),
    UpdateItem(
        title = "Creator Academy Guides",
        description = "Step-by-step masterclasses on brand pitching, reel engagement, and affiliate monetization.",
        tag = "LEARNING",
        color = EmeraldGlow
    )
)

@Composable
fun WhatsNewSection(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "🚀 What's New",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            WHATS_NEW_UPDATES.forEach { update ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x18FFFFFF))
                        .border(BorderStroke(1.dp, update.color.copy(alpha = 0.35f)), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = update.title,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(update.color.copy(alpha = 0.2f))
                                    .border(BorderStroke(0.8.dp, update.color), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = update.tag,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = update.color
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = update.description,
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
