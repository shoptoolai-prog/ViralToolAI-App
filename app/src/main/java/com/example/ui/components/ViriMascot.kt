package com.example.ui.components

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

// ============================================================================
// VIRALTOOLAI OFFICIAL 3D AI MASCOT — "VIRI" (DS-16)
// ============================================================================
private val PureBlack = Color(0xFF000000)
private val DarkBody = Color(0xFF121212)
private val SoftWhiteBody = Color(0xFFF0F4F8)
private val CyanGlow = Color(0xFF20D9E8)
private val TextWhite = Color(0xFFFFFFFF)
private val GoldBadge = Color(0xFFFFD700)

enum class ViriAction {
    IDLE,
    LOOKING,
    WALKING,
    RUNNING,
    JUMPING,
    SLEEPING,
    SNORING,
    WAVING,
    POINTING,
    SITTING,
    PEEKING,
    HANGING,
    HIDING,
    SITTING_ON_TIMELINE,
    SLEEPING_EMPTY,
    WAKING_UP,
    THINKING,
    HAPPY,
    ANGRY,
    CURIOUS,
    YAWNING,
    CELEBRATING,
    DANCING,
    POINTING_REELS,
    POINTING_EDITOR
}

enum class ViriLevel(val levelName: String, val levelNum: Int, val accessory: String) {
    LEVEL_1("New Creator", 1, "Starter Aura"),
    LEVEL_2("Consistent Creator", 2, "Tiny Cap"),
    LEVEL_3("Smart Creator", 3, "Cyan Headphones"),
    LEVEL_4("Pro Creator", 4, "Pro Camera Badge"),
    LEVEL_5("Viral Creator", 5, "Golden Creator Crown")
}

object ViriPrefs {
    private const val PREF_NAME = "viri_mascot_prefs"
    private const val KEY_EXP = "viri_creator_exp"

    fun getExp(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_EXP, 100)
    }

    fun addExp(context: Context, amount: Int): ViriLevel {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val newExp = getExp(context) + amount
        prefs.edit().putInt(KEY_EXP, newExp).apply()
        return getLevelFromExp(newExp)
    }

    fun getLevel(context: Context): ViriLevel {
        return getLevelFromExp(getExp(context))
    }

    private fun getLevelFromExp(exp: Int): ViriLevel {
        return when {
            exp >= 1000 -> ViriLevel.LEVEL_5
            exp >= 600 -> ViriLevel.LEVEL_4
            exp >= 350 -> ViriLevel.LEVEL_3
            exp >= 200 -> ViriLevel.LEVEL_2
            else -> ViriLevel.LEVEL_1
        }
    }
}

/**
 * Dynamic message engine creating non-repeating Hinglish speech bubbles (DS-16)
 */
object ViriMessageEngine {
    private val hinglishAutoMessages = listOf(
        "Kya scene hai?",
        "Upload kar na.",
        "Aaj reel nahi banayi?",
        "Main bore ho raha hu.",
        "Hook weak lag raha hai.",
        "Ek baar scan kar le.",
        "Touch mat kar... so raha hu.",
        "Reel bana pehle!",
        "Viral hone ke 99% chances hai! 🔥",
        "Viri is ready, let's scan!"
    )

    private val touchRepliesWhenAwake = listOf(
        "Abe tickle mat kar 😂",
        "Reel bana pehle!",
        "Arey wah! Viri is happy!",
        "Kya scene hai boss?",
        "Ek baar scan kar le!"
    )

    private val touchRepliesWhenAsleep = listOf(
        "Touch mat kar... so raha hu.",
        "Abe sone de yaar...",
        "5 min aur sone de 😴",
        "Waking up... kya scene hai?"
    )

    private var lastMsgIndex = -1

    fun getRandomMessage(context: Context): String {
        var nextIdx = Random.nextInt(hinglishAutoMessages.size)
        while (nextIdx == lastMsgIndex && hinglishAutoMessages.size > 1) {
            nextIdx = Random.nextInt(hinglishAutoMessages.size)
        }
        lastMsgIndex = nextIdx
        return hinglishAutoMessages[nextIdx]
    }

