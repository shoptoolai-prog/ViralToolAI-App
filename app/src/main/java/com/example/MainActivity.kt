package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.CompositionLocalProvider
import com.example.ui.components.Material3SharedElementContainer
import com.example.ui.components.LocalAnimatedVisibilityScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import androidx.compose.material.icons.filled.Videocam
import com.example.ui.screens.VideoEditingScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.screens.CreatorAcademyScreen
import com.example.ui.screens.CreatorAcademySetupScreen
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.example.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShoppingItem
import com.example.ui.screens.AiLabScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.ReferAndEarnScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

import com.example.data.ShareIntentHandler
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import com.example.ui.screens.OnboardingPrefs
import com.example.ui.screens.OnboardingScreen
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cloud.LiveCloudManager
import com.example.cloud.MaintenanceDialog
import com.example.cloud.ForceUpdateDialog
import com.example.cloud.LiveAnnouncementDialog

import com.example.core.rememberIsOnlineState
import com.example.ui.components.OfflineBanner
import androidx.compose.foundation.layout.statusBarsPadding
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.CachePolicy
import android.util.Log

class MainActivity : ComponentActivity() {

    private val sharedUrlState = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Edge to edge support ensures full-bleed AMOLED Black styling
        enableEdgeToEdge()

        // Handle Android Share Target Intent
        handleIncomingIntent(intent)
        
        setContent {
            MyApplicationTheme {
                MainAppLayout(sharedUrl = sharedUrlState.value)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        val extractedUrl = ShareIntentHandler.extractSharedUrl(intent)
        if (!extractedUrl.isNullOrBlank()) {
            sharedUrlState.value = extractedUrl
        }
    }
}

enum class Screen {
    Splash,
    Onboarding,
    Home,
    CreatorAcademySetup,
    CreatorAcademy,
    AiLab,
    Projects,
    VideoEditing,
    ReferAndEarn,
    History,
    Profile,
    Analysis,
    Result,
    MediaPicker,
    ProjectSetup,
    AiCreatorAssistant,
    ThumbnailPicker,
    SubtitlesGenerator,
    VoiceCleaner,
    SmartVideoText,
    ShoppingAssistant,
    RemoveBackground
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MainAppLayout(sharedUrl: String? = null) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        com.example.core.LanguageEngine.init(context)
    }

    var currentScreen by remember { mutableStateOf(if (!sharedUrl.isNullOrBlank()) Screen.Home else Screen.Splash) }
    var analyzedLink by remember { mutableStateOf("") }
    var selectedMediaForProject by remember { mutableStateOf<List<com.example.ui.screens.MediaPickerItem>>(emptyList()) }
    var currentProjectConfig by remember { mutableStateOf<com.example.ui.screens.ProjectSetupConfig?>(null) }

    // Automatically navigate to Home screen when a link is shared from Android Share sheet
    LaunchedEffect(sharedUrl) {
        if (!sharedUrl.isNullOrBlank()) {
            currentScreen = Screen.Home
        }
    }
    
    // Core reactive local history state
    var historyList by remember { mutableStateOf(listOf<ShoppingItem>()) }
    
    val onAddHistoryItem = { item: ShoppingItem ->
        historyList = listOf(item) + historyList
    }
    
    val onClearHistory = {
        historyList = emptyList()
    }

    val onDeleteHistoryItem = { id: String ->
        historyList = historyList.filterNot { it.id == id }
    }

    val isOnline by rememberIsOnlineState()

    // Live Cloud System States
    val maintenanceMode by LiveCloudManager.maintenanceMode.collectAsStateWithLifecycle()
    val forceUpdate by LiveCloudManager.forceUpdate.collectAsStateWithLifecycle()
    val minimumSupportedVersion by LiveCloudManager.minimumSupportedVersion.collectAsStateWithLifecycle()
    val playstoreUrl by LiveCloudManager.playstoreUrl.collectAsStateWithLifecycle()
    val announcementConfig by LiveCloudManager.announcementConfig.collectAsStateWithLifecycle()

    var showAnnouncementDialog by remember { mutableStateOf(false) }

    LaunchedEffect(announcementConfig) {
        if (announcementConfig.enabled && announcementConfig.title.isNotBlank()) {
            showAnnouncementDialog = true
        }
    }

    // Render cloud global dialogs
    if (maintenanceMode) {
        MaintenanceDialog(onRetry = { /* Re-check */ })
    } else if (forceUpdate) {
        ForceUpdateDialog(playstoreUrl = playstoreUrl, minimumVersion = minimumSupportedVersion)
    } else if (showAnnouncementDialog && announcementConfig.enabled) {
        LiveAnnouncementDialog(
            config = announcementConfig,
            onDismiss = { showAnnouncementDialog = false }
        )
    }

