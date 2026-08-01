package com.example.ui.components

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.theme.*

private val GlassBg = Color(0x1AFFFFFF)
private val GlassBorder = Color(0x288B5CF6)
private val SuccessGreen = Color(0xFF10B981)
private val WarningAmber = Color(0xFFF59E0B)

// Data models for Creator OS Dashboard
data class ContentTask(
    val id: String,
    val title: String,
    val time: String,
    var isCompleted: Boolean
)

data class ContentIdea(
    val title: String,
    val hook: String,
    val format: String,
    val nicheCategory: String
)

data class CreatorMission(
    val id: Int,
    val title: String,
    val xpReward: Int,
    val description: String
)

private const val OS_PREF_NAME = "creator_os_dashboard_prefs"
private const val KEY_COMPLETED_REELS_COUNT = "completed_reels_count"
private const val KEY_COMPLETED_POSTS_COUNT = "completed_posts_count"
private const val KEY_TASKS_FINISHED_COUNT = "tasks_finished_count"
private const val KEY_CURRENT_MISSION_INDEX = "current_mission_index"
private const val KEY_TASK_REEL_DONE = "task_reel_done"
private const val KEY_TASK_STORY_DONE = "task_story_done"

object CreatorOsPrefs {
    fun getReelsCount(context: Context): Int =
        context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE).getInt(KEY_COMPLETED_REELS_COUNT, 0)

    fun incrementReels(context: Context) {
        val prefs = context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_COMPLETED_REELS_COUNT, prefs.getInt(KEY_COMPLETED_REELS_COUNT, 0) + 1).apply()
    }

    fun getPostsCount(context: Context): Int =
        context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE).getInt(KEY_COMPLETED_POSTS_COUNT, 0)

    fun incrementPosts(context: Context) {
        val prefs = context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_COMPLETED_POSTS_COUNT, prefs.getInt(KEY_COMPLETED_POSTS_COUNT, 0) + 1).apply()
    }

    fun getTasksFinished(context: Context): Int =
        context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE).getInt(KEY_TASKS_FINISHED_COUNT, 0)

    fun incrementTasksFinished(context: Context) {
        val prefs = context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_TASKS_FINISHED_COUNT, prefs.getInt(KEY_TASKS_FINISHED_COUNT, 0) + 1).apply()
    }

    fun getMissionIndex(context: Context): Int =
        context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE).getInt(KEY_CURRENT_MISSION_INDEX, 0)

    fun advanceMission(context: Context) {
        val prefs = context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE)
        val cur = prefs.getInt(KEY_CURRENT_MISSION_INDEX, 0)
        prefs.edit().putInt(KEY_CURRENT_MISSION_INDEX, cur + 1).apply()
    }

    fun getTaskDone(context: Context, key: String): Boolean =
        context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE).getBoolean(key, false)

    fun setTaskDone(context: Context, key: String, done: Boolean) {
        context.getSharedPreferences(OS_PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, done).apply()
    }
}

private val SAMPLE_MISSIONS = listOf(
    CreatorMission(1, "Upload 1 Reel", 100, "Publish a 15-sec value reel using a high-curiosity hook."),
    CreatorMission(2, "Complete 1 Lesson", 150, "Finish any Creator Academy module to boost your skills."),
    CreatorMission(3, "Write 5 Content Ideas", 100, "Brainstorm 5 reel ideas for your niche in AI Studio."),
    CreatorMission(4, "Practice 3 Hooks", 120, "Test 3 distinct visual & text hooks on your audience."),
    CreatorMission(5, "Audit Your Bio & Highlights", 150, "Add a clear value proposition statement to your profile.")
)