    fun getTouchReply(isAsleep: Boolean): String {
        return if (isAsleep) {
            touchRepliesWhenAsleep.random()
        } else {
            touchRepliesWhenAwake.random()
        }
    }
}

@Composable
fun ViriMascotWidget(
    modifier: Modifier = Modifier,
    size: Dp = 110.dp,
    action: ViriAction = ViriAction.HAPPY,
    onTapAction: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var currentAction by remember(action) { mutableStateOf(action) }
    var speechText by remember { mutableStateOf<String?>(null) }
    var isBubbleVisible by remember { mutableStateOf(false) }
    val mascotLevel = remember { mutableStateOf(ViriPrefs.getLevel(context)) }

    // Position memory & target offsets for autonomous non-obstructive movement
    var targetOffsetX by remember { mutableFloatStateOf(0f) }
    var targetOffsetY by remember { mutableFloatStateOf(0f) }
    var userDragX by remember { mutableFloatStateOf(0f) }
    var userDragY by remember { mutableFloatStateOf(0f) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = targetOffsetX + userDragX,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "mascot_x"
    )
    val animatedOffsetY by animateFloatAsState(
        targetValue = targetOffsetY + userDragY,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "mascot_y"
    )

    var lastActivityTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Floating breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "viri_float")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val eyeBlinkProgress by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.05f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3200
                1f at 0
                1f at 2900
                0.05f at 3000
                1f at 3100
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "blink"
    )

    // Non-repeating behavior shuffle queue
    var behaviorQueue by remember {
        mutableStateOf(
            listOf(
                ViriAction.WALKING, ViriAction.RUNNING, ViriAction.JUMPING,
                ViriAction.WAVING, ViriAction.POINTING, ViriAction.SITTING,
                ViriAction.PEEKING, ViriAction.HANGING, ViriAction.HIDING,
                ViriAction.THINKING, ViriAction.HAPPY, ViriAction.DANCING
            ).shuffled()
        )
    }

    // Autonomous mascot movement & random non-repeating behavior loop
    LaunchedEffect(Unit) {
        val positionOffsets = listOf(
            Pair(0f, 0f),           // Corner
            Pair(-160f, -450f),     // Top Header
            Pair(-200f, -320f),     // AI Card
            Pair(-30f, -300f),      // Edit Card
            Pair(-170f, -180f),     // Recent Projects
            Pair(-220f, -10f),      // Bottom Edge
            Pair(-240f, -280f),     // Behind Cards (Peeking)
            Pair(-100f, -10f)       // Near Navigation
        )

        while (true) {
            delay(10000)
            val idleSec = (System.currentTimeMillis() - lastActivityTime) / 1000
            if (idleSec < 30) {
                if (behaviorQueue.isEmpty()) {
                    behaviorQueue = listOf(
                        ViriAction.WALKING, ViriAction.RUNNING, ViriAction.JUMPING,
                        ViriAction.WAVING, ViriAction.POINTING, ViriAction.SITTING,
                        ViriAction.PEEKING, ViriAction.HANGING, ViriAction.HIDING,
                        ViriAction.THINKING, ViriAction.HAPPY, ViriAction.DANCING
                    ).shuffled()
                }

                val nextAction = behaviorQueue.first()
                behaviorQueue = behaviorQueue.drop(1)
                currentAction = nextAction

                val nextPos = positionOffsets.random()
                targetOffsetX = nextPos.first
                targetOffsetY = nextPos.second

                // Auto speech bubble for 3 seconds
                if (Random.nextFloat() < 0.50f) {
                    speechText = ViriMessageEngine.getRandomMessage(context)
                    isBubbleVisible = true
                }
            }
        }
    }

    // Strict Idle user reaction loop: 30s Yawn -> 45s Sit -> 60s Sleep -> 90s Snore
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            val idleSec = (System.currentTimeMillis() - lastActivityTime) / 1000
            when {
                idleSec >= 90 -> {
                    if (currentAction != ViriAction.SNORING) {
                        currentAction = ViriAction.SNORING
                        speechText = "Touch mat kar... so raha hu."
                        isBubbleVisible = true
                    }
                }
                idleSec >= 60 -> {
                    if (currentAction != ViriAction.SLEEPING && currentAction != ViriAction.SNORING) {
                        currentAction = ViriAction.SLEEPING
                        speechText = "Main bore ho raha hu."
                        isBubbleVisible = true
                    }
                }
                idleSec >= 45 -> {
                    if (currentAction != ViriAction.SITTING && currentAction != ViriAction.SLEEPING && currentAction != ViriAction.SNORING) {
                        currentAction = ViriAction.SITTING
                        speechText = "Aaj reel nahi banayi?"
                        isBubbleVisible = true
                    }
                }
                idleSec >= 30 -> {
                    if (currentAction != ViriAction.YAWNING && currentAction != ViriAction.SITTING && currentAction != ViriAction.SLEEPING) {
                        currentAction = ViriAction.YAWNING
                        speechText = "Kya scene hai?"
                        isBubbleVisible = true
                    }
                }
            }
        }
    }

    // Auto-hide speech bubble after 3 seconds
    LaunchedEffect(isBubbleVisible) {
        if (isBubbleVisible) {
            delay(3000)
            isBubbleVisible = false
        }
    }

    Column(
        modifier = modifier
            .offset { IntOffset(animatedOffsetX.roundToInt(), animatedOffsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    userDragX += dragAmount.x
                    userDragY += dragAmount.y
                    lastActivityTime = System.currentTimeMillis()
                }
            }
            .testTag("viri_mascot_container"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Floating Hinglish Speech Bubble (Auto appears / disappears in 3s)
        AnimatedVisibility(
            visible = isBubbleVisible && !speechText.isNullOrBlank(),
            enter = fadeIn(tween(200)) + scaleIn(tween(200), initialScale = 0.8f),
            exit = fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.8f)
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .widthIn(max = 210.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF222834), Color(0xFF141720))
                        )
                    )
                    .border(1.5.dp, CyanGlow.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = speechText ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    lineHeight = 16.sp
                )
            }
        }

        // 3D Robot Mascot Canvas Render + Tap Interaction
        Box(
            modifier = Modifier
                .size(size)
                .graphicsLayer { translationY = floatY }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    val isCurrentlyAsleep = currentAction == ViriAction.SLEEPING || currentAction == ViriAction.SNORING
                    lastActivityTime = System.currentTimeMillis() // Reset idle timer
                    currentAction = if (isCurrentlyAsleep) ViriAction.WAKING_UP else ViriAction.HAPPY
                    speechText = ViriMessageEngine.getTouchReply(isCurrentlyAsleep)
                    isBubbleVisible = true
                    onTapAction?.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            ViriBodyCanvas(
                action = currentAction,
                level = mascotLevel.value,
                eyeBlink = eyeBlinkProgress,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Native Canvas drawing of Viri - 3D Pixar Style Robot Mascot with Head, Eyes, Hands, Legs, Body, Antenna, & Expressions.
 */
@Composable
private fun ViriBodyCanvas(
    action: ViriAction,
    level: ViriLevel,
    eyeBlink: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "robot_anim")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 3D Metallic Shading Brushes
        val metallicBody = Brush.linearGradient(
            colors = listOf(Color(0xFF3B4252), Color(0xFF2E3440), Color(0xFF1E222A)),
            start = Offset(w * 0.2f, h * 0.1f),
            end = Offset(w * 0.8f, h * 0.9f)
        )
        val metallicJoint = Color(0xFF4C566A)
        val glassVisor = Brush.verticalGradient(
            colors = listOf(Color(0xFF0F172A), Color(0xFF020617))
        )
        val cyanGlow = Color(0xFF22D7E8)
        val cyanLightGlow = Color(0xFF80EEF8)
        val goldCrown = Color(0xFFFFD700)

        val sinPhase = kotlin.math.sin(animPhase)

        val armSwing = when (action) {
            ViriAction.WALKING, ViriAction.RUNNING -> sinPhase * 16f
            ViriAction.DANCING, ViriAction.HAPPY, ViriAction.CELEBRATING -> sinPhase * 28f
            ViriAction.WAVING -> sinPhase * 32f
            else -> sinPhase * 4f
        }

        val legBounce = when (action) {
            ViriAction.JUMPING -> kotlin.math.abs(sinPhase) * 14f
            ViriAction.WALKING, ViriAction.RUNNING -> kotlin.math.abs(sinPhase) * 7f
            ViriAction.DANCING -> kotlin.math.abs(sinPhase) * 10f
            else -> 0f
        }

        // ==========================================
        // 1. FEET & LEGS
        // ==========================================
        val isSitting = action == ViriAction.SITTING || action == ViriAction.SITTING_ON_TIMELINE
        val legTopY = if (isSitting) h * 0.58f else h * 0.68f - legBounce
        val footY = if (isSitting) h * 0.70f else h * 0.86f - legBounce

        // Left Leg
        drawLine(
            color = metallicJoint,
            start = Offset(w * 0.38f, legTopY),
            end = Offset(if (isSitting) w * 0.28f else w * 0.36f + (if (action == ViriAction.WALKING) sinPhase * 6f else 0f), footY),
            strokeWidth = 6.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Left Foot Shoe
        drawRoundRect(
            color = Color(0xFF232832),
            topLeft = Offset(if (isSitting) w * 0.20f else w * 0.28f, footY - 2.dp.toPx()),
            size = Size(w * 0.16f, h * 0.08f),
            cornerRadius = CornerRadius(6.dp.toPx())
        )
        drawLine(
            color = cyanGlow,
            start = Offset(if (isSitting) w * 0.22f else w * 0.30f, footY + h * 0.05f),
            end = Offset(if (isSitting) w * 0.34f else w * 0.42f, footY + h * 0.05f),
            strokeWidth = 2.dp.toPx()
        )

        // Right Leg
        drawLine(
            color = metallicJoint,
            start = Offset(w * 0.62f, legTopY),
            end = Offset(if (isSitting) w * 0.72f else w * 0.64f - (if (action == ViriAction.WALKING) sinPhase * 6f else 0f), footY),
            strokeWidth = 6.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Right Foot Shoe
        drawRoundRect(
            color = Color(0xFF232832),
            topLeft = Offset(if (isSitting) w * 0.64f else w * 0.56f, footY - 2.dp.toPx()),
            size = Size(w * 0.16f, h * 0.08f),
            cornerRadius = CornerRadius(6.dp.toPx())
        )
        drawLine(
            color = cyanGlow,
            start = Offset(if (isSitting) w * 0.66f else w * 0.58f, footY + h * 0.05f),
            end = Offset(if (isSitting) w * 0.78f else w * 0.70f, footY + h * 0.05f),
            strokeWidth = 2.dp.toPx()
        )

        // ==========================================
        // 2. TORSO / BODY
        // ==========================================
        val bodyY = if (isSitting) h * 0.38f else h * 0.42f - legBounce
        val bodySize = Size(w * 0.44f, h * 0.30f)
        val bodyRect = Offset(w * 0.28f, bodyY)

        // Main 3D Body Shell
        drawRoundRect(
            brush = metallicBody,
            topLeft = bodyRect,
            size = bodySize,
            cornerRadius = CornerRadius(16.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFF4C566A),
            topLeft = bodyRect,
            size = bodySize,
            cornerRadius = CornerRadius(16.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Glowing Cyan Core Heart on Chest
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(cyanGlow, Color.Transparent),
                center = Offset(w * 0.5f, bodyY + h * 0.14f),
                radius = w * 0.12f
            ),
            radius = w * 0.12f,
            center = Offset(w * 0.5f, bodyY + h * 0.14f)
        )
        drawCircle(
            color = cyanLightGlow,
            radius = w * 0.04f,
            center = Offset(w * 0.5f, bodyY + h * 0.14f)
        )

        // ==========================================
        // 3. ARMS, HANDS & FINGERS
        // ==========================================
        val leftShoulder = Offset(w * 0.25f, bodyY + h * 0.06f)
        val rightShoulder = Offset(w * 0.75f, bodyY + h * 0.06f)

        val leftHandY = when (action) {
            ViriAction.HANGING -> bodyY - h * 0.22f
            ViriAction.POINTING_REELS, ViriAction.POINTING_EDITOR -> bodyY - h * 0.10f
            ViriAction.THINKING -> bodyY - h * 0.05f
            else -> bodyY + h * 0.20f + armSwing
        }
        val leftHandX = when (action) {
            ViriAction.HANGING -> w * 0.22f
            ViriAction.POINTING_REELS -> w * 0.08f
            ViriAction.THINKING -> w * 0.32f
            else -> w * 0.18f
        }

        drawLine(
            color = metallicJoint,
            start = leftShoulder,
            end = Offset(leftHandX, leftHandY),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(color = Color(0xFF2E3440), radius = w * 0.045f, center = Offset(leftHandX, leftHandY))
        // Articulated fingers
        drawLine(
            color = cyanGlow,
            start = Offset(leftHandX, leftHandY),
            end = Offset(leftHandX - w * 0.03f, leftHandY - h * 0.02f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        val rightHandY = when (action) {
            ViriAction.HANGING -> bodyY - h * 0.22f
            ViriAction.WAVING, ViriAction.CELEBRATING, ViriAction.POINTING_EDITOR -> bodyY - h * 0.15f + armSwing
            else -> bodyY + h * 0.20f - armSwing
        }
        val rightHandX = when (action) {
            ViriAction.HANGING -> w * 0.78f
            ViriAction.POINTING_EDITOR -> w * 0.92f
            ViriAction.WAVING -> w * 0.88f
            else -> w * 0.82f
        }

        drawLine(
            color = metallicJoint,
            start = rightShoulder,
            end = Offset(rightHandX, rightHandY),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(color = Color(0xFF2E3440), radius = w * 0.045f, center = Offset(rightHandX, rightHandY))
        drawLine(
            color = cyanGlow,
            start = Offset(rightHandX, rightHandY),
            end = Offset(rightHandX + w * 0.03f, rightHandY - h * 0.02f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // ==========================================
        // 4. NECK & HEAD
        // ==========================================
        val neckY = bodyY - h * 0.05f
        drawLine(
            color = metallicJoint,
            start = Offset(w * 0.5f, bodyY),
            end = Offset(w * 0.5f, neckY),
            strokeWidth = 8.dp.toPx(),
            cap = StrokeCap.Round
        )

        val headY = neckY - h * 0.24f
        val headSize = Size(w * 0.48f, h * 0.24f)
        val headTopLeft = Offset(w * 0.26f, headY)

        drawRoundRect(
            brush = metallicBody,
            topLeft = headTopLeft,
            size = headSize,
            cornerRadius = CornerRadius(20.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFF4C566A),
            topLeft = headTopLeft,
            size = headSize,
            cornerRadius = CornerRadius(20.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Antenna with Glowing Bulb
        drawLine(
            color = metallicJoint,
            start = Offset(w * 0.5f, headY),
            end = Offset(w * 0.5f, headY - h * 0.06f),
            strokeWidth = 3.dp.toPx()
        )
        drawCircle(
            color = cyanGlow,
            radius = w * 0.035f,
            center = Offset(w * 0.5f, headY - h * 0.06f)
        )

        // Visor & Eyes Expression Morphing
        val visorSize = Size(w * 0.38f, h * 0.16f)
        val visorTopLeft = Offset(w * 0.31f, headY + h * 0.04f)
        drawRoundRect(
            brush = glassVisor,
            topLeft = visorTopLeft,
            size = visorSize,
            cornerRadius = CornerRadius(12.dp.toPx())
        )

        val eyeY = headY + h * 0.11f
        val leftEyeX = w * 0.41f
        val rightEyeX = w * 0.59f
        val isSleepingOrSnoring = action == ViriAction.SLEEPING || action == ViriAction.SNORING || action == ViriAction.SLEEPING_EMPTY
        val eyeH = 10.dp.toPx() * if (isSleepingOrSnoring) 0.1f else eyeBlink
        val eyeW = 8.dp.toPx()
        val eyeColor = if (action == ViriAction.ANGRY) Color(0xFFFF5252) else cyanGlow

        if (isSleepingOrSnoring) {
            drawLine(
                color = cyanGlow,
                start = Offset(leftEyeX - 5.dp.toPx(), eyeY),
                end = Offset(leftEyeX + 5.dp.toPx(), eyeY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = cyanGlow,
                start = Offset(rightEyeX - 5.dp.toPx(), eyeY),
                end = Offset(rightEyeX + 5.dp.toPx(), eyeY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        } else {
            drawOval(
                color = eyeColor,
                topLeft = Offset(leftEyeX - eyeW / 2, eyeY - eyeH / 2),
                size = Size(eyeW, eyeH)
            )
            drawOval(
                color = eyeColor,
                topLeft = Offset(rightEyeX - eyeW / 2, eyeY - eyeH / 2),
                size = Size(eyeW, eyeH)
            )
            drawCircle(color = cyanLightGlow, radius = 2.dp.toPx(), center = Offset(leftEyeX, eyeY))
            drawCircle(color = cyanLightGlow, radius = 2.dp.toPx(), center = Offset(rightEyeX, eyeY))
        }

        // Expressive Mouth Line Path
        val mouthPath = Path().apply {
            when (action) {
                ViriAction.SLEEPING, ViriAction.SNORING, ViriAction.YAWNING, ViriAction.SLEEPING_EMPTY -> {
                    moveTo(w * 0.47f, headY + h * 0.17f)
                    lineTo(w * 0.53f, headY + h * 0.17f)
                }
                ViriAction.ANGRY -> {
                    moveTo(w * 0.45f, headY + h * 0.18f)
                    quadraticTo(w * 0.50f, headY + h * 0.14f, w * 0.55f, headY + h * 0.18f)
                }
                ViriAction.CELEBRATING, ViriAction.HAPPY, ViriAction.DANCING, ViriAction.JUMPING -> {
                    moveTo(w * 0.44f, headY + h * 0.15f)
                    quadraticTo(w * 0.50f, headY + h * 0.20f, w * 0.56f, headY + h * 0.15f)
                }
                else -> {
                    moveTo(w * 0.46f, headY + h * 0.16f)
                    quadraticTo(w * 0.50f, headY + h * 0.19f, w * 0.54f, headY + h * 0.16f)
                }
            }
        }
        drawPath(
            path = mouthPath,
            color = cyanGlow,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )

        // Crown for Level 5 Creators
        if (level == ViriLevel.LEVEL_5) {
            val crownPath = Path().apply {
                moveTo(w * 0.42f, headY - h * 0.01f)
                lineTo(w * 0.40f, headY - h * 0.08f)
                lineTo(w * 0.46f, headY - h * 0.04f)
                lineTo(w * 0.50f, headY - h * 0.09f)
                lineTo(w * 0.54f, headY - h * 0.04f)
                lineTo(w * 0.60f, headY - h * 0.08f)
                lineTo(w * 0.58f, headY - h * 0.01f)
                close()
            }
            drawPath(path = crownPath, color = goldCrown)
        }
    }
}

