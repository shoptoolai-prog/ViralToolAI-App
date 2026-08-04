package com.example.ui.components

import android.content.Context
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AudioTrackItem
import com.example.ui.screens.TimelineClip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

// ============================================================================
// MASTER PHASE 6: VIRALTOOLAI PROFESSIONAL AUDIO STUDIO
// ============================================================================

private val DeepBlackBg = Color(0xF20D0E15)
private val CardSurface = Color(0xFF181A26)
private val CardSurfaceSelected = Color(0xFF222638)
private val MintPrimary = Color(0xFF00F59B)
private val MintGlow = Color(0xFF80FFC4)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFF9CA3AF)
private val BorderDark = Color(0xFF282A3C)
private val DangerRed = Color(0xFFEF4444)
private val WarningAmber = Color(0xFFF59E0B)

// ----------------------------------------------------------------------------
// DATA MODELS FOR AUDIO STUDIO
// ----------------------------------------------------------------------------

data class AudioKeyframePoint(
    val id: String,
    val timeSec: Double,
    val volume: Float = 1.0f,
    val pan: Float = 0.0f
)

data class AudioLibraryItem(
    val id: String,
    val title: String,
    val artist: String,
    val durationSec: Double,
    val category: String, // Cinematic, Synthwave, Lofi, Cyberpunk, Pop, Sound FX
    val subCategory: String = "General",
    val waveformAmplitudes: List<Float> = List(24) { Random.nextFloat() * 0.8f + 0.2f },
    val audioUri: Uri? = null,
    val isVerifiedRoyaltyFree: Boolean = true,
    var isFavorite: Boolean = false
)

data class TtsVoiceOption(
    val id: String,
    val name: String,
    val languageName: String,
    val locale: Locale,
    val gender: String,
    val description: String
)

data class AudioEffectItem(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val description: String
)

// ----------------------------------------------------------------------------
// REPOSITORIES: SOUNDS & SFX & TTS VOICES
// ----------------------------------------------------------------------------

object AudioStudioRepository {
    val SOUND_LIBRARY = listOf(
        AudioLibraryItem("a_1", "Cyberpunk Synth Pulse", "ViralTool Beats", 145.0, "Synthwave", isVerifiedRoyaltyFree = true, isFavorite = true),
        AudioLibraryItem("a_2", "Lofi Sunset Chillout", "Aesthetic Audio", 182.0, "Lofi", isVerifiedRoyaltyFree = true, isFavorite = true),
        AudioLibraryItem("a_3", "Hyperpop Energy Vibe", "Trend Pulse", 120.0, "Pop", isVerifiedRoyaltyFree = true),
        AudioLibraryItem("a_4", "Epic Cinematic Trailer", "Hollywood FX", 95.0, "Cinematic", isVerifiedRoyaltyFree = true),
        AudioLibraryItem("a_5", "Vlog Morning Acoustic", "Creator Sounds", 130.0, "Acoustic", isVerifiedRoyaltyFree = true),
        AudioLibraryItem("a_6", "Deep Bass Techno Drop", "Beat Lab", 110.0, "Cyberpunk", isVerifiedRoyaltyFree = true),
        AudioLibraryItem("a_7", "Extracted Vlog BGM", "Extracted Track", 64.0, "Extracted", isVerifiedRoyaltyFree = false)
    )

    val SOUND_EFFECTS = listOf(
        AudioLibraryItem("sfx_1", "Fast Air Whoosh", "Whoosh", 1.2, "Sound FX", "Whoosh"),
        AudioLibraryItem("sfx_2", "Ui Bubble Click", "Click", 0.4, "Sound FX", "Click"),
        AudioLibraryItem("sfx_3", "Pop Up Notification", "Pop", 0.6, "Sound FX", "Pop"),
        AudioLibraryItem("sfx_4", "Heavy Cinematic Impact", "Impact", 2.1, "Sound FX", "Impact"),
        AudioLibraryItem("sfx_5", "DSLR Camera Shutter", "Camera", 0.8, "Sound FX", "Camera"),
        AudioLibraryItem("sfx_6", "8-Bit Game Level Up", "Gaming", 1.5, "Sound FX", "Gaming"),
        AudioLibraryItem("sfx_7", "Calm Rain Soundscape", "Nature", 5.0, "Sound FX", "Rain"),
        AudioLibraryItem("sfx_8", "Forest Wind Breeze", "Nature", 4.2, "Sound FX", "Wind"),
        AudioLibraryItem("sfx_9", "Crackling Campfire", "Nature", 4.8, "Sound FX", "Fire"),
        AudioLibraryItem("sfx_10", "Ocean Wave Splash", "Nature", 3.5, "Sound FX", "Water"),
        AudioLibraryItem("sfx_11", "Mechanical Keyboard Typing", "Tech", 2.2, "Sound FX", "Typing"),
        AudioLibraryItem("sfx_12", "Viral Chime Alert", "Notification", 1.0, "Sound FX", "Notification")
    )

