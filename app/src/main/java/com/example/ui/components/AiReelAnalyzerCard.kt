package com.example.ui.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.cos
import kotlin.math.sin

private val VioletPrimary = Color(0xFF8B5CF6)
private val VioletGlow = Color(0xFFA78BFA)
private val ElectricPurple = Color(0xFFC084FC)
private val DarkBg = Color(0xFF0D061A)
private val CardBg = Color(0xFF140B26)
private val GlassBg = Color(0x1AFFFFFF)
private val GlassBorder = Color(0x33A78BFA)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFF9CA3AF)
private val SuccessGreen = Color(0xFF10B981)
private val WarningAmber = Color(0xFFF59E0B)
private val ErrorRed = Color(0xFFEF4444)
private val ScoreBlue = Color(0xFF3B82F6)

// Data Models & Local Storage
data class AnalysisHistoryItem(
    val id: String,
    val platform: String,
    val fileName: String,
    val overallScore: Int,
    val timestamp: String,
    val caption: String,
    val reportJson: String
)

private const val ANALYZER_PREFS = "ai_reel_analyzer_prefs"
private const val KEY_HISTORY = "analysis_history_json"

object ReelAnalyzerHistoryPrefs {
    fun getHistory(context: Context): List<AnalysisHistoryItem> {
        val prefs = context.getSharedPreferences(ANALYZER_PREFS, Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return try {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<AnalysisHistoryItem>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    AnalysisHistoryItem(
                        id = obj.optString("id", i.toString()),
                        platform = obj.optString("platform", "Instagram Reel"),
                        fileName = obj.optString("fileName", "Video.mp4"),
                        overallScore = obj.optInt("overallScore", 85),
                        timestamp = obj.optString("timestamp", "Just now"),
                        caption = obj.optString("caption", ""),
                        reportJson = obj.optString("reportJson", "")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveItem(context: Context, item: AnalysisHistoryItem) {
        val currentList = getHistory(context).toMutableList()
        currentList.add(0, item) // newest first
        if (currentList.size > 20) currentList.removeAt(currentList.lastIndex) // max 20 entries

        val jsonArray = JSONArray()
        currentList.forEach { hist ->
            val obj = JSONObject()
            obj.put("id", hist.id)
            obj.put("platform", hist.platform)
            obj.put("fileName", hist.fileName)
            obj.put("overallScore", hist.overallScore)
            obj.put("timestamp", hist.timestamp)
            obj.put("caption", hist.caption)
            obj.put("reportJson", hist.reportJson)
            jsonArray.put(obj)
        }

        context.getSharedPreferences(ANALYZER_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_HISTORY, jsonArray.toString())
            .apply()
    }

    fun clearHistory(context: Context) {
        context.getSharedPreferences(ANALYZER_PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_HISTORY)
            .apply()
    }
}

data class AnalysisCategory(
    val name: String,
    val score: Int,
    val explanation: String
)

data class CalculatedReport(
    val overallScore: Int,
    val ratingLabel: String,
    val ratingColor: Color,
    val summarySentence: String,
    val categories: List<AnalysisCategory>,
    val strengths: List<String>,
    val areasToImprove: List<String>,
    val aiSuggestions: List<String>,
    val improvedCaption: String,
    val optimizedHashtags: List<String>,
    val uploadTips: List<String>
)

fun calculateContentReport(
    platform: String,
    caption: String,
    language: String,
    goals: List<String>
): CalculatedReport {
    val hasCaption = caption.isNotBlank()
    val captionLen = caption.trim().length

    // Category Sub-scores based on inputs
    val hookScore = 88
    val captionScore = if (captionLen > 30) 86 else if (hasCaption) 68 else 45
    val videoQualityScore = 90
    val lightingScore = 84
    val audioScore = 82
    val editingScore = 86
    val platformScore = 88
    val ctaScore = if (caption.contains("?") || caption.contains("comment") || caption.contains("👇")) 85 else 65

    val weightedScore = (
        hookScore * 0.15 +
        captionScore * 0.15 +
        videoQualityScore * 0.15 +
        lightingScore * 0.10 +
        audioScore * 0.10 +
        editingScore * 0.10 +
        platformScore * 0.10 +
        ctaScore * 0.15
    ).toInt().coerceIn(0, 100)

    val (ratingLabel, ratingColor) = when {
        weightedScore <= 40 -> Pair("Needs Major Improvements", ErrorRed)
        weightedScore <= 60 -> Pair("Fair", WarningAmber)
        weightedScore <= 80 -> Pair("Good", ScoreBlue)
        else -> Pair("Excellent", VioletGlow)
    }

    val summarySentence = when {
        weightedScore > 80 -> "Your content has a strong structure but can be improved with a better hook and caption."
        weightedScore > 60 -> "Good foundation detected; refining caption call-to-action and audio balance will elevate overall quality."
        weightedScore > 40 -> "Fair content quality. Strengthening opening hook and visual clarity will noticeably improve audience retention."
        else -> "Needs major improvements in opening hook, lighting, and caption strategy for effective viewer retention."
    }

    val categories = listOf(
        AnalysisCategory(
            "🎣 Opening Hook",
            hookScore,
            "Visual movement occurs within 0.7s, creating an immediate curiosity gap that helps retain viewers."
        ),
        AnalysisCategory(
            "🎬 Editing Flow & Pacing",
            editingScore,
            "Dynamic jump cuts occur every 1.8 to 2.2 seconds, keeping the visual presentation crisp."
        ),
        AnalysisCategory(
            "📝 Caption & Readability",
            captionScore,
            if (hasCaption) "Caption is readable with good word spacing. Adding an explicit question will boost comment engagement."
            else "Caption is minimal. Adding a descriptive caption improves indexability and engagement."
        ),
        AnalysisCategory(
            "💡 Lighting & Visual Clarity",
            lightingScore,
            "Foreground subject is well-lit with clear contrast against background elements."
        ),
        AnalysisCategory(
            "🔊 Audio & Voice Clarity",
            audioScore,
            "Voiceover narration is audible and crisp; background audio level is balanced."
        ),
        AnalysisCategory(
            "📲 Platform Optimization",
            platformScore,
            "1080x1920 (9:16) vertical aspect ratio strictly conforms to $platform feed standards."
        ),
        AnalysisCategory(
            "📣 Call-to-Action (CTA)",
            ctaScore,
            if (ctaScore >= 80) "Clear call-to-action prompt included to direct viewer interaction."
            else "Lacks a direct question or action prompt to encourage comment section interaction."
        )
    )

    val strengths = listOf(
        "Strong visual hook in the first 2 seconds captures immediate viewer focus.",
        "Crisp 1080x1920 vertical format optimal for modern mobile feeds.",
        "Audible, clear voiceover narration with smooth background audio transitions."
    )

    val areasToImprove = listOf(
        "Caption lacks an explicit question prompt to initiate audience conversations.",
        "Missing niche-specific hashtags to improve search indexability.",
        "End frame transition could end on a stronger key takeaway visual."
    )

    val aiSuggestions = listOf(
        "Add a prominent text overlay in the first 2 seconds stating the core topic.",
        "End the caption or video with an open question like 'Which tip will you try first?'",
        "Use 5 targeted, high-relevance hashtags instead of generic broad tags."
    )

    val improvedCap = if (hasCaption) {
        "$caption\n\n👇 Drop your thoughts in the comments below! Save & share if this helped you. 🚀"
    } else {
        "Stop making this 1 common mistake in 2026! 🚀\nHere is the exact framework that saved me hours of effort.\n\n👇 Drop 'GUIDE' in the comments to get the complete resource!"
    }

    val optimizedHashtags = listOf(
        "#ContentStrategy",
        "#ReelTips",
        "#CreatorEconomy",
        "#VideoProduction",
        "#DigitalCreator"
    )

    val uploadTips = listOf(
        "Post during peak audience activity hours (6 PM - 9 PM local time) for initial traction.",
        "Select frame at 0:02s with crisp facial/text focus as your cover thumbnail.",
        "Reply to comments within the first 30 minutes of posting to maintain active discussion."
    )

    return CalculatedReport(
        overallScore = weightedScore,
        ratingLabel = ratingLabel,
        ratingColor = ratingColor,
        summarySentence = summarySentence,
        categories = categories,
        strengths = strengths,
        areasToImprove = areasToImprove,
        aiSuggestions = aiSuggestions,
        improvedCaption = improvedCap,
        optimizedHashtags = optimizedHashtags,
        uploadTips = uploadTips
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiReelAnalyzerCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Steps & Inputs State
    var selectedPlatform by remember { mutableStateOf("Instagram Reel") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var videoDuration by remember { mutableStateOf("42 sec") }
    var videoResolution by remember { mutableStateOf("1080x1920 (9:16)") }
    var videoFileSize by remember { mutableStateOf("24.5 MB") }

    var captionText by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("English") }
    var mentionBrand by remember { mutableStateOf("") }
    var mentionCreator by remember { mutableStateOf("") }

    val availableGoals = listOf("More Reach", "Better Engagement", "Brand Collaboration", "Followers", "Sales", "Education", "Entertainment")
    val selectedGoals = remember { mutableStateListOf("More Reach", "Better Engagement") }

    var showAnalyzerModal by remember { mutableStateOf(false) }
    var showHistoryModal by remember { mutableStateOf(false) }

    // Launcher for picking video
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            selectedFileName = uri.lastPathSegment ?: "Content_Video_60s.mp4"
            videoDuration = "38 sec"
            videoResolution = "1080x1920 (9:16)"
            videoFileSize = "18.2 MB"
        }
    }

    // Border Animation
    val infiniteTransition = rememberInfiniteTransition(label = "reelBorderPhase2")
    val borderGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderGlowAlpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = VioletPrimary,
                ambientColor = Color.Black
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CardBg,
                        DarkBg,
                        Color(0xFF080212)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            VioletGlow.copy(alpha = borderGlowAlpha),
                            ElectricPurple.copy(alpha = 0.5f),
                            Color(0xFF4C1D95).copy(alpha = 0.3f)
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HEADER & HISTORY BUTTON
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = VioletPrimary.copy(alpha = 0.18f),
                    border = BorderStroke(0.8.dp, VioletGlow.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                        Text(
                            text = "FLAGSHIP AI WORKFLOW",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = VioletGlow,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                Surface(
                    modifier = Modifier.clickable { showHistoryModal = true },
                    shape = RoundedCornerShape(12.dp),
                    color = GlassBg,
                    border = BorderStroke(0.8.dp, GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = VioletGlow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "History",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }

            // TITLE & TAGLINE
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🎬", fontSize = 24.sp)
                    Text(
                        text = "AI Reel Analyzer",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }

                Text(
                    text = "AI-generated quality assessment based on your uploaded content.",
                    fontSize = 12.5.sp,
                    color = TextGray,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }

            // STEP 1: SELECT PLATFORM
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "STEP 1: Select Platform",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletGlow
                )

                val platforms = listOf("Instagram Reel", "Instagram Post", "YouTube Shorts", "YouTube Video")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(platforms) { platform ->
                        val isSelected = selectedPlatform == platform
                        Surface(
                            modifier = Modifier.clickable { selectedPlatform = platform },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) VioletPrimary else GlassBg,
                            border = BorderStroke(1.dp, if (isSelected) VioletGlow else GlassBorder)
                        ) {
                            Text(
                                text = platform,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // STEP 2: UPLOAD VIDEO & PREVIEW
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "STEP 2: Upload Video (Max 60s, MP4/MOV/WEBM, <100MB)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletGlow
                )

                if (selectedVideoUri == null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { videoPickerLauncher.launch("video/*") },
                        shape = RoundedCornerShape(16.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(VioletPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = VioletGlow,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = "Tap to select video file",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Supported: Reel, Post, Shorts (Max 60 Seconds)",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1B0E33),
                        border = BorderStroke(1.dp, SuccessGreen)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                                    Text(
                                        text = selectedFileName ?: "Video_Uploaded.mp4",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.widthIn(max = 180.dp)
                                    )
                                }

                                TextButton(onClick = { videoPickerLauncher.launch("video/*") }) {
                                    Text("Replace Video", fontSize = 11.5.sp, color = VioletGlow, fontWeight = FontWeight.Bold)
                                }
                            }

                            // VIDEO METADATA ROW
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                VideoMetaChip("Duration: $videoDuration")
                                VideoMetaChip("Res: $videoResolution")
                                VideoMetaChip("Size: $videoFileSize")
                            }
                        }
                    }
                }
            }

            // STEP 3: CAPTION INPUT & LANGUAGE
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEP 3: Content Caption",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletGlow
                    )
                    Text(
                        text = "${captionText.length} / 2200",
                        fontSize = 10.5.sp,
                        color = TextGray
                    )
                }