    val showBottomNav = currentScreen != Screen.Splash && 
            currentScreen != Screen.Onboarding && 
            currentScreen != Screen.CreatorAcademySetup && 
            currentScreen != Screen.Result && 
            currentScreen != Screen.Analysis &&
            currentScreen != Screen.MediaPicker &&
            currentScreen != Screen.ProjectSetup &&
            currentScreen != Screen.VideoEditing &&
            currentScreen != Screen.AiCreatorAssistant &&
            currentScreen != Screen.ThumbnailPicker &&
            currentScreen != Screen.SubtitlesGenerator &&
            currentScreen != Screen.VoiceCleaner &&
            currentScreen != Screen.SmartVideoText &&
            currentScreen != Screen.ShoppingAssistant &&
            currentScreen != Screen.RemoveBackground

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AmoledBlack,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { selected ->
                        currentScreen = selected
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Offline notification banner
            if (currentScreen != Screen.Splash && currentScreen != Screen.Onboarding) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .zIndex(150f)
                ) {
                    OfflineBanner(
                        isOffline = !isOnline,
                        onRetry = {
                            // Retry connectivity check
                        }
                    )
                }
            }
            // Screen switching with smooth Material 3 Shared Element & Container Transform transitions
            Material3SharedElementContainer {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        val duration = 380
                        (fadeIn(animationSpec = tween(duration, easing = FastOutSlowInEasing)) +
                                scaleIn(initialScale = 0.93f, animationSpec = tween(duration, easing = FastOutSlowInEasing)) +
                                slideInHorizontally(initialOffsetX = { it / 10 }, animationSpec = tween(duration))) togetherWith
                        (fadeOut(animationSpec = tween(duration, easing = FastOutSlowInEasing)) +
                                scaleOut(targetScale = 1.05f, animationSpec = tween(duration, easing = FastOutSlowInEasing)) +
                                slideOutHorizontally(targetOffsetX = { -it / 10 }, animationSpec = tween(duration)))
                    },
                    label = "Material3SharedElementScreenTransition"
                ) { screen ->
                    CompositionLocalProvider(
                        LocalAnimatedVisibilityScope provides this
                    ) {
                        when (screen) {
                    Screen.Splash -> {
                        SplashScreen(onSplashComplete = {
                            try {
                                if (!OnboardingPrefs.isOnboardingCompleted(context)) {
                                    currentScreen = Screen.Onboarding
                                } else {
                                    currentScreen = Screen.Home
                                }
                            } catch (e: Exception) {
                                currentScreen = Screen.Home
                            }
                        })
                    }
                    Screen.Onboarding -> {
                        OnboardingScreen(onOnboardingFinished = {
                            currentScreen = Screen.Home
                        })
                    }
                    Screen.CreatorAcademySetup -> {
                        CreatorAcademySetupScreen(
                            onSetupCompleted = {
                                currentScreen = Screen.CreatorAcademy
                            },
                            onBack = {
                                currentScreen = Screen.Home
                            }
                        )
                    }
                    Screen.CreatorAcademy -> {
                        CreatorAcademyScreen(
                            onSwitchExperience = {
                                currentScreen = Screen.Home
                            },
                            onResetSetup = {
                                currentScreen = Screen.Home
                            }
                        )
                    }
                    Screen.VideoEditing -> {
                        VideoEditingScreen(
                            projectConfig = currentProjectConfig,
                            onNavigateToHome = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.ReferAndEarn -> {
                        ReferAndEarnScreen()
                    }
                    Screen.Home -> {
                        HomeScreen(
                            onNavigateToProjects = { currentScreen = Screen.Projects },
                            onNavigateToAiLab = { currentScreen = Screen.AiLab },
                            onNavigateToAcademy = { currentScreen = Screen.CreatorAcademy },
                            onNavigateToMediaPicker = { currentScreen = Screen.MediaPicker },
                            onVideoImportedToEditor = { config ->
                                currentProjectConfig = config
                                currentScreen = Screen.VideoEditing
                            },
                            onNavigateToAiCreatorAssistant = { currentScreen = Screen.AiCreatorAssistant },
                            onNavigateToThumbnailPicker = { currentScreen = Screen.ThumbnailPicker },
                            onNavigateToSubtitlesGenerator = { currentScreen = Screen.SubtitlesGenerator },
                            onNavigateToVoiceCleaner = { currentScreen = Screen.VoiceCleaner },
                            onNavigateToSmartVideoText = { currentScreen = Screen.SmartVideoText },
                            onNavigateToShoppingAssistant = { currentScreen = Screen.ShoppingAssistant },
                            onNavigateToRemoveBackground = { currentScreen = Screen.RemoveBackground },
                            initialSharedUrl = sharedUrl
                        )
                    }
                    Screen.RemoveBackground -> {
                        com.example.ui.screens.tools.RemoveBackgroundScreen(
                            onBack = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.ShoppingAssistant -> {
                        com.example.ui.screens.tools.ShoppingAssistantScreen(
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.ThumbnailPicker -> {
                        com.example.ui.screens.tools.ThumbnailPickerScreen(
                            onBack = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.SubtitlesGenerator -> {
                        com.example.ui.screens.tools.SubtitlesGeneratorScreen(
                            onBack = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.VoiceCleaner -> {
                        com.example.ui.screens.tools.VoiceCleanerScreen(
                            onBack = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.SmartVideoText -> {
                        com.example.ui.screens.tools.SmartVideoTextScreen(
                            onBack = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.AiCreatorAssistant -> {
                        com.example.creatorassistant.ui.AiCreatorAssistantScreen(
                            onNavigateToHome = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.MediaPicker -> {
                        com.example.ui.screens.MediaPickerScreen(
                            onClose = { currentScreen = Screen.Home },
                            onNext = { items ->
                                selectedMediaForProject = items
                                currentScreen = Screen.ProjectSetup
                            }
                        )
                    }
                    Screen.ProjectSetup -> {
                        com.example.ui.screens.ProjectSetupScreen(
                            initialSelectedMedia = selectedMediaForProject,
                            onBackToPicker = { currentScreen = Screen.MediaPicker },
                            onStartEditing = { config ->
                                currentProjectConfig = config
                                currentScreen = Screen.VideoEditing
                            }
                        )
                    }
                    Screen.AiLab -> {
                        AiLabScreen(
                            onNavigateToHistory = { currentScreen = Screen.History },
                            onNavigateToAnalysis = { link ->
                                analyzedLink = link
                                currentScreen = Screen.Analysis
                            }
                        )
                    }
                    Screen.Projects -> {
                        ProjectsScreen(
                            onNavigateToHome = { currentScreen = Screen.Home },
                            onNavigateToAcademy = { currentScreen = Screen.CreatorAcademy }
                        )
                    }
                    Screen.History -> {
                        HistoryScreen(
                            historyList = historyList,
                            onClearHistory = onClearHistory,
                            onDeleteHistoryItem = onDeleteHistoryItem,
                            onNavigateToHome = { currentScreen = Screen.Home },
                            onReopenReport = { url ->
                                analyzedLink = url
                                currentScreen = Screen.Result
                            }
                        )
                    }
                    Screen.Profile -> {
                        ProfileScreen(
                            onSwitchExperience = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Analysis -> {
                        com.example.ui.screens.ResultScreen(
                            analyzedLink = analyzedLink,
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Result -> {
                        com.example.ui.screens.ResultScreen(
                            analyzedLink = analyzedLink,
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                }
            }
        }
    }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    // Premium Floating Bottom Navigation Bar (Height 72dp, rounded, dark luxury container)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .zIndex(100f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(36.dp)),
            color = Color(0xFF141414),
            shape = RoundedCornerShape(36.dp),
            border = BorderStroke(1.dp, Color(0xFF1B1B1B)),
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Home tab
                NavigationTabItem(
                    screen = Screen.Home,
                    icon = Icons.Default.Home,
                    label = "Home",
                    isSelected = currentScreen == Screen.Home,
                    onClick = { onScreenSelected(Screen.Home) },
                    testTag = "tab_home"
                )

                // 2. Creator Hub tab
                NavigationTabItem(
                    screen = Screen.CreatorAcademy,
                    icon = Icons.Default.School,
                    label = "Creator Hub",
                    isSelected = currentScreen == Screen.CreatorAcademy,
                    onClick = { onScreenSelected(Screen.CreatorAcademy) },
                    testTag = "tab_creator_hub"
                )

                // 3. AI Labs tab
                NavigationTabItem(
                    screen = Screen.AiLab,
                    icon = Icons.Default.AutoAwesome,
                    label = "AI Labs",
                    isSelected = currentScreen == Screen.AiLab,
                    onClick = { onScreenSelected(Screen.AiLab) },
                    testTag = "tab_ai_labs"
                )

                // 4. Referral tab
                NavigationTabItem(
                    screen = Screen.ReferAndEarn,
                    icon = Icons.Default.CardGiftcard,
                    label = "Referral",
                    isSelected = currentScreen == Screen.ReferAndEarn,
                    onClick = { onScreenSelected(Screen.ReferAndEarn) },
                    testTag = "tab_referral"
                )

                // 5. About tab
                NavigationTabItem(
                    screen = Screen.Profile,
                    icon = Icons.Default.Person,
                    label = "About",
                    isSelected = currentScreen == Screen.Profile,
                    onClick = { onScreenSelected(Screen.Profile) },
                    testTag = "tab_about"
                )
            }
        }
    }
}

@Composable
fun NavigationTabItem(
    screen: Screen,
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .testTag(testTag)
            .height(48.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) Color(0xFF1B1B1B) else Color.Transparent
            )
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(horizontal = if (isSelected) 14.dp else 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF20D9E8) else Color(0xFFB7B7B7),
                modifier = Modifier.size(20.dp)
            )

            // Label appears only when selected
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        color = Color(0xFFFFFFFF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