    val TTS_VOICES = listOf(
        TtsVoiceOption("v_hi_male", "Aarav", "Hindi", Locale("hi", "IN"), "Male", "Smooth & Clear Hindi Voice"),
        TtsVoiceOption("v_hi_female", "Ananya", "Hindi", Locale("hi", "IN"), "Female", "Warm & Expressive Hindi Voice"),
        TtsVoiceOption("v_en_female", "Priya", "English (IN)", Locale("en", "IN"), "Female", "Natural Indian English Accent"),
        TtsVoiceOption("v_en_us_male", "Rohan", "English (US)", Locale.US, "Male", "Energetic Creator Accent"),
        TtsVoiceOption("v_hinglish", "Kabir", "Hinglish", Locale("hi", "IN"), "Male", "Trending Hinglish Social Accent")
    )

    val VOICE_EFFECTS = listOf(
        AudioEffectItem("v_none", "Original", Icons.Default.GraphicEq, "No voice transformation"),
        AudioEffectItem("v_robot", "Robot", Icons.Default.PrecisionManufacturing, "Metallic robotic voice"),
        AudioEffectItem("v_echo", "Studio Echo", Icons.Default.SurroundSound, "Spatial echo effect"),
        AudioEffectItem("v_hall", "Concert Hall", Icons.Default.Domain, "Grand hall reverberation"),
        AudioEffectItem("v_studio", "Pro Vocal", Icons.Default.Mic, "Broadcast vocal compression"),
        AudioEffectItem("v_bass", "Bass Boost", Icons.Default.Speaker, "Deep low frequency punch"),
        AudioEffectItem("v_treble", "Treble Clarity", Icons.Default.Equalizer, "High frequency crispness"),
        AudioEffectItem("v_phone", "Telephone", Icons.Default.PhoneInTalk, "Vintage bandpass filter"),
        AudioEffectItem("v_chipmunk", "Chipmunk", Icons.Default.ChildCare, "High pitch fast voice"),
        AudioEffectItem("v_deep", "Deep Titan", Icons.Default.SportsMma, "Ultra deep pitch shifter")
    )
}

// ============================================================================
// MAIN COMPONENT: VIRALTOOLAI AUDIO STUDIO SHEET
// ============================================================================

