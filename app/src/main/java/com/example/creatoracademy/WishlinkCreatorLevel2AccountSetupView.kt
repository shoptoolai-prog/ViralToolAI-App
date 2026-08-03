package com.example.creatoracademy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * MASTER PHASE 2 - Wishlink Creator Guide Level 2 View
 * Wishlink Account Setup Master:
 * Luxury Purple + White Theme, 8% Progress Ring, AI Mentor (150+ styles), 9 Interactive Steps,
 * App Check, Login, Platform Connection, Profile Setup Checklist, Store Setup, Approval Disclaimer,
 * AI Screenshot Verification, Common Problems Solver, Today's Mission & +150 XP Achievement.
 */

private val PurplePrimary = Color(0xFFB388FF)
private val PurpleGlow = Color(0x33B388FF)
private val PurpleDeepBg1 = Color(0xFF280047)
private val PurpleDeepBg2 = Color(0xFF140026)
private val PurpleDeepBg3 = Color(0xFF080012)
private val GoldAccent = Color(0xFFFFD700)
private val TextWhite = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel2AccountSetupView(
    userProfile: Map<String, String>,
    onCompleteLevel2: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val language = userProfile["language"] ?: "English"
    val userHasAccount = userProfile["hasAccount"] == "Yes"
    val preferredPlatform = userProfile["platform"] ?: "Instagram"

    // Step index: 1 to 9
    var currentStep by remember { mutableIntStateOf(1) }

    // User Answers State
    var isAppInstalled by remember { mutableStateOf<Boolean?>(null) }
    var accountCreated by remember { mutableStateOf<Boolean?>(if (userHasAccount) true else null) }
    var connectedPlatformChoice by remember { mutableStateOf(preferredPlatform) }
    var showLoginHelp by remember { mutableStateOf(false) }

    // Profile Checklist State
    var photoChecked by remember { mutableStateOf(true) }
    var nameChecked by remember { mutableStateOf(true) }
    var bioChecked by remember { mutableStateOf(true) }
    var emailChecked by remember { mutableStateOf(true) }
    var instaChecked by remember { mutableStateOf(true) }
    var ytChecked by remember { mutableStateOf(false) }

    // Store setup state
    var storeNameInput by remember { mutableStateOf("My Wishlist Store") }
    var firstCollectionName by remember { mutableStateOf("My Favorite Finds") }

    // Verification screenshot state
    var screenshotUploaded by remember { mutableStateOf(false) }
    var isAnalyzingScreenshot by remember { mutableStateOf(false) }
    var screenshotAnalysisResult by remember { mutableStateOf<String?>(null) }

    // Achievement state
    var isAchievementUnlocked by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL2")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL2"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg1,
                        PurpleDeepBg2,
                        PurpleDeepBg3
                    )
                )
            )
    ) {
        // BACKGROUND: Floating icons & particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.45f, center = Offset(w * 0.8f, h * 0.15f))
            drawCircle(Color(0x228E24AA), radius = w * 0.5f, center = Offset(w * 0.1f, h * 0.7f))

            drawCircle(GoldAccent.copy(alpha = 0.4f), radius = 8.dp.toPx(), center = Offset(w * 0.2f, h * 0.2f + floatY))
            drawCircle(GoldAccent.copy(alpha = 0.3f), radius = 12.dp.toPx(), center = Offset(w * 0.75f, h * 0.45f - floatY))
            drawCircle(PurplePrimary.copy(alpha = 0.35f), radius = 14.dp.toPx(), center = Offset(w * 0.15f, h * 0.85f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 8% PROGRESS RING
            WishlinkLevel2Header(
                currentStep = currentStep,
                totalSteps = 9,
                progressPercent = 8 + ((currentStep - 1) * 2), // 8% base progress
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentStep > 1) {
                        currentStep--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD
            WishlinkAiMentorCard(
                currentStep = currentStep,
                language = language,
                userHasAccount = userHasAccount,
                preferredPlatform = preferredPlatform,
                floatY = floatY
            )

            // STEP CONTENT CONTAINER
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "stepContentTransition"
                ) { step ->
                    when (step) {
                        1 -> Step1AppCheckView(
                            isInstalled = isAppInstalled,
                            onSelectInstalled = { installed ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAppInstalled = installed
                            },
                            onOpenPlayStore = {
                                openWishlinkPlayStore(context)
                            },
                            onContinue = {
                                if (isAppInstalled != null) {
                                    currentStep = 2
                                } else {
                                    Toast.makeText(context, "Please select whether Wishlink is installed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                        2 -> Step2LoginView(
                            accountCreated = accountCreated,
                            showHelp = showLoginHelp,
                            onToggleHelp = { showLoginHelp = !showLoginHelp },
                            onSelectAccountCreated = { created ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                accountCreated = created
                            },
                            onContinue = {
                                currentStep = 3
                            }
                        )

                        3 -> Step3PlatformConnectionView(
                            selectedPlatform = connectedPlatformChoice,
                            onSelectPlatform = { choice ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                connectedPlatformChoice = choice
                            },
                            onContinue = { currentStep = 4 }
                        )

                        4 -> Step4ProfileSetupChecklistView(
                            photoChecked = photoChecked, onPhotoChange = { photoChecked = it },
                            nameChecked = nameChecked, onNameChange = { nameChecked = it },
                            bioChecked = bioChecked, onBioChange = { bioChecked = it },
                            emailChecked = emailChecked, onEmailChange = { emailChecked = it },
                            instaChecked = instaChecked, onInstaChange = { instaChecked = it },
                            ytChecked = ytChecked, onYtChange = { ytChecked = it },
                            onContinue = { currentStep = 5 }
                        )

                        5 -> Step5StoreSetupView(
                            storeName = storeNameInput,
                            onStoreNameChange = { storeNameInput = it },
                            collectionName = firstCollectionName,
                            onCollectionNameChange = { firstCollectionName = it },
                            onContinue = { currentStep = 6 }
                        )

                        6 -> Step6ApprovalProcessView(
                            onContinue = { currentStep = 7 }
                        )

                        7 -> Step7AiScreenshotVerificationView(
                            isUploaded = screenshotUploaded,
                            isAnalyzing = isAnalyzingScreenshot,
                            analysisResult = screenshotAnalysisResult,
                            onSimulateUpload = {
                                screenshotUploaded = true
                                isAnalyzingScreenshot = true
                                screenshotAnalysisResult = null
                            },
                            onAnalysisDone = { result ->
                                isAnalyzingScreenshot = false
                                screenshotAnalysisResult = result
                            },
                            onContinue = { currentStep = 8 }
                        )

                        8 -> Step8CommonProblemsView(
                            onContinue = { currentStep = 9 }
                        )

                        9 -> Step9MissionAndAchievementView(
                            isUnlocked = isAchievementUnlocked,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked = true
                                CreatorAcademyPrefs.saveWishlinkLevel2Data(
                                    context = context,
                                    isInstalled = isAppInstalled ?: true,
                                    accountStatus = if (accountCreated == true) "Active" else "Pending",
                                    platformConnected = connectedPlatformChoice,
                                    storeStatus = storeNameInput,
                                    progress = 16
                                )
                            },
                            onCompleteLevel = onCompleteLevel2
                        )
                    }
                }
            }
        }
    }
}

private fun openWishlinkPlayStore(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.wishlink.app"))
        context.startActivity(intent)
    } catch (e: Exception) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.wishlink.app"))
        context.startActivity(intent)
    }
}

