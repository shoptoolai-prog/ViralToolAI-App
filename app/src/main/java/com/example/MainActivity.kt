package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

import com.example.data.ShareIntentHandler
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import com.example.ui.screens.BrandAmbassadorPosterScreen
import com.example.ui.screens.BrandAmbassadorPrefs
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
    BrandAmbassadorPoster,
    Onboarding,
    Home,
    CreatorAcademySetup,
    CreatorAcademy,
    VideoEditing,
    History,
    Profile,
    Analysis,
    Result
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MainAppLayout(sharedUrl: String? = null) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        com.example.core.LanguageEngine.init(context)
        try {
            val imageLoader = context.imageLoader
            val request = ImageRequest.Builder(context)
                .data("https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/1785321241752.png")
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
            imageLoader.enqueue(request)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error preloading poster image", e)
        }
    }

    var currentScreen by remember { mutableStateOf(if (!sharedUrl.isNullOrBlank()) Screen.Home else Screen.Splash) }
    var analyzedLink by remember { mutableStateOf("") }

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
            currentScreen != Screen.BrandAmbassadorPoster && 
            currentScreen != Screen.Onboarding && 
            currentScreen != Screen.CreatorAcademySetup && 
            currentScreen != Screen.Result && 
            currentScreen != Screen.Analysis

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
                                    currentScreen = Screen.BrandAmbassadorPoster
                                }
                            } catch (e: Exception) {
                                currentScreen = Screen.Home
                            }
                        })
                    }
                    Screen.BrandAmbassadorPoster -> {
                        BrandAmbassadorPosterScreen(
                            onDismiss = {
                                try {
                                    if (CreatorAcademyPrefs.isRememberExperience(context) &&
                                        CreatorAcademyPrefs.getExperienceChoice(context) == "CREATOR_ACADEMY") {
                                        currentScreen = Screen.CreatorAcademy
                                    } else {
                                        currentScreen = Screen.Home
                                    }
                                } catch (e: Exception) {
                                    currentScreen = Screen.Home
                                }
                            },
                            onExploreClicked = {
                                currentScreen = Screen.Home
                            }
                        )
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
                            onNavigateToHome = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Home -> {
                        HomeScreen(
                            historyList = historyList,
                            onAddHistoryItem = onAddHistoryItem,
                            onNavigateToHistory = { currentScreen = Screen.History },
                            onNavigateToAnalysis = { link ->
                                analyzedLink = link
                                currentScreen = Screen.Analysis
                            },
                            onNavigateToCreatorAcademy = {
                                currentScreen = Screen.CreatorAcademy
                            },
                            initialSharedUrl = sharedUrl
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
                        com.example.ui.screens.AnalysisScreen(
                            analyzedLink = analyzedLink,
                            onAddHistoryItem = onAddHistoryItem,
                            onBackClick = { currentScreen = Screen.Home },
                            onNavigateToResult = { currentScreen = Screen.Result }
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
    // Pinned Bottom Navigation Bar attached directly to the bottom edge of device
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(100f),
        color = Color(0xFF0B0B12), // Deep dark background
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0B0B12))
                .navigationBarsPadding()
        ) {
            // Top hairline border with subtle luxury gradient line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                EmeraldPrimary.copy(alpha = 0.5f),
                                ElectricPurple.copy(alpha = 0.5f),
                                EmeraldGlow.copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF0B0B12))
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home tab
                NavigationTabItem(
                    screen = Screen.Home,
                    icon = Icons.Default.Home,
                    label = com.example.core.LanguageEngine.get("tab_home"),
                    isSelected = currentScreen == Screen.Home,
                    onClick = { onScreenSelected(Screen.Home) },
                    testTag = "tab_home"
                )

                // Creator Academy tab
                NavigationTabItem(
                    screen = Screen.CreatorAcademy,
                    icon = Icons.Default.School,
                    label = com.example.core.LanguageEngine.get("tab_academy"),
                    isSelected = currentScreen == Screen.CreatorAcademy,
                    onClick = { onScreenSelected(Screen.CreatorAcademy) },
                    testTag = "tab_academy"
                )
                
                // Video Editing tab
                NavigationTabItem(
                    screen = Screen.VideoEditing,
                    icon = Icons.Default.Videocam,
                    label = "Video Editing",
                    isSelected = currentScreen == Screen.VideoEditing,
                    onClick = { onScreenSelected(Screen.VideoEditing) },
                    testTag = "tab_video_editing"
                )
                
                // Profile tab
                NavigationTabItem(
                    screen = Screen.Profile,
                    icon = Icons.Default.Person,
                    label = com.example.core.LanguageEngine.get("tab_profile"),
                    isSelected = currentScreen == Screen.Profile,
                    onClick = { onScreenSelected(Screen.Profile) },
                    testTag = "tab_profile"
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
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val responsiveMetrics = LocalResponsiveMetrics.current
    val contentColor = if (isSelected) EmeraldGlow else TextWhite.copy(alpha = 0.4f)
    
    val backgroundBrush = if (isSelected) {
        Brush.horizontalGradient(
            colors = listOf(EmeraldPrimary.copy(alpha = 0.25f), EmeraldGlow.copy(alpha = 0.15f))
        )
    } else {
        null
    }

    val horizontalPad = if (responsiveMetrics.isSmallPhone) 8.dp else 12.dp

    Box(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(20.dp))
            .let {
                if (backgroundBrush != null) {
                    it.background(backgroundBrush)
                } else {
                    it
                }
            }
            .clickable {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(vertical = 8.dp, horizontal = horizontalPad),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(EmeraldGlow)
                        .shadow(4.dp, CircleShape, spotColor = EmeraldGlow)
                )
            }

            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            
            // Animated indicator text expansion
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(4.dp))
                    AutoResizedText(
                        text = label,
                        color = contentColor,
                        fontSize = if (responsiveMetrics.isSmallPhone) 10.sp else 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        minFontSize = 8.sp
                    )
                }
            }
        }
    }
}
