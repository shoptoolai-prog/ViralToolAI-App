@file:OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.media3.common.util.UnstableApi::class
)

package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.annotation.OptIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMicros
import coil.decode.VideoFrameDecoder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

// ============================================================================
// COLOR PALETTE & THEME CONSTANTS FOR DEDICATED MINT GREEN + BLACK EDITOR WORKSPACE
// ============================================================================
private val DarkBg = Color(0xFF050507) // Deep Black #050507
private val SurfaceCard = Color(0xFF141722) // Dark Graphite
private val SurfaceCardLight = Color(0xFF1C2030)
private val SoftViolet = Color(0xFF10B981) // Mapped to Mint Green for Editor
private val PrimaryViolet = Color(0xFF10B981) // Premium Mint Green #10B981
private val ElectricPurple = Color(0xFF00F59B) // Soft Mint Glow #00F59B
private val DarkPurpleGrad = Color(0xFF059669) // Dark Mint Green
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFF94A3B8)
private val GreenSaveDot = Color(0xFF10B981)
private val PrimaryGradient = Brush.horizontalGradient(listOf(PrimaryViolet, ElectricPurple))

// Dedicated Editor Mint Colors
private val MintPrimary = Color(0xFF10B981)
private val MintGlow = Color(0xFF00F59B)
private val MintDark = Color(0xFF059669)
private val MintSurface = Color(0xFF0B1F17)

// ============================================================================
// DATA MODELS
// ============================================================================

data class TimelineClip(
    val id: String,
    val mediaUri: Uri?,
    val title: String,
    val originalDurationSec: Double,
    var startTrimSec: Double = 0.0,
    var endTrimSec: Double = originalDurationSec,
    var speed: Float = 1.0f,
    var volume: Float = 1.0f,
    var isMuted: Boolean = false,
    var opacity: Float = 1.0f,
    var rotation: Float = 0f,
    var isFlippedHorizontal: Boolean = false,
    var isFlippedVertical: Boolean = false,
    var isReversed: Boolean = false,
    var fadeInSec: Float = 0f,
    var fadeOutSec: Float = 0f,
    var brightness: Float = 0f,
    var contrast: Float = 1f,
    var saturation: Float = 1f,
    var highlights: Float = 0f,
    var shadows: Float = 0f,
    var sharpen: Float = 0f,
    var temperature: Float = 0f,
    var tint: Float = 0f,
    var fade: Float = 0f,
    var exposure: Float = 0f,
    var gamma: Float = 1f,
    var vignette: Float = 0f,
    var grain: Float = 0f,
    var retouchSmooth: Float = 0f,
    var cropRatio: String = "Original",
    val resolutionLabel: String = "1080p",
    var isLocked: Boolean = false,
    var groupId: String? = null,
    var filterName: String = "none",
    var filterIntensity: Float = 1.0f,
    var effectName: String = "e_none",
    var effectIntensity: Float = 0.8f,
    var effectDuration: Float = 3.0f,
    var effectBlend: Float = 0.5f,
    var effectOpacity: Float = 1.0f,
    var keyframes: List<com.example.ui.components.KeyframePoint> = emptyList(),
    var entryAnimationId: String? = null,
    var exitAnimationId: String? = null,
    var loopAnimationId: String? = null,
    var offsetX: Float = 0f,
    var offsetY: Float = 0f,
    var scale: Float = 1.0f,
    var blendModeName: String = "Normal",
    var shadowRadius: Float = 0f,
    var borderWidth: Float = 0f,
    var borderColorHex: String = "#10B981",
    var cornerRadiusDp: Float = 0f,
    var cropTopPercent: Float = 0f,
    var cropBottomPercent: Float = 0f,
    var cropLeftPercent: Float = 0f,
    var cropRightPercent: Float = 0f,
    var hasMotionTracking: Boolean = false,
    var isStabilized: Boolean = false,
    var isAutoReframed: Boolean = false,
    var isEnhancedQuality: Boolean = false,
    var isHdrEnhanced: Boolean = false,
    var isBgRemoved: Boolean = false,
    var maskType: String = "None",
    var curveSpeedPreset: String = "Standard",
    var voiceIsolation: Boolean = false,
    var noiseReduction: Boolean = false,
    var voiceEnhance: Boolean = false,
    var audioEffect: String = "None"
) {
    val durationSec: Double
        get() = (((endTrimSec - startTrimSec) / speed)).coerceAtLeast(0.1)

    fun copyClip(): TimelineClip = copy()
}

enum class EditorPanelMode {
    MAIN_TOOLBAR,
    CLIP_EDIT_TOOLBAR,
    ADJUST,
    FILTERS,
    EFFECTS,
    ANIMATION,
    SPEED,
    VOLUME,
    OPACITY,
    TRANSFORM,
    CROP,
    MASK,
    RETOUCH
}

data class AudioTrackItem(
    val id: String,
    val title: String,
    val startSec: Double,
    val durationSec: Double,
    val volume: Float = 0.8f,
    val isMuted: Boolean = false,
    val fadeInSec: Float = 0.0f,
    val fadeOutSec: Float = 0.0f,
    val balance: Float = 0.0f,
    val speed: Float = 1.0f,
    val pitchLock: Boolean = true,
    val pitchSemitones: Float = 0.0f,
    val bassDb: Float = 0.0f,
    val trebleDb: Float = 0.0f,
    val echoLevel: Float = 0.0f,
    val isLimiterEnabled: Boolean = false,
    val isNormalized: Boolean = false,
    val voiceEnhanceEnabled: Boolean = false,
    val voiceEffect: String = "v_none",
    val voiceEffectIntensity: Float = 0.8f,
    val noiseReductionEnabled: Boolean = false,
    val noiseReductionLevel: Float = 0.5f,
    val isDuckingEnabled: Boolean = false,
    val duckingLevel: Float = 0.4f,
    val audioUri: Uri? = null,
    val category: String = "Imported"
)

data class TextTrackItem(
    val id: String,
    val text: String,
    val startSec: Double,
    val durationSec: Double,
    val textColorHex: String = "#FFFFFF",
    val styleName: String = "Bold Modern",
    val fontSizeSp: Float = 22f,
    val isBold: Boolean = true,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val alignment: String = "Center",
    val opacity: Float = 1.0f,
    val letterSpacingSp: Float = 0f,
    val lineHeightSp: Float = 26f,
    val strokeColorHex: String = "#000000",
    val strokeWidthDp: Float = 0f,
    val bgColorHex: String = "#00000000",
    val bgRadiusDp: Float = 6f,
    val shadowColorHex: String = "#80000000",
    val shadowBlurDp: Float = 0f,
    val glowColorHex: String = "#00F59B",
    val glowRadiusDp: Float = 0f,
    val isGradient: Boolean = false,
    val gradientSecondaryHex: String = "#00F59B",
    val entryAnimation: String = "Fade",
    val exitAnimation: String = "Fade",
    val loopAnimation: String = "None",
    val categoryType: String = "Custom",
    val positionX: Float = 0f,
    val positionY: Float = 0f,
    val scale: Float = 1.0f,
    val rotation: Float = 0f,
    val isLocked: Boolean = false,
    val isHidden: Boolean = false
)

data class StickerTrackItem(
    val id: String,
    val stickerEmoji: String,
    val startSec: Double,
    val durationSec: Double,
    val category: String = "Emoji",
    val label: String = "Sticker",
    val positionX: Float = 0f,
    val positionY: Float = 0f,
    val scale: Float = 1.0f,
    val rotation: Float = 0f,
    val opacity: Float = 1.0f,
    val isLocked: Boolean = false,
    val isHidden: Boolean = false
)

data class DrawingTrackItem(
    val id: String,
    val label: String = "Drawing",
    val startSec: Double,
    val durationSec: Double,
    val strokeColorHex: String = "#00F59B",
    val strokeWidthDp: Float = 4f,
    val opacity: Float = 1.0f,
    val toolType: String = "Brush",
    val isLocked: Boolean = false,
    val isHidden: Boolean = false,
    val points: List<Pair<Float, Float>> = emptyList()
)

data class EffectTrackItem(
    val id: String,
    val name: String,
    val effectType: String,
    val startSec: Double,
    val durationSec: Double,
    val colorHex: String = "#8B5CF6"
)

data class ClipTransition(
    val id: String,
    val fromClipId: String,
    val toClipId: String,
    val transitionType: String = "t_cross_fade",
    val durationSec: Double = 0.5,
    val direction: String = "Right",
    val curve: String = "EaseInOut",
    val intensity: Float = 1.0f,
    val isReversed: Boolean = false
)

enum class EditorActiveTool {
    NONE,
    EDIT,
    AUDIO,
    TEXT,
    EFFECTS,
    FILTERS,
    OVERLAY,
    CANVAS,
    AI,
    MORE
}

enum class CanvasBgMode {
    AMOLED_BLACK,
    STUDIO_WHITE,
    DARK_INDIGO,
    CHECKERBOARD,
    BLUR_GRADIENT
}

enum class ActiveBottomSheet {
    NONE,
    RESOLUTION_PICKER,
    SPEED_ADJUST,
    CURVE_SPEED,
    VOLUME_ADJUST,
    VOLUME_FADE_ADJUST,
    OPACITY_ADJUST,
    TRANSFORM_PANEL,
    CROP_ASPECT,
    TRIM_EDITOR,
    ROTATE_FLIP,
    MASK_SELECTION,
    RETOUCH_PANEL,
    AUDIO_SELECTION,
    AUDIO_EFFECTS,
    AI_TRANSLATOR,
    COLOR_ADJUST,
    STICKER_PICKER,
    TEMPLATES_CATALOG,
    TEXT_EDITOR,
    EFFECTS_CATALOG,
    FILTERS_CATALOG,
    CANVAS_SETTING,
    EXPORT_MODAL,
    UNSUPPORTED_FORMAT_DIALOG,
    TRANSITION_LIBRARY,
    ANIMATIONS_KEYFRAMES,
    ADD_MEDIA_PICKER,
    CLIP_ACTION_MENU,
    EDIT_PANEL
}