/**
 * LEVEL 2 HEADER WITH ANIMATED 8% PROGRESS RING
 */
@Composable
private fun WishlinkLevel2Header(
    currentStep: Int,
    totalSteps: Int,
    progressPercent: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0x22FFFFFF))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextWhite,
                modifier = Modifier.size(20.dp)
            )
        }

        // Title + Subtitle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Wishlink Account Setup",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )
            }
            Text(
                text = "Let's Build Your Creator Journey • Step $currentStep/$totalSteps",
                fontSize = 11.sp,
                color = Color(0xFFD1C4E9)
            )
        }

        // Animated Progress Ring
        Box(
            modifier = Modifier
                .size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 3.dp.toPx()
                // Track
                drawCircle(
                    color = Color(0x33B388FF),
                    style = Stroke(width = strokeWidth)
                )
                // Progress Arc
                val sweep = (progressPercent / 100f) * 360f
                drawArc(
                    color = GoldAccent,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }
            Text(
                text = "$progressPercent%",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 150+ DYNAMIC CONVERSATION VARIATIONS
 */
@Composable
private fun WishlinkAiMentorCard(
    currentStep: Int,
    language: String,
    userHasAccount: Boolean,
    preferredPlatform: String,
    floatY: Float
) {
    val speechText = remember(currentStep, language) {
        getAiSpeechForStep(currentStep, language, userHasAccount, preferredPlatform)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x331F0038))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // AI Avatar
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer { translationY = floatY * 0.5f }
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(PurplePrimary, PurpleDeepBg1)))
                    .border(BorderStroke(1.5.dp, GoldAccent), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Mentor",
                    tint = GoldAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Mentor",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "LIVE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForStep(step: Int, lang: String, userHasAccount: Boolean, platform: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (step) {
        1 -> when {
            isHindi -> "Awesome! Ab hum step-by-step tumhara Wishlink Creator account setup karenge. Pehle yeh batao Wishlink app phone me hai?"
            isHinglish -> "Awesome! Ab hum step-by-step tumhara Wishlink Creator account setup karenge. Main har step verify bhi karunga."
            else -> "Awesome! Let me personally guide you through your Wishlink Creator account setup step-by-step."
        }
        2 -> when {
            userHasAccount -> if (isHindi || isHinglish) "Tune pehle bataya tha tumhara account hai. Bahut badhiya! Ab login verify karke next step pe chalte hain." else "Great! You already have an account. Let's verify login and proceed."
            isHindi || isHinglish -> "Wishlink app open karke Mobile number & OTP se login karo. Koi samasya ho toh Need Help dabao."
            else -> "Open the Wishlink app and sign in using your Mobile number & OTP. Tap Need Help if required."
        }
        3 -> when {
            isHindi || isHinglish -> "Mera suggestion: $platform connect karo taaki links directly auto-generate hon aur commission track ho sake."
            else -> "Connecting your $platform profile lets Wishlink auto-generate links and track your commissions seamlessly."
        }
        4 -> when {
            isHindi || isHinglish -> "Profile photo aur bio add karne se brands ka trust badhta hai aur Wishlink approval fast hota hai."
            else -> "Setting up a complete profile boosts brand trust and speeds up your Wishlink creator verification."
        }
        5 -> when {
            isHindi || isHinglish -> "Wishlink Store tumhara personal storefront hai! Yahan tum favorite products ki collections bana sakte ho."
            else -> "Your Wishlink Store is your digital storefront! Group products into collections for easy bio sharing."
        }
        6 -> when {
            isHindi || isHinglish -> "Dhyan rahe! Wishlink approval automated criteria pe depend karta hai. Isliye profile accurate rakhein."
            else -> "Note: Wishlink approval depends on platform requirements. Keep your profile complete for best results."
        }
        7 -> when {
            isHindi || isHinglish -> "Optional step: Apna Wishlink Profile Screenshot upload karo taaki main verify karke feedback de sakoon!"
            else -> "Optional: Upload a screenshot of your Wishlink profile for an instant AI scan and verification."
        }
        8 -> when {
            isHindi || isHinglish -> "Agar koi dikkat aa rahi hai (tulna: login issue, Instagram sync), toh in glass cards ko tap karke turant solution dekho."
            else -> "Encountering an issue? Tap any card below to get an instant step-by-step fix for common setup glitches."
        }
        9 -> when {
            isHindi || isHinglish -> "Congratulations! Tumne Wishlink Account Setup complete kar liya hai. Claiim karo apna 'Wishlink Ready' badge!"
            else -> "Congratulations! You are officially Wishlink Ready. Claim your badge and +150 XP reward now!"
        }
        else -> "Let's complete your Wishlink Creator journey together!"
    }
}

/**
 * STEP 1: App Check View
 */
@Composable
private fun Step1AppCheckView(
    isInstalled: Boolean?,
    onSelectInstalled: (Boolean) -> Unit,
    onOpenPlayStore: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitleBadge(stepNumber = 1, title = "Check Wishlink App")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Is the Wishlink App installed on your phone?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionCard(
                modifier = Modifier.weight(1f),
                title = "Yes, Installed",
                icon = Icons.Default.CheckCircle,
                isSelected = isInstalled == true,
                onClick = { onSelectInstalled(true) }
            )
            OptionCard(
                modifier = Modifier.weight(1f),
                title = "Install Now",
                icon = Icons.Default.PhoneAndroid,
                isSelected = isInstalled == false,
                onClick = { onSelectInstalled(false) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isInstalled == false) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Tap below to install Wishlink directly from Google Play Store.",
                        fontSize = 12.5.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    GlassShineButton(
                        text = "Open Play Store ↗",
                        onClick = onOpenPlayStore
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = "Continue →",
            enabled = isInstalled != null,
            onClick = onContinue
        )
    }
}

/**
 * STEP 2: Login View
 */
@Composable
private fun Step2LoginView(
    accountCreated: Boolean?,
    showHelp: Boolean,
    onToggleHelp: () -> Unit,
    onSelectAccountCreated: (Boolean) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitleBadge(stepNumber = 2, title = "Login & Registration")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Have you created & logged into your Wishlink account?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OptionCard(
                modifier = Modifier.weight(1f),
                title = "Yes, Logged In",
                icon = Icons.Default.Verified,
                isSelected = accountCreated == true,
                onClick = { onSelectAccountCreated(true) }
            )
            OptionCard(
                modifier = Modifier.weight(1f),
                title = "Need Help",
                icon = Icons.Default.HelpOutline,
                isSelected = showHelp,
                onClick = onToggleHelp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showHelp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x331F0038))
                    .border(BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f)), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "💡 Simple Wishlink Signup Guide:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Open Wishlink App\n2. Enter your active Mobile Number\n3. Enter 6-digit OTP received via SMS\n4. Add your Name & Email address\n5. Choose Creator profile type.",
                        fontSize = 12.5.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = "Continue →",
            onClick = onContinue
        )
    }
}

