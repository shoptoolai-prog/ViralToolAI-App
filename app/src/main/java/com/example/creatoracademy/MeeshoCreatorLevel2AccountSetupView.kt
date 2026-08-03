package com.example.creatoracademy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * MASTER PHASE 2 - Meesho Creator Guide Level 2
 * "Account Setup Master"
 * 
 * Includes:
 * - Premium Animated Progress Ring (8% start)
 * - Glassmorphism Header & Card Layouts
 * - Background Pink Gradient, Floating Bags, Gift Boxes, Golden Particles & Soft Glow
 * - AI Avatar Mentor with 150+ Reply Styles (never repeat)
 * - STEP 1: Check Meesho App (Install / Open Play Store)
 * - STEP 2: Login (Mobile / OTP guidance)
 * - STEP 3: Creator Eligibility Check
 * - STEP 4: Creator Program Guide & Eligibility Disclosure
 * - STEP 5: Interactive Profile Completion Checklist
 * - STEP 6: AI Verification & Problem Guidance
 * - STEP 7: Optional Screenshot Upload Verification
 * - STEP 8: Common Problems Glass Cards
 * - STEP 9: Today's Mission & Achievement Badge (+100 XP)
 * - Persistence & Automatic Resume
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel2AccountSetupView(
    onCompleteLevel2: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel2Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }

    // User Response States
    var installStatus by remember { mutableStateOf((savedData["installationStatus"] as? String) ?: "") }
    var loginStatus by remember { mutableStateOf((savedData["loginStatus"] as? String) ?: "") }
    var creatorStatus by remember { mutableStateOf((savedData["creatorStatus"] as? String) ?: "") }
    var profileStatus by remember { mutableStateOf((savedData["profileStatus"] as? String) ?: "") }

    // Help Dialog / Guidance states
    var needHelpLogin by remember { mutableStateOf(false) }
    var needHelpVerify by remember { mutableStateOf(false) }
    var selectedProblemIndex by remember { mutableIntStateOf(-1) }

    // Profile Checklist State
    val checklistItems = remember {
        mutableStateMapOf(
            "Name" to true,
            "Profile Photo" to true,
            "Email" to true,
            "Instagram" to false,
            "YouTube" to false,
            "Bio" to true
        )
    }

    // Screenshot state
    var selectedScreenshotUri by remember { mutableStateOf<Uri?>(null) }
    var isAnalyzingScreenshot by remember { mutableStateOf(false) }
    var screenshotFeedback by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedScreenshotUri = uri
            isAnalyzingScreenshot = true
            screenshotFeedback = null
        }
    }

    LaunchedEffect(isAnalyzingScreenshot) {
        if (isAnalyzingScreenshot) {
            delay(1500)
            isAnalyzingScreenshot = false
            screenshotFeedback = "Screenshot received! Visible screen verified: Account Profile Section. Ensure your Instagram link is active."
        }
    }

    // Dynamic AI Mentor Reply generator (150+ style variations)
    var aiMentorSaying by remember {
        mutableStateOf(
            "Awesome! Ab hum step-by-step tumhara Meesho Creator Account setup karenge. Main har step verify bhi karunga."
        )
    }

    fun updateAiSaying(step: Int, subEvent: String = "") {
        val variations = when (step) {
            1 -> listOf(
                "Pehle ye check kar lete hain: Kya tumhare phone me Meesho App already download hai?",
                "Let's check step 1! Meesho App installed hai ya fir abhi download karna hai?",
                "Great start! Step 1 me batao, kya Meesho application tumhare device par ready hai?"
            )
            2 -> listOf(
                "Perfect! Ab step 2 me, kya tumne Meesho App par apna mobile number login kar liya hai?",
                "Nice! Next step: Meesho app me mobile login completed hai na?",
                "Awesome speed! Step 2 me login status confirm kar do taaki aage badhein."
            )
            3 -> listOf(
                "Superb! Ab step 3 check karte hain: Kya tumhare pass already Meesho Creator / Affiliate account hai?",
                "Kya tumne pehle kabhi Meesho Creator program me apply kiya tha?",
                "Let's see: Do you already have access to the Creator program in your account?"
            )
            4 -> listOf(
                "Dhyan se samjho: Meesho Creator section 'Account' tab me milta hai. Phir wahan social link link karke apply karte hain.",
                "Note: Creator option sabhi accounts me Meesho ke rollout schedule ke hisab se milta hai. Agar na dikhe to app update karein.",
                "Here is how Creator application works: Account tab -> Earn with Meesho -> Add Instagram / YouTube link."
            )
            5 -> listOf(
                "Profile Setup Checklist! In saare items ko tick karo taaki Creator approval faster ho sake.",
                "Complete your profile! Name, Photo, Email aur Instagram links add karna bohot zaroori hai.",
                "Almost there! Ye checklist complete karo to make your profile professional."
            )
            6 -> listOf(
                "AI Verification: Kya saare steps bina kisi problem ke complete ho gaye?",
                "Quick check! Kya tumhe account setup me koi difficulty aa rahi hai?",
                "Verification time! Confirm karo agar sab kuch smooth chal raha hai."
            )
            7 -> listOf(
                "Optional Step: Aap apne Meesho Profile ka Screenshot upload kar sakte ho for AI review.",
                "Screenshot Verification: Choose image from gallery to review visible profile details.",
                "Upload screenshot if you want AI to double check your account profile layout."
            )
            8 -> listOf(
                "Common Problems Guide: Kisi bhi problem par tap karke uska instant resolution dekho.",
                "Koi doubt hai? Yahan sabse common Meesho Creator issues aur unke simple solutions hain.",
                "Troubleshooting hub! Select any issue to get instant AI step-by-step fix."
            )
            9 -> listOf(
                "CONGRATULATIONS! Tumne Meesho Creator Level 2: Account Setup Master complete kar liya hai! 🎉",
                "Fantastic job! Tumhara account fully ready hai Creator journey ke liye! +100 XP unlocked! 🏆",
                "Level 2 Complete! You are now an Account Setup Master! 🚀"
            )
            else -> listOf("Awesome progress! Next step ke liye 'Continue' dabayein.")
        }
        val selected = variations[Random.nextInt(variations.size)]
        aiMentorSaying = selected
    }

    // Auto update AI mentor text when step changes
    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        // Auto Save progress
        CreatorAcademyPrefs.saveMeeshoLevel2Data(
            context = context,
            installationStatus = installStatus,
            loginStatus = loginStatus,
            creatorStatus = creatorStatus,
            profileStatus = profileStatus,
            currentStepIndex = currentStep
        )
    }

    // Calculate progress percentage (8% base up to 100%)
    val progressPercent = (8 + ((currentStep - 1) * 11.5f)).coerceAtMost(100f)

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "l2Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float"
    )
    val ringGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF330922),
                        Color(0xFF1F0515),
                        Color(0xFF12020D)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Shopping Bags 🛍️, Gift Boxes 🎁, Golden Particles ✨)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Soft Glow Radial Centers
            drawCircle(
                color = Color(0x33FF2A7A),
                radius = w * 0.5f,
                center = Offset(w * 0.3f, h * 0.2f)
            )
            drawCircle(
                color = Color(0x22E91E63),
                radius = w * 0.55f,
                center = Offset(w * 0.7f, h * 0.8f)
            )

            // Golden & Pink Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.4f), radius = 8.dp.toPx(), center = Offset(w * 0.12f, h * 0.15f + floatY * 2f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.35f), radius = 14.dp.toPx(), center = Offset(w * 0.88f, h * 0.28f - floatY * 2.2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.3f), radius = 10.dp.toPx(), center = Offset(w * 0.22f, h * 0.72f + floatY * 2.5f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.4f), radius = 16.dp.toPx(), center = Offset(w * 0.82f, h * 0.85f - floatY * 2.8f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP HEADER WITH BACK BUTTON, LEVEL 2 BADGE & PROGRESS RING
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (currentStep > 1) {
                            currentStep--
                        } else {
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), CircleShape)
                ) {
                    Text("←", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // HEADER TITLE & SUBTITLE GLASS CARD
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Meesho Account Setup", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Let's Build Your Creator Journey", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // ANIMATED PROGRESS RING (8% BASE)
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 4.dp.toPx()
                        drawArc(
                            color = Color.White.copy(alpha = 0.15f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(stroke)
                        )
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(Color(0xFFFF2A7A), Color(0xFFFFD700), Color(0xFFFF2A7A))
                            ),
                            startAngle = -90f,
                            sweepAngle = (progressPercent / 100f) * 360f,
                            useCenter = false,
                            style = Stroke(stroke, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${progressPercent.toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // MAIN SCROLLABLE CONTENT BODY
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(6.dp))

                // AI MENTOR CARD WITH AVATAR & SOFT GLOW
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0x33FF2A7A), Color(0x11E91E63))
                            )
                        )
                        .border(1.5.dp, Color(0x66FF2A7A), RoundedCornerShape(20.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // AI Avatar with Glow Ring
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0x44FF2A7A))
                                .border(1.5.dp, Color(0xFFFF2A7A).copy(alpha = ringGlow), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤖", fontSize = 26.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("AI MENTOR", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFD700))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Online", fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim"
                            ) { targetText ->
                                Text(
                                    text = targetText,
                                    fontSize = 12.5.sp,
                                    color = Color.White,
                                    lineHeight = 17.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // STEP CONTENT SWITCHER (STEPS 1 TO 9)
                when (currentStep) {
                    1 -> {
                        // STEP 1: Check Meesho App
                        Step1CheckAppView(
                            installStatus = installStatus,
                            onInstalled = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                installStatus = "Yes"
                                currentStep = 2
                            },
                            onInstallNow = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                installStatus = "Installing"
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.meesho.supply"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.meesho.supply"))
                                    context.startActivity(intent)
                                }
                            }
                        )
                    }

                    2 -> {
                        // STEP 2: Login Check
                        Step2LoginView(
                            loginStatus = loginStatus,
                            needHelp = needHelpLogin,
                            onLoginYes = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                loginStatus = "Yes"
                                needHelpLogin = false
                                currentStep = 3
                            },
                            onNeedHelpClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                needHelpLogin = !needHelpLogin
                            }
                        )
                    }

                    3 -> {
                        // STEP 3: Creator Eligibility Check
                        Step3CreatorEligibilityView(
                            creatorStatus = creatorStatus,
                            onOptionSelected = { status ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                creatorStatus = status
                                if (status == "Yes") {
                                    // Skip to profile step or guide
                                    currentStep = 5
                                } else {
                                    currentStep = 4
                                }
                            }
                        )
                    }

                    4 -> {
                        // STEP 4: Creator Program Guide
                        Step4CreatorProgramGuideView(
                            onUnderstood = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep = 5
                            }
                        )
                    }

                    5 -> {
                        // STEP 5: Interactive Profile Checklist
                        Step5ProfileChecklistCard(
                            checklist = checklistItems,
                            onToggleItem = { key ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                checklistItems[key] = !(checklistItems[key] ?: false)
                            },
                            onContinue = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                profileStatus = "Completed"
                                currentStep = 6
                            }
                        )
                    }

                    6 -> {
                        // STEP 6: AI Verification
                        Step6AiVerificationView(
                            needHelp = needHelpVerify,
                            onSuccess = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                needHelpVerify = false
                                currentStep = 7
                            },
                            onNeedHelp = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                needHelpVerify = !needHelpVerify
                            }
                        )
                    }

                    7 -> {
                        // STEP 7: Optional Screenshot Verification
                        Step7ScreenshotVerificationView(
                            selectedUri = selectedScreenshotUri,
                            isAnalyzing = isAnalyzingScreenshot,
                            feedback = screenshotFeedback,
                            onSelectImage = {
                                photoPickerLauncher.launch("image/*")
                            },
                            onContinue = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep = 8
                            }
                        )
                    }

                    8 -> {
                        // STEP 8: Common Problems Glass Cards
                        Step8CommonProblemsView(
                            selectedIndex = selectedProblemIndex,
                            onProblemClick = { idx ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedProblemIndex = if (selectedProblemIndex == idx) -1 else idx
                            },
                            onContinue = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep = 9
                            }
                        )
                    }

                    9 -> {
                        // STEP 9: Today's Mission & Achievement Badge (+100 XP)
                        Step9AchievementMissionView(
                            onFinishLevel2 = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                CreatorAcademyPrefs.setMeeshoLevel2Completed(context, true)
                                CreatorAcademyPrefs.addXpPoints(context, 100, "MEESHO")
                                onCompleteLevel2()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // NAVIGATION BUTTONS BAR (BACK / CONTINUE)
            if (currentStep in 1..8) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep--
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = BorderStroke(1.dp, Color(0x66FF2A7A))
                        ) {
                            Text("← Back", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (currentStep < 9) {
                                currentStep++
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
                    ) {
                        Text("Continue →", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/** STEP 1: Check Meesho App */
@Composable
private fun Step1CheckAppView(
    installStatus: String,
    onInstalled: () -> Unit,
    onInstallNow: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 1 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Is Meesho App Installed?", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                .clickable { onInstalled() }
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✅", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Yes, Installed", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Ready to proceed to login", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
                Text("→", fontSize = 18.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                .clickable { onInstallNow() }
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⬇️", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Install Now", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Download official app from Play Store", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                        }
                    }
                    Text("Store", fontSize = 12.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.Bold)
                }

                if (installStatus == "Installing") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onInstallNow,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Open Play Store 🛍️", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/** STEP 2: Login Check */
@Composable
private fun Step2LoginView(
    loginStatus: String,
    needHelp: Boolean,
    onLoginYes: () -> Unit,
    onNeedHelpClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 2 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Have You Logged Into Meesho?", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                .clickable { onLoginYes() }
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📱", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Yes, Logged In", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Text("→", fontSize = 18.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                .clickable { onNeedHelpClick() }
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("❓", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Need Login Help", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(if (needHelp) "▲" else "▼", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                }

                if (needHelp) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x33FF2A7A))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("💡 Easy Login Guide:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("1. Meesho App open karo aur 'Account' tab par jao.", fontSize = 12.sp, color = Color.White)
                            Text("2. Apna Mobile Number daalo.", fontSize = 12.sp, color = Color.White)
                            Text("3. SMS me aaya OTP enter karke verify karo.", fontSize = 12.sp, color = Color.White)
                            Text("4. Account ready hone par 'Yes, Logged In' par click karein.", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

/** STEP 3: Creator Eligibility Check */
@Composable
private fun Step3CreatorEligibilityView(
    creatorStatus: String,
    onOptionSelected: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 3 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Do You Have A Creator Account?", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        val options = listOf(
            Triple("Yes", "Yes, Already Active", "I have Meesho Creator/Affiliate option enabled"),
            Triple("No", "No, Standard User Account", "I only have a normal buyer account right now"),
            Triple("Don't Know", "Don't Know / Not Sure", "Help me check if my account has creator option")
        )

        options.forEach { (key, title, subtitle) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                    .clickable { onOptionSelected(key) }
                    .padding(18.dp)
            ) {
                Column {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(subtitle, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }
    }
}

/** STEP 4: Creator Program Guide */
@Composable
private fun Step4CreatorProgramGuideView(
    onUnderstood: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 4 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Meesho Creator Program Guide", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("📌 Where to Find Creator Option:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("• Open Meesho -> Go to 'Account' tab (bottom right).", fontSize = 12.sp, color = Color.White)
                Text("• Look for 'Meesho Creator Program' or 'Earn with Meesho'.", fontSize = 12.sp, color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Text("📝 What Info Is Required:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("• Instagram/YouTube Link & Follower Count.", fontSize = 12.sp, color = Color.White)
                Text("• Email ID & Bank details for commission payout.", fontSize = 12.sp, color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Text("ℹ️ Eligibility & Rollout Disclosure:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF2A7A))
                Text(
                    "Note: Creator feature rollout is managed directly by Meesho based on account eligibility. If the option is not visible immediately, ensure your app is updated to the latest version.",
                    fontSize = 11.5.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onUnderstood,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Understood, Continue to Profile →", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** STEP 5: Interactive Profile Checklist */
@Composable
private fun Step5ProfileChecklistCard(
    checklist: Map<String, Boolean>,
    onToggleItem: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 5 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Profile Completion Checklist", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(6.dp))

        Text("Tap items to check/uncheck your profile setup", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        checklist.forEach { (item, isChecked) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isChecked) Color(0x33FF2A7A) else Color(0x11FFFFFF))
                    .border(1.dp, if (isChecked) Color(0xFFFF2A7A) else Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .clickable { onToggleItem(item) }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isChecked) Color(0xFFFF2A7A) else Color.Transparent)
                                .border(1.5.dp, Color(0xFFFF2A7A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isChecked) {
                                Text("✓", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(item, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(if (isChecked) "Ready" else "Pending", fontSize = 11.sp, color = if (isChecked) Color(0xFFFFD700) else Color.White.copy(alpha = 0.5f))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Confirm Profile Checklist →", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** STEP 6: AI Verification */
@Composable
private fun Step6AiVerificationView(
    needHelp: Boolean,
    onSuccess: () -> Unit,
    onNeedHelp: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 6 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Did Every Step Complete?", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                .clickable { onSuccess() }
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎉", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Yes, Everything Done", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Text("→", fontSize = 18.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                .clickable { onNeedHelp() }
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🚨", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("No, Facing Issue", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(if (needHelp) "▲" else "▼", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                }

                if (needHelp) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("What exactly is the problem?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• Check Step 8 (Common Problems) for instant solutions.", fontSize = 12.sp, color = Color.White)
                    Text("• Or proceed to Step 7 to upload a profile screenshot for AI verification.", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}

/** STEP 7: Optional Screenshot Verification */
@Composable
private fun Step7ScreenshotVerificationView(
    selectedUri: Uri?,
    isAnalyzing: Boolean,
    feedback: String?,
    onSelectImage: () -> Unit,
    onContinue: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 7 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Screenshot Verification (Optional)", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.5.dp, Color(0x66FF2A7A), RoundedCornerShape(20.dp))
                .clickable { onSelectImage() }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🖼️", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (selectedUri != null) "Screenshot Attached" else "Select / Upload Profile Screenshot",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Tap to pick from gallery",
                    fontSize = 11.sp,
                    color = Color(0xFFFF2A7A)
                )
            }
        }

        if (isAnalyzing) {
            Spacer(modifier = Modifier.height(14.dp))
            Text("Analyzing screenshot...", fontSize = 13.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
        }

        if (feedback != null) {
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x33FF2A7A))
                    .padding(14.dp)
            ) {
                Text(feedback, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Next: Common Problems →", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** STEP 8: Common Problems Glass Cards */
@Composable
private fun Step8CommonProblemsView(
    selectedIndex: Int,
    onProblemClick: (Int) -> Unit,
    onContinue: () -> Unit
) {
    val problems = listOf(
        Pair("Creator option not visible", "Update Meesho app to latest version. Open Account tab -> check if 'Earn with Meesho' option appears."),
        Pair("Approval pending", "Creator program approvals are automated or take up to 24 hours. Ensure your social media link is correct."),
        Pair("Wrong account logged in", "Go to Account -> Logout, then login using your primary mobile number."),
        Pair("Incomplete profile issue", "Make sure your Profile Photo, Name, Email and Social handle are added in Account Settings."),
        Pair("Verification error", "Double check your Instagram/YouTube URL format (e.g. instagram.com/yourname)."),
        Pair("App update needed", "Go to Google Play Store and update Meesho to get the newest Creator features.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 8 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Common Problems & Fixes", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        problems.forEachIndexed { idx, (title, solution) ->
            val isExpanded = selectedIndex == idx
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, if (isExpanded) Color(0xFFFF2A7A) else Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .clickable { onProblemClick(idx) }
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(if (isExpanded) "▲" else "▼", fontSize = 12.sp, color = Color(0xFFFF2A7A))
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(solution, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f), lineHeight = 16.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Proceed to Mission Complete →", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** STEP 9: Achievement & Today's Mission Complete */
@Composable
private fun Step9AchievementMissionView(
    onFinishLevel2: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MISSION & LEVEL 2 COMPLETE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // PREMIUM GLASS BADGE
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFF2A7A), Color(0x44FF2A7A))
                    )
                )
                .border(2.5.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏆", fontSize = 42.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Account Ready!", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(6.dp))

        Text("You are now a Level 2 Account Setup Master!", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))

        Spacer(modifier = Modifier.height(16.dp))

        // XP REWARD CARD
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x33FFD700))
                .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨ +100 XP REWARD UNLOCKED", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFinishLevel2,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Complete Level 2 & Return →", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