// ============================================================================
// MAIN REDESIGNED VIRALTOOLAI VIDEO EDITOR WORKSPACE (PHASE 9)
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun VideoEditingScreen(
    projectConfig: ProjectSetupConfig? = null,
    onNavigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // ------------------------------------------------------------------------
    // 1. INITIALIZE PROJECT & TIMELINE CLIPS STATE
    // ------------------------------------------------------------------------
    var projectName by remember {
        mutableStateOf(projectConfig?.projectName ?: "Viral Reel Project #${(100..999).random()}")
    }

    var selectedAspectRatio by remember {
        mutableStateOf(projectConfig?.aspectRatio ?: "9:16")
    }

    var selectedResolution by remember {
        mutableStateOf(projectConfig?.resolution ?: "1080p")
    }

    var selectedFps by remember {
        mutableStateOf(projectConfig?.fps ?: "30 FPS")
    }

    // Main Timeline Clips State
    val initialClips = remember(projectConfig) {
        if (projectConfig != null && projectConfig.selectedMedia.isNotEmpty()) {
            projectConfig.selectedMedia.mapIndexed { idx, item ->
                val itemDur = if (item.durationSeconds > 0) item.durationSeconds.toDouble() else {
                    try {
                        val uri = item.uri
                        if (uri != null && uri != Uri.EMPTY) {
                            val retriever = android.media.MediaMetadataRetriever()
                            try {
                                retriever.setDataSource(context, uri)
                                val durMs = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)?.toDoubleOrNull() ?: 15000.0
                                (durMs / 1000.0).coerceAtLeast(1.0)
                            } finally {
                                try { retriever.release() } catch (_: Throwable) {}
                            }
                        } else {
                            15.0
                        }
                    } catch (e: Throwable) {
                        15.0
                    }
                }
                TimelineClip(
                    id = "clip_${idx}_${System.currentTimeMillis()}",
                    mediaUri = item.uri,
                    title = item.title,
                    originalDurationSec = itemDur,
                    endTrimSec = itemDur,
                    resolutionLabel = item.resolutionLabel
                )
            }
        } else {
            val prefs = context.getSharedPreferences("viraltool_editor_draft", Context.MODE_PRIVATE)
            val draftTitle = prefs.getString("draft_project_name", "Viral Reel Draft") ?: "Viral Reel Draft"
            projectName = draftTitle
            listOf(
                TimelineClip(
                    id = "draft_clip_1",
                    mediaUri = null,
                    title = "Main Reel Shot",
                    originalDurationSec = 15.0,
                    endTrimSec = 15.0
                )
            )
        }
    }

    var clips by remember { mutableStateOf(initialClips) }
    var selectedClipId by remember { mutableStateOf<String?>(clips.firstOrNull()?.id) }

    // Multi-Select Mode State
    var isMultiSelectMode by remember { mutableStateOf(false) }
    var selectedClipIds by remember { mutableStateOf<Set<String>>(emptySet()) }

    // Secondary Tracks State (Clean & Empty by default)
    var audioTracks by remember { mutableStateOf<List<AudioTrackItem>>(emptyList()) }
    var textTracks by remember { mutableStateOf<List<TextTrackItem>>(emptyList()) }
    var stickerTracks by remember { mutableStateOf<List<StickerTrackItem>>(emptyList()) }
    var effectTracks by remember { mutableStateOf<List<EffectTrackItem>>(emptyList()) }
    var drawingTracks by remember { mutableStateOf<List<DrawingTrackItem>>(emptyList()) }

    var selectedTextTrackId by remember { mutableStateOf<String?>(null) }
    var selectedStickerTrackId by remember { mutableStateOf<String?>(null) }

    val activeTextTrack = remember(textTracks, selectedTextTrackId) {
        textTracks.find { it.id == selectedTextTrackId } ?: textTracks.firstOrNull()
    }
    val activeStickerTrack = remember(stickerTracks, selectedStickerTrackId) {
        stickerTracks.find { it.id == selectedStickerTrackId } ?: stickerTracks.firstOrNull()
    }

    // Transitions State
    var clipTransitions by remember {
        mutableStateOf<List<ClipTransition>>(emptyList())
    }
    var activeTransitionPair by remember { mutableStateOf<Pair<String, String>?>(null) }

    // Playback State
    var isPlaying by remember { mutableStateOf(false) }
    var currentPlayheadSec by remember { mutableStateOf(0.0) }
    var playbackSpeed by remember { mutableStateOf(1.0f) }
    var isLoopEnabled by remember { mutableStateOf(false) }
    var showInspectorPanel by remember { mutableStateOf(false) }

    var selectedAudioTrackId by remember { mutableStateOf<String?>(null) }
    val activeAudioTrack = remember(audioTracks, selectedAudioTrackId) {
        audioTracks.find { it.id == selectedAudioTrackId } ?: audioTracks.firstOrNull()
    }

    // Preview Toggles State
    var isSafeAreaEnabled by remember { mutableStateOf(false) }
    var isGridEnabled by remember { mutableStateOf(false) }
    var isFullscreenPreview by remember { mutableStateOf(false) }
    var isPreviewMuted by remember { mutableStateOf(false) }

    // Total Duration derived from clips
    val totalDurationSec = remember(clips) {
        clips.sumOf { it.durationSec }.coerceAtLeast(1.0)
    }

    // Active Selected Clip
    val activeClip = remember(clips, selectedClipId) {
        clips.find { it.id == selectedClipId } ?: clips.firstOrNull()
    }

    // Undo / Redo Engine
    val undoStack = remember { mutableStateListOf<List<TimelineClip>>() }
    val redoStack = remember { mutableStateListOf<List<TimelineClip>>() }

    fun pushUndoState() {
        undoStack.add(clips.map { it.copyClip() })
        redoStack.clear()
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.add(clips.map { it.copyClip() })
            val last = undoStack.removeAt(undoStack.lastIndex)
            clips = last
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.add(clips.map { it.copyClip() })
            val last = redoStack.removeAt(redoStack.lastIndex)
            clips = last
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
        }
    }

    // Media Launchers
    val addMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            pushUndoState()
            val newClips = uris.mapIndexed { idx, uri ->
                val fileName = uri.lastPathSegment ?: "Imported Clip ${idx + 1}"
                val durSec = try {
                    val retriever = android.media.MediaMetadataRetriever()
                    retriever.setDataSource(context, uri)
                    val durMs = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)?.toDoubleOrNull() ?: 15000.0
                    retriever.release()
                    (durMs / 1000.0).coerceAtLeast(1.0)
                } catch (e: Exception) {
                    15.0
                }
                TimelineClip(
                    id = "imported_${idx}_${System.currentTimeMillis()}",
                    mediaUri = uri,
                    title = fileName,
                    originalDurationSec = durSec,
                    endTrimSec = durSec
                )
            }
            clips = clips.filterNot { it.mediaUri == null } + newClips
            selectedClipId = newClips.firstOrNull()?.id
            Toast.makeText(context, "Added ${uris.size} clip(s) to timeline", Toast.LENGTH_SHORT).show()
        }
    }

    // Preview Canvas Display State
    var canvasBgMode by remember { mutableStateOf(CanvasBgMode.AMOLED_BLACK) }
    var previewScale by remember { mutableStateOf(1.0f) }
    var isPreviewFilled by remember { mutableStateOf(false) }

    // Timeline Scale & Snapping (Phase 9 Zoom support)
    var timelineZoomPxPerSec by remember { mutableStateOf(24f) }
    var enableSnapToGrid by remember { mutableStateOf(true) }

    // Active Tool and Sheet States
    var activeMainTool by remember { mutableStateOf(EditorActiveTool.NONE) }
    var activeBottomSheet by remember { mutableStateOf(ActiveBottomSheet.NONE) }
    var activePanelMode by remember { mutableStateOf(EditorPanelMode.MAIN_TOOLBAR) }
    var showResolutionDropdown by remember { mutableStateOf(false) }

    // Phase 10 Live Preview Engine & Compare Mode State
    var tempPreviewFilter by remember { mutableStateOf<com.example.ui.components.FilterItem?>(null) }
    var tempPreviewFilterIntensity by remember { mutableFloatStateOf(1.0f) }

    var tempPreviewEffect by remember { mutableStateOf<com.example.ui.components.EffectItem?>(null) }
    var tempPreviewEffectIntensity by remember { mutableFloatStateOf(0.8f) }
    var tempPreviewEffectDuration by remember { mutableFloatStateOf(3.0f) }
    var tempPreviewEffectBlend by remember { mutableFloatStateOf(0.5f) }
    var tempPreviewEffectOpacity by remember { mutableFloatStateOf(1.0f) }

    var isCompareHolding by remember { mutableStateOf(false) }

    // Phase 11 Transition Engine State
    var tempPreviewTransitionConfig by remember { mutableStateOf<com.example.ui.components.TransitionConfig?>(null) }

    // Playback Coroutine Loop (Fallback for draft canvas without video file)
    LaunchedEffect(isPlaying, playbackSpeed, totalDurationSec, isLoopEnabled, activeClip?.mediaUri) {
        if (isPlaying && activeClip?.mediaUri == null) {
            val stepMs = 33L
            val incrementSec = (stepMs / 1000.0) * playbackSpeed
            while (isPlaying) {
                delay(stepMs)
                var nextSec = currentPlayheadSec + incrementSec

                if (enableSnapToGrid) {
                    var accumulatedTime = 0.0
                    for (c in clips) {
                        accumulatedTime += c.durationSec
                        if (kotlin.math.abs(nextSec - accumulatedTime) < 0.15) {
                            nextSec = accumulatedTime
                            break
                        }
                    }
                }

                currentPlayheadSec = nextSec
                if (currentPlayheadSec >= totalDurationSec) {
                    currentPlayheadSec = 0.0
                    if (!isLoopEnabled) {
                        isPlaying = false
                    }
                }
            }
        }
    }

    // Auto-Save Draft (Phase 9 Requirement)
    LaunchedEffect(clips, projectName, selectedAspectRatio, currentPlayheadSec) {
        val prefs = context.getSharedPreferences("viraltool_editor_draft", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("draft_project_name", projectName)
            putString("draft_aspect_ratio", selectedAspectRatio)
            putFloat("draft_playhead_sec", currentPlayheadSec.toFloat())
            putInt("draft_clip_count", clips.size)
            putLong("draft_last_saved", System.currentTimeMillis())
            apply()
        }
    }

    // ROOT LAYOUT CONTAINER
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        if (isLandscape) {
            // LANDSCAPE LAYOUT (Side-by-side)
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Column: Preview + Overlay Controls (50% width)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(DarkBg)
                ) {
                    TopBarHeader(
                        projectName = projectName,
                        selectedResolution = selectedResolution,
                        showResolutionDropdown = showResolutionDropdown,
                        onToggleResolutionDropdown = { showResolutionDropdown = it },
                        onSelectResolution = { selectedResolution = it },
                        onBack = onNavigateToHome,
                        onExport = { activeBottomSheet = ActiveBottomSheet.EXPORT_MODAL }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        PreviewCanvasArea(
                            selectedAspectRatio = selectedAspectRatio,
                            canvasBgMode = canvasBgMode,
                            previewScale = previewScale,
                            onUpdatePreviewScale = { previewScale = it },
                            isPreviewFilled = isPreviewFilled,
                            onTogglePreviewFilled = { isPreviewFilled = !isPreviewFilled },
                            activeClip = activeClip,
                            selectedClipId = selectedClipId,
                            textTracks = textTracks,
                            stickerTracks = stickerTracks,
                            drawingTracks = drawingTracks,
                            currentPlayheadSec = currentPlayheadSec,
                            isPlaying = isPlaying,
                            isSafeAreaEnabled = isSafeAreaEnabled,
                            isGridEnabled = isGridEnabled,
                            isPreviewMuted = isPreviewMuted,
                            tempPreviewFilter = tempPreviewFilter,
                            tempPreviewEffect = tempPreviewEffect,
                            isCompareHolding = isCompareHolding,
                            onToggleSafeArea = { isSafeAreaEnabled = !isSafeAreaEnabled },
                            onToggleGrid = { isGridEnabled = !isGridEnabled },
                            onToggleMute = { isPreviewMuted = !isPreviewMuted },
                            onToggleFullscreen = { isFullscreenPreview = !isFullscreenPreview },
                            onOpenInspector = { showInspectorPanel = true },
                            onUpdateTransform = { panX, panY, scale, rot ->
                                activeClip?.let { c ->
                                    val updated = c.copy(offsetX = panX, offsetY = panY, scale = scale, rotation = rot)
                                    clips = clips.map { if (it.id == c.id) updated else it }
                                }
                            },
                            onRotateClip = {
                                activeClip?.let { c ->
                                    val updated = c.copy(rotation = (c.rotation + 90f) % 360f)
                                    clips = clips.map { if (it.id == c.id) updated else it }
                                }
                            },
                            onMirrorClip = {
                                activeClip?.let { c ->
                                    val updated = c.copy(isFlippedHorizontal = !c.isFlippedHorizontal)
                                    clips = clips.map { if (it.id == c.id) updated else it }
                                }
                            },
                            playbackSpeed = playbackSpeed,
                            onPlayheadUpdate = { newTime -> currentPlayheadSec = newTime },
                            onPlaybackEnded = {
                                isPlaying = false
                                if (isLoopEnabled) {
                                    currentPlayheadSec = 0.0
                                    isPlaying = true
                                }
                            }
                        )
                    }
                }

                // Right Column: Controls + Timeline + Toolbar (50% width)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(DarkBg)
                ) {
                    ControlBar(
                        currentPlayheadSec = currentPlayheadSec,
                        totalDurationSec = totalDurationSec,
                        isPlaying = isPlaying,
                        playbackSpeed = playbackSpeed,
                        isLoopEnabled = isLoopEnabled,
                        undoEnabled = undoStack.isNotEmpty(),
                        redoEnabled = redoStack.isNotEmpty(),
                        onSeek = { currentPlayheadSec = it },
                        onUndo = { undo() },
                        onRedo = { redo() },
                        onPrevFrame = { currentPlayheadSec = (currentPlayheadSec - 0.033).coerceAtLeast(0.0) },
                        onNextFrame = { currentPlayheadSec = (currentPlayheadSec + 0.033).coerceAtMost(totalDurationSec) },
                        onJumpStart = { currentPlayheadSec = 0.0 },
                        onJumpEnd = { currentPlayheadSec = totalDurationSec },
                        onTogglePlay = {
                            isPlaying = !isPlaying
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        },
                        onToggleLoop = { isLoopEnabled = !isLoopEnabled },
                        onOpenSpeedSheet = { activeBottomSheet = ActiveBottomSheet.SPEED_ADJUST }
                    )

                    // Phase 9 Compact Mini Toolbar (Icons Only) Above Timeline
                    CapCutMiniIconToolbar(
                        undoEnabled = undoStack.isNotEmpty(),
                        redoEnabled = redoStack.isNotEmpty(),
                        enableSnapToGrid = enableSnapToGrid,
                        isPreviewMuted = isPreviewMuted,
                        isMultiSelectMode = isMultiSelectMode,
                        currentPlayheadSec = currentPlayheadSec,
                        totalDurationSec = totalDurationSec,
                        onUndo = { undo() },
                        onRedo = { redo() },
                        onZoomIn = { timelineZoomPxPerSec = (timelineZoomPxPerSec + 8f).coerceAtMost(120f) },
                        onZoomOut = { timelineZoomPxPerSec = (timelineZoomPxPerSec - 8f).coerceAtLeast(10f) },
                        onToggleSnap = { enableSnapToGrid = !enableSnapToGrid },
                        onToggleMute = { isPreviewMuted = !isPreviewMuted },
                        onToggleMultiSelect = {
                            isMultiSelectMode = !isMultiSelectMode
                            if (!isMultiSelectMode) selectedClipIds = emptySet()
                        }
                    )

                    // Phase 9 Timeline Multi-Track Area (Compact ~30% smaller)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Phase9CompactTimeline(
                            clips = clips,
                            selectedClipId = selectedClipId,
                            isMultiSelectMode = isMultiSelectMode,
                            selectedClipIds = selectedClipIds,
                            audioTracks = audioTracks,
                            selectedAudioTrackId = selectedAudioTrackId,
                            textTracks = textTracks,
                            stickerTracks = stickerTracks,
                            effectTracks = effectTracks,
                            clipTransitions = clipTransitions,
                            currentPlayheadSec = currentPlayheadSec,
                            totalDurationSec = totalDurationSec,
                            timelineZoomPxPerSec = timelineZoomPxPerSec,
                            onUpdateZoom = { timelineZoomPxPerSec = it.coerceIn(10f, 120f) },
                            onSelectClip = { clipId ->
                                if (isMultiSelectMode) {
                                    selectedClipIds = if (selectedClipIds.contains(clipId)) selectedClipIds - clipId else selectedClipIds + clipId
                                } else {
                                    selectedClipId = clipId
                                    activeMainTool = EditorActiveTool.EDIT
                                }
                            },
                            onTrimClip = { cId, sTrim, eTrim ->
                                pushUndoState()
                                clips = clips.map { c ->
                                    if (c.id == cId) c.copy(startTrimSec = sTrim, endTrimSec = eTrim) else c
                                }
                            },
                            onSelectAudioTrack = { audId ->
                                selectedAudioTrackId = audId
                                activeBottomSheet = ActiveBottomSheet.AUDIO_SELECTION
                            },
                            onAddAudioTrack = {
                                activeBottomSheet = ActiveBottomSheet.AUDIO_SELECTION
                            },
                            onLongClickClip = { clipId ->
                                selectedClipId = clipId
                                activeBottomSheet = ActiveBottomSheet.CLIP_ACTION_MENU
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            },
                            onAddMediaBetween = {
                                activeBottomSheet = ActiveBottomSheet.ADD_MEDIA_PICKER
                            },
                            onOpenTransition = { fromId, toId ->
                                activeTransitionPair = Pair(fromId, toId)
                                activeBottomSheet = ActiveBottomSheet.TRANSITION_LIBRARY
                            },
                            onSeek = { currentPlayheadSec = it }
                        )
                    }

                    // Bottom Toolbar
                    BottomToolbarRow(
                        activeMainTool = activeMainTool,
                        onSelectTool = { toolLabel ->
                            val matchingEnum = EditorActiveTool.entries.find { it.name.equals(toolLabel, ignoreCase = true) } ?: EditorActiveTool.NONE
                            activeMainTool = if (activeMainTool == matchingEnum) EditorActiveTool.NONE else matchingEnum

                            when (toolLabel) {
                                "Edit" -> {
                                    if (selectedClipId == null) {
                                        selectedClipId = clips.firstOrNull()?.id
                                    }
                                }
                                "Audio" -> activeBottomSheet = ActiveBottomSheet.AUDIO_SELECTION
                                "Text" -> activeBottomSheet = ActiveBottomSheet.TEXT_EDITOR
                                "Effects" -> activeBottomSheet = ActiveBottomSheet.EFFECTS_CATALOG
                                "Filters" -> activeBottomSheet = ActiveBottomSheet.FILTERS_CATALOG
                                "Canvas" -> activeBottomSheet = ActiveBottomSheet.CANVAS_SETTING
                                "AI" -> Toast.makeText(context, "ViralAI Studio Active", Toast.LENGTH_SHORT).show()
                                else -> Toast.makeText(context, "$toolLabel tool opened", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        } else {
            // PORTRAIT LAYOUT (Standard Vertical Mobile Flow)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBg)
            ) {
                // TOP BAR: ← Back | Project Name • Save Dot | 1080P ▼ | Export
                TopBarHeader(
                    projectName = projectName,
                    selectedResolution = selectedResolution,
                    showResolutionDropdown = showResolutionDropdown,
                    onToggleResolutionDropdown = { showResolutionDropdown = it },
                    onSelectResolution = { selectedResolution = it },
                    onBack = onNavigateToHome,
                    onExport = { activeBottomSheet = ActiveBottomSheet.EXPORT_MODAL }
                )

                // PREVIEW CANVAS AREA (Occupies ~48% height)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.48f)
                        .background(DarkBg)
                ) {
                    PreviewCanvasArea(
                        selectedAspectRatio = selectedAspectRatio,
                        canvasBgMode = canvasBgMode,
                        previewScale = previewScale,
                        onUpdatePreviewScale = { previewScale = it },
                        isPreviewFilled = isPreviewFilled,
                        onTogglePreviewFilled = { isPreviewFilled = !isPreviewFilled },
                        activeClip = activeClip,
                        selectedClipId = selectedClipId,
                        textTracks = textTracks,
                        stickerTracks = stickerTracks,
                        drawingTracks = drawingTracks,
                        currentPlayheadSec = currentPlayheadSec,
                        isPlaying = isPlaying,
                        playbackSpeed = playbackSpeed,
                        isSafeAreaEnabled = isSafeAreaEnabled,
                        isGridEnabled = isGridEnabled,
                        isPreviewMuted = isPreviewMuted,
                        tempPreviewFilter = tempPreviewFilter,
                        tempPreviewEffect = tempPreviewEffect,
                        isCompareHolding = isCompareHolding,
                        onToggleSafeArea = { isSafeAreaEnabled = !isSafeAreaEnabled },
                        onToggleGrid = { isGridEnabled = !isGridEnabled },
                        onToggleMute = { isPreviewMuted = !isPreviewMuted },
                        onToggleFullscreen = { isFullscreenPreview = !isFullscreenPreview },
                        onOpenInspector = { showInspectorPanel = true },
                        onUpdateTransform = { panX, panY, scale, rot ->
                            activeClip?.let { c ->
                                val updated = c.copy(offsetX = panX, offsetY = panY, scale = scale, rotation = rot)
                                clips = clips.map { if (it.id == c.id) updated else it }
                            }
                        },
                        onRotateClip = {
                            activeClip?.let { c ->
                                val updated = c.copy(rotation = (c.rotation + 90f) % 360f)
                                clips = clips.map { if (it.id == c.id) updated else it }
                            }
                        },
                        onMirrorClip = {
                            activeClip?.let { c ->
                                val updated = c.copy(isFlippedHorizontal = !c.isFlippedHorizontal)
                                clips = clips.map { if (it.id == c.id) updated else it }
                            }
                        },
                        onPlayheadUpdate = { newTime -> currentPlayheadSec = newTime },
                        onPlaybackEnded = {
                            isPlaying = false
                            if (isLoopEnabled) {
                                currentPlayheadSec = 0.0
                                isPlaying = true
                            }
                        }
                    )
                }

                // CONTROL BAR (Directly below preview)
                ControlBar(
                    currentPlayheadSec = currentPlayheadSec,
                    totalDurationSec = totalDurationSec,
                    isPlaying = isPlaying,
                    playbackSpeed = playbackSpeed,
                    isLoopEnabled = isLoopEnabled,
                    undoEnabled = undoStack.isNotEmpty(),
                    redoEnabled = redoStack.isNotEmpty(),
                    onSeek = { currentPlayheadSec = it },
                    onUndo = { undo() },
                    onRedo = { redo() },
                    onPrevFrame = { currentPlayheadSec = (currentPlayheadSec - 0.033).coerceAtLeast(0.0) },
                    onNextFrame = { currentPlayheadSec = (currentPlayheadSec + 0.033).coerceAtMost(totalDurationSec) },
                    onJumpStart = { currentPlayheadSec = 0.0 },
                    onJumpEnd = { currentPlayheadSec = totalDurationSec },
                    onTogglePlay = {
                        isPlaying = !isPlaying
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    },
                    onToggleLoop = { isLoopEnabled = !isLoopEnabled },
                    onOpenSpeedSheet = { activeBottomSheet = ActiveBottomSheet.SPEED_ADJUST }
                )

                // Phase 9 Premium Mini Icon Toolbar (Undo, Redo, Zoom, Magnet, Mute, Select - NO TEXT)
                CapCutMiniIconToolbar(
                    undoEnabled = undoStack.isNotEmpty(),
                    redoEnabled = redoStack.isNotEmpty(),
                    enableSnapToGrid = enableSnapToGrid,
                    isPreviewMuted = isPreviewMuted,
                    isMultiSelectMode = isMultiSelectMode,
                    currentPlayheadSec = currentPlayheadSec,
                    totalDurationSec = totalDurationSec,
                    onUndo = { undo() },
                    onRedo = { redo() },
                    onZoomIn = { timelineZoomPxPerSec = (timelineZoomPxPerSec + 8f).coerceAtMost(120f) },
                    onZoomOut = { timelineZoomPxPerSec = (timelineZoomPxPerSec - 8f).coerceAtLeast(10f) },
                    onToggleSnap = { enableSnapToGrid = !enableSnapToGrid },
                    onToggleMute = { isPreviewMuted = !isPreviewMuted },
                    onToggleMultiSelect = {
                        isMultiSelectMode = !isMultiSelectMode
                        if (!isMultiSelectMode) selectedClipIds = emptySet()
                    }
                )

                // PHASE 9 COMPACT TIMELINE MULTI-TRACK AREA (~30% height reduction: weight 0.28f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.28f)
                ) {
                    Phase9CompactTimeline(
                        clips = clips,
                        selectedClipId = selectedClipId,
                        isMultiSelectMode = isMultiSelectMode,
                        selectedClipIds = selectedClipIds,
                        audioTracks = audioTracks,
                        selectedAudioTrackId = selectedAudioTrackId,
                        textTracks = textTracks,
                        stickerTracks = stickerTracks,
                        effectTracks = effectTracks,
                        clipTransitions = clipTransitions,
                        currentPlayheadSec = currentPlayheadSec,
                        totalDurationSec = totalDurationSec,
                        timelineZoomPxPerSec = timelineZoomPxPerSec,
                        onUpdateZoom = { timelineZoomPxPerSec = it.coerceIn(10f, 120f) },
                        onSelectClip = { clipId ->
                            if (isMultiSelectMode) {
                                selectedClipIds = if (selectedClipIds.contains(clipId)) selectedClipIds - clipId else selectedClipIds + clipId
                            } else {
                                selectedClipId = if (clipId.isEmpty()) null else clipId
                                activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                            }
                        },
                        onTrimClip = { cId, sTrim, eTrim ->
                            pushUndoState()
                            clips = clips.map { c ->
                                if (c.id == cId) c.copy(startTrimSec = sTrim, endTrimSec = eTrim) else c
                            }
                        },
                        onSelectAudioTrack = { audId ->
                            selectedAudioTrackId = audId
                            activeBottomSheet = ActiveBottomSheet.AUDIO_SELECTION
                        },
                        onAddAudioTrack = {
                            activeBottomSheet = ActiveBottomSheet.AUDIO_SELECTION
                        },
                        onLongClickClip = { clipId ->
                            selectedClipId = clipId
                            activePanelMode = EditorPanelMode.CLIP_EDIT_TOOLBAR
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        },
                        onAddMediaBetween = {
                            activeBottomSheet = ActiveBottomSheet.ADD_MEDIA_PICKER
                        },
                        onOpenTransition = { fromId, toId ->
                            activeTransitionPair = Pair(fromId, toId)
                            activeBottomSheet = ActiveBottomSheet.TRANSITION_LIBRARY
                        },
                        onSeek = { currentPlayheadSec = it }
                    )
                }

                // DYNAMIC BOTTOM TOOLBAR & SLIDER PANELS AREA (CapCut Pro Morphing)
                // THE TIMELINE ABOVE REMAINS FULLY VISIBLE AT ALL TIMES
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color(0xFF0D0E15))
                ) {
                    AnimatedContent(
                        targetState = activePanelMode,
                        transitionSpec = {
                            (slideInVertically(
                                animationSpec = tween(220, easing = FastOutSlowInEasing),
                                initialOffsetY = { it }
                            ) + fadeIn()).togetherWith(
                                slideOutVertically(
                                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                                    targetOffsetY = { it }
                                ) + fadeOut()
                            )
                        },
                        label = "BottomPanelTransition"
                    ) { mode ->
                        when (mode) {
                            EditorPanelMode.MAIN_TOOLBAR -> {
                                BottomToolbarRow(
                                    activeMainTool = activeMainTool,
                                    onSelectTool = { toolLabel ->
                                        when (toolLabel) {
                                            "Edit" -> {
                                                if (selectedClipId == null) {
                                                    selectedClipId = clips.firstOrNull()?.id
                                                }
                                                activePanelMode = EditorPanelMode.CLIP_EDIT_TOOLBAR
                                            }
                                            "Audio" -> activeBottomSheet = ActiveBottomSheet.AUDIO_SELECTION
                                            "Text" -> activeBottomSheet = ActiveBottomSheet.TEXT_EDITOR
                                            "Effects" -> activePanelMode = EditorPanelMode.EFFECTS
                                            "Filters" -> activePanelMode = EditorPanelMode.FILTERS
                                            "Overlay" -> activeBottomSheet = ActiveBottomSheet.ADD_MEDIA_PICKER
                                            "Caption" -> {
                                                pushUndoState()
                                                val captionTrack = TextTrackItem(
                                                    id = "txt_auto_${System.currentTimeMillis()}",
                                                    text = "Auto-generated Viral Caption",
                                                    startSec = currentPlayheadSec,
                                                    durationSec = 4.0,
                                                    textColorHex = "#00F59B",
                                                    styleName = "Viral Caption Glow"
                                                )
                                                textTracks = textTracks + captionTrack
                                                Toast.makeText(context, "AI Caption track generated", Toast.LENGTH_SHORT).show()
                                            }
                                            "Adjust" -> activePanelMode = EditorPanelMode.ADJUST
                                            "Sticker" -> activeBottomSheet = ActiveBottomSheet.STICKER_PICKER
                                            "Canvas" -> activeBottomSheet = ActiveBottomSheet.CANVAS_SETTING
                                            "Templates" -> activeBottomSheet = ActiveBottomSheet.TEMPLATES_CATALOG
                                        }
                                    }
                                )
                            }
                            EditorPanelMode.CLIP_EDIT_TOOLBAR -> {
                                ClipEditToolbarRow(
                                    activeClip = activeClip,
                                    onToolClick = { tool ->
                                        when (tool) {
                                            "Split" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    val clipStartSec = clips.takeWhile { it.id != c.id }.sumOf { it.durationSec }
                                                    val localTime = (currentPlayheadSec - clipStartSec).coerceIn(0.1, c.durationSec - 0.1)
                                                    val splitTrim = c.startTrimSec + (localTime * c.speed)
                                                    val clip1 = c.copy(id = "${c.id}_a", endTrimSec = splitTrim)
                                                    val clip2 = c.copy(id = "${c.id}_b", startTrimSec = splitTrim)
                                                    val idx = clips.indexOfFirst { it.id == c.id }
                                                    if (idx != -1) {
                                                        val mutable = clips.toMutableList()
                                                        mutable.removeAt(idx)
                                                        mutable.add(idx, clip1)
                                                        mutable.add(idx + 1, clip2)
                                                        clips = mutable
                                                        selectedClipId = clip2.id
                                                        Toast.makeText(context, "Clip Split at ${formatTimecode(currentPlayheadSec)}", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                            "Trim" -> Toast.makeText(context, "Drag edge handles on timeline clip to trim", Toast.LENGTH_SHORT).show()
                                            "Speed" -> activePanelMode = EditorPanelMode.SPEED
                                            "Volume" -> activePanelMode = EditorPanelMode.VOLUME
                                            "Animation" -> activePanelMode = EditorPanelMode.ANIMATION
                                            "Effects" -> activePanelMode = EditorPanelMode.EFFECTS
                                            "Adjust" -> activePanelMode = EditorPanelMode.ADJUST
                                            "Filters" -> activePanelMode = EditorPanelMode.FILTERS
                                            "Opacity" -> activePanelMode = EditorPanelMode.OPACITY
                                            "Transform" -> activePanelMode = EditorPanelMode.TRANSFORM
                                            "Crop" -> activePanelMode = EditorPanelMode.TRANSFORM
                                            "Mask" -> activePanelMode = EditorPanelMode.MASK
                                            "Retouch" -> activePanelMode = EditorPanelMode.RETOUCH
                                            "Delete" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    clips = clips.filter { it.id != c.id }
                                                    selectedClipId = clips.firstOrNull()?.id
                                                    if (selectedClipId == null) activePanelMode = EditorPanelMode.MAIN_TOOLBAR
                                                    Toast.makeText(context, "Clip Deleted", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            "Duplicate" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    val dup = c.copy(id = "clip_${System.currentTimeMillis()}", title = "${c.title} (Copy)")
                                                    val idx = clips.indexOfFirst { it.id == c.id }
                                                    if (idx != -1) {
                                                        val mutable = clips.toMutableList()
                                                        mutable.add(idx + 1, dup)
                                                        clips = mutable
                                                        selectedClipId = dup.id
                                                        Toast.makeText(context, "Clip Duplicated", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                            "Replace" -> activeBottomSheet = ActiveBottomSheet.ADD_MEDIA_PICKER
                                            "Reverse" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    c.isReversed = !c.isReversed
                                                    Toast.makeText(context, if (c.isReversed) "Clip Reversed" else "Normal Direction", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            "Freeze" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    val freezeClip = c.copy(id = "freeze_${System.currentTimeMillis()}", title = "Freeze Frame", startTrimSec = 0.0, endTrimSec = 2.0)
                                                    val idx = clips.indexOfFirst { it.id == c.id }
                                                    if (idx != -1) {
                                                        val mutable = clips.toMutableList()
                                                        mutable.add(idx + 1, freezeClip)
                                                        clips = mutable
                                                        Toast.makeText(context, "2.0s Freeze Frame Inserted", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                            "Extract Audio" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    c.isMuted = true
                                                    val newAudio = AudioTrackItem(
                                                        id = "extracted_${System.currentTimeMillis()}",
                                                        title = "Extracted Audio (${c.title})",
                                                        startSec = clips.takeWhile { it.id != c.id }.sumOf { it.durationSec },
                                                        durationSec = c.durationSec,
                                                        volume = c.volume
                                                    )
                                                    audioTracks = audioTracks + newAudio
                                                    Toast.makeText(context, "Audio Extracted to Track", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            "Voice" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    c.voiceIsolation = !c.voiceIsolation
                                                    Toast.makeText(context, if (c.voiceIsolation) "Voice Isolation On" else "Voice Isolation Off", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            "Noise" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    c.noiseReduction = !c.noiseReduction
                                                    Toast.makeText(context, if (c.noiseReduction) "Denoise Active (-12dB)" else "Denoise Off", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            "Enhance" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    c.isEnhancedQuality = !c.isEnhancedQuality
                                                    Toast.makeText(context, if (c.isEnhancedQuality) "4K AI Super Resolution On" else "Standard Quality", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            "Remove BG" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    c.isBgRemoved = !c.isBgRemoved
                                                    Toast.makeText(context, if (c.isBgRemoved) "AI Cutout / BG Removed" else "Background Restored", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            "Stabilize" -> {
                                                pushUndoState()
                                                activeClip?.let { c ->
                                                    c.isStabilized = !c.isStabilized
                                                    Toast.makeText(context, if (c.isStabilized) "Optical Stabilization Active" else "Stabilization Off", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            "Translator" -> activeBottomSheet = ActiveBottomSheet.AI_TRANSLATOR
                                        }
                                    },
                                    onCloseEdit = {
                                        selectedClipId = null
                                        activePanelMode = EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.ADJUST -> {
                                AdjustPanelBottomSheet(
                                    activeClip = activeClip,
                                    onUpdateClip = { updated ->
                                        pushUndoState()
                                        clips = clips.map { if (it.id == updated.id) updated else it }
                                    },
                                    onApplyToAll = { templateClip ->
                                        pushUndoState()
                                        clips = clips.map { c ->
                                            c.copy(
                                                brightness = templateClip.brightness,
                                                contrast = templateClip.contrast,
                                                saturation = templateClip.saturation,
                                                highlights = templateClip.highlights,
                                                shadows = templateClip.shadows,
                                                sharpen = templateClip.sharpen,
                                                temperature = templateClip.temperature,
                                                tint = templateClip.tint,
                                                fade = templateClip.fade,
                                                exposure = templateClip.exposure,
                                                gamma = templateClip.gamma,
                                                vignette = templateClip.vignette,
                                                grain = templateClip.grain
                                            )
                                        }
                                        Toast.makeText(context, "Adjustments Applied To All Clips", Toast.LENGTH_SHORT).show()
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.FILTERS -> {
                                FilterPanelBottomSheet(
                                    activeClip = activeClip,
                                    onApplyFilter = { filterId, intensity ->
                                        pushUndoState()
                                        activeClip?.let { c ->
                                            c.filterName = filterId
                                            c.filterIntensity = intensity
                                        }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.EFFECTS -> {
                                EffectPanelBottomSheet(
                                    activeClip = activeClip,
                                    onApplyEffect = { effectId, intensity ->
                                        pushUndoState()
                                        activeClip?.let { c ->
                                            c.effectName = effectId
                                            c.effectIntensity = intensity
                                        }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.ANIMATION -> {
                                AnimationPanelBottomSheet(
                                    activeClip = activeClip,
                                    onApplyAnimation = { animId, type, duration ->
                                        pushUndoState()
                                        activeClip?.let { c ->
                                            when (type) {
                                                "IN" -> c.entryAnimationId = animId
                                                "OUT" -> c.exitAnimationId = animId
                                                "LOOP" -> c.loopAnimationId = animId
                                                else -> c.entryAnimationId = animId
                                            }
                                        }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.SPEED -> {
                                SpeedPanelBottomSheet(
                                    activeClip = activeClip,
                                    onUpdateSpeed = { speedVal ->
                                        pushUndoState()
                                        activeClip?.let { c ->
                                            c.speed = speedVal
                                        }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.VOLUME -> {
                                VolumePanelBottomSheet(
                                    activeClip = activeClip,
                                    onUpdateVolume = { volVal ->
                                        pushUndoState()
                                        activeClip?.let { c ->
                                            c.volume = volVal
                                        }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.OPACITY -> {
                                OpacityPanelBottomSheet(
                                    activeClip = activeClip,
                                    onUpdateOpacity = { opacityVal ->
                                        pushUndoState()
                                        activeClip?.let { c ->
                                            c.opacity = opacityVal
                                        }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.TRANSFORM, EditorPanelMode.CROP -> {
                                CropTransformPanelBottomSheet(
                                    activeClip = activeClip,
                                    onUpdateTransform = { updatedClip ->
                                        pushUndoState()
                                        clips = clips.map { if (it.id == updatedClip.id) updatedClip else it }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.MASK -> {
                                MaskPanelBottomSheet(
                                    activeClip = activeClip,
                                    onUpdateMask = { maskName ->
                                        pushUndoState()
                                        activeClip?.let { c -> c.maskType = maskName }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                            EditorPanelMode.RETOUCH -> {
                                RetouchPanelBottomSheet(
                                    activeClip = activeClip,
                                    onUpdateRetouch = { smoothVal ->
                                        pushUndoState()
                                        activeClip?.let { c -> c.retouchSmooth = smoothVal }
                                    },
                                    onClose = {
                                        activePanelMode = if (selectedClipId != null) EditorPanelMode.CLIP_EDIT_TOOLBAR else EditorPanelMode.MAIN_TOOLBAR
                                    }
                                )
                            }
                        }
                    }
                }

                // CAPCUT STYLE INSPECTOR PANEL OVERLAY
                AnimatedVisibility(
                    visible = showInspectorPanel,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    BottomInspectorPanel(
                        activeClip = activeClip,
                        selectedClipIds = selectedClipIds,
                        isMultiSelectMode = isMultiSelectMode,
                        onUpdateClip = { updated ->
                            clips = clips.map { if (it.id == updated.id) updated else it }
                        },
                        onBringForward = {
                            activeClip?.let { c ->
                                val idx = clips.indexOfFirst { it.id == c.id }
                                if (idx != -1 && idx < clips.size - 1) {
                                    val mutable = clips.toMutableList()
                                    val temp = mutable[idx]
                                    mutable[idx] = mutable[idx + 1]
                                    mutable[idx + 1] = temp
                                    clips = mutable
                                }
                            }
                        },
                        onSendBackward = {
                            activeClip?.let { c ->
                                val idx = clips.indexOfFirst { it.id == c.id }
                                if (idx > 0) {
                                    val mutable = clips.toMutableList()
                                    val temp = mutable[idx]
                                    mutable[idx] = mutable[idx - 1]
                                    mutable[idx - 1] = temp
                                    clips = mutable
                                }
                            }
                        },
                        onBringToFront = {
                            activeClip?.let { c ->
                                val filtered = clips.filterNot { it.id == c.id }
                                clips = filtered + c
                            }
                        },
                        onSendToBack = {
                            activeClip?.let { c ->
                                val filtered = clips.filterNot { it.id == c.id }
                                clips = listOf(c) + filtered
                            }
                        },
                        onDeleteSelected = {
                            if (selectedClipId != null && clips.size > 1) {
                                pushUndoState()
                                clips = clips.filterNot { it.id == selectedClipId }
                                selectedClipId = clips.firstOrNull()?.id
                                showInspectorPanel = false
                            }
                        },
                        onDuplicateSelected = {
                            activeClip?.let { c ->
                                pushUndoState()
                                val dup = c.copy(id = "clip_dup_${System.currentTimeMillis()}", title = "${c.title} Copy")
                                clips = clips + dup
                            }
                        },
                        onGroupSelected = {},
                        onUngroupSelected = {},
                        onResetTransform = {
                            activeClip?.let { c ->
                                val reset = c.copy(offsetX = 0f, offsetY = 0f, scale = 1.0f, rotation = 0f, opacity = 1.0f)
                                clips = clips.map { if (it.id == c.id) reset else it }
                            }
                        },
                        onCloseInspector = { showInspectorPanel = false }
                    )
                }
            }
        }

        // FULLSCREEN PREVIEW MODAL OVERLAY
        if (isFullscreenPreview) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { isFullscreenPreview = false }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(activeClip?.mediaUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Fullscreen Preview",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = { isFullscreenPreview = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close Fullscreen", tint = TextWhite)
                }
            }
        }

        // GLASSMORPHIC BOTTOM SHEETS
        if (activeBottomSheet != ActiveBottomSheet.NONE) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable {
                        tempPreviewFilter = null
                        tempPreviewEffect = null
                        activeBottomSheet = ActiveBottomSheet.NONE
                    },
                contentAlignment = Alignment.BottomCenter
            ) {
                if (activeBottomSheet == ActiveBottomSheet.EDIT_PANEL) {
                    Box(modifier = Modifier.clickable(enabled = false) {}) {
                        EditBottomSheetPanel(
                            activeClip = activeClip,
                            clipsCount = clips.size,
                            currentPlayheadSec = currentPlayheadSec,
                            totalDurationSec = totalDurationSec,
                            onToolClick = { toolLabel ->
                                when (toolLabel) {
                                    "Split" -> {
                                        pushUndoState()
                                        if (activeClip != null && clips.size < 30) {
                                            val newClip = activeClip.copy(
                                                id = "clip_split_${System.currentTimeMillis()}",
                                                title = "${activeClip.title} Part B",
                                                endTrimSec = (activeClip.endTrimSec / 2.0).coerceAtLeast(1.0)
                                            )
                                            clips = clips + newClip
                                            Toast.makeText(context, "Clip split successfully", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Trim" -> activeBottomSheet = ActiveBottomSheet.TRIM_EDITOR
                                    "Speed" -> activeBottomSheet = ActiveBottomSheet.SPEED_ADJUST
                                    "Curve Speed" -> activeBottomSheet = ActiveBottomSheet.CURVE_SPEED
                                    "Animation" -> activeBottomSheet = ActiveBottomSheet.ANIMATIONS_KEYFRAMES
                                    "Crop" -> activeBottomSheet = ActiveBottomSheet.CROP_ASPECT
                                    "Rotate", "Flip" -> activeBottomSheet = ActiveBottomSheet.ROTATE_FLIP
                                    "Transform" -> activeBottomSheet = ActiveBottomSheet.TRANSFORM_PANEL
                                    "Opacity" -> activeBottomSheet = ActiveBottomSheet.OPACITY_ADJUST
                                    "Mask" -> activeBottomSheet = ActiveBottomSheet.MASK_SELECTION
                                    "Keyframes" -> activeBottomSheet = ActiveBottomSheet.ANIMATIONS_KEYFRAMES
                                    "Tracking" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.hasMotionTracking = !it.hasMotionTracking
                                            Toast.makeText(context, if (it.hasMotionTracking) "AI Motion Tracking Enabled" else "Motion Tracking Disabled", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Stabilize" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.isStabilized = !it.isStabilized
                                            Toast.makeText(context, if (it.isStabilized) "Video Stabilization Applied (Optical Flow)" else "Stabilization Off", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Auto Reframe" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.isAutoReframed = !it.isAutoReframed
                                            Toast.makeText(context, if (it.isAutoReframed) "Auto-Reframe Active (9:16 Subject Tracking)" else "Auto-Reframe Off", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Enhance" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.isEnhancedQuality = !it.isEnhancedQuality
                                            Toast.makeText(context, if (it.isEnhancedQuality) "4K Super-Resolution Active" else "Enhance Off", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "HDR" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.isHdrEnhanced = !it.isHdrEnhanced
                                            Toast.makeText(context, if (it.isHdrEnhanced) "HDR10 Tone Mapping Enabled" else "HDR Off", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Retouch" -> activeBottomSheet = ActiveBottomSheet.RETOUCH_PANEL
                                    "Remove BG" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.isBgRemoved = !it.isBgRemoved
                                            Toast.makeText(context, if (it.isBgRemoved) "AI Background Cutout Applied" else "Background Restored", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Reverse" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.isReversed = !it.isReversed
                                            Toast.makeText(context, if (it.isReversed) "Playback Reversed" else "Normal Direction", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Freeze" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            val freeze = TimelineClip(
                                                id = "freeze_${System.currentTimeMillis()}",
                                                mediaUri = it.mediaUri,
                                                title = "Freeze Frame",
                                                originalDurationSec = 2.0,
                                                endTrimSec = 2.0
                                            )
                                            clips = clips + freeze
                                            Toast.makeText(context, "Freeze Frame inserted (2s)", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Duplicate" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            val dup = it.copy(id = "clip_dup_${System.currentTimeMillis()}", title = "${it.title} Copy")
                                            clips = clips + dup
                                            Toast.makeText(context, "Clip duplicated", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Delete" -> {
                                        if (clips.size > 1 && selectedClipId != null) {
                                            pushUndoState()
                                            clips = clips.filterNot { it.id == selectedClipId }
                                            selectedClipId = clips.firstOrNull()?.id
                                            Toast.makeText(context, "Clip deleted", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Cannot delete single clip", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Replace" -> activeBottomSheet = ActiveBottomSheet.ADD_MEDIA_PICKER
                                    "Volume" -> activeBottomSheet = ActiveBottomSheet.VOLUME_ADJUST
                                    "Extract Audio" -> {
                                        pushUndoState()
                                        activeClip?.let { c ->
                                            val extTrack = AudioTrackItem(
                                                id = "aud_${System.currentTimeMillis()}",
                                                title = "Extracted - ${c.title}",
                                                startSec = c.startTrimSec,
                                                durationSec = c.durationSec,
                                                category = "Extracted"
                                            )
                                            audioTracks = audioTracks + extTrack
                                            selectedAudioTrackId = extTrack.id
                                            Toast.makeText(context, "Audio extracted to Audio layer", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Voice Iso" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.voiceIsolation = !it.voiceIsolation
                                            Toast.makeText(context, if (it.voiceIsolation) "AI Voice Isolation Active" else "Voice Isolation Off", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Audio FX" -> activeBottomSheet = ActiveBottomSheet.AUDIO_EFFECTS
                                    "Denoise" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.noiseReduction = !it.noiseReduction
                                            Toast.makeText(context, if (it.noiseReduction) "Denoise Active (-12dB Noise Floor)" else "Denoise Off", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Enhance Voice" -> {
                                        pushUndoState()
                                        activeClip?.let {
                                            it.voiceEnhance = !it.voiceEnhance
                                            Toast.makeText(context, if (it.voiceEnhance) "Studio Voice EQ Active" else "Voice EQ Off", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Translator" -> activeBottomSheet = ActiveBottomSheet.AI_TRANSLATOR
                                    else -> Toast.makeText(context, "$toolLabel action triggered", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onClose = { activeBottomSheet = ActiveBottomSheet.NONE }
                        )
                    }
                } else if (activeBottomSheet == ActiveBottomSheet.FILTERS_CATALOG || activeBottomSheet == ActiveBottomSheet.EFFECTS_CATALOG) {
                    Box(modifier = Modifier.clickable(enabled = false) {}) {
                        com.example.ui.components.FiltersEffectsStudioSheet(
                            activeMediaUri = activeClip?.mediaUri,
                            currentFilterId = activeClip?.filterName ?: "none",
                            currentFilterIntensity = activeClip?.filterIntensity ?: 1.0f,
                            currentEffectId = activeClip?.effectName ?: "e_none",
                            currentEffectIntensity = activeClip?.effectIntensity ?: 0.8f,
                            currentEffectDuration = activeClip?.effectDuration ?: 3.0f,
                            currentEffectBlend = activeClip?.effectBlend ?: 0.5f,
                            currentEffectOpacity = activeClip?.effectOpacity ?: 1.0f,
                            onPreviewFilter = { filter, intensity ->
                                tempPreviewFilter = filter
                                tempPreviewFilterIntensity = intensity
                            },
                            onPreviewEffect = { effect, intensity, duration, blend, opacity ->
                                tempPreviewEffect = effect
                                tempPreviewEffectIntensity = intensity
                                tempPreviewEffectDuration = duration
                                tempPreviewEffectBlend = blend
                                tempPreviewEffectOpacity = opacity
                            },
                            onComparePressChanged = { holding ->
                                isCompareHolding = holding
                            },
                            onApplyFilter = { filter, intensity ->
                                pushUndoState()
                                activeClip?.let {
                                    it.filterName = filter.id
                                    it.filterIntensity = intensity
                                }
                                tempPreviewFilter = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            },
                            onApplyEffect = { effect, intensity, duration, blend, opacity ->
                                pushUndoState()
                                activeClip?.let {
                                    it.effectName = effect.id
                                    it.effectIntensity = intensity
                                    it.effectDuration = duration
                                    it.effectBlend = blend
                                    it.effectOpacity = opacity
                                }
                                tempPreviewEffect = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            },
                            onCancel = {
                                tempPreviewFilter = null
                                tempPreviewEffect = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            },
                            onClose = {
                                tempPreviewFilter = null
                                tempPreviewEffect = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            }
                        )
                    }
                } else if (activeBottomSheet == ActiveBottomSheet.TRANSITION_LIBRARY) {
                    Box(modifier = Modifier.clickable(enabled = false) {}) {
                        val (fromClipId, toClipId) = activeTransitionPair ?: Pair(clips.getOrNull(0)?.id ?: "", clips.getOrNull(1)?.id ?: "")
                        val fromClip = clips.find { it.id == fromClipId }
                        val toClip = clips.find { it.id == toClipId }
                        val existingTrans = clipTransitions.find { it.fromClipId == fromClipId && it.toClipId == toClipId }
                        val currentCfg = com.example.ui.components.TransitionConfig(
                            transitionId = existingTrans?.transitionType ?: "t_cross_fade",
                            durationSec = existingTrans?.durationSec ?: 0.5,
                            direction = existingTrans?.direction ?: "Right",
                            curve = existingTrans?.curve ?: "EaseInOut",
                            intensity = existingTrans?.intensity ?: 1.0f,
                            isReversed = existingTrans?.isReversed ?: false
                        )

                        com.example.ui.components.TransitionsStudioSheet(
                            fromMediaUri = fromClip?.mediaUri,
                            toMediaUri = toClip?.mediaUri,
                            currentTransitionId = existingTrans?.transitionType ?: "t_none",
                            currentConfig = currentCfg,
                            onPreviewTransition = { cfg -> tempPreviewTransitionConfig = cfg },
                            onApplyTransition = { cfg ->
                                pushUndoState()
                                val newTrans = ClipTransition(
                                    id = existingTrans?.id ?: "trans_${System.currentTimeMillis()}",
                                    fromClipId = fromClipId,
                                    toClipId = toClipId,
                                    transitionType = cfg.transitionId,
                                    durationSec = cfg.durationSec,
                                    direction = cfg.direction,
                                    curve = cfg.curve,
                                    intensity = cfg.intensity,
                                    isReversed = cfg.isReversed
                                )
                                clipTransitions = clipTransitions.filterNot { it.fromClipId == fromClipId && it.toClipId == toClipId } + newTrans
                                tempPreviewTransitionConfig = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            },
                            onApplyToAll = { cfg ->
                                pushUndoState()
                                var newTransitions = emptyList<ClipTransition>()
                                for (i in 0 until clips.size - 1) {
                                    val c1 = clips[i].id
                                    val c2 = clips[i + 1].id
                                    newTransitions = newTransitions + ClipTransition(
                                        id = "trans_${System.currentTimeMillis()}_$i",
                                        fromClipId = c1,
                                        toClipId = c2,
                                        transitionType = cfg.transitionId,
                                        durationSec = cfg.durationSec,
                                        direction = cfg.direction,
                                        curve = cfg.curve,
                                        intensity = cfg.intensity,
                                        isReversed = cfg.isReversed
                                    )
                                }
                                clipTransitions = newTransitions
                                tempPreviewTransitionConfig = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            },
                            onRemoveTransition = {
                                pushUndoState()
                                clipTransitions = clipTransitions.filterNot { it.fromClipId == fromClipId && it.toClipId == toClipId }
                                tempPreviewTransitionConfig = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            },
                            onCancel = {
                                tempPreviewTransitionConfig = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            },
                            onClose = {
                                tempPreviewTransitionConfig = null
                                activeBottomSheet = ActiveBottomSheet.NONE
                            }
                        )
                    }
                } else if (activeBottomSheet == ActiveBottomSheet.ANIMATIONS_KEYFRAMES) {
                    Box(modifier = Modifier.clickable(enabled = false) {}) {
                        val activeTargetName = activeClip?.title ?: "Clip"
                        val activeKfs = activeClip?.keyframes ?: emptyList()

                        com.example.ui.components.AnimationsKeyframeStudioSheet(
                            targetName = activeTargetName,
                            currentTimeSec = currentPlayheadSec,
                            keyframes = activeKfs,
                            onAddKeyframe = { kf ->
                                pushUndoState()
                                activeClip?.let {
                                    it.keyframes = (it.keyframes + kf).sortedBy { p -> p.timeSec }
                                }
                            },
                            onDeleteKeyframe = { kfId ->
                                pushUndoState()
                                activeClip?.let {
                                    it.keyframes = it.keyframes.filterNot { p -> p.id == kfId }
                                }
                            },
                            onApplyEntryAnimation = { anim ->
                                pushUndoState()
                                activeClip?.let { it.entryAnimationId = anim.id }
                            },
                            onApplyExitAnimation = { anim ->
                                pushUndoState()
                                activeClip?.let { it.exitAnimationId = anim.id }
                            },
                            onApplyLoopAnimation = { anim ->
                                pushUndoState()
                                activeClip?.let { it.loopAnimationId = anim.id }
                            },
                            onClose = {
                                activeBottomSheet = ActiveBottomSheet.NONE
                            }
                        )
                    }
                } else if (activeBottomSheet == ActiveBottomSheet.AUDIO_SELECTION || activeBottomSheet == ActiveBottomSheet.VOLUME_ADJUST) {
                    Box(modifier = Modifier.clickable(enabled = false) {}) {
                        com.example.ui.components.AudioStudioMainSheet(
                            activeTrack = activeAudioTrack,
                            currentPlayheadSec = currentPlayheadSec,
                            videoClips = clips,
                            audioTracks = audioTracks,
                            onAddTrackToTimeline = { item ->
                                pushUndoState()
                                val newTrack = AudioTrackItem(
                                    id = "aud_${System.currentTimeMillis()}",
                                    title = item.title,
                                    startSec = currentPlayheadSec,
                                    durationSec = item.durationSec,
                                    category = item.category
                                )
                                audioTracks = audioTracks + newTrack
                                selectedAudioTrackId = newTrack.id
                            },
                            onUpdateTrackProperties = { updated ->
                                pushUndoState()
                                audioTracks = audioTracks.map { if (it.id == updated.id) updated else it }
                            },
                            onDeleteTrack = { trackId ->
                                pushUndoState()
                                audioTracks = audioTracks.filterNot { it.id == trackId }
                                if (selectedAudioTrackId == trackId) {
                                    selectedAudioTrackId = audioTracks.firstOrNull()?.id
                                }
                            },
                            onDuplicateTrack = { track ->
                                pushUndoState()
                                val dup = track.copy(
                                    id = "aud_dup_${System.currentTimeMillis()}",
                                    title = "${track.title} Copy",
                                    startSec = track.startSec + track.durationSec
                                )
                                audioTracks = audioTracks + dup
                                selectedAudioTrackId = dup.id
                            },
                            onExtractAudioFromVideo = { option ->
                                pushUndoState()
                                activeClip?.let { c ->
                                    if (option == "Mute Original" || option == "Replace Original") {
                                        c.isMuted = true
                                    }
                                    val extTrack = AudioTrackItem(
                                        id = "aud_${System.currentTimeMillis()}",
                                        title = "Extracted - ${c.title}",
                                        startSec = c.startTrimSec,
                                        durationSec = c.durationSec,
                                        category = "Extracted"
                                    )
                                    audioTracks = audioTracks + extTrack
                                    selectedAudioTrackId = extTrack.id
                                }
                            },
                            pushUndoState = { pushUndoState() },
                            onClose = {
                                activeBottomSheet = ActiveBottomSheet.NONE
                            }
                        )
                    }
                } else if (activeBottomSheet == ActiveBottomSheet.TEXT_EDITOR || activeBottomSheet == ActiveBottomSheet.STICKER_PICKER) {
                    Box(modifier = Modifier.clickable(enabled = false) {}) {
                        com.example.ui.components.TextStudioMainSheet(
                            activeTextTrack = activeTextTrack,
                            activeStickerTrack = activeStickerTrack,
                            textTracks = textTracks,
                            stickerTracks = stickerTracks,
                            drawingTracks = drawingTracks,
                            currentPlayheadSec = currentPlayheadSec,
                            videoClips = clips,
                            onAddTextTrack = { newTextTrack ->
                                pushUndoState()
                                textTracks = textTracks + newTextTrack
                                selectedTextTrackId = newTextTrack.id
                            },
                            onUpdateTextTrack = { updatedTextTrack ->
                                pushUndoState()
                                textTracks = textTracks.map { if (it.id == updatedTextTrack.id) updatedTextTrack else it }
                            },
                            onDeleteTextTrack = { trackId ->
                                pushUndoState()
                                textTracks = textTracks.filterNot { it.id == trackId }
                                if (selectedTextTrackId == trackId) {
                                    selectedTextTrackId = textTracks.firstOrNull()?.id
                                }
                            },
                            onAddStickerTrack = { newStickerTrack ->
                                pushUndoState()
                                stickerTracks = stickerTracks + newStickerTrack
                                selectedStickerTrackId = newStickerTrack.id
                            },
                            onUpdateStickerTrack = { updatedStickerTrack ->
                                pushUndoState()
                                stickerTracks = stickerTracks.map { if (it.id == updatedStickerTrack.id) updatedStickerTrack else it }
                            },
                            onDeleteStickerTrack = { trackId ->
                                pushUndoState()
                                stickerTracks = stickerTracks.filterNot { it.id == trackId }
                                if (selectedStickerTrackId == trackId) {
                                    selectedStickerTrackId = stickerTracks.firstOrNull()?.id
                                }
                            },
                            onAddDrawingTrack = { newDrawing ->
                                pushUndoState()
                                drawingTracks = drawingTracks + newDrawing
                            },
                            onUpdateDrawingTrack = { updatedDrawing ->
                                pushUndoState()
                                drawingTracks = drawingTracks.map { if (it.id == updatedDrawing.id) updatedDrawing else it }
                            },
                            onDeleteDrawingTrack = { trackId ->
                                pushUndoState()
                                drawingTracks = drawingTracks.filterNot { it.id == trackId }
                            },
                            pushUndoState = { pushUndoState() },
                            onClose = {
                                activeBottomSheet = ActiveBottomSheet.NONE
                            }
                        )
                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .clickable(enabled = false) {},
                        color = SurfaceCard,
                        border = BorderStroke(1.dp, Color(0xFF27273A))
                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Sheet Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (activeBottomSheet) {
                                    ActiveBottomSheet.SPEED_ADJUST -> "Adjust Speed"
                                    ActiveBottomSheet.VOLUME_ADJUST -> "Volume & Audio Controls"
                                    ActiveBottomSheet.OPACITY_ADJUST -> "Adjust Opacity"
                                    ActiveBottomSheet.CROP_ASPECT -> "Crop & Canvas Ratio"
                                    ActiveBottomSheet.TRIM_EDITOR -> "Frame-Accurate Trim"
                                    ActiveBottomSheet.ROTATE_FLIP -> "Rotate & Mirror Clip"
                                    ActiveBottomSheet.AUDIO_SELECTION -> "Audio Soundtracks"
                                    ActiveBottomSheet.TEXT_EDITOR -> "Text & Captions"
                                    ActiveBottomSheet.EFFECTS_CATALOG -> "Video Effects"
                                    ActiveBottomSheet.FILTERS_CATALOG -> "Color Filters"
                                    ActiveBottomSheet.CANVAS_SETTING -> "Background Mode"
                                    ActiveBottomSheet.EXPORT_MODAL -> "Export Video Settings"
                                    ActiveBottomSheet.TRANSITION_LIBRARY -> "Transition Library"
                                    ActiveBottomSheet.ADD_MEDIA_PICKER -> "Add Media Track"
                                    ActiveBottomSheet.CLIP_ACTION_MENU -> "Clip Operations"
                                    else -> "Tool Settings"
                                },
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            IconButton(
                                onClick = { activeBottomSheet = ActiveBottomSheet.NONE },
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF27273A))
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite, modifier = Modifier.size(14.dp))
                            }
                        }

                        HorizontalDivider(color = Color(0xFF27273A))

                        // Sheet Content Body
                        when (activeBottomSheet) {
                            ActiveBottomSheet.ADD_MEDIA_PICKER -> {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("Select Content Type to Add:", color = TextGray, fontSize = 11.sp)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        MediaAddOptionItem(icon = Icons.Default.Videocam, label = "Video") {
                                            activeBottomSheet = ActiveBottomSheet.NONE
                                            addMediaLauncher.launch("video/*")
                                        }
                                        MediaAddOptionItem(icon = Icons.Default.Image, label = "Photo") {
                                            activeBottomSheet = ActiveBottomSheet.NONE
                                            addMediaLauncher.launch("image/*")
                                        }
                                        MediaAddOptionItem(icon = Icons.Default.Title, label = "Text") {
                                            activeBottomSheet = ActiveBottomSheet.TEXT_EDITOR
                                        }
                                        MediaAddOptionItem(icon = Icons.Default.MusicNote, label = "Audio") {
                                            activeBottomSheet = ActiveBottomSheet.AUDIO_SELECTION
                                        }
                                        MediaAddOptionItem(icon = Icons.Default.Layers, label = "Overlay") {
                                            activeBottomSheet = ActiveBottomSheet.NONE
                                            val newSticker = StickerTrackItem("stk_${System.currentTimeMillis()}", "✨", currentPlayheadSec, 4.0)
                                            stickerTracks = stickerTracks + newSticker
                                            Toast.makeText(context, "Overlay element added", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }

                            ActiveBottomSheet.TRANSITION_LIBRARY -> {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("Choose Transition Effect:", color = TextGray, fontSize = 11.sp)
                                    val transitionsList = listOf("Fade", "Dissolve", "Zoom In", "Whip Pan", "Glitch", "Spin", "Slide", "Blur")
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        itemsIndexed(transitionsList) { _, trans ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(SurfaceCardLight)
                                                    .border(BorderStroke(1.dp, PrimaryViolet))
                                                    .clickable {
                                                        activeTransitionPair?.let { (from, to) ->
                                                            val newTrans = ClipTransition("trans_${System.currentTimeMillis()}", from, to, trans)
                                                            clipTransitions = clipTransitions.filterNot { it.fromClipId == from && it.toClipId == to } + newTrans
                                                            Toast.makeText(context, "$trans Transition Applied", Toast.LENGTH_SHORT).show()
                                                        }
                                                        activeBottomSheet = ActiveBottomSheet.NONE
                                                    }
                                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                            ) {
                                                Text(trans, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            ActiveBottomSheet.CLIP_ACTION_MENU -> {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Clip Operations for ${activeClip?.title ?: "Clip"}", color = SoftViolet, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Button(
                                            onClick = {
                                                activeClip?.let { it.isLocked = !it.isLocked }
                                                Toast.makeText(context, if (activeClip?.isLocked == true) "Clip Locked" else "Clip Unlocked", Toast.LENGTH_SHORT).show()
                                                activeBottomSheet = ActiveBottomSheet.NONE
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardLight)
                                        ) {
                                            Icon(if (activeClip?.isLocked == true) Icons.Default.Lock else Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(4.dp))
                                            Text(if (activeClip?.isLocked == true) "Unlock" else "Lock", fontSize = 11.sp)
                                        }

                                        Button(
                                            onClick = {
                                                pushUndoState()
                                                activeClip?.let {
                                                    val dup = it.copy(id = "dup_${System.currentTimeMillis()}", title = "${it.title} (Copy)")
                                                    clips = clips + dup
                                                }
                                                activeBottomSheet = ActiveBottomSheet.NONE
                                                Toast.makeText(context, "Clip Duplicated", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardLight)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(4.dp))
                                            Text("Duplicate", fontSize = 11.sp)
                                        }

                                        Button(
                                            onClick = {
                                                if (clips.size > 1 && selectedClipId != null) {
                                                    pushUndoState()
                                                    clips = clips.filterNot { it.id == selectedClipId }
                                                    selectedClipId = clips.firstOrNull()?.id
                                                }
                                                activeBottomSheet = ActiveBottomSheet.NONE
                                                Toast.makeText(context, "Clip Deleted", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.8f))
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(4.dp))
                                            Text("Delete", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }

                            ActiveBottomSheet.TRIM_EDITOR -> {
                                activeClip?.let { clip ->
                                    var startTrim by remember { mutableStateOf(clip.startTrimSec) }
                                    var endTrim by remember { mutableStateOf(clip.endTrimSec) }

                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = "Trim Clip: ${clip.title}",
                                            color = TextWhite,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "Duration: ${String.format(Locale.US, "%.1f", (endTrim - startTrim) / clip.speed)}s",
                                            color = SoftViolet,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Column {
                                            Text("Start: ${String.format(Locale.US, "%.1f", startTrim)}s", color = TextGray, fontSize = 10.sp)
                                            Slider(
                                                value = startTrim.toFloat(),
                                                onValueChange = { startTrim = it.toDouble().coerceIn(0.0, endTrim - 0.2) },
                                                valueRange = 0f..clip.originalDurationSec.toFloat(),
                                                colors = SliderDefaults.colors(thumbColor = SoftViolet, activeTrackColor = PrimaryViolet)
                                            )
                                        }

                                        Column {
                                            Text("End: ${String.format(Locale.US, "%.1f", endTrim)}s", color = TextGray, fontSize = 10.sp)
                                            Slider(
                                                value = endTrim.toFloat(),
                                                onValueChange = { endTrim = it.toDouble().coerceIn(startTrim + 0.2, clip.originalDurationSec) },
                                                valueRange = 0f..clip.originalDurationSec.toFloat(),
                                                colors = SliderDefaults.colors(thumbColor = SoftViolet, activeTrackColor = PrimaryViolet)
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Button(
                                                onClick = {
                                                    startTrim = 0.0
                                                    endTrim = clip.originalDurationSec
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardLight)
                                            ) {
                                                Text("Reset", fontSize = 11.sp)
                                            }

                                            Button(
                                                onClick = {
                                                    pushUndoState()
                                                    clip.startTrimSec = startTrim
                                                    clip.endTrimSec = endTrim
                                                    activeBottomSheet = ActiveBottomSheet.NONE
                                                    Toast.makeText(context, "Trim applied", Toast.LENGTH_SHORT).show()
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryViolet)
                                            ) {
                                                Text("Apply Trim", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            ActiveBottomSheet.SPEED_ADJUST -> {
                                var tempSpeed by remember { mutableStateOf(playbackSpeed) }
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("Speed: ${String.format(Locale.US, "%.2f", tempSpeed)}x", color = SoftViolet, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Slider(
                                        value = tempSpeed,
                                        onValueChange = { tempSpeed = it },
                                        valueRange = 0.2f..4.0f,
                                        colors = SliderDefaults.colors(thumbColor = SoftViolet, activeTrackColor = PrimaryViolet)
                                    )
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                        listOf(0.5f, 1.0f, 1.5f, 2.0f, 3.0f).forEach { spd ->
                                            Button(
                                                onClick = { tempSpeed = spd },
                                                colors = ButtonDefaults.buttonColors(containerColor = if (tempSpeed == spd) PrimaryViolet else SurfaceCardLight)
                                            ) {
                                                Text("${spd}x", fontSize = 10.sp)
                                            }
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            playbackSpeed = tempSpeed
                                            activeClip?.speed = tempSpeed
                                            activeBottomSheet = ActiveBottomSheet.NONE
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryViolet),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Save Speed", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            ActiveBottomSheet.EXPORT_MODAL -> {
                                ExportSheetContent(
                                    projectName = projectName,
                                    resolution = selectedResolution,
                                    fps = selectedFps,
                                    durationFormatted = formatTimecode(totalDurationSec),
                                    onExportDone = {
                                        activeBottomSheet = ActiveBottomSheet.NONE
                                        Toast.makeText(context, "Video Exported Successfully to Gallery!", Toast.LENGTH_LONG).show()
                                    }
                                )
                            }

                            else -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text("Settings Ready for Tool", color = TextWhite, fontSize = 12.sp)
                                    Button(
                                        onClick = { activeBottomSheet = ActiveBottomSheet.NONE },
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryViolet)
                                    ) {
                                        Text("Close")
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
}

// ============================================================================
// PHASE 9 COMPACT CAPCUT-STYLE MINI ICON TOOLBAR (NO TEXT)
// ============================================================================
@Composable
private fun CapCutMiniIconToolbar(
    undoEnabled: Boolean,
    redoEnabled: Boolean,
    enableSnapToGrid: Boolean,
    isPreviewMuted: Boolean,
    isMultiSelectMode: Boolean,
    currentPlayheadSec: Double = 0.0,
    totalDurationSec: Double = 15.0,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onToggleSnap: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleMultiSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        color = SurfaceCard,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Group: Snap Magnet, Mute, Select
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleSnap, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.GridOn,
                        contentDescription = "Magnet Snap",
                        tint = if (enableSnapToGrid) MintGlow else TextGray,
                        modifier = Modifier.size(14.dp)
                    )
                }
                IconButton(onClick = onToggleMute, modifier = Modifier.size(24.dp)) {
                    Icon(
                        if (isPreviewMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Mute",
                        tint = if (isPreviewMuted) Color(0xFFEF4444) else TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }
                IconButton(onClick = onToggleMultiSelect, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.SelectAll,
                        contentDescription = "Multi-Select",
                        tint = if (isMultiSelectMode) MintGlow else TextGray,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Center Group: Timecode Counter (00:02.4 / 00:15.0) + Zoom - / Zoom +
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onZoomOut, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = TextWhite, modifier = Modifier.size(14.dp))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${formatTimecode(currentPlayheadSec)} / ${formatTimecode(totalDurationSec)}",
                        color = MintGlow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onZoomIn, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = TextWhite, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

// ============================================================================
// PHASE 3 PROFESSIONAL MULTI-LAYER CAPCUT TIMELINE ENGINE
// ============================================================================
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Phase9CompactTimeline(
    clips: List<TimelineClip>,
    selectedClipId: String?,
    isMultiSelectMode: Boolean,
    selectedClipIds: Set<String>,
    audioTracks: List<AudioTrackItem>,
    selectedAudioTrackId: String? = null,
    textTracks: List<TextTrackItem>,
    stickerTracks: List<StickerTrackItem>,
    effectTracks: List<EffectTrackItem>,
    clipTransitions: List<ClipTransition>,
    currentPlayheadSec: Double,
    totalDurationSec: Double,
    timelineZoomPxPerSec: Float,
    onUpdateZoom: (Float) -> Unit,
    onSelectClip: (String) -> Unit,
    onTrimClip: ((String, Double, Double) -> Unit)? = null,
    onSelectAudioTrack: ((String) -> Unit)? = null,
    onAddAudioTrack: (() -> Unit)? = null,
    onLongClickClip: (String) -> Unit,
    onAddMediaBetween: () -> Unit,
    onOpenTransition: (String, String) -> Unit,
    onSeek: (Double) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val density = LocalDensity.current.density

    // Layer Mute / Hide / Lock States
    var isVideoHidden by remember { mutableStateOf(false) }
    var isVideoLocked by remember { mutableStateOf(false) }

    var isAudioHidden by remember { mutableStateOf(false) }
    var isAudioMuted by remember { mutableStateOf(false) }
    var isAudioLocked by remember { mutableStateOf(false) }

    var isTextHidden by remember { mutableStateOf(false) }
    var isCaptionHidden by remember { mutableStateOf(false) }
    var isOverlayHidden by remember { mutableStateOf(false) }
    var isEffectsHidden by remember { mutableStateOf(false) }
    var isFiltersHidden by remember { mutableStateOf(false) }
    var isStickersHidden by remember { mutableStateOf(false) }

    val horizontalScrollState = rememberScrollState()
    val pxPerSec = timelineZoomPxPerSec * density

    // Synchronize user scrubbing -> onSeek
    LaunchedEffect(horizontalScrollState.value, horizontalScrollState.isScrollInProgress) {
        if (horizontalScrollState.isScrollInProgress) {
            val derivedSec = (horizontalScrollState.value / pxPerSec.toDouble()).coerceIn(0.0, totalDurationSec)
            if (kotlin.math.abs(derivedSec - currentPlayheadSec) > 0.02) {
                onSeek(derivedSec)
            }
        }
    }

    // Synchronize playhead position -> horizontal scroll offset
    LaunchedEffect(currentPlayheadSec, timelineZoomPxPerSec) {
        if (!horizontalScrollState.isScrollInProgress) {
            val targetPx = (currentPlayheadSec * pxPerSec).toInt()
            if (kotlin.math.abs(horizontalScrollState.value - targetPx) > 2) {
                horizontalScrollState.scrollTo(targetPx)
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    onUpdateZoom((timelineZoomPxPerSec * zoom).coerceIn(10f, 120f))
                }
            }
    ) {
        val totalWidthDp = maxWidth
        val headerWidthDp = 40.dp
        val trackAreaWidthDp = (totalWidthDp - headerWidthDp).coerceAtLeast(100.dp)
        val centerPaddingDp = trackAreaWidthDp / 2f

        Row(modifier = Modifier.fillMaxSize()) {
            // FIXED TRACK HEADERS COLUMN ON LEFT (width 40dp)
            Column(
                modifier = Modifier
                    .width(headerWidthDp)
                    .fillMaxHeight()
                    .background(Color(0xFF0F121C))
                    .padding(top = 26.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                LayerTrackHeader(
                    icon = Icons.Default.Videocam,
                    color = Color(0xFF10B981),
                    heightDp = 56.dp,
                    isHidden = isVideoHidden,
                    isLocked = isVideoLocked,
                    onToggleHide = { isVideoHidden = !isVideoHidden },
                    onToggleLock = { isVideoLocked = !isVideoLocked }
                )
                LayerTrackHeader(
                    icon = Icons.Default.MusicNote,
                    color = Color(0xFF3B82F6),
                    heightDp = 44.dp,
                    isHidden = isAudioHidden,
                    isLocked = isAudioLocked,
                    isMuted = isAudioMuted,
                    onToggleHide = { isAudioHidden = !isAudioHidden },
                    onToggleLock = { isAudioLocked = !isAudioLocked },
                    onToggleMute = { isAudioMuted = !isAudioMuted }
                )
                LayerTrackHeader(
                    icon = Icons.Default.Title,
                    color = Color(0xFF8B5CF6),
                    heightDp = 44.dp,
                    isHidden = isTextHidden,
                    onToggleHide = { isTextHidden = !isTextHidden }
                )
                LayerTrackHeader(
                    icon = Icons.Default.ClosedCaption,
                    color = Color(0xFFF97316),
                    heightDp = 44.dp,
                    isHidden = isCaptionHidden,
                    onToggleHide = { isCaptionHidden = !isCaptionHidden }
                )
            }

            // HORIZONTALLY SCROLLING TRACK CONTENT AREA
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .horizontalScroll(horizontalScrollState)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = centerPaddingDp, end = centerPaddingDp)
                        .wrapContentWidth()
                        .fillMaxHeight()
                ) {
            Spacer(Modifier.height(2.dp))

            // PROFESSIONAL TIMELINE RULER (00:00, 00:01, 00:02 with frame marks 5f, 10f, 15f when zoomed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .background(Color(0xFF0F121C))
                    .padding(start = 44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isZoomedIn = timelineZoomPxPerSec > 35f
                val tickStepSec = if (isZoomedIn) 1.0 else if (timelineZoomPxPerSec > 18f) 2.0 else 5.0
                val tickCount = (totalDurationSec / tickStepSec).toInt().coerceIn(4, 120)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy((tickStepSec * timelineZoomPxPerSec).dp)
                ) {
                    (0 until tickCount).forEach { tickIdx ->
                        val timeVal = tickIdx * tickStepSec
                        val mins = (timeVal / 60).toInt()
                        val secs = (timeVal % 60).toInt()
                        val secFormatted = String.format(Locale.US, "%02d:%02d", mins, secs)

                        Column(horizontalAlignment = Alignment.Start) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .width(1.5.dp)
                                        .height(10.dp)
                                        .background(MintPrimary.copy(alpha = 0.8f))
                                )
                                Spacer(Modifier.width(3.dp))
                                Text(
                                    text = secFormatted,
                                    color = TextWhite,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Show frame ticks when zoomed in
                            if (isZoomedIn && timelineZoomPxPerSec > 45f) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy((timelineZoomPxPerSec * 0.2f).dp),
                                    modifier = Modifier.padding(top = 1.dp)
                                ) {
                                    listOf("5f", "10f", "15f", "20f", "25f").forEach { frameLabel ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .width(1.dp)
                                                    .height(4.dp)
                                                    .background(TextGray.copy(alpha = 0.5f))
                                            )
                                            Spacer(Modifier.width(1.dp))
                                            Text(
                                                text = frameLabel,
                                                color = TextGray.copy(alpha = 0.7f),
                                                fontSize = 6.5.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // ================================================================
            // LAYER 1: MAIN VIDEO TRACK (Height 48dp, CapCut Style Clips)
            // ================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LayerTrackHeader(
                    icon = Icons.Default.Videocam,
                    color = Color(0xFF10B981),
                    heightDp = 48.dp,
                    isHidden = isVideoHidden,
                    isLocked = isVideoLocked,
                    onToggleHide = { isVideoHidden = !isVideoHidden },
                    onToggleLock = { isVideoLocked = !isVideoLocked }
                )

                Spacer(Modifier.width(4.dp))

                if (!isVideoHidden) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        clips.forEachIndexed { index, clip ->
                            val isSelected = if (isMultiSelectMode) selectedClipIds.contains(clip.id) else clip.id == selectedClipId
                            val hasActiveSelection = selectedClipId != null || selectedClipIds.isNotEmpty()
                            val isDimmed = hasActiveSelection && !isSelected
                            val clipWidthDp = (clip.durationSec * timelineZoomPxPerSec).dp.coerceAtLeast(56.dp)

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // CapCut Clip Box (Height 48dp)
                                Box(
                                    modifier = Modifier
                                        .width(clipWidthDp)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0D1E16))
                                        .border(
                                            border = if (isSelected) BorderStroke(2.dp, MintPrimary) else BorderStroke(1.dp, Color(0xFF0F4D32)),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .combinedClickable(
                                            onClick = {
                                                if (!isVideoLocked) {
                                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                                    onSelectClip(clip.id)
                                                }
                                            },
                                            onLongClick = { onLongClickClip(clip.id) }
                                        )
                                ) {
                                    if (clip.mediaUri != null) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(clip.mediaUri)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = clip.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    // Selection Glow & Dimming Overlay
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                when {
                                                    isSelected -> MintPrimary.copy(alpha = 0.20f)
                                                    isDimmed -> Color.Black.copy(alpha = 0.50f)
                                                    else -> Color.Black.copy(alpha = 0.20f)
                                                }
                                            )
                                    )

                                    // Mint side resize handles when selected
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.CenterStart)
                                                .width(5.dp)
                                                .fillMaxHeight()
                                                .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                                                .background(MintPrimary)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.CenterEnd)
                                                .width(5.dp)
                                                .fillMaxHeight()
                                                .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                                                .background(MintPrimary)
                                        )
                                    }

                                    // Clip title label
                                    Text(
                                        text = clip.title,
                                        color = TextWhite,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(start = if (isSelected) 7.dp else 4.dp, top = 3.dp)
                                    )

                                    // Duration badge
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(bottom = 3.dp, end = if (isSelected) 7.dp else 4.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.Black.copy(alpha = 0.85f))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "${String.format(Locale.US, "%.1f", clip.durationSec)}s",
                                            color = MintPrimary,
                                            fontSize = 7.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                // Transition Button between clips
                                if (index < clips.size - 1) {
                                    val nextClip = clips[index + 1]
                                    val existingTrans = clipTransitions.find { it.fromClipId == clip.id && it.toClipId == nextClip.id }

                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 2.dp)
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(if (existingTrans != null) MintPrimary else Color(0xFF1E2030))
                                            .border(BorderStroke(1.dp, MintPrimary.copy(alpha = 0.5f)), CircleShape)
                                            .clickable { onOpenTransition(clip.id, nextClip.id) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Tune,
                                            contentDescription = "Transition",
                                            tint = if (existingTrans != null) Color.Black else TextWhite,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E2030))
                                .border(BorderStroke(1.dp, MintPrimary), CircleShape)
                                .clickable { onAddMediaBetween() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Clip", tint = MintPrimary, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // ================================================================
            // LAYER 2: AUDIO TRACK (Height 30dp)
            // ================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LayerTrackHeader(
                    icon = Icons.Default.MusicNote,
                    color = Color(0xFF3B82F6),
                    heightDp = 30.dp,
                    isHidden = isAudioHidden,
                    isLocked = isAudioLocked,
                    isMuted = isAudioMuted,
                    onToggleHide = { isAudioHidden = !isAudioHidden },
                    onToggleLock = { isAudioLocked = !isAudioLocked },
                    onToggleMute = { isAudioMuted = !isAudioMuted }
                )

                Spacer(Modifier.width(4.dp))

                if (!isAudioHidden) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        audioTracks.forEachIndexed { _, aud ->
                            val isSel = selectedAudioTrackId == aud.id
                            val trackWidth = (aud.durationSec * timelineZoomPxPerSec).dp.coerceAtLeast(60.dp)
                            Box(
                                modifier = Modifier
                                    .width(trackWidth)
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E3A8A))
                                    .border(
                                        BorderStroke(if (isSel) 1.5.dp else 1.dp, if (isSel) Color(0xFF60A5FA) else Color(0xFF2563EB)),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable { onSelectAudioTrack?.invoke(aud.id) }
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val barCount = (size.width / 3.dp.toPx()).toInt().coerceAtLeast(8)
                                    val barWidth = 1.5.dp.toPx()
                                    val gap = 1.5.dp.toPx()
                                    val midY = size.height / 2f
                                    val alphaVal = if (isAudioMuted || aud.isMuted) 0.3f else 1.0f

                                    for (i in 0 until barCount) {
                                        val x = i * (barWidth + gap) + gap
                                        val hNorm = (kotlin.math.sin(i * 0.8) * 0.4 + 0.5).toFloat().coerceIn(0.2f, 0.9f)
                                        val barH = size.height * hNorm * 0.7f
                                        drawLine(
                                            color = Color(0xFF60A5FA).copy(alpha = alphaVal),
                                            start = Offset(x, midY - barH / 2f),
                                            end = Offset(x, midY + barH / 2f),
                                            strokeWidth = barWidth
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(aud.title, color = TextWhite, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                    if (isAudioMuted || aud.isMuted) {
                                        Icon(Icons.Default.VolumeOff, contentDescription = "Muted", tint = Color.Red, modifier = Modifier.size(10.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ================================================================
            // LAYER 3: TEXT TRACK (Height 30dp)
            // ================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LayerTrackHeader(
                    icon = Icons.Default.Title,
                    color = Color(0xFF8B5CF6),
                    heightDp = 30.dp,
                    isHidden = isTextHidden,
                    onToggleHide = { isTextHidden = !isTextHidden }
                )

                Spacer(Modifier.width(4.dp))

                if (!isTextHidden) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        textTracks.forEachIndexed { _, txt ->
                            Box(
                                modifier = Modifier
                                    .width((txt.durationSec * timelineZoomPxPerSec).dp.coerceAtLeast(50.dp))
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF4C1D95))
                                    .border(BorderStroke(1.dp, Color(0xFF8B5CF6)), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(txt.text, color = TextWhite, fontSize = 8.5.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                            }
                        }
                    }
                }
            }

            // ================================================================
            // LAYER 4: AUTO CAPTIONS TRACK (Height 30dp)
            // ================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LayerTrackHeader(
                    icon = Icons.Default.ClosedCaption,
                    color = Color(0xFFF97316),
                    heightDp = 30.dp,
                    isHidden = isCaptionHidden,
                    onToggleHide = { isCaptionHidden = !isCaptionHidden }
                )

                Spacer(Modifier.width(4.dp))

                if (!isCaptionHidden) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        repeat(3) { idx ->
                            Box(
                                modifier = Modifier
                                    .width((3.0 * timelineZoomPxPerSec).dp.coerceAtLeast(45.dp))
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF7C2D12))
                                    .border(BorderStroke(1.dp, Color(0xFFF97316)), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Caption ${idx + 1}", color = TextWhite, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // ================================================================
            // LAYER 5: EFFECTS TRACK (Height 30dp)
            // ================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LayerTrackHeader(
                    icon = Icons.Default.AutoAwesome,
                    color = Color(0xFF06B6D4),
                    heightDp = 30.dp,
                    isHidden = isEffectsHidden,
                    onToggleHide = { isEffectsHidden = !isEffectsHidden }
                )

                Spacer(Modifier.width(4.dp))

                if (!isEffectsHidden) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        effectTracks.forEachIndexed { _, fx ->
                            Box(
                                modifier = Modifier
                                    .width((fx.durationSec * timelineZoomPxPerSec).dp.coerceAtLeast(48.dp))
                                    .height(30.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF164E63))
                                    .border(BorderStroke(1.dp, Color(0xFF06B6D4)), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✨ ${fx.name}", color = TextWhite, fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            }
                        }
                    }
                }
            }

            // ================================================================
            // LAYER 6: OVERLAY / PIP TRACK (Height 30dp)
            // ================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LayerTrackHeader(
                    icon = Icons.Default.Layers,
                    color = Color(0xFFEC4899),
                    heightDp = 30.dp,
                    isHidden = isOverlayHidden,
                    onToggleHide = { isOverlayHidden = !isOverlayHidden }
                )

                Spacer(Modifier.width(4.dp))

                if (!isOverlayHidden) {
                    Box(modifier = Modifier.height(30.dp).weight(1f))
                }
            }
        }
    }

    // FIXED PLAYHEAD LINE & HANDLE OVERLAY (#34D399, 2dp line, 10dp circle top handle)
    val playheadX = headerWidthDp + centerPaddingDp
    Box(
        modifier = Modifier
            .offset(x = playheadX - 1.dp)
            .fillMaxHeight()
            .width(2.dp)
            .background(Color(0xFF34D399))
    )

    Box(
        modifier = Modifier
            .offset(x = playheadX - 5.dp, y = 2.dp)
            .size(10.dp)
            .clip(CircleShape)
            .background(Color(0xFF34D399))
            .border(BorderStroke(1.5.dp, TextWhite), CircleShape)
    )
}
}
}

@Composable
private fun TimelineAddPill(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceCard)
            .border(BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = label, tint = accentColor, modifier = Modifier.size(14.dp))
            Text(label, color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LayerTrackHeader(
    icon: ImageVector,
    color: Color,
    heightDp: Dp = 30.dp,
    isHidden: Boolean = false,
    isLocked: Boolean = false,
    isMuted: Boolean = false,
    onToggleHide: () -> Unit = {},
    onToggleLock: (() -> Unit)? = null,
    onToggleMute: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(heightDp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF131520))
            .border(BorderStroke(1.dp, Color(0xFF222536)), RoundedCornerShape(6.dp))
            .clickable { onToggleHide() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isHidden) Icons.Default.VisibilityOff else icon,
            contentDescription = null,
            tint = if (isHidden) Color(0xFFEF4444) else color,
            modifier = Modifier.size(16.dp)
        )

        if (isMuted || isLocked) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEF4444))
            )
        }
    }
}

@Composable
private fun MediaAddOptionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCardLight)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = SoftViolet, modifier = Modifier.size(20.dp))
        Text(label, color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

// ============================================================================
// TOP BAR HEADER COMPOSABLE
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarHeader(
    projectName: String,
    selectedResolution: String,
    showResolutionDropdown: Boolean,
    onToggleResolutionDropdown: (Boolean) -> Unit,
    onSelectResolution: (String) -> Unit,
    onBack: () -> Unit,
    onExport: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = DarkBg,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Project Title + Auto-Save Dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(GreenSaveDot)
                )

                Text(
                    text = projectName,
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Right Group: Resolution Picker + Export Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Resolution Selector Dropdown
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .clickable { onToggleResolutionDropdown(!showResolutionDropdown) }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = selectedResolution,
                            color = SoftViolet,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Resolution",
                            tint = SoftViolet,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showResolutionDropdown,
                        onDismissRequest = { onToggleResolutionDropdown(false) },
                        modifier = Modifier.background(SurfaceCard)
                    ) {
                        listOf("720p", "1080p", "2K", "4K").forEach { res ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        res,
                                        color = if (res == selectedResolution) SoftViolet else TextWhite,
                                        fontSize = 11.sp,
                                        fontWeight = if (res == selectedResolution) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    onSelectResolution(res)
                                    onToggleResolutionDropdown(false)
                                }
                            )
                        }
                    }
                }

                // Export Button (Solid Purple #7C4DFF, 14dp Radius, Premium Glass Effect)
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF7C4DFF))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)), RoundedCornerShape(14.dp))
                        .clickable { onExport() }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.IosShare,
                            contentDescription = "Export Video",
                            tint = TextWhite,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Export",
                            color = TextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// PREVIEW CANVAS AREA COMPOSABLE
// ============================================================================
@Composable
private fun PreviewCanvasArea(
    selectedAspectRatio: String,
    canvasBgMode: CanvasBgMode,
    previewScale: Float,
    onUpdatePreviewScale: (Float) -> Unit,
    isPreviewFilled: Boolean,
    onTogglePreviewFilled: () -> Unit,
    activeClip: TimelineClip?,
    selectedClipId: String?,
    textTracks: List<TextTrackItem>,
    stickerTracks: List<StickerTrackItem> = emptyList(),
    drawingTracks: List<DrawingTrackItem> = emptyList(),
    currentPlayheadSec: Double,
    isPlaying: Boolean = false,
    playbackSpeed: Float = 1.0f,
    isSafeAreaEnabled: Boolean,
    isGridEnabled: Boolean,
    isPreviewMuted: Boolean,
    tempPreviewFilter: com.example.ui.components.FilterItem? = null,
    tempPreviewEffect: com.example.ui.components.EffectItem? = null,
    isCompareHolding: Boolean = false,
    onToggleSafeArea: () -> Unit,
    onToggleGrid: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onOpenInspector: () -> Unit = {},
    onUpdateTransform: (Float, Float, Float, Float) -> Unit = { _, _, _, _ -> },
    onRotateClip: () -> Unit = {},
    onMirrorClip: () -> Unit = {},
    onPlayheadUpdate: (Double) -> Unit = {},
    onPlaybackEnded: () -> Unit = {}
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    var showFloatingControls by remember { mutableStateOf(false) }

    LaunchedEffect(selectedClipId) {
        if (selectedClipId == null) {
            showFloatingControls = false
        }
    }

    var snapCenterX by remember { mutableStateOf(false) }
    var snapCenterY by remember { mutableStateOf(false) }

    val canvasBgColor = when (canvasBgMode) {
        CanvasBgMode.AMOLED_BLACK -> Color.Black
        CanvasBgMode.STUDIO_WHITE -> Color(0xFFF3F4F6)
        CanvasBgMode.DARK_INDIGO -> Color(0xFF1E1B4B)
        CanvasBgMode.CHECKERBOARD -> Color(0xFF181824)
        CanvasBgMode.BLUR_GRADIENT -> Color(0xFF0F0E17)
    }

    val aspectModifier = when (selectedAspectRatio) {
        "9:16" -> Modifier.aspectRatio(9f / 16f)
        "16:9" -> Modifier.aspectRatio(16f / 9f)
        "1:1" -> Modifier.aspectRatio(1f)
        "4:5" -> Modifier.aspectRatio(4f / 5f)
        "21:9" -> Modifier.aspectRatio(21f / 9f)
        else -> Modifier.aspectRatio(9f / 16f)
    }

    val activeColorFilter = remember(tempPreviewFilter, activeClip?.filterName, isCompareHolding) {
        if (isCompareHolding) null
        else if (tempPreviewFilter != null) androidx.compose.ui.graphics.ColorFilter.colorMatrix(tempPreviewFilter.colorMatrix)
        else if (activeClip != null && activeClip.filterName != "none") {
            val f = com.example.ui.components.FilterMatrixRepository.getFilterById(activeClip.filterName)
            androidx.compose.ui.graphics.ColorFilter.colorMatrix(f.colorMatrix)
        } else null
    }

    val activeEffectOverlayColor = remember(tempPreviewEffect, activeClip?.effectName, isCompareHolding) {
        if (isCompareHolding) Color.Transparent
        else if (tempPreviewEffect != null) tempPreviewEffect.colorOverlay
        else if (activeClip != null && activeClip.effectName != "e_none") {
            val e = com.example.ui.components.EffectRepository.getEffectById(activeClip.effectName)
            e.colorOverlay
        } else Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050507)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .then(aspectModifier)
                .clip(RoundedCornerShape(12.dp))
                .background(canvasBgColor)
                .border(
                    BorderStroke(
                        if (selectedClipId != null) 1.5.dp else 1.dp,
                        if (selectedClipId != null) MintPrimary else Color(0xFF27273A)
                    ),
                    RoundedCornerShape(12.dp)
                )
                .pointerInput(selectedClipId, activeClip) {
                    detectTransformGestures { _, pan, zoom, rotation ->
                        if (activeClip != null) {
                            showFloatingControls = true
                            var newScale = (activeClip.scale * zoom).coerceIn(0.2f, 5.0f)
                            var newRotation = activeClip.rotation + rotation
                            var newOffsetX = activeClip.offsetX + pan.x
                            var newOffsetY = activeClip.offsetY + pan.y

                            if (kotlin.math.abs(newOffsetX) < 18f) {
                                newOffsetX = 0f
                                if (!snapCenterX) {
                                    snapCenterX = true
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                }
                            } else {
                                snapCenterX = false
                            }

                            if (kotlin.math.abs(newOffsetY) < 18f) {
                                newOffsetY = 0f
                                if (!snapCenterY) {
                                    snapCenterY = true
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                }
                            } else {
                                snapCenterY = false
                            }

                            onUpdateTransform(newOffsetX, newOffsetY, newScale, newRotation)
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (activeClip != null) {
                                onUpdateTransform(0f, 0f, 1.0f, 0f)
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            }
                        },
                        onTap = {
                            showFloatingControls = !showFloatingControls
                        },
                        onLongPress = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onOpenInspector()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            val clipScale = (activeClip?.scale ?: 1.0f) * previewScale
            val clipRot = activeClip?.rotation ?: 0f
            val clipX = activeClip?.offsetX ?: 0f
            val clipY = activeClip?.offsetY ?: 0f
            val clipAlpha = activeClip?.opacity ?: 1.0f

            val context = LocalContext.current
            val videoImageLoader = remember(context) {
                coil.ImageLoader.Builder(context)
                    .components {
                        add(VideoFrameDecoder.Factory())
                    }
                    .build()
            }

            val targetFrameMicros = remember(currentPlayheadSec, activeClip) {
                val sec = (currentPlayheadSec + (activeClip?.startTrimSec ?: 0.0)).coerceAtLeast(0.0)
                (sec * 1_000_000.0).toLong()
            }

            val videoImageRequest = remember(activeClip?.mediaUri, targetFrameMicros) {
                ImageRequest.Builder(context)
                    .data(activeClip?.mediaUri)
                    .videoFrameMicros(targetFrameMicros)
                    .decoderFactory(VideoFrameDecoder.Factory())
                    .crossfade(false)
                    .build()
            }

            if (activeClip?.mediaUri != null) {
                ExoPlayerVideoPreview(
                    mediaUri = activeClip.mediaUri,
                    currentPlayheadSec = currentPlayheadSec,
                    startTrimSec = activeClip.startTrimSec,
                    isPlaying = isPlaying,
                    playbackSpeed = playbackSpeed,
                    contentScale = if (isPreviewFilled) ContentScale.Crop else ContentScale.Fit,
                    colorFilter = activeColorFilter,
                    onPlayheadUpdate = onPlayheadUpdate,
                    onPlaybackEnded = onPlaybackEnded,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            translationX = clipX,
                            translationY = clipY,
                            scaleX = clipScale * (if (activeClip.isFlippedHorizontal) -1f else 1f),
                            scaleY = clipScale * (if (activeClip.isFlippedVertical) -1f else 1f),
                            rotationZ = clipRot,
                            alpha = clipAlpha
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1A1829), Color(0xFF0F0E18))
                            )
                        )
                        .graphicsLayer(
                            translationX = clipX,
                            translationY = clipY,
                            scaleX = clipScale * (if (activeClip?.isFlippedHorizontal == true) -1f else 1f),
                            scaleY = clipScale * (if (activeClip?.isFlippedVertical == true) -1f else 1f),
                            rotationZ = clipRot,
                            alpha = clipAlpha
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = MintPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = activeClip?.title ?: "Main Shot",
                            color = TextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Pinch to scale • Drag to position • Double tap reset",
                            color = TextGray,
                            fontSize = 9.sp
                        )
                    }
                }
            }

            if (selectedClipId != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(BorderStroke(1.5.dp, MintPrimary))
                ) {
                    Box(modifier = Modifier.size(10.dp).align(Alignment.TopStart).background(Color.White, CircleShape).border(1.5.dp, MintPrimary, CircleShape))
                    Box(modifier = Modifier.size(10.dp).align(Alignment.TopEnd).background(Color.White, CircleShape).border(1.5.dp, MintPrimary, CircleShape))
                    Box(modifier = Modifier.size(10.dp).align(Alignment.BottomStart).background(Color.White, CircleShape).border(1.5.dp, MintPrimary, CircleShape))
                    Box(modifier = Modifier.size(10.dp).align(Alignment.BottomEnd).background(Color.White, CircleShape).border(1.5.dp, MintPrimary, CircleShape))

                    Box(modifier = Modifier.size(8.dp).align(Alignment.TopCenter).background(Color.White, CircleShape).border(1.dp, MintPrimary, CircleShape))
                    Box(modifier = Modifier.size(8.dp).align(Alignment.BottomCenter).background(Color.White, CircleShape).border(1.dp, MintPrimary, CircleShape))
                    Box(modifier = Modifier.size(8.dp).align(Alignment.CenterStart).background(Color.White, CircleShape).border(1.dp, MintPrimary, CircleShape))
                    Box(modifier = Modifier.size(8.dp).align(Alignment.CenterEnd).background(Color.White, CircleShape).border(1.dp, MintPrimary, CircleShape))

                    Box(modifier = Modifier.size(6.dp).align(Alignment.Center).background(MintPrimary, CircleShape))

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-18).dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MintPrimary)
                            .clickable { onRotateClip() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RotateRight,
                            contentDescription = "Rotate Handle",
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            if (snapCenterX) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.5.dp)
                        .background(Color(0xFF06B6D4))
                        .align(Alignment.Center)
                )
            }
            if (snapCenterY) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.5.dp)
                        .background(Color(0xFF06B6D4))
                        .align(Alignment.Center)
                )
            }

            if (activeEffectOverlayColor != Color.Transparent) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(activeEffectOverlayColor)
                )
            }

            if (isGridEnabled) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val gridColor = Color.White.copy(alpha = 0.25f)
                    val stroke = 1.dp.toPx()

                    drawLine(gridColor, start = Offset(w / 3f, 0f), end = Offset(w / 3f, h), strokeWidth = stroke)
                    drawLine(gridColor, start = Offset((w * 2f) / 3f, 0f), end = Offset((w * 2f) / 3f, h), strokeWidth = stroke)
                    drawLine(gridColor, start = Offset(0f, h / 3f), end = Offset(w, h / 3f), strokeWidth = stroke)
                    drawLine(gridColor, start = Offset(0f, (h * 2f) / 3f), end = Offset(w, (h * 2f) / 3f), strokeWidth = stroke)
                }
            }

            if (isSafeAreaEnabled) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .border(BorderStroke(1.dp, MintPrimary.copy(alpha = 0.7f)), RoundedCornerShape(8.dp))
                )
            }

            drawingTracks.forEach { drw ->
                if (!drw.isHidden && currentPlayheadSec >= drw.startSec && currentPlayheadSec <= (drw.startSec + drw.durationSec)) {
                    val parsedColor = runCatching { Color(android.graphics.Color.parseColor(drw.strokeColorHex)) }.getOrDefault(MintPrimary)
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        if (drw.points.size > 1) {
                            val path = Path().apply {
                                moveTo(drw.points.first().first, drw.points.first().second)
                                for (i in 1 until drw.points.size) {
                                    lineTo(drw.points[i].first, drw.points[i].second)
                                }
                            }
                            drawPath(
                                path = path,
                                color = parsedColor.copy(alpha = drw.opacity),
                                style = Stroke(width = drw.strokeWidthDp.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                        }
                    }
                }
            }

            stickerTracks.forEach { stk ->
                if (!stk.isHidden && currentPlayheadSec >= stk.startSec && currentPlayheadSec <= (stk.startSec + stk.durationSec)) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .graphicsLayer(
                                translationX = stk.positionX,
                                translationY = stk.positionY,
                                scaleX = stk.scale,
                                scaleY = stk.scale,
                                rotationZ = stk.rotation,
                                alpha = stk.opacity
                            )
                    ) {
                        Text(
                            text = stk.stickerEmoji,
                            fontSize = 36.sp
                        )
                    }
                }
            }

            textTracks.forEach { txt ->
                if (!txt.isHidden && currentPlayheadSec >= txt.startSec && currentPlayheadSec <= (txt.startSec + txt.durationSec)) {
                    val parsedTextColor = runCatching { Color(android.graphics.Color.parseColor(txt.textColorHex)) }.getOrDefault(Color.White)
                    val parsedBgColor = runCatching { Color(android.graphics.Color.parseColor(txt.bgColorHex)) }.getOrDefault(Color.Transparent)
                    val fontFamilyVal = when (txt.styleName) {
                        "Playfair Serif" -> FontFamily.Serif
                        "Cyber Tech" -> FontFamily.Monospace
                        "Handwritten" -> FontFamily.Cursive
                        else -> FontFamily.SansSerif
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .graphicsLayer(
                                translationX = txt.positionX,
                                translationY = txt.positionY,
                                scaleX = txt.scale,
                                scaleY = txt.scale,
                                rotationZ = txt.rotation,
                                alpha = txt.opacity
                            )
                            .clip(RoundedCornerShape(txt.bgRadiusDp.dp))
                            .background(parsedBgColor)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = txt.text,
                            color = parsedTextColor,
                            fontSize = txt.fontSizeSp.sp,
                            fontFamily = fontFamilyVal,
                            fontWeight = if (txt.isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (txt.isItalic) FontStyle.Italic else FontStyle.Normal,
                            textDecoration = if (txt.isUnderline) TextDecoration.Underline else TextDecoration.None,
                            textAlign = when (txt.alignment) {
                                "Left" -> TextAlign.Left
                                "Right" -> TextAlign.Right
                                else -> TextAlign.Center
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = showFloatingControls && selectedClipId != null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, MintPrimary.copy(alpha = 0.5f)),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onUpdateTransform(0f, 0f, 1.0f, activeClip?.rotation ?: 0f) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FitScreen, contentDescription = "Fit", tint = TextWhite, modifier = Modifier.size(20.dp))
                        }
                        IconButton(
                            onClick = { onTogglePreviewFilled() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Crop, contentDescription = "Fill", tint = TextWhite, modifier = Modifier.size(20.dp))
                        }
                        IconButton(
                            onClick = { onRotateClip() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.RotateRight, contentDescription = "Rotate", tint = TextWhite, modifier = Modifier.size(20.dp))
                        }
                        IconButton(
                            onClick = { onMirrorClip() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Flip, contentDescription = "Mirror", tint = TextWhite, modifier = Modifier.size(20.dp))
                        }
                        IconButton(
                            onClick = { onOpenInspector() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Opacity, contentDescription = "Opacity", tint = MintPrimary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onToggleSafeArea,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (isSafeAreaEnabled) MintPrimary.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.CropFree,
                        contentDescription = "Safe Area",
                        tint = if (isSafeAreaEnabled) Color.Black else TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }

                IconButton(
                    onClick = onToggleGrid,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (isGridEnabled) MintPrimary.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = "Grid",
                        tint = if (isGridEnabled) Color.Black else TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }

                IconButton(
                    onClick = onToggleMute,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (isPreviewMuted) Color(0xFFEF4444).copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = if (isPreviewMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Mute",
                        tint = TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }

                IconButton(
                    onClick = onToggleFullscreen,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Fullscreen",
                        tint = TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

// ============================================================================
// CONTROL BAR COMPOSABLE (Directly below preview)
// ============================================================================
@Composable
private fun ControlBar(
    currentPlayheadSec: Double,
    totalDurationSec: Double,
    isPlaying: Boolean,
    playbackSpeed: Float,
    isLoopEnabled: Boolean = false,
    undoEnabled: Boolean,
    redoEnabled: Boolean,
    onSeek: (Double) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onPrevFrame: () -> Unit,
    onNextFrame: () -> Unit,
    onJumpStart: () -> Unit = {},
    onJumpEnd: () -> Unit = {},
    onTogglePlay: () -> Unit,
    onToggleLoop: () -> Unit = {},
    onOpenSpeedSheet: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF050507)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Undo & Redo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(
                    onClick = onUndo,
                    enabled = undoEnabled,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (undoEnabled) Color(0xFF1A1A24) else Color(0xFF1A1A24).copy(alpha = 0.4f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Undo",
                        tint = if (undoEnabled) TextWhite else TextGray.copy(alpha = 0.4f),
                        modifier = Modifier.size(15.dp)
                    )
                }

                IconButton(
                    onClick = onRedo,
                    enabled = redoEnabled,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (redoEnabled) Color(0xFF1A1A24) else Color(0xFF1A1A24).copy(alpha = 0.4f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Redo,
                        contentDescription = "Redo",
                        tint = if (redoEnabled) TextWhite else TextGray.copy(alpha = 0.4f),
                        modifier = Modifier.size(15.dp)
                    )
                }

                Text(
                    text = "${formatTimecode(currentPlayheadSec)} / ${formatTimecode(totalDurationSec)}",
                    color = MintPrimary,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // Playback Controls (Prev Frame, Play/Pause, Next Frame)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onPrevFrame,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1A1A24))
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Frame",
                        tint = TextWhite,
                        modifier = Modifier.size(15.dp)
                    )
                }

                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(MintPrimary, Color(0xFF059669))
                            )
                        )
                        .shadow(4.dp, spotColor = MintGlow)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onNextFrame,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1A1A24))
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Frame",
                        tint = TextWhite,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Playback Speed Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1A1A24))
                    .border(BorderStroke(1.dp, MintPrimary.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                    .clickable { onOpenSpeedSheet() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${playbackSpeed}x",
                    color = MintPrimary,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ============================================================================
// BOTTOM INSPECTOR PANEL COMPOSABLE (Phase 4 CapCut Style Inspector)
// ============================================================================
@Composable
private fun BottomInspectorPanel(
    activeClip: TimelineClip?,
    selectedClipIds: Set<String>,
    isMultiSelectMode: Boolean,
    onUpdateClip: (TimelineClip) -> Unit,
    onBringForward: () -> Unit,
    onSendBackward: () -> Unit,
    onBringToFront: () -> Unit,
    onSendToBack: () -> Unit,
    onDeleteSelected: () -> Unit,
    onDuplicateSelected: () -> Unit,
    onGroupSelected: () -> Unit,
    onUngroupSelected: () -> Unit,
    onResetTransform: () -> Unit,
    onCloseInspector: () -> Unit
) {
    if (activeClip == null) return

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF0D0D12),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        border = BorderStroke(1.dp, MintPrimary.copy(alpha = 0.3f)),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MintPrimary)
                    )
                    Text(
                        text = if (isMultiSelectMode) "Group Inspector (${selectedClipIds.size})" else "Inspector: ${activeClip.title}",
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onResetTransform,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Reset", color = MintPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = onCloseInspector,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close Inspector", tint = TextGray, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Divider(color = Color(0xFF222230), thickness = 1.dp)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .width(160.dp)
                            .background(Color(0xFF161622), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Position (X / Y)", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("X: ${activeClip.offsetX.toInt()}px", color = TextWhite, fontSize = 11.sp)
                        Slider(
                            value = activeClip.offsetX,
                            onValueChange = { onUpdateClip(activeClip.copy(offsetX = it)) },
                            valueRange = -200f..200f,
                            colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                            modifier = Modifier.height(16.dp)
                        )
                        Text("Y: ${activeClip.offsetY.toInt()}px", color = TextWhite, fontSize = 11.sp)
                        Slider(
                            value = activeClip.offsetY,
                            onValueChange = { onUpdateClip(activeClip.copy(offsetY = it)) },
                            valueRange = -200f..200f,
                            colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                            modifier = Modifier.height(16.dp)
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .width(160.dp)
                            .background(Color(0xFF161622), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Scale & Rotation", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Scale: ${(activeClip.scale * 100).toInt()}%", color = TextWhite, fontSize = 11.sp)
                        Slider(
                            value = activeClip.scale,
                            onValueChange = { onUpdateClip(activeClip.copy(scale = it)) },
                            valueRange = 0.2f..3.0f,
                            colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                            modifier = Modifier.height(16.dp)
                        )
                        Text("Rotate: ${activeClip.rotation.toInt()}°", color = TextWhite, fontSize = 11.sp)
                        Slider(
                            value = activeClip.rotation,
                            onValueChange = { onUpdateClip(activeClip.copy(rotation = it)) },
                            valueRange = -180f..180f,
                            colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                            modifier = Modifier.height(16.dp)
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .width(140.dp)
                            .background(Color(0xFF161622), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Opacity", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("${(activeClip.opacity * 100).toInt()}%", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Slider(
                            value = activeClip.opacity,
                            onValueChange = { onUpdateClip(activeClip.copy(opacity = it)) },
                            valueRange = 0f..1.0f,
                            colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                            modifier = Modifier.height(20.dp)
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFF161622), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Layer Ordering", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(
                                onClick = onBringForward,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222230)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Forward", color = TextWhite, fontSize = 10.sp)
                            }
                            Button(
                                onClick = onSendBackward,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222230)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Backward", color = TextWhite, fontSize = 10.sp)
                            }
                            Button(
                                onClick = onBringToFront,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222230)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("To Front", color = MintPrimary, fontSize = 10.sp)
                            }
                            Button(
                                onClick = onSendToBack,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222230)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("To Back", color = TextWhite, fontSize = 10.sp)
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFF161622), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Actions", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(
                                onClick = { onUpdateClip(activeClip.copy(isFlippedHorizontal = !activeClip.isFlippedHorizontal)) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222230)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Flip H", color = TextWhite, fontSize = 10.sp)
                            }
                            Button(
                                onClick = onDuplicateSelected,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222230)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Duplicate", color = TextWhite, fontSize = 10.sp)
                            }
                            Button(
                                onClick = onDeleteSelected,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF991B1B)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Delete", color = Color.White, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// EDIT BOTTOM SHEET PANEL COMPOSABLE (Phase 5 CapCut Professional Edit Panel)
// Compact 4-column responsive grid, 3 Pages (34 tools total), Slide Up 220ms
// ============================================================================
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EditBottomSheetPanel(
    activeClip: TimelineClip?,
    clipsCount: Int,
    currentPlayheadSec: Double,
    totalDurationSec: Double,
    onToolClick: (String) -> Unit,
    onClose: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color(0xFF0D0E15))
            .border(
                BorderStroke(1.dp, MintPrimary.copy(alpha = 0.35f)),
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ),
        color = Color(0xFF0D0E15)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Drag Handle Bar
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF333344))
                    .align(Alignment.CenterHorizontally)
            )

            // Header Bar
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
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MintPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCut,
                            contentDescription = null,
                            tint = MintGlow,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Professional Edit Workspace",
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = activeClip?.title ?: "Clip Selected",
                            color = TextGray,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF222230))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Page Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF161722))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val pageTitles = listOf("1. Timing & Basics", "2. FX & Transform", "3. Audio & AI")
                pageTitles.forEachIndexed { index, title ->
                    val isSelected = currentPage == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) MintPrimary.copy(alpha = 0.25f) else Color.Transparent)
                            .border(
                                BorderStroke(1.dp, if (isSelected) MintPrimary else Color.Transparent),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { currentPage = index }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) MintGlow else TextGray,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // 4-COLUMN COMPACT GRID FOR CURRENT PAGE
            val toolsForPage = when (currentPage) {
                0 -> listOf(
                    "Split" to Icons.Default.CallSplit,
                    "Trim" to Icons.Default.ContentCut,
                    "Delete" to Icons.Default.Delete,
                    "Duplicate" to Icons.Default.ContentCopy,
                    "Replace" to Icons.Default.SwapHoriz,
                    "Speed" to Icons.Default.Speed,
                    "Curve Speed" to Icons.Default.ShowChart,
                    "Freeze" to Icons.Default.AcUnit,
                    "Reverse" to Icons.Default.FastRewind,
                    "Crop" to Icons.Default.Crop,
                    "Rotate" to Icons.Default.RotateRight,
                    "Flip" to Icons.Default.Flip
                )
                1 -> listOf(
                    "Opacity" to Icons.Default.Opacity,
                    "Transform" to Icons.Default.Transform,
                    "Keyframes" to Icons.Default.Animation,
                    "Mask" to Icons.Default.Masks,
                    "Motion Tracking" to Icons.Default.CenterFocusWeak,
                    "Stabilize" to Icons.Default.CameraRoll,
                    "Auto Reframe" to Icons.Default.AspectRatio,
                    "Video Quality" to Icons.Default.HighQuality,
                    "HDR Enhance" to Icons.Default.HdrOn,
                    "Retouch" to Icons.Default.FaceRetouchingNatural,
                    "Remove BG" to Icons.Default.PersonRemove,
                    "Mirror" to Icons.Default.FlipToBack
                )
                else -> listOf(
                    "Extract Audio" to Icons.Default.GraphicEq,
                    "Unlink Audio" to Icons.Default.LinkOff,
                    "Voice Isolation" to Icons.Default.RecordVoiceOver,
                    "Noise Reduction" to Icons.Default.HearingDisabled,
                    "Voice Enhance" to Icons.Default.Mic,
                    "Translator" to Icons.Default.Translate,
                    "Audio Effects" to Icons.Default.GraphicEq,
                    "Normalize Audio" to Icons.Default.Tune,
                    "Volume" to Icons.Default.VolumeUp,
                    "Fade" to Icons.Default.LinearScale
                )
            }

            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(toolsForPage) { (label, icon) ->
                    val isDelete = label == "Delete"
                    val isAccent = label in listOf("Split", "Trim", "Speed", "Curve Speed", "Keyframes", "Mask", "Translator")

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(62.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                when {
                                    isDelete -> Color(0xFF3B1818)
                                    isAccent -> MintPrimary.copy(alpha = 0.18f)
                                    else -> Color(0xFF1B1C28)
                                }
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    when {
                                        isDelete -> Color(0xFF991B1B)
                                        isAccent -> MintPrimary.copy(alpha = 0.5f)
                                        else -> Color(0xFF282A3C)
                                    }
                                ),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onToolClick(label) }
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = when {
                                isDelete -> Color(0xFFEF4444)
                                isAccent -> MintGlow
                                else -> TextWhite
                            },
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = label,
                            color = if (isDelete) Color(0xFFEF4444) else TextWhite,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// BOTTOM TOOLBAR ROW COMPOSABLE (CapCut Standard 11 Primary Tools)
// Edit | Audio | Text | Effects | Filters | Overlay | Caption | Adjust | Sticker | Canvas | Templates
// ============================================================================
@Composable
private fun BottomToolbarRow(
    activeMainTool: EditorActiveTool,
    onSelectTool: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = DarkBg,
        shadowElevation = 8.dp
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tools = listOf(
                "Edit" to Icons.Default.ContentCut,
                "Audio" to Icons.Default.MusicNote,
                "Text" to Icons.Default.Title,
                "Effects" to Icons.Default.AutoAwesome,
                "Filters" to Icons.Default.FilterBAndW,
                "Overlay" to Icons.Default.Layers,
                "Caption" to Icons.Default.ClosedCaption,
                "Adjust" to Icons.Default.Tune,
                "Sticker" to Icons.Default.EmojiEmotions,
                "Canvas" to Icons.Default.AspectRatio,
                "Templates" to Icons.Default.Dashboard
            )

            items(tools) { (label, icon) ->
                val isActive = activeMainTool.name.equals(label, ignoreCase = true)

                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isActive) MintPrimary.copy(alpha = 0.25f) else Color.Transparent)
                        .clickable {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            onSelectTool(label)
                        }
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isActive) MintGlow else TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = label,
                            color = if (isActive) MintGlow else TextGray,
                            fontSize = 10.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// PHASE 8 PREMIUM INLINE EDIT TOOLBAR & SLIDER PANELS (CapCut Pro Style)
// ============================================================================

@Composable
private fun ClipEditToolbarRow(
    activeClip: TimelineClip?,
    onToolClick: (String) -> Unit,
    onCloseEdit: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color(0xFF0D0E15),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onCloseEdit,
                modifier = Modifier
                    .padding(start = 6.dp, end = 2.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B1C28))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Done",
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp)
                    .background(Color(0xFF282A3C))
            )

            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val tools = listOf(
                    "Split" to Icons.Default.CallSplit,
                    "Trim" to Icons.Default.ContentCut,
                    "Speed" to Icons.Default.Speed,
                    "Volume" to Icons.Default.VolumeUp,
                    "Animation" to Icons.Default.Animation,
                    "Effects" to Icons.Default.AutoAwesome,
                    "Adjust" to Icons.Default.Tune,
                    "Filters" to Icons.Default.FilterBAndW,
                    "Opacity" to Icons.Default.Opacity,
                    "Transform" to Icons.Default.Transform,
                    "Crop" to Icons.Default.Crop,
                    "Mask" to Icons.Default.Masks,
                    "Retouch" to Icons.Default.FaceRetouchingNatural,
                    "Delete" to Icons.Default.Delete,
                    "Duplicate" to Icons.Default.ContentCopy,
                    "Replace" to Icons.Default.SwapHoriz,
                    "Reverse" to Icons.Default.FastRewind,
                    "Freeze" to Icons.Default.AcUnit,
                    "Extract Audio" to Icons.Default.GraphicEq,
                    "Voice" to Icons.Default.RecordVoiceOver,
                    "Noise" to Icons.Default.HearingDisabled,
                    "Enhance" to Icons.Default.HdrOn,
                    "Remove BG" to Icons.Default.PersonRemove,
                    "Stabilize" to Icons.Default.CameraRoll,
                    "Translator" to Icons.Default.Translate
                )

                items(tools) { (label, icon) ->
                    val isDelete = label == "Delete"
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDelete) Color(0xFF3B1818) else Color(0xFF1B1C28))
                            .border(
                                BorderStroke(1.dp, if (isDelete) Color(0xFF991B1B) else Color(0xFF282A3C)),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                onToolClick(label)
                            }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isDelete) Color(0xFFEF4444) else TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = label,
                                color = if (isDelete) Color(0xFFEF4444) else TextWhite,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PremiumAdjustSlider(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    label: String,
    displayValue: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = TextWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MintPrimary.copy(alpha = 0.15f))
                    .border(BorderStroke(1.dp, MintPrimary.copy(alpha = 0.4f)), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = displayValue,
                    color = MintPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .shadow(2.dp, CircleShape)
                )
            },
            track = { sliderState ->
                val start = sliderState.valueRange.start
                val end = sliderState.valueRange.endInclusive
                val rangeLen = (end - start).coerceAtLeast(0.001f)
                val fraction = ((sliderState.value - start) / rangeLen).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF222433))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction)
                            .background(MintPrimary)
                    )
                }
            }
        )
    }
}

@Composable
private fun AdjustPanelBottomSheet(
    activeClip: TimelineClip?,
    onUpdateClip: (TimelineClip) -> Unit,
    onApplyToAll: (TimelineClip) -> Unit,
    onClose: () -> Unit
) {
    if (activeClip == null) return
    var activeParam by remember { mutableStateOf("Brightness") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        color = Color(0xFF0D0E15),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Adjustments", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = {
                            onUpdateClip(
                                activeClip.copy(
                                    brightness = 0f, contrast = 1f, saturation = 1f, highlights = 0f,
                                    shadows = 0f, sharpen = 0f, temperature = 0f, tint = 0f,
                                    fade = 0f, exposure = 0f, gamma = 1f, vignette = 0f, grain = 0f
                                )
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Reset All", color = TextGray, fontSize = 11.sp)
                    }

                    TextButton(
                        onClick = { onApplyToAll(activeClip) },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Apply To All", color = MintPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MintPrimary.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Done", tint = MintPrimary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            val params = listOf(
                "Brightness", "Contrast", "Saturation", "Highlights", "Shadows",
                "Sharpen", "Temperature", "Tint", "Fade", "Exposure", "Gamma", "Vignette", "Grain"
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(params) { p ->
                    val isSel = activeParam == p
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) MintPrimary.copy(alpha = 0.25f) else Color(0xFF1A1C28))
                            .border(BorderStroke(1.dp, if (isSel) MintPrimary else Color(0xFF282A3C)), RoundedCornerShape(6.dp))
                            .clickable { activeParam = p }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(p, color = if (isSel) MintPrimary else TextWhite, fontSize = 10.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium)
                    }
                }
            }

            when (activeParam) {
                "Brightness" -> PremiumAdjustSlider(
                    value = activeClip.brightness,
                    valueRange = -1f..1f,
                    label = "Brightness",
                    displayValue = "${(activeClip.brightness * 100).toInt()}",
                    onValueChange = { onUpdateClip(activeClip.copy(brightness = it)) }
                )
                "Contrast" -> PremiumAdjustSlider(
                    value = activeClip.contrast,
                    valueRange = 0.5f..2.0f,
                    label = "Contrast",
                    displayValue = "${(activeClip.contrast * 100).toInt()}%",
                    onValueChange = { onUpdateClip(activeClip.copy(contrast = it)) }
                )
                "Saturation" -> PremiumAdjustSlider(
                    value = activeClip.saturation,
                    valueRange = 0.0f..2.0f,
                    label = "Saturation",
                    displayValue = "${(activeClip.saturation * 100).toInt()}%",
                    onValueChange = { onUpdateClip(activeClip.copy(saturation = it)) }
                )
                "Highlights" -> PremiumAdjustSlider(
                    value = activeClip.highlights,
                    valueRange = -100f..100f,
                    label = "Highlights",
                    displayValue = "${activeClip.highlights.toInt()}",
                    onValueChange = { onUpdateClip(activeClip.copy(highlights = it)) }
                )
                "Shadows" -> PremiumAdjustSlider(
                    value = activeClip.shadows,
                    valueRange = -100f..100f,
                    label = "Shadows",
                    displayValue = "${activeClip.shadows.toInt()}",
                    onValueChange = { onUpdateClip(activeClip.copy(shadows = it)) }
                )
                "Sharpen" -> PremiumAdjustSlider(
                    value = activeClip.sharpen,
                    valueRange = 0f..100f,
                    label = "Sharpen",
                    displayValue = "${activeClip.sharpen.toInt()}",
                    onValueChange = { onUpdateClip(activeClip.copy(sharpen = it)) }
                )
                "Temperature" -> PremiumAdjustSlider(
                    value = activeClip.temperature,
                    valueRange = -100f..100f,
                    label = "Temperature (K)",
                    displayValue = "${activeClip.temperature.toInt()}",
                    onValueChange = { onUpdateClip(activeClip.copy(temperature = it)) }
                )
                "Tint" -> PremiumAdjustSlider(
                    value = activeClip.tint,
                    valueRange = -100f..100f,
                    label = "Tint",
                    displayValue = "${activeClip.tint.toInt()}",
                    onValueChange = { onUpdateClip(activeClip.copy(tint = it)) }
                )
                "Fade" -> PremiumAdjustSlider(
                    value = activeClip.fade,
                    valueRange = 0f..100f,
                    label = "Fade",
                    displayValue = "${activeClip.fade.toInt()}%",
                    onValueChange = { onUpdateClip(activeClip.copy(fade = it)) }
                )
                "Exposure" -> PremiumAdjustSlider(
                    value = activeClip.exposure,
                    valueRange = -100f..100f,
                    label = "Exposure",
                    displayValue = "${activeClip.exposure.toInt()}",
                    onValueChange = { onUpdateClip(activeClip.copy(exposure = it)) }
                )
                "Gamma" -> PremiumAdjustSlider(
                    value = activeClip.gamma,
                    valueRange = 0.5f..2.0f,
                    label = "Gamma",
                    displayValue = String.format(Locale.US, "%.2f", activeClip.gamma),
                    onValueChange = { onUpdateClip(activeClip.copy(gamma = it)) }
                )
                "Vignette" -> PremiumAdjustSlider(
                    value = activeClip.vignette,
                    valueRange = 0f..100f,
                    label = "Vignette",
                    displayValue = "${activeClip.vignette.toInt()}%",
                    onValueChange = { onUpdateClip(activeClip.copy(vignette = it)) }
                )
                else -> PremiumAdjustSlider(
                    value = activeClip.grain,
                    valueRange = 0f..100f,
                    label = "Film Grain",
                    displayValue = "${activeClip.grain.toInt()}%",
                    onValueChange = { onUpdateClip(activeClip.copy(grain = it)) }
                )
            }
        }
    }
}

@Composable
private fun FilterPanelBottomSheet(
    activeClip: TimelineClip?,
    onApplyFilter: (String, Float) -> Unit,
    onClose: () -> Unit
) {
    com.example.ui.components.CapCutProMarketplaceSheet(
        initialTab = com.example.ui.components.AssetType.FILTER,
        activeFilterId = activeClip?.filterName,
        onSelectAsset = { asset ->
            onApplyFilter(asset.id, activeClip?.filterIntensity ?: 1.0f)
        },
        onClose = onClose
    )
}

@Composable
private fun EffectPanelBottomSheet(
    activeClip: TimelineClip?,
    onApplyEffect: (String, Float) -> Unit,
    onClose: () -> Unit
) {
    com.example.ui.components.CapCutProMarketplaceSheet(
        initialTab = com.example.ui.components.AssetType.EFFECT,
        activeEffectId = activeClip?.effectName,
        onSelectAsset = { asset ->
            onApplyEffect(asset.id, activeClip?.effectIntensity ?: 0.8f)
        },
        onClose = onClose
    )
}

@Composable
private fun AnimationPanelBottomSheet(
    activeClip: TimelineClip?,
    onApplyAnimation: (String, String, Float) -> Unit,
    onClose: () -> Unit
) {
    com.example.ui.components.CapCutProMarketplaceSheet(
        initialTab = com.example.ui.components.AssetType.ANIMATION,
        activeAnimId = activeClip?.entryAnimationId ?: activeClip?.exitAnimationId,
        onSelectAsset = { asset ->
            onApplyAnimation(asset.id, "IN", 0.5f)
        },
        onClose = onClose
    )
}

@Composable
private fun SpeedPanelBottomSheet(
    activeClip: TimelineClip?,
    onUpdateSpeed: (Float) -> Unit,
    onClose: () -> Unit
) {
    if (activeClip == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        color = Color(0xFF0D0E15),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Clip Speed Control", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MintPrimary.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Done", tint = MintPrimary, modifier = Modifier.size(16.dp))
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(0.5f, 1.0f, 1.5f, 2.0f, 3.0f, 5.0f).forEach { spd ->
                    val isSel = kotlin.math.abs(activeClip.speed - spd) < 0.05f
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) MintPrimary.copy(alpha = 0.25f) else Color(0xFF1A1C28))
                            .border(BorderStroke(1.dp, if (isSel) MintPrimary else Color(0xFF282A3C)), RoundedCornerShape(6.dp))
                            .clickable { onUpdateSpeed(spd) }
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${spd}x", color = if (isSel) MintPrimary else TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            PremiumAdjustSlider(
                value = activeClip.speed,
                valueRange = 0.1f..10.0f,
                label = "Speed Multiplier",
                displayValue = String.format(Locale.US, "%.1fx", activeClip.speed),
                onValueChange = { onUpdateSpeed(it) }
            )
        }
    }
}

@Composable
private fun VolumePanelBottomSheet(
    activeClip: TimelineClip?,
    onUpdateVolume: (Float) -> Unit,
    onClose: () -> Unit
) {
    if (activeClip == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        color = Color(0xFF0D0E15),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Clip Audio Volume", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MintPrimary.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Done", tint = MintPrimary, modifier = Modifier.size(16.dp))
                }
            }

            PremiumAdjustSlider(
                value = activeClip.volume,
                valueRange = 0.0f..2.0f,
                label = "Volume Level",
                displayValue = "${(activeClip.volume * 100).toInt()}%",
                onValueChange = { onUpdateVolume(it) }
            )
        }
    }
}