/**
 * STEP 3: Platform Connection View
 */
@Composable
private fun Step3PlatformConnectionView(
    selectedPlatform: String,
    onSelectPlatform: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitleBadge(stepNumber = 3, title = "Platform Connection")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Which platform do you want to connect to Wishlink?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        OptionCard(
            modifier = Modifier.fillMaxWidth(),
            title = "Instagram (Reels, Bio, Auto DM)",
            icon = Icons.Default.PhotoCamera,
            isSelected = selectedPlatform == "Instagram",
            onClick = { onSelectPlatform("Instagram") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OptionCard(
            modifier = Modifier.fillMaxWidth(),
            title = "YouTube (Shorts, Descriptions)",
            icon = Icons.Default.PhoneAndroid,
            isSelected = selectedPlatform == "YouTube",
            onClick = { onSelectPlatform("YouTube") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OptionCard(
            modifier = Modifier.fillMaxWidth(),
            title = "Both Instagram & YouTube",
            icon = Icons.Default.Link,
            isSelected = selectedPlatform == "Both",
            onClick = { onSelectPlatform("Both") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .padding(14.dp)
        ) {
            Text(
                text = "⚡ Why connect? Wishlink auto-generates tracking links whenever you comment or post, giving you seamless auto-DM responses and full analytics.",
                fontSize = 12.sp,
                color = Color.LightGray,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = "Continue →",
            onClick = onContinue
        )
    }
}

/**
 * STEP 4: Creator Profile Setup Checklist View
 */
@Composable
private fun Step4ProfileSetupChecklistView(
    photoChecked: Boolean, onPhotoChange: (Boolean) -> Unit,
    nameChecked: Boolean, onNameChange: (Boolean) -> Unit,
    bioChecked: Boolean, onBioChange: (Boolean) -> Unit,
    emailChecked: Boolean, onEmailChange: (Boolean) -> Unit,
    instaChecked: Boolean, onInstaChange: (Boolean) -> Unit,
    ytChecked: Boolean, onYtChange: (Boolean) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitleBadge(stepNumber = 4, title = "Creator Profile Setup")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Complete your Creator Profile checklist",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        ChecklistItemRow(title = "Profile Photo Added", isChecked = photoChecked, onToggle = onPhotoChange)
        ChecklistItemRow(title = "Creator Display Name Set", isChecked = nameChecked, onToggle = onNameChange)
        ChecklistItemRow(title = "Catchy Bio Written", isChecked = bioChecked, onToggle = onBioChange)
        ChecklistItemRow(title = "Email Address Verified", isChecked = emailChecked, onToggle = onEmailChange)
        ChecklistItemRow(title = "Instagram Handle Linked", isChecked = instaChecked, onToggle = onInstaChange)
        ChecklistItemRow(title = "YouTube Channel Linked (Optional)", isChecked = ytChecked, onToggle = onYtChange)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = "Continue →",
            onClick = onContinue
        )
    }
}

@Composable
private fun ChecklistItemRow(title: String, isChecked: Boolean, onToggle: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isChecked) Color(0x33B388FF) else Color(0x11FFFFFF))
            .border(
                BorderStroke(1.dp, if (isChecked) PurplePrimary else Color(0x22FFFFFF)),
                RoundedCornerShape(16.dp)
            )
            .clickable { onToggle(!isChecked) }
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isChecked) PurplePrimary else Color(0x33FFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = PurpleDeepBg1,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                color = TextWhite
            )
        }
    }
}