private val CONTENT_IDEAS_DATABASE = mapOf(
    "Tech" to listOf(
        ContentIdea("3 Hidden Smartphone Hacks You Need", "'Stop using your phone like this in 2026!'", "Reel (15s)", "Tech"),
        ContentIdea("AI Tools Replacing 5 Hours of Work", "'If you are not using these 3 free AI sites...'", "Carousel", "Tech"),
        ContentIdea("How to Edit Like a Pro on Phone", "'This 1 hidden setting changes everything!'", "Reel (30s)", "Tech")
    ),
    "Shopping Reviews" to listOf(
        ContentIdea("Top 5 Amazon Tech Products Under ₹500", "'I bought 5 cheap gadgets so you don't have to!'", "Reel (30s)", "Shopping"),
        ContentIdea("3 Meesho Finds That Look Expensive", "'Is this ₹399 outfit worth it?'", "Reel (20s)", "Shopping"),
        ContentIdea("Before You Buy This Viral Product", "'The truth nobody is telling you about...'", "Reel (15s)", "Shopping")
    ),
    "Fashion" to listOf(
        ContentIdea("Styling 1 Shirt 3 Different Ways", "'Stop wearing your basic shirt the same boring way!'", "Reel (15s)", "Fashion"),
        ContentIdea("5 Color Combinations That Look Rich", "'Elevate your outfit with these luxury pairs.'", "Carousel", "Fashion"),
        ContentIdea("Myntra Haul Under ₹999", "'Found the dream streetwear combo!'", "Reel (25s)", "Fashion")
    ),
    "General" to listOf(
        ContentIdea("3 Mistakes Ruining Your Organic Reach", "'Do NOT upload your reels until you check this!'", "Reel (20s)", "Growth"),
        ContentIdea("How to Get Your First 10,000 Followers", "'The exact 30-day strategy I used.'", "Carousel", "Growth"),
        ContentIdea("5 High-Converting Hook Formulas", "'Use these exact words to get 10x more watch time!'", "Reel (15s)", "Growth")
    )
)

private val TRENDING_TOPICS_POOL = listOf(
    Pair("#AIReels", "🔥 1.2M Reels • 42% High Reach"),
    Pair("#ShoppingHaul", "🛍️ 850K Reels • Viral Search"),
    Pair("#CapCutTemplates", "✂️ 3.4M Reels • High Engagement"),
    Pair("#MicroSaaS", "💻 450K Reels • High RPM Niche"),
    Pair("#OutfitInspo", "👗 2.1M Reels • Steady Growth"),
    Pair("#TechUnboxing", "📦 1.8M Reels • Brand Magnet")
)