@Composable
private fun OpacityPanelBottomSheet(
    activeClip: TimelineClip?,
    onUpdateOpacity: (Float) -> Unit,
    onClose: () -> Unit
) {
    if (activeClip == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        color = Color(0xFF0D0E15),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Clip Opacity", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MintPrimary.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Done", tint = MintPrimary, modifier = Modifier.size(16.dp))
                }
            }

            PremiumAdjustSlider(
                value = activeClip.opacity,
                valueRange = 0.0f..1.0f,
                label = "Opacity Level",
                displayValue = "${(activeClip.opacity * 100).toInt()}%",
                onValueChange = { onUpdateOpacity(it) }
            )
        }
    }
}

@Composable
private fun CropTransformPanelBottomSheet(
    activeClip: TimelineClip?,
    onUpdateTransform: (TimelineClip) -> Unit,
    onClose: () -> Unit
) {
    if (activeClip == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        color = Color(0xFF0D0E15),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Transform & Canvas Scale", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MintPrimary.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Done", tint = MintPrimary, modifier = Modifier.size(16.dp))
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onUpdateTransform(activeClip.copy(isFlippedHorizontal = !activeClip.isFlippedHorizontal)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1C28)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Flip H", color = TextWhite, fontSize = 10.sp)
                }
                Button(
                    onClick = { onUpdateTransform(activeClip.copy(rotation = (activeClip.rotation + 90f) % 360f)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1C28)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Rotate 90°", color = TextWhite, fontSize = 10.sp)
                }
                Button(
                    onClick = { onUpdateTransform(activeClip.copy(scale = 1.0f, rotation = 0f, offsetX = 0f, offsetY = 0f)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1C28)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Reset Transform", color = MintPrimary, fontSize = 10.sp)
                }
            }

            PremiumAdjustSlider(
                value = activeClip.scale,
                valueRange = 0.2f..3.0f,
                label = "Scale",
                displayValue = "${(activeClip.scale * 100).toInt()}%",
                onValueChange = { onUpdateTransform(activeClip.copy(scale = it)) }
            )
        }
    }
}