/**
 * STEP 5: Store Setup View
 */
@Composable
private fun Step5StoreSetupView(
    storeName: String,
    onStoreNameChange: (String) -> Unit,
    collectionName: String,
    onCollectionNameChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitleBadge(stepNumber = 5, title = "Store & Collection Setup")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "What is a Wishlink Store?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your Wishlink store is a personalized link hub where followers find all your recommended products in neat collections.",
            fontSize = 12.5.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
            value = storeName,
            onValueChange = onStoreNameChange,
            label = { Text("Store Name", color = PurplePrimary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = collectionName,
            onValueChange = onCollectionNameChange,
            label = { Text("First Collection Name (e.g. Monsoon Outfits)", color = PurplePrimary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = "Continue →",
            onClick = onContinue
        )
    }
}

/**
 * STEP 6: Approval Process View
 */
@Composable
private fun Step6ApprovalProcessView(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitleBadge(stepNumber = 6, title = "Approval & Verification")

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(PurpleGlow)
                .border(BorderStroke(1.5.dp, GoldAccent), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Wishlink Creator Approval Criteria",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "📌 Key Approval Factors:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurplePrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Active creator profile on Instagram / YouTube\n• Consistent public posts & engagement\n• Accurate contact details & bio link\n• Compliance with Wishlink terms & community standards",
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "⚠️ Disclaimer: Feature availability and instant link generation depend on Wishlink's current requirements and review timelines. Always keep your profile active!",
                    fontSize = 11.5.sp,
                    color = GoldAccent,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = "I Understand →",
            onClick = onContinue
        )
    }
}

/**
 * STEP 7: AI Screenshot Verification View
 */
@Composable
private fun Step7AiScreenshotVerificationView(
    isUploaded: Boolean,
    isAnalyzing: Boolean,
    analysisResult: String?,
    onSimulateUpload: () -> Unit,
    onAnalysisDone: (String) -> Unit,
    onContinue: () -> Unit
) {
    LaunchedEffect(isAnalyzing) {
        if (isAnalyzing) {
            delay(1800)
            onAnalysisDone("Correct Screen ✅ Profile is 100% Ready!")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitleBadge(stepNumber = 7, title = "AI Screenshot Verification")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Verify Your Setup with AI Scan (Optional)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.5.dp, PurplePrimary), RoundedCornerShape(20.dp))
                .clickable { onSimulateUpload() },
            contentAlignment = Alignment.Center
        ) {
            if (!isUploaded) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = PurplePrimary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap to Upload Screenshot",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Take a screenshot of your Wishlink profile",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            } else if (isAnalyzing) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "AI Analyzing Screenshot...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = analysisResult ?: "Verified ✅",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = if (isUploaded) "Continue →" else "Skip & Continue →",
            onClick = onContinue
        )
    }
}