@Composable
fun CreatorOsDashboard(
    modifier: Modifier = Modifier,
    onNavigateToAcademy: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(true) }

    // Persistent State
    val academySetup = remember { CreatorAcademyPrefs.getSetupData(context) }
    var taskReelDone by remember { mutableStateOf(CreatorOsPrefs.getTaskDone(context, KEY_TASK_REEL_DONE)) }
    var taskStoryDone by remember { mutableStateOf(CreatorOsPrefs.getTaskDone(context, KEY_TASK_STORY_DONE)) }

    val reelsCount = remember { mutableStateOf(CreatorOsPrefs.getReelsCount(context)) }
    val postsCount = remember { mutableStateOf(CreatorOsPrefs.getPostsCount(context)) }
    val tasksFinishedCount = remember { mutableStateOf(CreatorOsPrefs.getTasksFinished(context)) }
    val missionIndexState = remember { mutableStateOf(CreatorOsPrefs.getMissionIndex(context)) }

    // Completed course lessons count from Instagram & YouTube
    val instaCompletedSteps = remember { CreatorAcademyPrefs.getInstagramCompletedSteps(context).size }
    val ytCompletedSteps = remember { CreatorAcademyPrefs.getYouTubeCompletedSteps(context).size }
    val totalCoursesCompleted = instaCompletedSteps + ytCompletedSteps

    // Calculate Real Growth Score (0 to 100)
    val hasLocalData = totalCoursesCompleted > 0 || reelsCount.value > 0 || postsCount.value > 0 || tasksFinishedCount.value > 0
    val rawScore = if (hasLocalData) {
        val coursePart = (totalCoursesCompleted * 8).coerceAtMost(40)
        val taskPart = (tasksFinishedCount.value * 10).coerceAtMost(30)
        val reelPart = (reelsCount.value * 10).coerceAtMost(20)
        val streakPart = (CreatorAcademyPrefs.getStreakDays(context) * 5).coerceAtMost(10)
        (coursePart + taskPart + reelPart + streakPart).coerceIn(10, 100)
    } else 0

    // Ideas cycle index
    var ideaIndex by remember { mutableStateOf(0) }
    val currentNicheKey = if (CONTENT_IDEAS_DATABASE.containsKey(academySetup.niche)) academySetup.niche else "General"
    val nicheIdeas = CONTENT_IDEAS_DATABASE[currentNicheKey] ?: CONTENT_IDEAS_DATABASE["General"]!!
    val currentIdea = nicheIdeas[ideaIndex % nicheIdeas.size]

    // Trending topics cycle
    var topicOffset by remember { mutableStateOf(0) }
    val activeTrendingTopics = remember(topicOffset) {
        val size = TRENDING_TOPICS_POOL.size
        listOf(
            TRENDING_TOPICS_POOL[topicOffset % size],
            TRENDING_TOPICS_POOL[(topicOffset + 1) % size],
            TRENDING_TOPICS_POOL[(topicOffset + 2) % size]
        )
    }

    // Current Mission
    val curMissionIndex = missionIndexState.value % SAMPLE_MISSIONS.size
    val currentMission = SAMPLE_MISSIONS[curMissionIndex]

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = VioletPrimary,
                ambientColor = Color.Black
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B0B33),
                        Color(0xFF120724),
                        Color(0xFF090413)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            VioletGlow.copy(alpha = 0.8f),
                            VioletPrimary.copy(alpha = 0.5f),
                            ElectricPurple.copy(alpha = 0.3f)
                        )
                    )
                ),
                RoundedCornerShape(24.dp)
            ),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ==========================================
            // HEADER WITH EXPAND/COLLAPSE TOGGLE
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(VioletPrimary.copy(alpha = 0.8f), Color.Transparent)
                                )
                            )
                            .border(1.dp, VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚀", fontSize = 18.sp)
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Creator OS Dashboard",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = VioletPrimary.copy(alpha = 0.25f),
                                border = BorderStroke(0.8.dp, VioletGlow)
                            ) {
                                Text(
                                    text = "CORE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VioletGlow,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Daily Command Center & Growth Engine",
                            fontSize = 10.5.sp,
                            color = TextGray
                        )
                    }
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Dashboard",
                        tint = VioletGlow
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Divider(color = GlassBorder, thickness = 1.dp)

                    // ------------------------------------------
                    // 1. TODAY'S CONTENT PLAN
                    // ------------------------------------------
                    DashboardSectionCard(
                        icon = "📅",
                        title = "Today's Content Plan",
                        subtitle = "Scheduled creator tasks"
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            TaskRowItem(
                                title = "Reel – 7:00 PM",
                                subtitle = "High-Hook Value Video",
                                isDone = taskReelDone,
                                onToggle = { newDone ->
                                    taskReelDone = newDone
                                    CreatorOsPrefs.setTaskDone(context, KEY_TASK_REEL_DONE, newDone)
                                    if (newDone) {
                                        CreatorOsPrefs.incrementReels(context)
                                        reelsCount.value = CreatorOsPrefs.getReelsCount(context)
                                    }
                                }
                            )

                            TaskRowItem(
                                title = "Story – 8:30 PM",
                                subtitle = "Behind-the-scenes Poll / Q&A",
                                isDone = taskStoryDone,
                                onToggle = { newDone ->
                                    taskStoryDone = newDone
                                    CreatorOsPrefs.setTaskDone(context, KEY_TASK_STORY_DONE, newDone)
                                    if (newDone) {
                                        CreatorOsPrefs.incrementPosts(context)
                                        postsCount.value = CreatorOsPrefs.getPostsCount(context)
                                    }
                                }
                            )
                        }
                    }

                    // ------------------------------------------
                    // 2. TODAY'S AI CONTENT IDEA
                    // ------------------------------------------
                    DashboardSectionCard(
                        icon = "💡",
                        title = "Today's AI Content Idea",
                        subtitle = "Personalized for ${academySetup.niche}",
                        actionButtonText = "Cycle Idea 🔄",
                        onActionClick = { ideaIndex++ }
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(0.8.dp, GlassBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = currentIdea.title,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF2D1452),
                                        border = BorderStroke(0.5.dp, VioletGlow)
                                    ) {
                                        Text(
                                            text = currentIdea.format,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VioletGlow,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Hook: ${currentIdea.hook}",
                                    fontSize = 11.5.sp,
                                    color = WarningAmber,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // ------------------------------------------
                    // 3. GROWTH SCORE
                    // ------------------------------------------
                    DashboardSectionCard(
                        icon = "📈",
                        title = "Growth Score",
                        subtitle = "Real local progress metrics"
                    ) {
                        if (!hasLocalData) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(0.8.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
                                    Text(
                                        text = "Start your creator journey to unlock your Growth Score.",
                                        fontSize = 12.sp,
                                        color = TextGray,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "$rawScore / 100",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Black,
                                            color = SuccessGreen
                                        )
                                        Text(
                                            text = "Based on course completion & completed tasks",
                                            fontSize = 10.5.sp,
                                            color = TextGray
                                        )
                                    }

                                    Button(
                                        onClick = { onNavigateToAcademy?.invoke() },
                                        colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Boost Score 🚀", fontSize = 10.5.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                    }
                                }

                                LinearProgressIndicator(
                                    progress = { rawScore / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = SuccessGreen,
                                    trackColor = GlassBg
                                )
                            }
                        }
                    }

                    // ------------------------------------------
                    // 4. BRAND OPPORTUNITIES
                    // ------------------------------------------
                    DashboardSectionCard(
                        icon = "🤝",
                        title = "Brand Opportunities",
                        subtitle = "Sponsorships & Collabs"
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(0.8.dp, GlassBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = "No opportunities available yet",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                                Text(
                                    text = "Backend integration required for live campaigns. Build your Media Kit in Creator Academy to get brand-ready!",
                                    fontSize = 11.sp,
                                    color = TextGray
                                )
                            }
                        }
                    }

                    // ------------------------------------------
                    // 5. TRENDING TOPICS
                    // ------------------------------------------
                    DashboardSectionCard(
                        icon = "🔥",
                        title = "Trending Topics",
                        subtitle = "High-reach creator categories",
                        actionButtonText = "Refresh 🔥",
                        onActionClick = { topicOffset++ }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            activeTrendingTopics.forEach { (tag, info) ->
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    color = GlassBg,
                                    border = BorderStroke(0.8.dp, GlassBorder)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = tag,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VioletGlow,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = info,
                                            fontSize = 9.sp,
                                            color = TextGray,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ------------------------------------------
                    // 6. WEEKLY REVIEW
                    // ------------------------------------------
                    DashboardSectionCard(
                        icon = "📊",
                        title = "Weekly Review",
                        subtitle = "7-day activity breakdown"
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatBoxItem("Posts", postsCount.value.toString(), Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(6.dp))
                            StatBoxItem("Reels", reelsCount.value.toString(), Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(6.dp))
                            StatBoxItem("Courses", totalCoursesCompleted.toString(), Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(6.dp))
                            StatBoxItem("Tasks", tasksFinishedCount.value.toString(), Modifier.weight(1f))
                        }
                    }

                    // ------------------------------------------
                    // 7. NEXT MISSION
                    // ------------------------------------------
                    DashboardSectionCard(
                        icon = "🎯",
                        title = "Next Mission",
                        subtitle = "Achievable Creator Goals"
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF1E0E38),
                            border = BorderStroke(1.dp, VioletGlow)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = currentMission.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SuccessGreen.copy(alpha = 0.2f),
                                        border = BorderStroke(0.8.dp, SuccessGreen)
                                    ) {
                                        Text(
                                            text = "+${currentMission.xpReward} XP",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessGreen,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = currentMission.description,
                                    fontSize = 11.5.sp,
                                    color = TextGray
                                )

                                Button(
                                    onClick = {
                                        CreatorAcademyPrefs.addXpPoints(context, currentMission.xpReward)
                                        CreatorOsPrefs.incrementTasksFinished(context)
                                        CreatorOsPrefs.advanceMission(context)
                                        tasksFinishedCount.value = CreatorOsPrefs.getTasksFinished(context)
                                        missionIndexState.value = CreatorOsPrefs.getMissionIndex(context)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Complete Mission & Get Next ➔", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardSectionCard(
    icon: String,
    title: String,
    subtitle: String,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = icon, fontSize = 14.sp)
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            if (actionButtonText != null && onActionClick != null) {
                Text(
                    text = actionButtonText,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletGlow,
                    modifier = Modifier.clickable { onActionClick() }
                )
            }
        }

        content()
    }
}

@Composable
private fun TaskRowItem(
    title: String,
    subtitle: String,
    isDone: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isDone) },
        shape = RoundedCornerShape(10.dp),
        color = if (isDone) Color(0xFF0F261B) else GlassBg,
        border = BorderStroke(0.8.dp, if (isDone) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { onToggle(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = SuccessGreen,
                        uncheckedColor = TextGray
                    ),
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = title,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDone) SuccessGreen else TextWhite
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.5.sp,
                        color = TextGray
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isDone) SuccessGreen.copy(alpha = 0.2f) else GlassBg
            ) {
                Text(
                    text = if (isDone) "Completed ✅" else "Pending ⏳",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDone) SuccessGreen else WarningAmber,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun StatBoxItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = GlassBg,
        border = BorderStroke(0.8.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = VioletGlow
            )
            Text(
                text = label,
                fontSize = 9.5.sp,
                color = TextGray
            )
        }
    }
}