@Composable
private fun MaskPanelBottomSheet(
    activeClip: TimelineClip?,
    onUpdateMask: (String) -> Unit,
    onClose: () -> Unit
) {
    val masks = listOf("None", "Circle", "Rectangle", "Linear", "Heart", "Star")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        color = Color(0xFF0D0E15),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Video Mask Shape", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MintPrimary.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Done", tint = MintPrimary, modifier = Modifier.size(16.dp))
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                masks.forEach { maskName ->
                    val isSel = activeClip?.maskType == maskName
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) MintPrimary.copy(alpha = 0.25f) else Color(0xFF1A1C28))
                            .border(BorderStroke(1.dp, if (isSel) MintPrimary else Color(0xFF282A3C)), RoundedCornerShape(6.dp))
                            .clickable { onUpdateMask(maskName) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(maskName, color = if (isSel) MintPrimary else TextWhite, fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun RetouchPanelBottomSheet(
    activeClip: TimelineClip?,
    onUpdateRetouch: (Float) -> Unit,
    onClose: () -> Unit
) {
    if (activeClip == null) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        color = Color(0xFF0D0E15),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("AI Face Retouch & Smooth", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MintPrimary.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Done", tint = MintPrimary, modifier = Modifier.size(16.dp))
                }
            }

            PremiumAdjustSlider(
                value = activeClip.retouchSmooth,
                valueRange = 0f..100f,
                label = "Skin Smoothing Level",
                displayValue = "${activeClip.retouchSmooth.toInt()}%",
                onValueChange = { onUpdateRetouch(it) }
            )
        }
    }
}