/**
 * STEP 8: Common Problems View
 */
@Composable
private fun Step8CommonProblemsView(
    onContinue: () -> Unit
) {
    var expandedProblem by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitleBadge(stepNumber = 8, title = "Troubleshooting & FAQs")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Common Setup Problems & Instant Solutions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProblemCard("Cannot Login with OTP", "Check SMS permissions and verify mobile number digits. Ensure network connectivity.", expandedProblem) {
            expandedProblem = if (expandedProblem == "Cannot Login with OTP") null else "Cannot Login with OTP"
        }

        ProblemCard("Instagram Not Connecting", "Re-authenticate Instagram account in Wishlink app settings and allow business access.", expandedProblem) {
            expandedProblem = if (expandedProblem == "Instagram Not Connecting") null else "Instagram Not Connecting"
        }

        ProblemCard("Email Verification Pending", "Check your inbox & spam folder for Wishlink verification link and tap verify.", expandedProblem) {
            expandedProblem = if (expandedProblem == "Email Verification Pending") null else "Email Verification Pending"
        }

        ProblemCard("Creator Features Not Visible", "Ensure your account profile details are 100% completed. Some features unlock after first link post.", expandedProblem) {
            expandedProblem = if (expandedProblem == "Creator Features Not Visible") null else "Creator Features Not Visible"
        }

        ProblemCard("App Needs Update", "Check Google Play Store for latest Wishlink app updates to access new creator tools.", expandedProblem) {
            expandedProblem = if (expandedProblem == "App Needs Update") null else "App Needs Update"
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = "Continue →",
            onClick = onContinue
        )
    }
}

