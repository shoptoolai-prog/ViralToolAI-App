package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.example.ui.screens.ExperienceSelectorScreen
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.ui.screens.OnboardingPrefs
import com.example.ui.screens.OnboardingScreen
import androidx.compose.ui.platform.LocalContext

import com.example.core.rememberIsOnlineState
import com.example.ui.components.OfflineBanner
import androidx.compose.foundation.layout.statusBarsPadding

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
    ExperienceSelector,
    Home,
    CreatorAcademySetup,
    CreatorAcademy,
    History,
    Profile,
    Analysis,
    Result
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainAppLayout(sharedUrl: String? = null) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        com.example.core.LanguageEngine.init(context)
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AmoledBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (currentScreen == Screen.Splash) 0.dp else 4.dp)
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
            // Screen switching with smooth, premium crossfade animations (under 400ms)
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    val duration = 350
                    (fadeIn(animationSpec = tween(duration)) +
                            scaleIn(initialScale = 0.94f, animationSpec = tween(duration)) +
                            slideInHorizontally(initialOffsetX = { it / 10 }, animationSpec = tween(duration))) with
                    (fadeOut(animationSpec = tween(duration)) +
                            scaleOut(targetScale = 1.04f, animationSpec = tween(duration)) +
                            slideOutHorizontally(targetOffsetX = { -it / 10 }, animationSpec = tween(duration)))
                },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.Splash -> {
                        SplashScreen(onSplashComplete = {
                            if (!OnboardingPrefs.isOnboardingCompleted(context)) {
                                currentScreen = Screen.Onboarding
                            } else {
                                if (CreatorAcademyPrefs.isRememberExperience(context)) {
                                    val choice = CreatorAcademyPrefs.getExperienceChoice(context)
                                    if (choice == "CREATOR_ACADEMY") {
                                        currentScreen = if (CreatorAcademyPrefs.isSetupCompleted(context)) {
                                            Screen.CreatorAcademy
                                        } else {
                                            Screen.CreatorAcademySetup
                                        }
                                    } else {
                                        currentScreen = Screen.Home
                                    }
                                } else {
                                    currentScreen = Screen.ExperienceSelector
                                }
                            }
                        })
                    }
                    Screen.Onboarding -> {
                        OnboardingScreen(onOnboardingFinished = {
                            currentScreen = Screen.ExperienceSelector
                        })
                    }
                    Screen.ExperienceSelector -> {
                        ExperienceSelectorScreen(
                            onExperienceSelected = { choice ->
                                if (choice == "CREATOR_ACADEMY") {
                                    currentScreen = if (CreatorAcademyPrefs.isSetupCompleted(context)) {
                                        Screen.CreatorAcademy
                                    } else {
                                        Screen.CreatorAcademySetup
                                    }
                                } else {
                                    currentScreen = Screen.Home
                                }
                            }
                        )
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
                                currentScreen = Screen.ExperienceSelector
                            },
                            onResetSetup = {
                                CreatorAcademyPrefs.resetSetup(context)
                                currentScreen = Screen.CreatorAcademySetup
                            }
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
                                currentScreen = if (CreatorAcademyPrefs.isSetupCompleted(context)) {
                                    Screen.CreatorAcademy
                                } else {
                                    Screen.CreatorAcademySetup
                                }
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
                            onSwitchExperience = { currentScreen = Screen.ExperienceSelector }
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

            // Bottom Navigation floating glass capsule (only display after Splash, Onboarding, ExperienceSelector & Setup screens, not in Analysis or Result)
            if (currentScreen != Screen.Splash && currentScreen != Screen.Onboarding && currentScreen != Screen.ExperienceSelector && currentScreen != Screen.CreatorAcademySetup && currentScreen != Screen.Result && currentScreen != Screen.Analysis) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(100f)
                        .navigationBarsPadding() // EXTREMELY CRITICAL: Prevents system gesture bar overlap
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    FloatingBottomNavigation(
                        currentScreen = currentScreen,
                        onScreenSelected = { selected ->
                            if (selected == Screen.CreatorAcademy) {
                                currentScreen = if (CreatorAcademyPrefs.isSetupCompleted(context)) {
                                    Screen.CreatorAcademy
                                } else {
                                    Screen.CreatorAcademySetup
                                }
                            } else {
                                currentScreen = selected
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingBottomNavigation(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    // Beautiful floating Glassmorphism Capsule with Luxury Emerald & Purple Border
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = EmeraldPrimary)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xF2090910)) // Deep Dark Graphite capsule background
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            EmeraldPrimary.copy(alpha = 0.6f),
                            ElectricPurple.copy(alpha = 0.4f),
                            Color(0x33FFFFFF)
                        )
                    )
                ),
                RoundedCornerShape(32.dp)
            )
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
            isSelected = currentScreen == Screen.CreatorAcademy || currentScreen == Screen.CreatorAcademySetup,
            onClick = { onScreenSelected(Screen.CreatorAcademy) },
            testTag = "tab_academy"
        )
        
        // History tab
        NavigationTabItem(
            screen = Screen.History,
            icon = Icons.Default.History,
            label = com.example.core.LanguageEngine.get("tab_history"),
            isSelected = currentScreen == Screen.History,
            onClick = { onScreenSelected(Screen.History) },
            testTag = "tab_history"
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
    val contentColor = if (isSelected) EmeraldGlow else TextWhite.copy(alpha = 0.4f)
    
    val backgroundBrush = if (isSelected) {
        Brush.horizontalGradient(
            colors = listOf(EmeraldPrimary.copy(alpha = 0.25f), EmeraldGlow.copy(alpha = 0.15f))
        )
    } else {
        null
    }

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
            .padding(vertical = 10.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
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
                Row {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        color = contentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