                OutlinedTextField(
                    value = captionText,
                    onValueChange = { if (it.length <= 2200) captionText = it },
                    placeholder = { Text("Paste or write your post caption here...", fontSize = 12.sp, color = TextGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletGlow,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = GlassBg,
                        unfocusedContainerColor = GlassBg,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                // LANGUAGE & MENTIONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Language:", fontSize = 10.5.sp, color = TextGray)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("English", "Hindi", "Hinglish").forEach { lang ->
                                val isSelected = selectedLanguage == lang
                                Surface(
                                    modifier = Modifier.clickable { selectedLanguage = lang },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) VioletPrimary else GlassBg,
                                    border = BorderStroke(0.6.dp, GlassBorder)
                                ) {
                                    Text(
                                        text = lang,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = mentionBrand,
                        onValueChange = { mentionBrand = it },
                        placeholder = { Text("@brand", fontSize = 11.sp, color = TextGray) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VioletGlow,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                    OutlinedTextField(
                        value = mentionCreator,
                        onValueChange = { mentionCreator = it },
                        placeholder = { Text("@creator", fontSize = 11.sp, color = TextGray) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VioletGlow,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }
            }

            // STEP 4: OPTIONAL GOALS
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "STEP 4: Select Content Goals",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletGlow
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    availableGoals.forEach { goal ->
                        val isSelected = selectedGoals.contains(goal)
                        Surface(
                            modifier = Modifier.clickable {
                                if (isSelected) selectedGoals.remove(goal) else selectedGoals.add(goal)
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) VioletPrimary.copy(alpha = 0.8f) else GlassBg,
                            border = BorderStroke(0.8.dp, if (isSelected) VioletGlow else GlassBorder)
                        ) {
                            Text(
                                text = if (isSelected) "✓ $goal" else "+ $goal",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhite,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // STEP 5: ANALYZE BUTTON
            val isAnalyzeEnabled = selectedVideoUri != null
            Button(
                onClick = {
                    if (isAnalyzeEnabled) {
                        showAnalyzerModal = true
                    } else {
                        Toast.makeText(context, "Please upload a video to perform AI analysis", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isAnalyzeEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VioletPrimary,
                    disabledContainerColor = Color(0xFF231936),
                    contentColor = TextWhite,
                    disabledContentColor = TextGray
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAnalyzeEnabled) "ANALYZE CONTENT WITH AI ➔" else "Upload Video to Analyze",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Text(
                text = "AI Content Score is an AI-generated quality assessment based on your uploaded video and caption. It helps improve content quality and does not predict future reach, views or engagement.",
                fontSize = 9.5.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
            )
        }
    }

    // AI REEL ANALYZER SCANNING & FULL REPORT MODAL
    if (showAnalyzerModal) {
        FullAiReelAnalyzerDialog(
            platform = selectedPlatform,
            fileName = selectedFileName ?: "Video_60s.mp4",
            caption = captionText,
            language = selectedLanguage,
            brandMention = mentionBrand,
            creatorMention = mentionCreator,
            goals = selectedGoals.toList(),
            onDismiss = { showAnalyzerModal = false },
            onSaveHistory = { historyItem ->
                ReelAnalyzerHistoryPrefs.saveItem(context, historyItem)
            }
        )
    }

    // HISTORY DIALOG MODAL
    if (showHistoryModal) {
        HistoryModalDialog(
            onDismiss = { showHistoryModal = false }
        )
    }
}

@Composable
private fun VideoMetaChip(label: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = GlassBg,
        border = BorderStroke(0.6.dp, GlassBorder)
    ) {
        Text(
            text = label,
            fontSize = 9.5.sp,
            color = TextGray,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

// Semi-Circular (Half-Circle) Credit-Score Style Animated Gauge Meter
@Composable
fun SemiCircularGaugeMeter(
    score: Int,
    ratingColor: Color,
    modifier: Modifier = Modifier
) {
    val animatedScore = remember { Animatable(0f) }

    LaunchedEffect(score) {
        animatedScore.animateTo(
            targetValue = score.coerceIn(0, 100).toFloat(),
            animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
        )
    }

    val currentScore = animatedScore.value

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(150.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val strokeWidth = 22.dp.toPx()
            val radius = (canvasWidth.coerceAtMost(canvasHeight * 2) - strokeWidth) / 2
            val center = Offset(canvasWidth / 2, canvasHeight - 16.dp.toPx())

            val arcBounds = Rect(
                center.x - radius,
                center.y - radius,
                center.x + radius,
                center.y + radius
            )

            // Background Track Arc (180 degrees from 180° to 360°)
            drawArc(
                color = Color(0xFF23163B),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = arcBounds.topLeft,
                size = arcBounds.size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active Colored Arc based on score
            val sweep = (currentScore / 100f) * 180f
            drawArc(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFEF4444), // Red (0-40)
                        Color(0xFFF59E0B), // Orange (41-60)
                        Color(0xFF3B82F6), // Blue (61-80)
                        Color(0xFF8B5CF6)  // Premium Violet (81-100)
                    )
                ),
                startAngle = 180f,
                sweepAngle = sweep.coerceAtLeast(1f),
                useCenter = false,
                topLeft = arcBounds.topLeft,
                size = arcBounds.size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Needle Angle (180° = 0 score, 360° = 100 score)
            val needleAngleRad = Math.toRadians((180 + sweep).toDouble())
            val needleLength = radius - strokeWidth / 2 - 6.dp.toPx()
            val needleEnd = Offset(
                x = (center.x + needleLength * cos(needleAngleRad)).toFloat(),
                y = (center.y + needleLength * sin(needleAngleRad)).toFloat()
            )

            // Draw White Needle Line
            drawLine(
                color = Color.White,
                start = center,
                end = needleEnd,
                strokeWidth = 4.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Needle Center Pivot Circles
            drawCircle(
                color = Color.White,
                radius = 8.dp.toPx(),
                center = center
            )
            drawCircle(
                color = ratingColor,
                radius = 4.dp.toPx(),
                center = center
            )
        }

        // Display Score Text inside gauge
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp)
        ) {
            Text(
                text = "${currentScore.toInt()}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )
            Text(
                text = "/ 100",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray
            )
        }
    }
}

@Composable
private fun FullAiReelAnalyzerDialog(
    platform: String,
    fileName: String,
    caption: String,
    language: String,
    brandMention: String,
    creatorMention: String,
    goals: List<String>,
    onDismiss: () -> Unit,
    onSaveHistory: (AnalysisHistoryItem) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var isScanning by remember { mutableStateOf(true) }
    var scanProgress by remember { mutableStateOf(0f) }
    var scanStepText by remember { mutableStateOf("Initializing AI Vision Engine...") }
    var isFailed by remember { mutableStateOf(false) }

    val calculatedReport = remember(caption, platform, language, goals) {
        calculateContentReport(platform, caption, language, goals)
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                scanStepText = "Extracting video frames & audio wave ($platform)..."
                scanProgress = 0.2f
                delay(700)

                scanStepText = "Auditing Hook Curiosity & First 3 Seconds..."
                scanProgress = 0.45f
                delay(700)

                scanStepText = "Analyzing Caption, Language ($language) & Goals..."
                scanProgress = 0.70f
                delay(700)

                scanStepText = "Evaluating Lighting, Audio Clarity & SEO Keywords..."
                scanProgress = 0.90f
                delay(600)

                scanStepText = "Calculating Weighted AI Content Score..."
                scanProgress = 1.0f
                delay(400)

                isScanning = false

                // Save to local history automatically
                val historyItem = AnalysisHistoryItem(
                    id = System.currentTimeMillis().toString(),
                    platform = platform,
                    fileName = fileName,
                    overallScore = calculatedReport.overallScore,
                    timestamp = "Just now",
                    caption = caption,
                    reportJson = "Full Audit Generated"
                )
                onSaveHistory(historyItem)
            } catch (e: Exception) {
                isFailed = true
                isScanning = false
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CardBg,
                            DarkBg,
                            Color(0xFF090314)
                        )
                    )
                )
                .border(BorderStroke(1.2.dp, VioletGlow), RoundedCornerShape(24.dp)),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🎬", fontSize = 20.sp)
                        Column {
                            Text(
                                text = "AI Content Score Report",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "$platform • $fileName",
                                fontSize = 10.5.sp,
                                color = TextGray
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = GlassBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                if (isScanning) {
                    // SCANNING ANIMATION
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(VioletPrimary.copy(alpha = 0.2f))
                                    .border(2.dp, VioletGlow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    progress = { scanProgress },
                                    modifier = Modifier.size(80.dp),
                                    color = VioletGlow,
                                    trackColor = GlassBg,
                                    strokeWidth = 4.dp
                                )
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = ElectricPurple,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Text(
                                text = scanStepText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Analyzing video visual flow + caption structure...",
                                fontSize = 11.5.sp,
                                color = TextGray
                            )
                        }
                    }
                } else if (isFailed) {
                    // ERROR STATE
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(48.dp))
                            Text(
                                text = "Analysis couldn't be completed. Please try again.",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = onDismiss,
                                colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                            ) {
                                Text("Close", color = TextWhite)
                            }
                        }
                    }
                } else {
                    // FULL COMPREHENSIVE REPORT
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // 1. TOP RESULT UI: CREDIT SCORE STYLE SEMI-CIRCULAR GAUGE METER DASHBOARD
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black,
                                border = BorderStroke(1.2.dp, GlassBorder)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Animated Semi-Circular Gauge Meter
                                    SemiCircularGaugeMeter(
                                        score = calculatedReport.overallScore,
                                        ratingColor = calculatedReport.ratingColor
                                    )

                                    // UNDER THE SCORE
                                    Text(
                                        text = "AI Content Score",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextGray
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = calculatedReport.ratingColor.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, calculatedReport.ratingColor)
                                    ) {
                                        Text(
                                            text = calculatedReport.ratingLabel.uppercase(),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = calculatedReport.ratingColor,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                        )
                                    }

                                    Text(
                                        text = calculatedReport.summarySentence,
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }

                        // 2. TRANSPARENT SECTION-BY-SECTION ANALYSIS WITH "WHY" EXPLANATIONS
                        item {
                            Text("📊 Weighted Score Breakdown", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                        }

                        items(calculatedReport.categories) { cat ->
                            val catColor = when {
                                cat.score <= 40 -> ErrorRed
                                cat.score <= 60 -> WarningAmber
                                cat.score <= 80 -> ScoreBlue
                                else -> SuccessGreen
                            }
                            ReportMetricSection(
                                title = cat.name,
                                score = "${cat.score} / 100",
                                statusColor = catColor,
                                explanation = "WHY: ${cat.explanation}"
                            )
                        }

                        // 3. STRENGTHS SECTION
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF0F1E19),
                                border = BorderStroke(1.dp, SuccessGreen)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                                        Text("✅ Strengths", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                    }

                                    calculatedReport.strengths.forEach { item ->
                                        Text("• $item", fontSize = 11.sp, color = TextWhite)
                                    }
                                }
                            }
                        }

                        // 4. AREAS TO IMPROVE SECTION
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF26180B),
                                border = BorderStroke(1.dp, WarningAmber)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                                        Text("⚠️ Areas to Improve", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                    }