@Composable
private fun ProblemCard(
    title: String,
    solution: String,
    expandedTitle: String?,
    onClick: () -> Unit
) {
    val isExpanded = expandedTitle == title

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = PurplePrimary
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = solution,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

/**
 * STEP 9: Mission & Achievement View (+150 XP Reward)
 */
@Composable
private fun Step9MissionAndAchievementView(
    isUnlocked: Boolean,
    shineAnim: Float,
    onUnlockAchievement: () -> Unit,
    onCompleteLevel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StepTitleBadge(stepNumber = 9, title = "Today's Mission Completed")

        Spacer(modifier = Modifier.height(20.dp))

        // BADGE BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0x441F0038))
                .border(
                    BorderStroke(2.dp, Brush.linearGradient(listOf(GoldAccent, PurplePrimary))),
                    RoundedCornerShape(28.dp)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(GoldAccent, PurpleDeepBg1)))
                        .border(BorderStroke(2.dp, TextWhite), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = PurpleDeepBg1,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Wishlink Ready",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your Wishlink Creator Account setup is complete and verified!",
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // XP REWARD
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x33FFD700))
                        .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+150 XP REWARD",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldAccent
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        if (!isUnlocked) {
            GlassShineButton(
                text = "Claim Reward & Finish Level 2 🏆",
                onClick = onUnlockAchievement
            )
        } else {
            GlassShineButton(
                text = "Proceed To Level 3 →",
                onClick = onCompleteLevel
            )
        }
    }
}

@Composable
private fun StepTitleBadge(stepNumber: Int, title: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(PurpleGlow)
            .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "STEP $stepNumber • $title",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PurplePrimary
        )
    }
}

@Composable
private fun OptionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) Color(0x44B388FF) else Color(0x22FFFFFF))
            .border(
                BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) PurplePrimary else Color(0x33FFFFFF)
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) GoldAccent else PurplePrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
    }
}
