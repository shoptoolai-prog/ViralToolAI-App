package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.creatoracademy.BrandCollaborationAiDialog
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.creatoracademy.WishlinkCreatorAiDialog
import com.example.ui.components.EditingToolType
import com.example.ui.components.InstagramCreatorAiV2Dialog
import com.example.ui.components.MeeshoCreatorAiDialog
import com.example.ui.components.VideoEditingMentorAiDialog
import com.example.ui.components.YouTubeCreatorAiV2Dialog
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay

enum class CourseLaunchType {
    INSTAGRAM_GROWTH,
    YOUTUBE_GROWTH,
    BRAND_COLLAB,
    MEESHO_GUIDE,
    WISHLINK_GUIDE,
    CAPCUT,
    VN_EDITOR,
    INSTAGRAM_EDITS
}

data class CourseCardModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String, // "Creator Growth" or "Video Editing Learning Hub"
    val keywords: List<String>,
    val totalLessons: Int,
    val estimatedMinutes: Int,
    val difficulty: String,
    val primaryColor: Color,
    val gradientColors: List<Color>,
    val icon: ImageVector,
    val launchType: CourseLaunchType
)

private val GoldPrimary = Color(0xFFFFD700)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorAcademyScreen(
    onNavigateToHome: () -> Unit = {},
    onSwitchExperience: () -> Unit = {},
    onResetSetup: () -> Unit = {}
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    // State
    var searchQuery by remember { mutableStateOf("") }
    var selectedLanguage by remember {
        mutableStateOf(CreatorAcademyPrefs.getPreferredLanguage(context).ifBlank { "English" })
    }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var activeCourseLaunch by remember { mutableStateOf<CourseLaunchType?>(null) }

    // Initial Loading Skeleton Simulation
    LaunchedEffect(Unit) {
        delay(350)
        isLoading = false
    }

    // List of All Educational Courses
    val allCourses = remember {
        listOf(
            // SECTION 1: CREATOR GROWTH
            CourseCardModel(
                id = "course_instagram",
                title = "Instagram Growth Guide",
                subtitle = "Master Reels, viral hooks, algorithm secrets & monetization.",
                category = "Creator Growth",
                keywords = listOf("instagram", "ig", "reels", "growth", "followers", "hooks", "viral"),
                totalLessons = 8,
                estimatedMinutes = 30,
                difficulty = "Beginner to Pro",
                primaryColor = Color(0xFFE1306C),
                gradientColors = listOf(Color(0xFF833AB4), Color(0xFFE1306C), Color(0xFFFD1D1D)),
                icon = Icons.Default.TrendingUp,
                launchType = CourseLaunchType.INSTAGRAM_GROWTH
            ),
            CourseCardModel(
                id = "course_youtube",
                title = "YouTube Growth Guide",
                subtitle = "Shorts & Long-form growth, SEO titles, thumbnails & AdSense.",
                category = "Creator Growth",
                keywords = listOf("youtube", "yt", "shorts", "seo", "thumbnails", "adsense", "subscribers"),
                totalLessons = 10,
                estimatedMinutes = 40,
                difficulty = "All Levels",
                primaryColor = Color(0xFFFF0000),
                gradientColors = listOf(Color(0xFFFF0000), Color(0xFFB71C1C)),
                icon = Icons.Default.Videocam,
                launchType = CourseLaunchType.YOUTUBE_GROWTH
            ),
            CourseCardModel(
                id = "course_brand_collab",
                title = "Brand Collaboration Hub",
                subtitle = "Land paid brand deals, pitch sponsorships & write contracts.",
                category = "Creator Growth",
                keywords = listOf("brand", "sponsorship", "collaboration", "deals", "pitch", "monetization"),
                totalLessons = 6,
                estimatedMinutes = 25,
                difficulty = "Intermediate",
                primaryColor = GoldPrimary,
                gradientColors = listOf(GoldPrimary, Color(0xFFFF8C00)),
                icon = Icons.Default.Handshake,
                launchType = CourseLaunchType.BRAND_COLLAB
            ),
            CourseCardModel(
                id = "course_meesho",
                title = "Meesho Creator Guide",
                subtitle = "Reselling, fashion haul reels, product reviews & earning.",
                category = "Creator Growth",
                keywords = listOf("meesho", "reselling", "affiliate", "fashion", "guide", "shopping"),
                totalLessons = 5,
                estimatedMinutes = 20,
                difficulty = "Beginner",
                primaryColor = Color(0xFFE91E63),
                gradientColors = listOf(Color(0xFFE91E63), Color(0xFF9C27B0)),
                icon = Icons.Default.ShoppingBag,
                launchType = CourseLaunchType.MEESHO_GUIDE
            ),
            CourseCardModel(
                id = "course_wishlink",
                title = "Wishlink Creator Guide",
                subtitle = "Automate bio links, affiliate commissions & fashion storytelling.",
                category = "Creator Growth",
                keywords = listOf("wishlink", "affiliate", "bio link", "commissions", "fashion", "guide"),
                totalLessons = 5,
                estimatedMinutes = 18,
                difficulty = "Beginner",
                primaryColor = Color(0xFF00C9FF),
                gradientColors = listOf(Color(0xFF00C9FF), Color(0xFF92FE9D)),
                icon = Icons.Default.Link,
                launchType = CourseLaunchType.WISHLINK_GUIDE
            ),

            // SECTION 2: VIDEO EDITING LEARNING HUB
            CourseCardModel(
                id = "course_capcut",
                title = "CapCut Learning Hub",
                subtitle = "Velocity edits, keyframes, auto-captions & 3D zoom effects.",
                category = "Video Editing Learning Hub",
                keywords = listOf("capcut", "video editing", "velocity", "keyframes", "effects", "capcut edits"),
                totalLessons = 7,
                estimatedMinutes = 25,
                difficulty = "All Levels",
                primaryColor = Color(0xFF00F2FE),
                gradientColors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)),
                icon = Icons.Default.Movie,
                launchType = CourseLaunchType.CAPCUT
            ),
            CourseCardModel(
                id = "course_vn",
                title = "VN Learning Hub",
                subtitle = "Multi-track editing, LUT color grading & sound curve cuts.",
                category = "Video Editing Learning Hub",
                keywords = listOf("vn", "vn editor", "video editing", "lut", "color grade", "timeline"),
                totalLessons = 6,
                estimatedMinutes = 22,
                difficulty = "Intermediate",
                primaryColor = Color(0xFF8B5CF6),
                gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF6366F1)),
                icon = Icons.Default.Movie,
                launchType = CourseLaunchType.VN_EDITOR
            ),
            CourseCardModel(
                id = "course_instagram_edits",
                title = "Instagram Edits Learning Hub",
                subtitle = "Aesthetic Reel transitions, text overlays & beat sync cuts.",
                category = "Video Editing Learning Hub",
                keywords = listOf("instagram edits", "edits", "reels editing", "aesthetic", "transitions", "reels"),
                totalLessons = 6,
                estimatedMinutes = 20,
                difficulty = "Beginner",
                primaryColor = Color(0xFFFF4081),
                gradientColors = listOf(Color(0xFFFF4081), Color(0xFF7C4DFF)),
                icon = Icons.Default.AutoAwesome,
                launchType = CourseLaunchType.INSTAGRAM_EDITS
            )
        )
    }

    // Helper to calculate progress for a course
    fun getCourseProgress(launchType: CourseLaunchType): CourseProgressInfo {
        return when (launchType) {
            CourseLaunchType.INSTAGRAM_GROWTH -> {
                val currentStep = CreatorAcademyPrefs.getBrandCollabStepIndex(context).coerceAtLeast(0)
                val completed = currentStep.coerceAtMost(8)
                CourseProgressInfo(completed, 8)
            }
            CourseLaunchType.YOUTUBE_GROWTH -> {
                val currentStep = CreatorAcademyPrefs.getYouTubeCurrentStep(context)
                val completedList = CreatorAcademyPrefs.getYouTubeCompletedSteps(context)
                val completed = completedList.size.coerceAtLeast(if (currentStep > 1) currentStep - 1 else 0)
                CourseProgressInfo(completed, 10)
            }
            CourseLaunchType.BRAND_COLLAB -> {
                val currentStep = CreatorAcademyPrefs.getBrandCollabStepIndex(context).coerceAtLeast(0)
                CourseProgressInfo(currentStep, 6)
            }
            CourseLaunchType.MEESHO_GUIDE -> {
                val currentStep = CreatorAcademyPrefs.getMeeshoStepIndex(context).coerceAtLeast(0)
                CourseProgressInfo(currentStep, 5)
            }
            CourseLaunchType.WISHLINK_GUIDE -> {
                val currentStep = CreatorAcademyPrefs.getWishlinkStepIndex(context).coerceAtLeast(0)
                val completedList = CreatorAcademyPrefs.getWishlinkCompletedSteps(context)
                val completed = completedList.size.coerceAtLeast(if (currentStep > 0) currentStep else 0)
                CourseProgressInfo(completed, 5)
            }
            CourseLaunchType.CAPCUT -> {
                val currentStep = CreatorAcademyPrefs.getEditingToolCurrentStep(context, "capcut")
                val completedList = CreatorAcademyPrefs.getEditingToolCompletedSteps(context, "capcut")
                val completed = completedList.size.coerceAtLeast(if (currentStep > 1) currentStep - 1 else 0)
                CourseProgressInfo(completed, 7)
            }
            CourseLaunchType.VN_EDITOR -> {
                val currentStep = CreatorAcademyPrefs.getEditingToolCurrentStep(context, "vn")
                val completedList = CreatorAcademyPrefs.getEditingToolCompletedSteps(context, "vn")
                val completed = completedList.size.coerceAtLeast(if (currentStep > 1) currentStep - 1 else 0)
                CourseProgressInfo(completed, 6)
            }
            CourseLaunchType.INSTAGRAM_EDITS -> {
                val currentStep = CreatorAcademyPrefs.getEditingToolCurrentStep(context, "instagram_edits")
                val completedList = CreatorAcademyPrefs.getEditingToolCompletedSteps(context, "instagram_edits")
                val completed = completedList.size.coerceAtLeast(if (currentStep > 1) currentStep - 1 else 0)
                CourseProgressInfo(completed, 6)
            }
        }
    }

    // Filter courses by search query
    val queryTrimmed = searchQuery.trim().lowercase()
    val filteredCourses = remember(queryTrimmed, allCourses) {
        if (queryTrimmed.isEmpty()) {
            allCourses
        } else {
            allCourses.filter { course ->
                course.title.lowercase().contains(queryTrimmed) ||
                course.subtitle.lowercase().contains(queryTrimmed) ||
                course.category.lowercase().contains(queryTrimmed) ||
                course.keywords.any { it.contains(queryTrimmed) }
            }
        }
    }

    val creatorGrowthCourses = filteredCourses.filter { it.category == "Creator Growth" }
    val videoEditingCourses = filteredCourses.filter { it.category == "Video Editing Learning Hub" }

    // Find course in progress for Continue Learning section
    val continueLearningCourse = remember(allCourses) {
        allCourses.map { course ->
            Pair(course, getCourseProgress(course.launchType))
        }.firstOrNull { (_, progress) ->
            progress.completedCount > 0 && progress.completedCount < progress.totalCount
        } ?: allCourses.map { course ->
            Pair(course, getCourseProgress(course.launchType))
        }.firstOrNull { (_, progress) ->
            progress.completedCount > 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // ==================================================
            // TOP HEADER: Page Title & Language Selector
            // ==================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(EmeraldPrimary.copy(alpha = 0.3f), EmeraldGlow.copy(alpha = 0.15f))
                                )
                            )
                            .border(BorderStroke(1.2.dp, EmeraldGlow.copy(alpha = 0.6f)), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Creator Learning Hub",
                            tint = EmeraldGlow,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Creator Learning Hub",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "Learn. Create. Grow.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGlow,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                // Language Selector Button
                Surface(
                    onClick = { showLanguageDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1A1F2C),
                    border = BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = EmeraldGlow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = selectedLanguage,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ==================================================
            // INSTANT SEARCH BAR
            // ==================================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = EmeraldGlow),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF141824),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Courses",
                        tint = EmeraldGlow,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Search Instagram, YouTube, CapCut, VN...",
                                fontSize = 13.sp,
                                color = TextGray
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            cursorColor = EmeraldGlow
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        modifier = Modifier.weight(1f)
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TextGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==================================================
            // SKELETON / LOADING ANIMATION
            // ==================================================
            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    repeat(3) {
                        SkeletonCourseCard()
                    }
                }
            }

            if (!isLoading) {
                // ==================================================
                // CONTINUE LEARNING SECTION (Top priority if started)
                // ==================================================
                if (continueLearningCourse != null && queryTrimmed.isEmpty()) {
                    val (course, progress) = continueLearningCourse
                    val percent = progress.percentInt
                    val remainingMins = (course.estimatedMinutes * (1f - progress.fraction)).toInt().coerceAtLeast(2)

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 18.dp)
                            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = course.primaryColor),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF151926),
                        border = BorderStroke(
                            1.5.dp,
                            Brush.horizontalGradient(
                                listOf(course.primaryColor, EmeraldGlow, Color.White.copy(alpha = 0.2f))
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Continue",
                                        tint = course.primaryColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "CONTINUE LEARNING",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = course.primaryColor,
                                        letterSpacing = 1.sp
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = course.primaryColor.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, course.primaryColor.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "⏳ $remainingMins mins left",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = course.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Progress Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                LinearProgressIndicator(
                                    progress = { progress.fraction },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(CircleShape),
                                    color = course.primaryColor,
                                    trackColor = Color.White.copy(alpha = 0.1f)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "$percent%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { activeCourseLaunch = course.launchType },
                                colors = ButtonDefaults.buttonColors(containerColor = course.primaryColor),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Resume",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Continue ${course.title}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // ==================================================
                // EMPTY STATE
                // ==================================================
                if (filteredCourses.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGlow.copy(alpha = 0.15f))
                                    .border(BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.4f)), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "No courses",
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Start learning to grow as a creator.",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "No courses found matching '$searchQuery'. Try searching 'Instagram', 'CapCut', or 'YouTube'.",
                                fontSize = 12.sp,
                                color = TextGray,
                                textAlign = TextAlign.Center,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { searchQuery = "" },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Clear Search Filter", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }

                // ==================================================
                // SECTION 1: CREATOR GROWTH
                // ==================================================
                if (creatorGrowthCourses.isNotEmpty()) {
                    Text(
                        text = "🚀 CREATOR GROWTH",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldGlow,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        creatorGrowthCourses.forEach { course ->
                            val progress = getCourseProgress(course.launchType)
                            CourseCardItem(
                                course = course,
                                progress = progress,
                                onCourseClick = { activeCourseLaunch = course.launchType }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                }

                // ==================================================
                // SECTION 2: VIDEO EDITING LEARNING HUB
                // ==================================================
                if (videoEditingCourses.isNotEmpty()) {
                    Text(
                        text = "✂️ VIDEO EDITING LEARNING HUB",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ElectricPurple,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        videoEditingCourses.forEach { course ->
                            val progress = getCourseProgress(course.launchType)
                            CourseCardItem(
                                course = course,
                                progress = progress,
                                onCourseClick = { activeCourseLaunch = course.launchType }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }

        // ==================================================
        // ACTIVE COURSE DIALOG LAUNCHERS
        // ==================================================
        when (activeCourseLaunch) {
            CourseLaunchType.INSTAGRAM_GROWTH -> {
                InstagramCreatorAiV2Dialog(onDismiss = { activeCourseLaunch = null })
            }
            CourseLaunchType.YOUTUBE_GROWTH -> {
                YouTubeCreatorAiV2Dialog(onDismiss = { activeCourseLaunch = null })
            }
            CourseLaunchType.BRAND_COLLAB -> {
                BrandCollaborationAiDialog(onDismiss = { activeCourseLaunch = null })
            }
            CourseLaunchType.MEESHO_GUIDE -> {
                MeeshoCreatorAiDialog(onDismiss = { activeCourseLaunch = null })
            }
            CourseLaunchType.WISHLINK_GUIDE -> {
                WishlinkCreatorAiDialog(onDismiss = { activeCourseLaunch = null })
            }
            CourseLaunchType.CAPCUT -> {
                VideoEditingMentorAiDialog(
                    toolType = EditingToolType.CAPCUT,
                    onDismiss = { activeCourseLaunch = null }
                )
            }
            CourseLaunchType.VN_EDITOR -> {
                VideoEditingMentorAiDialog(
                    toolType = EditingToolType.VN,
                    onDismiss = { activeCourseLaunch = null }
                )
            }
            CourseLaunchType.INSTAGRAM_EDITS -> {
                VideoEditingMentorAiDialog(
                    toolType = EditingToolType.INSTAGRAM_EDITS,
                    onDismiss = { activeCourseLaunch = null }
                )
            }
            null -> {}
        }

        // ==================================================
        // LANGUAGE SELECTOR DIALOG
        // ==================================================
        if (showLanguageDialog) {
            LanguageSelectorModal(
                currentLanguage = selectedLanguage,
                onLanguageSelected = { lang ->
                    selectedLanguage = lang
                    CreatorAcademyPrefs.setPreferredLanguage(context, lang)
                    Toast.makeText(context, "Language updated to $lang", Toast.LENGTH_SHORT).show()
                    showLanguageDialog = false
                },
                onDismiss = { showLanguageDialog = false }
            )
        }
    }
}

data class CourseProgressInfo(
    val completedCount: Int,
    val totalCount: Int
) {
    val fraction: Float = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val percentInt: Int = (fraction * 100).toInt()
    val isStarted: Boolean = completedCount > 0
    val isCompleted: Boolean = completedCount >= totalCount && totalCount > 0
}

@Composable
private fun CourseCardItem(
    course: CourseCardModel,
    progress: CourseProgressInfo,
    onCourseClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, shape = RoundedCornerShape(18.dp), spotColor = course.primaryColor)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onCourseClick() },
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF141824),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                listOf(
                    course.primaryColor.copy(alpha = 0.7f),
                    Color.White.copy(alpha = 0.12f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(course.gradientColors)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = course.icon,
                            contentDescription = course.title,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = course.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "⏱️ ${course.estimatedMinutes} mins",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                            Text(
                                text = "•",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                            Text(
                                text = "🎯 ${course.difficulty}",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                        }
                    }
                }

                // Difficulty / Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (progress.isCompleted) EmeraldGlow.copy(alpha = 0.2f) else course.primaryColor.copy(alpha = 0.15f),
                    border = BorderStroke(
                        1.dp,
                        if (progress.isCompleted) EmeraldGlow.copy(alpha = 0.5f) else course.primaryColor.copy(alpha = 0.4f)
                    )
                ) {
                    Text(
                        text = if (progress.isCompleted) "✓ Completed" else if (progress.isStarted) "${progress.percentInt}%" else "New",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (progress.isCompleted) EmeraldGlow else TextWhite,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = course.subtitle,
                fontSize = 12.5.sp,
                color = TextWhite.copy(alpha = 0.8f),
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar if started
            if (progress.isStarted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LinearProgressIndicator(
                        progress = { progress.fraction },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape),
                        color = course.primaryColor,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${progress.completedCount}/${progress.totalCount} Lessons",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Action Button
            Button(
                onClick = onCourseClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (progress.isStarted) course.primaryColor else Color(0xFF222838)
                ),
                shape = RoundedCornerShape(10.dp),
                border = if (!progress.isStarted) BorderStroke(1.dp, course.primaryColor.copy(alpha = 0.5f)) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Icon(
                    imageVector = if (progress.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (progress.isCompleted) "Review Course" else if (progress.isStarted) "Continue Lesson" else "Start Course",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SkeletonCourseCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF141824).copy(alpha = alphaAnim),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {}
}

@Composable
private fun LanguageSelectorModal(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf(
        Pair("English", "International Standard English"),
        Pair("Hinglish", "Hindi + English Creator Style"),
        Pair("Hindi", "हिंदी - Complete Hindi Experience")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF161B26),
            border = BorderStroke(1.5.dp, EmeraldGlow.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "🌐 Choose Preferred Language",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                languages.forEach { (langName, langDesc) ->
                    val isSelected = currentLanguage.equals(langName, ignoreCase = true)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onLanguageSelected(langName) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) EmeraldGlow.copy(alpha = 0.15f) else Color(0xFF0F131D),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) EmeraldGlow else Color.White.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = langName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) EmeraldGlow else TextWhite
                                )
                                Text(
                                    text = langDesc,
                                    fontSize = 11.sp,
                                    color = TextGray
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