// ============================================================================
// EXPORT SHEET CONTENT COMPOSABLE
// ============================================================================
@Composable
private fun ExportSheetContent(
    projectName: String,
    resolution: String,
    fps: String,
    durationFormatted: String,
    onExportDone: () -> Unit
) {
    var isExporting by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(isExporting) {
        if (isExporting) {
            progress = 0f
            while (progress < 1f) {
                delay(100L)
                progress += 0.05f
            }
            delay(200L)
            onExportDone()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Project: $projectName", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Duration: $durationFormatted", color = TextGray, fontSize = 11.sp)
            Text("Format: MP4 ($resolution, $fps)", color = SoftViolet, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        if (isExporting) {
            Text("Rendering Video Frames (${(progress * 100).toInt()}%)...", color = SoftViolet, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                color = SoftViolet,
                trackColor = SurfaceCardLight
            )
        } else {
            Button(
                onClick = { isExporting = true },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryViolet),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Video Export", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

// ============================================================================
// TIMECODE FORMATTER HELPER
// ============================================================================
private fun formatTimecode(seconds: Double): String {
    val totalSec = seconds.toInt().coerceAtLeast(0)
    val mins = totalSec / 60
    val secs = totalSec % 60
    val millis = ((seconds - totalSec) * 100).toInt().coerceIn(0, 99)
    return String.format(Locale.US, "%02d:%02d.%02d", mins, secs, millis)
}

// ============================================================================
// MEDIA3 EXOPLAYER VIDEO PREVIEW COMPOSABLE
// ============================================================================
@OptIn(UnstableApi::class)
@Composable
private fun ExoPlayerVideoPreview(
    mediaUri: Uri,
    currentPlayheadSec: Double,
    startTrimSec: Double,
    isPlaying: Boolean,
    playbackSpeed: Float = 1.0f,
    contentScale: ContentScale,
    colorFilter: ColorFilter?,
    onPlayheadUpdate: (Double) -> Unit = {},
    onPlaybackEnded: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isExoError by remember { mutableStateOf(false) }

    val exoPlayer = remember(context, mediaUri) {
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(100)
            .setSeekForwardIncrementMs(100)
            .build().apply {
                repeatMode = Player.REPEAT_MODE_OFF
            }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                isExoError = true
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    onPlaybackEnded()
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    LaunchedEffect(mediaUri) {
        isExoError = false
        try {
            val mediaItem = MediaItem.fromUri(mediaUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            val initialPosMs = ((currentPlayheadSec + startTrimSec).coerceAtLeast(0.0) * 1000.0).toLong()
            exoPlayer.seekTo(initialPosMs)
        } catch (e: Exception) {
            isExoError = true
        }
    }

    LaunchedEffect(playbackSpeed) {
        try {
            exoPlayer.setPlaybackSpeed(playbackSpeed)
        } catch (e: Exception) {
            // ignore
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
            exoPlayer.pause()
            val targetPosMs = ((currentPlayheadSec + startTrimSec).coerceAtLeast(0.0) * 1000.0).toLong()
            exoPlayer.seekTo(targetPosMs)
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                withFrameNanos {
                    if (exoPlayer.isPlaying) {
                        val posSec = (exoPlayer.currentPosition / 1000.0) - startTrimSec
                        onPlayheadUpdate(posSec.coerceAtLeast(0.0))
                    }
                }
            }
        }
    }

    LaunchedEffect(currentPlayheadSec, startTrimSec) {
        if (!isPlaying) {
            val targetPosMs = ((currentPlayheadSec + startTrimSec).coerceAtLeast(0.0) * 1000.0).toLong()
            if (kotlin.math.abs(exoPlayer.currentPosition - targetPosMs) > 30) {
                exoPlayer.seekTo(targetPosMs)
            }
        }
    }

    if (!isExoError) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    player = exoPlayer
                    resizeMode = if (contentScale == ContentScale.Crop) {
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    } else {
                        AspectRatioFrameLayout.RESIZE_MODE_FIT
                    }
                    setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                }
            },
            update = { playerView ->
                playerView.player = exoPlayer
                playerView.resizeMode = if (contentScale == ContentScale.Crop) {
                    AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                } else {
                    AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
            modifier = modifier
        )
    } else {
        val fallbackImageRequest = remember(mediaUri) {
            ImageRequest.Builder(context)
                .data(mediaUri)
                .crossfade(true)
                .build()
        }
        AsyncImage(
            model = fallbackImageRequest,
            contentDescription = "Active Media Preview Frame",
            contentScale = contentScale,
            colorFilter = colorFilter,
            modifier = modifier
        )
    }
}