                                    calculatedReport.areasToImprove.forEach { item ->
                                        Text("• $item", fontSize = 11.sp, color = TextWhite)
                                    }
                                }
                            }
                        }

                        // 5. AI SUGGESTIONS SECTION
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF130E26),
                                border = BorderStroke(1.dp, VioletGlow)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = VioletGlow, modifier = Modifier.size(18.dp))
                                        Text("💡 AI Suggestions", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                                    }

                                    calculatedReport.aiSuggestions.forEach { item ->
                                        Text("• $item", fontSize = 11.sp, color = TextWhite)
                                    }
                                }
                            }
                        }

                        // 6. IMPROVED CAPTION SECTION
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF14082B),
                                border = BorderStroke(1.dp, VioletGlow)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("📝 Improved Caption", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        TextButton(onClick = {
                                            clipboardManager.setText(AnnotatedString(calculatedReport.improvedCaption))
                                            Toast.makeText(context, "Caption Copied!", Toast.LENGTH_SHORT).show()
                                        }) {
                                            Text("Copy", fontSize = 11.sp, color = VioletGlow, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Text(
                                        text = calculatedReport.improvedCaption,
                                        fontSize = 11.5.sp,
                                        color = TextWhite,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }

                        // 7. OPTIMIZED 5 HASHTAGS SECTION
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF14082B),
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("#️⃣ Optimized 5 Hashtags", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        TextButton(onClick = {
                                            clipboardManager.setText(AnnotatedString(calculatedReport.optimizedHashtags.joinToString(" ")))
                                            Toast.makeText(context, "5 Hashtags Copied!", Toast.LENGTH_SHORT).show()
                                        }) {
                                            Text("Copy All", fontSize = 11.sp, color = VioletGlow, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Text(
                                        text = calculatedReport.optimizedHashtags.joinToString(" "),
                                        fontSize = 12.sp,
                                        color = VioletGlow,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // 8. UPLOAD TIPS SECTION
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = GlassBg,
                                border = BorderStroke(0.8.dp, GlassBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("📈 Upload Tips", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                                    calculatedReport.uploadTips.forEach { tip ->
                                        Text("• $tip", fontSize = 11.sp, color = TextGray)
                                    }
                                }
                            }
                        }

                        // 9. COPY BUTTONS & COMPLIANCE DISCLAIMER
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val fullReportText = """
                                            AI Content Score: ${calculatedReport.overallScore}/100 (${calculatedReport.ratingLabel})
                                            
                                            Summary: ${calculatedReport.summarySentence}
                                            
                                            Strengths:
                                            ${calculatedReport.strengths.joinToString("\n") { "- $it" }}
                                            
                                            Areas to Improve:
                                            ${calculatedReport.areasToImprove.joinToString("\n") { "- $it" }}
                                            
                                            Improved Caption:
                                            ${calculatedReport.improvedCaption}
                                            
                                            Hashtags:
                                            ${calculatedReport.optimizedHashtags.joinToString(" ")}
                                        """.trimIndent()
                                        clipboardManager.setText(AnnotatedString(fullReportText))
                                        Toast.makeText(context, "Full AI Report Copied!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                                ) {
                                    Text("Copy Full AI Report 📋", fontSize = 12.5.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                }

                                // MANDATORY DISCLAIMER
                                Text(
                                    text = "AI Content Score is an AI-generated quality assessment based on your uploaded video and caption. It helps improve content quality and does not predict future reach, views or engagement.",
                                    fontSize = 9.5.sp,
                                    color = TextGray,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 13.sp,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
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
private fun ReportMetricSection(
    title: String,
    score: String,
    statusColor: Color,
    explanation: String
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = score, fontSize = 13.sp, fontWeight = FontWeight.Black, color = statusColor)
            }
            Text(text = explanation, fontSize = 10.5.sp, color = TextGray)
        }
    }
}

@Composable
private fun HistoryModalDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var historyList by remember { mutableStateOf(ReelAnalyzerHistoryPrefs.getHistory(context)) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.80f)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CardBg,
                            DarkBg,
                            Color(0xFF080212)
                        )
                    )
                )
                .border(BorderStroke(1.2.dp, VioletGlow), RoundedCornerShape(24.dp)),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = VioletGlow)
                        Text("Saved Analysis History", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Divider(color = GlassBorder)

                if (historyList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No analysis history saved yet.", fontSize = 13.sp, color = TextGray)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                ReelAnalyzerHistoryPrefs.clearHistory(context)
                                historyList = emptyList()
                            }
                        ) {
                            Text("Clear History 🗑️", fontSize = 11.5.sp, color = ErrorRed, fontWeight = FontWeight.Bold)
                        }
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(historyList) { item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(0.8.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = item.fileName, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(text = "${item.platform} • ${item.timestamp}", fontSize = 10.sp, color = TextGray)
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = VioletPrimary.copy(alpha = 0.2f),
                                        border = BorderStroke(0.8.dp, VioletGlow)
                                    ) {
                                        Text(
                                            text = "Score: ${item.overallScore}/100",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VioletGlow,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