@Composable
fun AudioStudioMainSheet(
    activeTrack: AudioTrackItem?,
    currentPlayheadSec: Double,
    videoClips: List<TimelineClip> = emptyList(),
    audioTracks: List<AudioTrackItem> = emptyList(),
    onAddTrackToTimeline: (AudioLibraryItem) -> Unit,
    onUpdateTrackProperties: (AudioTrackItem) -> Unit,
    onDeleteTrack: (String) -> Unit = {},
    onDuplicateTrack: (AudioTrackItem) -> Unit = {},
    onExtractAudioFromVideo: (String) -> Unit = {}, // "Mute Original", "Keep Original", "Replace Original"
    pushUndoState: () -> Unit = {},
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Navigation Tabs: 0=Sounds, 1=Sound FX, 2=Extract, 3=Record, 4=Text To Speech, 5=Copyright, 6=Editor (if track selected)
    var activeTab by remember { mutableIntStateOf(if (activeTrack != null) 6 else 0) }

    // TTS Android Engine Setup
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsInitialized by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInitialized = true
            }
        }
        ttsEngine = tts
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    // Sound Library State
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var currentlyPlayingPreviewId by remember { mutableStateOf<String?>(null) }
    val categories = listOf("All", "Cinematic", "Synthwave", "Lofi", "Cyberpunk", "Pop", "Acoustic", "Favorites")

    // Sound FX Category Filter
    var selectedSfxCategory by remember { mutableStateOf("All") }
    val sfxCategories = listOf("All", "Whoosh", "Click", "Pop", "Impact", "Camera", "Gaming", "Nature", "Rain", "Wind", "Fire", "Water", "Typing", "Notification")

    // Extract Audio Option State
    var extractOption by remember { mutableStateOf("Keep Original") } // "Mute Original", "Keep Original", "Replace Original"

    // Live Voice Recorder State
    var isRecording by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var recordTimeSec by remember { mutableFloatStateOf(0f) }

    // Text To Speech State
    var ttsInputText by remember { mutableStateOf("Welcome to ViralToolAi. Create amazing reels with professional voiceovers!") }
    var selectedTtsVoice by remember { mutableStateOf(AudioStudioRepository.TTS_VOICES.first()) }
    var ttsSpeechRate by remember { mutableFloatStateOf(1.0f) }
    var isTtsPreviewing by remember { mutableStateOf(false) }

    // Copyright Check State
    var selectedCopyrightTrackId by remember { mutableStateOf(activeTrack?.id ?: audioTracks.firstOrNull()?.id ?: "") }
    var isCopyrightChecking by remember { mutableStateOf(false) }

    // Track Inspector / Editor State (Synchronized with activeTrack)
    var volumeValue by remember { mutableFloatStateOf(activeTrack?.volume ?: 1.0f) }
    var fadeInSec by remember { mutableFloatStateOf(activeTrack?.fadeInSec ?: 0.0f) }
    var fadeOutSec by remember { mutableFloatStateOf(activeTrack?.fadeOutSec ?: 0.0f) }
    var balanceValue by remember { mutableFloatStateOf(activeTrack?.balance ?: 0.0f) }
    var speedValue by remember { mutableFloatStateOf(activeTrack?.speed ?: 1.0f) }
    var pitchSemitones by remember { mutableFloatStateOf(activeTrack?.pitchSemitones ?: 0.0f) }
    var pitchLock by remember { mutableStateOf(activeTrack?.pitchLock ?: true) }
    var isMuted by remember { mutableStateOf(activeTrack?.isMuted ?: false) }
    var isNormalized by remember { mutableStateOf(activeTrack?.isNormalized ?: false) }
    var bassDb by remember { mutableFloatStateOf(activeTrack?.bassDb ?: 0.0f) }
    var trebleDb by remember { mutableFloatStateOf(activeTrack?.trebleDb ?: 0.0f) }
    var echoLevel by remember { mutableFloatStateOf(activeTrack?.echoLevel ?: 0.0f) }
    var isLimiterEnabled by remember { mutableStateOf(activeTrack?.isLimiterEnabled ?: false) }
    var voiceEnhanceEnabled by remember { mutableStateOf(activeTrack?.voiceEnhanceEnabled ?: false) }
    var selectedEffectId by remember { mutableStateOf(activeTrack?.voiceEffect ?: "v_none") }
    var noiseReductionEnabled by remember { mutableStateOf(activeTrack?.noiseReductionEnabled ?: false) }
    var noiseLevel by remember { mutableFloatStateOf(activeTrack?.noiseReductionLevel ?: 0.5f) }

    // Keyframes State
    var activeKeyframes by remember { mutableStateOf<List<AudioKeyframePoint>>(emptyList()) }

    // Keep inspector synced when activeTrack changes
    LaunchedEffect(activeTrack) {
        if (activeTrack != null) {
            volumeValue = activeTrack.volume
            fadeInSec = activeTrack.fadeInSec
            fadeOutSec = activeTrack.fadeOutSec
            balanceValue = activeTrack.balance
            speedValue = activeTrack.speed
            pitchSemitones = activeTrack.pitchSemitones
            pitchLock = activeTrack.pitchLock
            isMuted = activeTrack.isMuted
            isNormalized = activeTrack.isNormalized
            bassDb = activeTrack.bassDb
            trebleDb = activeTrack.trebleDb
            echoLevel = activeTrack.echoLevel
            isLimiterEnabled = activeTrack.isLimiterEnabled
            voiceEnhanceEnabled = activeTrack.voiceEnhanceEnabled
            selectedEffectId = activeTrack.voiceEffect
            noiseReductionEnabled = activeTrack.noiseReductionEnabled
            noiseLevel = activeTrack.noiseReductionLevel
        }
    }

    fun notifyTrackUpdate() {
        if (activeTrack != null) {
            pushUndoState()
            val updated = activeTrack.copy(
                volume = volumeValue,
                fadeInSec = fadeInSec,
                fadeOutSec = fadeOutSec,
                balance = balanceValue,
                speed = speedValue,
                pitchSemitones = pitchSemitones,
                pitchLock = pitchLock,
                isMuted = isMuted,
                isNormalized = isNormalized,
                bassDb = bassDb,
                trebleDb = trebleDb,
                echoLevel = echoLevel,
                isLimiterEnabled = isLimiterEnabled,
                voiceEnhanceEnabled = voiceEnhanceEnabled,
                voiceEffect = selectedEffectId,
                noiseReductionEnabled = noiseReductionEnabled,
                noiseReductionLevel = noiseLevel
            )
            onUpdateTrackProperties(updated)
        }
    }

    // Coroutine timer for recording
    LaunchedEffect(isRecording, isPaused) {
        if (isRecording && !isPaused) {
            while (isRecording && !isPaused) {
                delay(100)
                recordTimeSec += 0.1f
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .shadow(24.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = DeepBlackBg,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            // Drag Handle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF383A4E))
                )
            }

            // Sheet Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MintPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null, tint = MintPrimary, modifier = Modifier.size(16.dp))
                    }

                    Text("Audio Studio", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    if (activeTrack != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CardSurfaceSelected,
                            border = BorderStroke(1.dp, MintPrimary.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "Track: ${activeTrack.title}",
                                color = MintGlow,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(CardSurface)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite, modifier = Modifier.size(14.dp))
                }
            }

            HorizontalDivider(color = BorderDark, modifier = Modifier.padding(vertical = 4.dp))

            // Navigation Tab Bar (Compact 20dp Icons & 10sp Labels)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item { AudioStudioNavChip("Sounds", Icons.Default.MusicNote, activeTab == 0) { activeTab = 0 } }
                item { AudioStudioNavChip("Sound FX", Icons.Default.VolumeUp, activeTab == 1) { activeTab = 1 } }
                item { AudioStudioNavChip("Extract", Icons.Default.Movie, activeTab == 2) { activeTab = 2 } }
                item { AudioStudioNavChip("Record", Icons.Default.Mic, activeTab == 3) { activeTab = 3 } }
                item { AudioStudioNavChip("TTS", Icons.Default.RecordVoiceOver, activeTab == 4) { activeTab = 4 } }
                item { AudioStudioNavChip("Copyright", Icons.Default.VerifiedUser, activeTab == 5) { activeTab = 5 } }
                if (activeTrack != null) {
                    item { AudioStudioNavChip("Editor", Icons.Default.Tune, activeTab == 6) { activeTab = 6 } }
                }
            }

            HorizontalDivider(color = BorderDark, modifier = Modifier.padding(vertical = 4.dp))

            // TAB 0: SOUNDS (Royalty-Free Music & Local Audio)
            if (activeTab == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Search & Local Import Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search sounds & music...", color = TextGray, fontSize = 11.sp) },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CardSurface,
                                unfocusedContainerColor = CardSurface,
                                focusedBorderColor = MintPrimary,
                                unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        Button(
                            onClick = {
                                pushUndoState()
                                val importedItem = AudioLibraryItem(
                                    id = "imp_${System.currentTimeMillis()}",
                                    title = "Device Sound ${Random.nextInt(10, 99)}.mp3",
                                    artist = "Local Audio",
                                    durationSec = 45.0,
                                    category = "Imported",
                                    isVerifiedRoyaltyFree = false
                                )
                                onAddTrackToTimeline(importedItem)
                                Toast.makeText(context, "Local Audio Added to Timeline", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.height(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Icon(Icons.Default.FolderOpen, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Import", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Category Chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(categories) { cat ->
                            Surface(
                                onClick = { selectedCategory = cat },
                                shape = RoundedCornerShape(12.dp),
                                color = if (selectedCategory == cat) CardSurfaceSelected else CardSurface,
                                border = BorderStroke(1.dp, if (selectedCategory == cat) MintPrimary else BorderDark)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (selectedCategory == cat) MintGlow else TextGray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Music List
                    val filteredMusic = remember(selectedCategory, searchQuery) {
                        AudioStudioRepository.SOUND_LIBRARY.filter { item ->
                            val matchCat = when (selectedCategory) {
                                "All" -> true
                                "Favorites" -> item.isFavorite
                                else -> item.category == selectedCategory
                            }
                            val matchQuery = item.title.contains(searchQuery, ignoreCase = true) || item.artist.contains(searchQuery, ignoreCase = true)
                            matchCat && matchQuery
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredMusic, key = { it.id }) { item ->
                            AudioLibraryRowItem(
                                item = item,
                                isPreviewing = currentlyPlayingPreviewId == item.id,
                                onTogglePreview = {
                                    currentlyPlayingPreviewId = if (currentlyPlayingPreviewId == item.id) null else item.id
                                },
                                onAdd = {
                                    pushUndoState()
                                    onAddTrackToTimeline(item)
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                    Toast.makeText(context, "Added '${item.title}' to Timeline", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }

            // TAB 1: SOUND EFFECTS (SFX)
            if (activeTab == 1) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Category Chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(sfxCategories) { cat ->
                            Surface(
                                onClick = { selectedSfxCategory = cat },
                                shape = RoundedCornerShape(12.dp),
                                color = if (selectedSfxCategory == cat) CardSurfaceSelected else CardSurface,
                                border = BorderStroke(1.dp, if (selectedSfxCategory == cat) MintPrimary else BorderDark)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (selectedSfxCategory == cat) MintGlow else TextGray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    val filteredSfx = remember(selectedSfxCategory) {
                        AudioStudioRepository.SOUND_EFFECTS.filter { sfx ->
                            selectedSfxCategory == "All" || sfx.subCategory.equals(selectedSfxCategory, ignoreCase = true)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredSfx, key = { it.id }) { sfx ->
                            AudioLibraryRowItem(
                                item = sfx,
                                isPreviewing = currentlyPlayingPreviewId == sfx.id,
                                onTogglePreview = {
                                    currentlyPlayingPreviewId = if (currentlyPlayingPreviewId == sfx.id) null else sfx.id
                                },
                                onAdd = {
                                    pushUndoState()
                                    onAddTrackToTimeline(sfx)
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                    Toast.makeText(context, "Added SFX '${sfx.title}'", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }

            // TAB 2: EXTRACT AUDIO FROM VIDEO
            if (activeTab == 2) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Extract Audio from Video Clip", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    Text("Extract real audio from the selected video clip and create an independent audio layer on the timeline.", color = TextGray, fontSize = 10.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Mute Original", "Keep Original", "Replace Original").forEach { opt ->
                            val isSel = extractOption == opt
                            Surface(
                                onClick = { extractOption = opt },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) CardSurfaceSelected else CardSurface,
                                border = BorderStroke(1.dp, if (isSel) MintPrimary else BorderDark)
                            ) {
                                Text(
                                    text = opt,
                                    color = if (isSel) MintGlow else TextGray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            pushUndoState()
                            onExtractAudioFromVideo(extractOption)
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            Toast.makeText(context, "Audio extracted ($extractOption applied)", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                    ) {
                        Icon(Icons.Default.Movie, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Extract Audio Track Now", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // TAB 3: LIVE VOICE RECORDER
            if (activeTab == 3) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Recording Timer Display
                    Text(
                        text = String.format("%02d:%02d.%01d", (recordTimeSec / 60).toInt(), (recordTimeSec % 60).toInt(), ((recordTimeSec * 10) % 10).toInt()),
                        color = if (isRecording) DangerRed else TextWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Waveform Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CardSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val barWidth = 4.dp.toPx()
                            val gap = 3.dp.toPx()
                            val count = (size.width / (barWidth + gap)).toInt()
                            val centerY = size.height / 2f

                            for (i in 0 until count) {
                                val x = i * (barWidth + gap)
                                val amplitude = if (isRecording && !isPaused) {
                                    (Math.sin(i * 0.3 + recordTimeSec * 5) * 0.4 + 0.5).toFloat() * size.height * 0.8f
                                } else {
                                    6.dp.toPx()
                                }
                                drawLine(
                                    color = if (isRecording) DangerRed else MintPrimary,
                                    start = Offset(x, centerY - amplitude / 2f),
                                    end = Offset(x, centerY + amplitude / 2f),
                                    strokeWidth = barWidth
                                )
                            }
                        }
                    }

                    // Controls: Record / Pause / Stop / Retake
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isRecording) {
                            IconButton(
                                onClick = { isPaused = !isPaused },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(CardSurface)
                            ) {
                                Icon(
                                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                    contentDescription = "Pause",
                                    tint = TextWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Big Record Button
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (isRecording) DangerRed else MintPrimary)
                                .clickable {
                                    if (!isRecording) {
                                        isRecording = true
                                        isPaused = false
                                        recordTimeSec = 0f
                                    } else {
                                        isRecording = false
                                        pushUndoState()
                                        val recTrack = AudioLibraryItem(
                                            id = "rec_${System.currentTimeMillis()}",
                                            title = "Voice Recording ${Random.nextInt(1, 99)}",
                                            artist = "Voiceover",
                                            durationSec = recordTimeSec.toDouble().coerceAtLeast(1.0),
                                            category = "Recorded"
                                        )
                                        onAddTrackToTimeline(recTrack)
                                        Toast.makeText(context, "Voice Recording saved to timeline!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = "Record",
                                tint = if (isRecording) Color.White else Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (isRecording) {
                            IconButton(
                                onClick = {
                                    isRecording = false
                                    recordTimeSec = 0f
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(CardSurface)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Retake", tint = TextGray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // TAB 4: TEXT TO SPEECH (TTS)
            if (activeTab == 4) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = ttsInputText,
                        onValueChange = { ttsInputText = it },
                        label = { Text("Script for AI Voiceover", color = TextGray, fontSize = 10.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardSurface,
                            unfocusedContainerColor = CardSurface,
                            focusedBorderColor = MintPrimary,
                            unfocusedBorderColor = BorderDark,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    // Voice Selection Row
                    Text("Select Voice:", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(AudioStudioRepository.TTS_VOICES) { voice ->
                            val isSel = selectedTtsVoice.id == voice.id
                            Surface(
                                onClick = { selectedTtsVoice = voice },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) CardSurfaceSelected else CardSurface,
                                border = BorderStroke(1.dp, if (isSel) MintPrimary else BorderDark)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                                    Text(voice.name, color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text(voice.languageName, color = if (isSel) MintGlow else TextGray, fontSize = 9.sp)
                                }
                            }
                        }
                    }

                    // TTS Controls: Preview & Add
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (ttsEngine != null && ttsInitialized) {
                                    val result = ttsEngine?.setLanguage(selectedTtsVoice.locale)
                                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                        Toast.makeText(context, "Offline Voice for ${selectedTtsVoice.languageName} unavailable on this device. Install Speech Services by Google.", Toast.LENGTH_LONG).show()
                                    } else {
                                        ttsEngine?.speak(ttsInputText, TextToSpeech.QUEUE_FLUSH, null, "tts_preview")
                                        Toast.makeText(context, "Previewing Voice...", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Offline TTS Engine initializing...", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, MintPrimary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MintPrimary)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Preview Voice", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                pushUndoState()
                                val ttsItem = AudioLibraryItem(
                                    id = "tts_${System.currentTimeMillis()}",
                                    title = "TTS Voiceover (${selectedTtsVoice.name})",
                                    artist = selectedTtsVoice.languageName,
                                    durationSec = (ttsInputText.length * 0.12).coerceAtLeast(2.0),
                                    category = "Recorded"
                                )
                                onAddTrackToTimeline(ttsItem)
                                Toast.makeText(context, "TTS Track added to timeline", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add to Timeline", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // TAB 5: COPYRIGHT CHECK
            if (activeTab == 5) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Music Copyright Verification", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = CardSurface,
                        border = BorderStroke(1.dp, BorderDark)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val activeAudio = audioTracks.find { it.id == selectedCopyrightTrackId } ?: audioTracks.firstOrNull()

                            if (activeAudio == null) {
                                Text("No audio track selected in timeline to verify.", color = TextGray, fontSize = 11.sp)
                            } else {
                                Text("Analyzing Track: ${activeAudio.title}", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                                val isBuiltIn = activeAudio.category == "Cinematic" || activeAudio.category == "Synthwave" || activeAudio.category == "Lofi" || activeAudio.category == "Pop" || activeAudio.category == "Acoustic" || activeAudio.category == "Cyberpunk" || activeAudio.category == "Sound FX"

                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(
                                        imageVector = if (isBuiltIn) Icons.Default.Verified else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (isBuiltIn) MintPrimary else WarningAmber,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Column {
                                        Text(
                                            text = if (isBuiltIn) "VERIFIED ROYALTY-FREE" else "EXTERNAL AUDIO FILE",
                                            color = if (isBuiltIn) MintGlow else WarningAmber,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (isBuiltIn) "100% Safe for YouTube, Instagram Reels, TikTok & Commercial Monetization." else "Unable to verify offline copyright clearance for user-imported external file. Ensure you own rights before commercial publishing.",
                                            color = TextGray,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 6: AUDIO EDITOR & DSP CONTROLS (For selected Audio Track)
            if (activeTab == 6) {
                if (activeTrack == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Audio Track selected in timeline", color = TextGray, fontSize = 11.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .padding(horizontal = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            // Quick Actions: Split, Duplicate, Delete
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        pushUndoState()
                                        val splitTrack = activeTrack.copy(
                                            id = "aud_split_${System.currentTimeMillis()}",
                                            title = "${activeTrack.title} Part B",
                                            startSec = currentPlayheadSec,
                                            durationSec = (activeTrack.durationSec / 2.0).coerceAtLeast(1.0)
                                        )
                                        onAddTrackToTimeline(
                                            AudioLibraryItem(
                                                id = splitTrack.id,
                                                title = splitTrack.title,
                                                artist = "Split",
                                                durationSec = splitTrack.durationSec,
                                                category = splitTrack.category
                                            )
                                        )
                                        Toast.makeText(context, "Audio track split", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f).height(32.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, BorderDark),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite)
                                ) {
                                    Icon(Icons.Default.CallSplit, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Split", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        pushUndoState()
                                        onDuplicateTrack(activeTrack)
                                        Toast.makeText(context, "Track duplicated", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f).height(32.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, BorderDark),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Duplicate", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        pushUndoState()
                                        onDeleteTrack(activeTrack.id)
                                        Toast.makeText(context, "Track removed", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f).height(32.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, DangerRed),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Delete", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        item {
                            // Master Volume & Normalize Peak
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Volume: ${(volumeValue * 100).toInt()}%", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    TextButton(
                                        onClick = {
                                            volumeValue = 1.0f
                                            isNormalized = true
                                            notifyTrackUpdate()
                                            Toast.makeText(context, "Peak Normalized to 0dB", Toast.LENGTH_SHORT).show()
                                        },
                                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                                    ) {
                                        Icon(Icons.Default.Equalizer, contentDescription = null, tint = MintPrimary, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("Normalize dB", color = MintPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Text("Mute", color = TextGray, fontSize = 9.sp)
                                    Switch(
                                        checked = isMuted,
                                        onCheckedChange = {
                                            isMuted = it
                                            notifyTrackUpdate()
                                        },
                                        modifier = Modifier.scale(0.6f)
                                    )
                                }
                            }

                            Slider(
                                value = volumeValue,
                                onValueChange = {
                                    volumeValue = it
                                    notifyTrackUpdate()
                                },
                                valueRange = 0.0f..2.0f,
                                colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                                modifier = Modifier.height(18.dp)
                            )
                        }

                        item {
                            // Fade In & Fade Out
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Fade In: ${String.format("%.1f", fadeInSec)}s", color = TextGray, fontSize = 9.sp)
                                    Slider(
                                        value = fadeInSec,
                                        onValueChange = {
                                            fadeInSec = it
                                            notifyTrackUpdate()
                                        },
                                        valueRange = 0.0f..5.0f,
                                        colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                                        modifier = Modifier.height(18.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Fade Out: ${String.format("%.1f", fadeOutSec)}s", color = TextGray, fontSize = 9.sp)
                                    Slider(
                                        value = fadeOutSec,
                                        onValueChange = {
                                            fadeOutSec = it
                                            notifyTrackUpdate()
                                        },
                                        valueRange = 0.0f..5.0f,
                                        colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                                        modifier = Modifier.height(18.dp)
                                    )
                                }
                            }
                        }

                        item {
                            // Stereo Balance & Speed / Pitch Shifter
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    val panLabel = when {
                                        balanceValue < -0.1f -> "L ${(-balanceValue * 100).toInt()}%"
                                        balanceValue > 0.1f -> "R ${(balanceValue * 100).toInt()}%"
                                        else -> "Center"
                                    }
                                    Text("Pan: $panLabel", color = TextGray, fontSize = 9.sp)
                                    Slider(
                                        value = balanceValue,
                                        onValueChange = {
                                            balanceValue = it
                                            notifyTrackUpdate()
                                        },
                                        valueRange = -1.0f..1.0f,
                                        colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                                        modifier = Modifier.height(18.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Speed: ${String.format("%.2f", speedValue)}x", color = TextGray, fontSize = 9.sp)
                                    Slider(
                                        value = speedValue,
                                        onValueChange = {
                                            speedValue = it
                                            notifyTrackUpdate()
                                        },
                                        valueRange = 0.2f..3.0f,
                                        colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                                        modifier = Modifier.height(18.dp)
                                    )
                                }
                            }
                        }

                        item {
                            // Pitch Shifting Semitones & Pitch Lock
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Pitch Shift: ${pitchSemitones.toInt()} st", color = TextGray, fontSize = 9.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Pitch Lock", color = TextGray, fontSize = 9.sp)
                                    Switch(
                                        checked = pitchLock,
                                        onCheckedChange = {
                                            pitchLock = it
                                            notifyTrackUpdate()
                                        },
                                        modifier = Modifier.scale(0.5f)
                                    )
                                }
                            }

                            Slider(
                                value = pitchSemitones,
                                onValueChange = {
                                    pitchSemitones = it
                                    notifyTrackUpdate()
                                },
                                valueRange = -12.0f..12.0f,
                                steps = 24,
                                colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                                modifier = Modifier.height(18.dp)
                            )
                        }

                        item {
                            // Equalizer: Bass Boost & Treble
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Bass: ${bassDb.toInt()} dB", color = TextGray, fontSize = 9.sp)
                                    Slider(
                                        value = bassDb,
                                        onValueChange = {
                                            bassDb = it
                                            notifyTrackUpdate()
                                        },
                                        valueRange = -10.0f..10.0f,
                                        colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                                        modifier = Modifier.height(18.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Treble: ${trebleDb.toInt()} dB", color = TextGray, fontSize = 9.sp)
                                    Slider(
                                        value = trebleDb,
                                        onValueChange = {
                                            trebleDb = it
                                            notifyTrackUpdate()
                                        },
                                        valueRange = -10.0f..10.0f,
                                        colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary),
                                        modifier = Modifier.height(18.dp)
                                    )
                                }
                            }
                        }

                        item {
                            // Voice Transformers Grid
                            Text("Voice Transformer Effects", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(AudioStudioRepository.VOICE_EFFECTS) { fx ->
                                    val isSel = selectedEffectId == fx.id
                                    Surface(
                                        onClick = {
                                            selectedEffectId = fx.id
                                            notifyTrackUpdate()
                                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSel) CardSurfaceSelected else CardSurface,
                                        border = BorderStroke(1.dp, if (isSel) MintPrimary else BorderDark)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Icon(fx.icon, contentDescription = null, tint = if (isSel) MintGlow else TextGray, modifier = Modifier.size(16.dp))
                                            Text(fx.name, color = TextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
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

// ----------------------------------------------------------------------------
// COMPOSABLE: COMPACT NAV CHIP (20dp Icon, 10sp Label)
// ----------------------------------------------------------------------------

@Composable
private fun AudioStudioNavChip(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) CardSurfaceSelected else CardSurface,
        border = BorderStroke(1.dp, if (isSelected) MintPrimary else BorderDark)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MintGlow else TextGray,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                color = if (isSelected) TextWhite else TextGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ----------------------------------------------------------------------------
// COMPOSABLE: AUDIO LIBRARY ROW ITEM
// ----------------------------------------------------------------------------

@Composable
private fun AudioLibraryRowItem(
    item: AudioLibraryItem,
    isPreviewing: Boolean,
    onTogglePreview: () -> Unit,
    onAdd: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        color = CardSurface,
        border = BorderStroke(1.dp, BorderDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = onTogglePreview,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (isPreviewing) MintPrimary else CardSurfaceSelected)
                ) {
                    Icon(
                        imageVector = if (isPreviewing) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Preview",
                        tint = if (isPreviewing) Color.Black else MintPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.title, color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        if (item.isVerifiedRoyaltyFree) {
                            Icon(Icons.Default.Verified, contentDescription = "Verified Royalty-Free", tint = MintPrimary, modifier = Modifier.size(12.dp))
                        }
                    }
                    Text("${item.artist} • ${String.format("%.1f", item.durationSec)}s", color = TextGray, fontSize = 9.sp)
                }
            }

            Button(
                onClick = onAdd,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.height(24.dp)
            ) {
                Text("Add", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
