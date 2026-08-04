package com.example.ui.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.StarOutline
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ============================================================================
// MASTER PHASE 11: PROFESSIONAL TRANSITION & ANIMATION ENGINE (VIRALTOOLAI)
// ============================================================================

// Theme Colors
private val GlassSheetBg = Color(0xEB14141F)
private val CardSurface = Color(0xFF1E1E2A)
private val CardSurfaceSelected = Color(0xFF2D2A4A)
private val AccentViolet = Color(0xFF8B5CF6)
private val SoftPurpleGlow = Color(0xFFA78BFA)
private val BrightPurple = Color(0xFF7C3AED)
private val TextMain = Color(0xFFFFFFFF)
private val TextSub = Color(0xFF9CA3AF)
private val GoldStar = Color(0xFFF59E0B)

// ----------------------------------------------------------------------------
// DATA MODELS: TRANSITIONS & ANIMATIONS
// ----------------------------------------------------------------------------

data class TransitionItem(
    val id: String,
    val name: String,
    val category: String,
    val defaultDurationSec: Double = 0.5,
    val supportsDirection: Boolean = true,
    val supportsCurve: Boolean = true,
    val description: String = ""
)

data class TransitionConfig(
    val transitionId: String,
    val durationSec: Double = 0.5,
    val direction: String = "Right", // Left, Right, Up, Down, Center
    val intensity: Float = 1.0f,
    val curve: String = "EaseInOut", // Linear, EaseIn, EaseOut, EaseInOut, Spring, Bounce
    val isReversed: Boolean = false
)

enum class AnimationType { ENTRY, EXIT, LOOP }

data class ElementAnimationItem(
    val id: String,
    val name: String,
    val type: AnimationType,
    val category: String = "General",
    val defaultDurationSec: Float = 1.0f,
    val description: String = ""
)

data class KeyframePoint(
    val id: String,
    val timeSec: Double,
    val translationX: Float = 0f,
    val translationY: Float = 0f,
    val scale: Float = 1.0f,
    val rotation: Float = 0f,
    val opacity: Float = 1.0f,
    val blur: Float = 0f,
    val volume: Float = 1.0f
)

// ----------------------------------------------------------------------------
// REPOSITORIES: 17 TRANSITION CATEGORIES & ALL ANIMATIONS
// ----------------------------------------------------------------------------

object TransitionRepository {
    val NONE = TransitionItem("t_none", "None", "Basic", 0.0, false, false, "No transition")

    // 1. Basic
    val CROSS_FADE = TransitionItem("t_cross_fade", "Cross Fade", "Basic", 0.5, false, true, "Classic smooth dissolve")
    val QUICK_DIP = TransitionItem("t_quick_dip", "Quick Dip", "Basic", 0.4, false, true, "Fast black fade pass")
    val SOFT_DISSOLVE = TransitionItem("t_soft_dissolve", "Soft Dissolve", "Basic", 0.6, false, true, "Soft contrast fade")

    // 2. Smooth
    val LIQUID_MORPH = TransitionItem("t_liquid_morph", "Liquid Morph", "Smooth", 0.7, true, true, "Organic fluid blending")
    val SILK_FLOW = TransitionItem("t_silk_flow", "Silk Flow", "Smooth", 0.6, true, true, "Ultra smooth directional slide")

    // 3. Camera
    val PAN_WHIP = TransitionItem("t_pan_whip", "Pan Whip", "Camera", 0.4, true, true, "Fast motion blur camera pan")
    val TILT_CRANE = TransitionItem("t_tilt_crane", "Tilt Crane", "Camera", 0.5, true, true, "Cinematic vertical tilt")

    // 4. Zoom
    val ELASTIC_PUNCH = TransitionItem("t_elastic_punch", "Elastic Punch", "Zoom", 0.45, true, true, "High energy zoom pop")
    val DOLLY_OUT = TransitionItem("t_dolly_out", "Dolly Out", "Zoom", 0.5, true, true, "Cinematic pullback dolly")

    // 5. Slide
    val EDGE_WIPE = TransitionItem("t_edge_wipe", "Edge Wipe", "Slide", 0.4, true, true, "Sharp edge slide pass")
    val CORNER_SLIP = TransitionItem("t_corner_slip", "Corner Slip", "Slide", 0.5, true, true, "Diagonal corner push")

    // 6. Push
    val POWER_PUSH = TransitionItem("t_power_push", "Power Push", "Push", 0.45, true, true, "Dynamic clip replacement push")
    val KINETIC_BUMP = TransitionItem("t_kinetic_bump", "Kinetic Bump", "Push", 0.4, true, true, "Impact kinetic force")

    // 7. Pull
    val MAGNET_DRAW = TransitionItem("t_magnet_draw", "Magnet Draw", "Pull", 0.5, true, true, "Magnetic clip suction")
    val VACUUM_SHIFT = TransitionItem("t_vacuum_shift", "Vacuum Shift", "Pull", 0.55, true, true, "Implosive pull transition")

    // 8. Fade
    val COLOR_FLASH = TransitionItem("t_color_flash", "Color Flash", "Fade", 0.35, false, true, "Vivid strobe color burst")
    val DEEP_BLACK = TransitionItem("t_deep_black", "Deep Black", "Fade", 0.5, false, true, "Full blackout transition")

    // 9. Blur
    val RADIAL_HAZE = TransitionItem("t_radial_haze", "Radial Haze", "Blur", 0.6, false, true, "Circular spinning blur fade")
    val BOKEH_BLOOM = TransitionItem("t_bokeh_bloom", "Bokeh Bloom", "Blur", 0.65, false, true, "Dreamy bokeh circle bloom")

    // 10. Spin
    val VORTEX_TWIST = TransitionItem("t_vortex_twist", "Vortex Twist", "Spin", 0.6, true, true, "Fast 360 center vortex")
    val ORBIT_TURN = TransitionItem("t_orbit_turn", "Orbit Turn", "Spin", 0.55, true, true, "3D orbital flip pass")

    // 11. Rotate
    val TILE_FLIP = TransitionItem("t_tile_flip", "Tile Flip", "Rotate", 0.5, true, true, "3D card tile rotation")
    val PENDULUM_SWING = TransitionItem("t_pendulum_swing", "Pendulum", "Rotate", 0.6, true, true, "Swinging pendulum rotation")

    // 12. Flash
    val NEON_BURN = TransitionItem("t_neon_burn", "Neon Burn", "Flash", 0.4, false, true, "High contrast neon strobe")
    val LIGHTNING_LEAK = TransitionItem("t_lightning_leak", "Lightning Pass", "Flash", 0.35, false, true, "Electric flash leak")

    // 13. Film
    val FILM_ROLL_35MM = TransitionItem("t_film_roll", "35mm Gate Roll", "Film", 0.5, true, true, "Analog film reel roll")
    val SPROCKET_SLIP = TransitionItem("t_sprocket_slip", "Sprocket Slip", "Film", 0.45, true, true, "Vintage film frame slip")

    // 14. Motion
    val INERTIA_DRIFT = TransitionItem("t_inertia_drift", "Inertia Drift", "Motion", 0.5, true, true, "Physics momentum slide")
    val SPRING_BOUNCE = TransitionItem("t_spring_bounce", "Spring Bounce", "Motion", 0.55, true, true, "Bouncy spring transition")

    // 15. Luxury
    val GOLDEN_FLARE = TransitionItem("t_golden_flare", "Golden Flare", "Luxury", 0.6, false, true, "Luxury gold light sweep")
    val DIAMOND_SHIMMER = TransitionItem("t_diamond_shimmer", "Diamond Shimmer", "Luxury", 0.65, false, true, "High end jewel glint pass")

    // 16. Minimal
    val LINE_CUT = TransitionItem("t_line_cut", "Line Cut", "Minimal", 0.35, true, true, "Clean minimalist slash")
    val MONO_SWEEP = TransitionItem("t_mono_sweep", "Mono Sweep", "Minimal", 0.4, true, true, "Architectural flat wipe")

    // 17. Creator
    val VIRAL_WHIP = TransitionItem("t_viral_whip", "Viral Whip", "Creator", 0.4, true, true, "Fast trend reel transition")
    val CYBER_MORPH = TransitionItem("t_cyber_morph", "Cyber Morph", "Creator", 0.55, true, true, "AI style glitch morphing")

    val ALL_TRANSITIONS = listOf(
        NONE, CROSS_FADE, QUICK_DIP, SOFT_DISSOLVE,
        LIQUID_MORPH, SILK_FLOW, PAN_WHIP, TILT_CRANE,
        ELASTIC_PUNCH, DOLLY_OUT, EDGE_WIPE, CORNER_SLIP,
        POWER_PUSH, KINETIC_BUMP, MAGNET_DRAW, VACUUM_SHIFT,
        COLOR_FLASH, DEEP_BLACK, RADIAL_HAZE, BOKEH_BLOOM,
        VORTEX_TWIST, ORBIT_TURN, TILE_FLIP, PENDULUM_SWING,
        NEON_BURN, LIGHTNING_LEAK, FILM_ROLL_35MM, SPROCKET_SLIP,
        INERTIA_DRIFT, SPRING_BOUNCE, GOLDEN_FLARE, DIAMOND_SHIMMER,
        LINE_CUT, MONO_SWEEP, VIRAL_WHIP, CYBER_MORPH
    )

    fun getTransitionById(id: String): TransitionItem {
        return ALL_TRANSITIONS.find { it.id == id } ?: NONE
    }
}

object AnimationRepository {
    val ENTRY_ANIMATIONS = listOf(
        ElementAnimationItem("e_fade_in", "Fade In", AnimationType.ENTRY, "Fade", 0.8f, "Smooth opacity ramp"),
        ElementAnimationItem("e_slide_up", "Slide Up", AnimationType.ENTRY, "Slide", 0.6f, "Enters from bottom edge"),
        ElementAnimationItem("e_slide_right", "Slide Right", AnimationType.ENTRY, "Slide", 0.6f, "Enters from left edge"),
        ElementAnimationItem("e_scale_bounce", "Scale Bounce", AnimationType.ENTRY, "Scale", 0.7f, "Pop in with spring bounce"),
        ElementAnimationItem("e_zoom_punch", "Zoom Punch", AnimationType.ENTRY, "Zoom", 0.5f, "High energy zoom in"),
        ElementAnimationItem("e_pop_in", "Pop In", AnimationType.ENTRY, "Pop", 0.4f, "Fast pop scale"),
        ElementAnimationItem("e_rotate_spin", "Rotate Spin", AnimationType.ENTRY, "Rotate", 0.8f, "360 spin entry"),
        ElementAnimationItem("e_blur_flare", "Blur Flare", AnimationType.ENTRY, "Blur", 0.7f, "Unblurs into focus")
    )

    val EXIT_ANIMATIONS = listOf(
        ElementAnimationItem("x_fade_out", "Fade Out", AnimationType.EXIT, "Fade", 0.8f, "Smooth opacity fade"),
        ElementAnimationItem("x_slide_down", "Slide Down", AnimationType.EXIT, "Slide", 0.6f, "Exits down to bottom"),
        ElementAnimationItem("x_zoom_shrink", "Zoom Shrink", AnimationType.EXIT, "Zoom", 0.5f, "Shrinks out of view"),
        ElementAnimationItem("x_pop_out", "Pop Out", AnimationType.EXIT, "Pop", 0.4f, "Fast pop disappear"),
        ElementAnimationItem("x_rotate_away", "Rotate Away", AnimationType.EXIT, "Rotate", 0.7f, "Spins out to corner"),
        ElementAnimationItem("x_blur_fade", "Blur Fade", AnimationType.EXIT, "Blur", 0.6f, "Blurs into background")
    )

    val LOOP_ANIMATIONS = listOf(
        ElementAnimationItem("l_pulse", "Pulse", AnimationType.LOOP, "Loop", 1.5f, "Continuous rhythmic heartbeat"),
        ElementAnimationItem("l_float", "Floating Drift", AnimationType.LOOP, "Loop", 2.0f, "Gentle up and down float"),
        ElementAnimationItem("l_glow_strobe", "Glow Strobe", AnimationType.LOOP, "Loop", 1.2f, "Pulsing ambient glow"),
        ElementAnimationItem("l_shake_jitter", "Shake Jitter", AnimationType.LOOP, "Loop", 0.8f, "High energy action shake"),
        ElementAnimationItem("l_breathing", "Breathing Wave", AnimationType.LOOP, "Loop", 2.5f, "Smooth expand and contract"),
        ElementAnimationItem("l_swing", "Pendulum Swing", AnimationType.LOOP, "Loop", 1.8f, "Side to side rotation swing")
    )
}

// ============================================================================
// MAIN COMPONENT 1: TRANSITIONS STUDIO SHEET (MASTER PHASE 11)
// ============================================================================

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransitionsStudioSheet(
    fromMediaUri: Uri?,
    toMediaUri: Uri?,
    currentTransitionId: String,
    currentConfig: TransitionConfig,
    onPreviewTransition: (TransitionConfig?) -> Unit,
    onApplyTransition: (TransitionConfig) -> Unit,
    onApplyToAll: (TransitionConfig) -> Unit,
    onRemoveTransition: () -> Unit,
    onCancel: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val categories = listOf(
        "Favorites", "Basic", "Smooth", "Camera", "Zoom", "Slide",
        "Push", "Pull", "Fade", "Blur", "Spin", "Rotate", "Flash",
        "Film", "Motion", "Luxury", "Minimal", "Creator"
    )

    var selectedCategory by remember { mutableStateOf("Basic") }
    val prefs = remember { context.getSharedPreferences("viraltool_transitions_prefs", Context.MODE_PRIVATE) }
    var favoriteIds by remember {
        mutableStateOf(prefs.getStringSet("fav_transitions", setOf("t_cross_fade", "t_pan_whip", "t_elastic_punch")) ?: emptySet())
    }

    var activeConfig by remember { mutableStateOf(currentConfig) }
    var selectedItem by remember { mutableStateOf(TransitionRepository.getTransitionById(currentTransitionId)) }
    var isLongPressingId by remember { mutableStateOf<String?>(null) }
    var isLoopingPreview by remember { mutableStateOf(false) }

    // Live preview trigger
    LaunchedEffect(selectedItem, activeConfig) {
        if (selectedItem.id == "t_none") {
            onPreviewTransition(null)
        } else {
            onPreviewTransition(activeConfig.copy(transitionId = selectedItem.id))
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .shadow(24.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = GlassSheetBg,
        border = BorderStroke(1.dp, Color(0xFF2A2A3C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            // Drag Handle & Top Header
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
                        .background(Color(0xFF4B4B60))
                )
            }

            // Top Title Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Transform, contentDescription = null, tint = SoftPurpleGlow, modifier = Modifier.size(18.dp))
                    Text("Transition Studio", color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    if (selectedItem.id != "t_none") {
                        Text("• ${selectedItem.name}", color = AccentViolet, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(CardSurface)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMain, modifier = Modifier.size(14.dp))
                }
            }

            HorizontalDivider(color = Color(0xFF222232), modifier = Modifier.padding(vertical = 4.dp))

            // Horizontal Categories Carousel
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(categories) { cat ->
                    CategoryChip(
                        label = cat,
                        isSelected = selectedCategory == cat,
                        onSelect = {
                            selectedCategory = cat
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                        }
                    )
                }
            }

            // Filtered Transition Cards
            val filteredList = remember(selectedCategory, favoriteIds) {
                if (selectedCategory == "Favorites") {
                    TransitionRepository.ALL_TRANSITIONS.filter { favoriteIds.contains(it.id) }
                } else {
                    TransitionRepository.ALL_TRANSITIONS.filter { it.category == selectedCategory || (selectedCategory == "Basic" && it.category == "Basic") }
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { trans ->
                    val isSelected = selectedItem.id == trans.id
                    val isFav = favoriteIds.contains(trans.id)

                    TransitionCardItem(
                        item = trans,
                        fromUri = fromMediaUri,
                        toUri = toMediaUri,
                        isSelected = isSelected,
                        isFavorite = isFav,
                        isLongPressing = isLongPressingId == trans.id,
                        onSingleTap = {
                            selectedItem = trans
                            activeConfig = activeConfig.copy(transitionId = trans.id, durationSec = trans.defaultDurationSec)
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                        },
                        onDoubleTap = {
                            selectedItem = trans
                            val updated = activeConfig.copy(transitionId = trans.id)
                            onApplyTransition(updated)
                            Toast.makeText(context, "${trans.name} Transition Applied!", Toast.LENGTH_SHORT).show()
                        },
                        onLongPress = {
                            isLongPressingId = if (isLongPressingId == trans.id) null else trans.id
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        },
                        onToggleFavorite = {
                            val newFavs = if (isFav) favoriteIds - trans.id else favoriteIds + trans.id
                            favoriteIds = newFavs
                            prefs.edit().putStringSet("fav_transitions", newFavs).apply()
                        }
                    )
                }
            }

            // Transition Controls: Duration, Direction, Curve, Apply to All
            if (selectedItem.id != "t_none") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Duration Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Duration: ${String.format("%.1f", activeConfig.durationSec)}s",
                            color = SoftPurpleGlow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            TextButton(
                                onClick = {
                                    val updated = activeConfig.copy(transitionId = selectedItem.id)
                                    onApplyToAll(updated)
                                    Toast.makeText(context, "Applied to All Clips!", Toast.LENGTH_SHORT).show()
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Apply to All", color = AccentViolet, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Slider(
                        value = activeConfig.durationSec.toFloat(),
                        onValueChange = { activeConfig = activeConfig.copy(durationSec = it.toDouble()) },
                        valueRange = 0.1f..2.0f,
                        colors = SliderDefaults.colors(thumbColor = AccentViolet, activeTrackColor = BrightPurple),
                        modifier = Modifier.height(18.dp)
                    )

                    // Direction & Curve Selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Direction Selector
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Direction", color = TextSub, fontSize = 10.sp)
                            val directions = listOf("Left", "Right", "Up", "Down")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                items(directions) { dir ->
                                    val isDirSel = activeConfig.direction == dir
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isDirSel) BrightPurple else CardSurface)
                                            .clickable { activeConfig = activeConfig.copy(direction = dir) }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(dir, color = TextMain, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }

                        // Curve Selector
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Curve", color = TextSub, fontSize = 10.sp)
                            val curves = listOf("EaseInOut", "Linear", "Spring", "Bounce")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                items(curves) { crv ->
                                    val isCrvSel = activeConfig.curve == crv
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isCrvSel) BrightPurple else CardSurface)
                                            .clickable { activeConfig = activeConfig.copy(curve = crv) }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(crv, color = TextMain, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFF222232), modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))

            // Action Buttons (Delete/Remove | Cancel | Apply)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        onRemoveTransition()
                        onClose()
                    },
                    modifier = Modifier
                        .height(34.dp)
                        .weight(0.3f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFEF4444)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Remove", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .height(34.dp)
                        .weight(0.3f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFF374151)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSub)
                ) {
                    Text("Cancel", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (selectedItem.id == "t_none") {
                            onRemoveTransition()
                        } else {
                            val finalCfg = activeConfig.copy(transitionId = selectedItem.id)
                            onApplyTransition(finalCfg)
                        }
                        onClose()
                    },
                    modifier = Modifier
                        .height(34.dp)
                        .weight(0.4f),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentViolet)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Apply", tint = TextMain, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Apply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ----------------------------------------------------------------------------
// COMPONENT: TRANSITION CARD ITEM (DYNAMIC PAIR ANIMATED PREVIEW)
// ----------------------------------------------------------------------------

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TransitionCardItem(
    item: TransitionItem,
    fromUri: Uri?,
    toUri: Uri?,
    isSelected: Boolean,
    isFavorite: Boolean,
    isLongPressing: Boolean,
    onSingleTap: () -> Unit,
    onDoubleTap: () -> Unit,
    onLongPress: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isLongPressing) 1.08f else 1.0f, label = "transScale")

    // Looping preview alpha animation for transition card thumbnail
    val infiniteTransition = rememberInfiniteTransition(label = "cardAnim")
    val previewProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clip(RoundedCornerShape(12.dp))
                .background(CardSurface)
                .border(
                    border = if (isSelected) BorderStroke(2.dp, AccentViolet) else BorderStroke(1.dp, Color(0xFF2B2B3D)),
                    shape = RoundedCornerShape(12.dp)
                )
                .combinedClickable(
                    onClick = onSingleTap,
                    onDoubleClick = onDoubleTap,
                    onLongClick = onLongPress
                )
        ) {
            // Live Dual-Clip Transition Thumbnail Preview
            if (fromUri != null && toUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(fromUri).crossfade(true).build(),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(toUri).crossfade(true).build(),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = previewProgress }
                )
            } else if (fromUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(fromUri).crossfade(true).build(),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF311042), Color(0xFF6B21A8)))
                        )
                )
            }

            // Favorite Icon
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .padding(2.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) GoldStar else Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(12.dp)
                )
            }

            // Selection Glow Overlay
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x338B5CF6))
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.name,
            color = if (isSelected) SoftPurpleGlow else TextMain,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

// ============================================================================
// MAIN COMPONENT 2: ANIMATIONS & KEYFRAME STUDIO SHEET (MASTER PHASE 11)
// ============================================================================

@Composable
fun AnimationsKeyframeStudioSheet(
    targetName: String,
    currentTimeSec: Double,
    keyframes: List<KeyframePoint>,
    onAddKeyframe: (KeyframePoint) -> Unit,
    onDeleteKeyframe: (String) -> Unit,
    onApplyEntryAnimation: (ElementAnimationItem) -> Unit,
    onApplyExitAnimation: (ElementAnimationItem) -> Unit,
    onApplyLoopAnimation: (ElementAnimationItem) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var activeSubTab by remember { mutableStateOf(0) } // 0 = ENTRY, 1 = EXIT, 2 = LOOP, 3 = KEYFRAMES

    // Property controls for keyframes
    var kScale by remember { mutableFloatStateOf(1.0f) }
    var kRotation by remember { mutableFloatStateOf(0.0f) }
    var kOpacity by remember { mutableFloatStateOf(1.0f) }
    var kTransX by remember { mutableFloatStateOf(0.0f) }
    var kTransY by remember { mutableFloatStateOf(0.0f) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .shadow(24.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = GlassSheetBg,
        border = BorderStroke(1.dp, Color(0xFF2A2A3C))
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
                        .background(Color(0xFF4B4B60))
                )
            }

            // Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Animation, contentDescription = null, tint = SoftPurpleGlow, modifier = Modifier.size(18.dp))
                    Text("Animations & Keyframes", color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("• $targetName", color = AccentViolet, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(CardSurface)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMain, modifier = Modifier.size(14.dp))
                }
            }

            HorizontalDivider(color = Color(0xFF222232), modifier = Modifier.padding(vertical = 4.dp))

            // Sub Navigation Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StudioMainTab("In", isSelected = activeSubTab == 0) { activeSubTab = 0 }
                StudioMainTab("Out", isSelected = activeSubTab == 1) { activeSubTab = 1 }
                StudioMainTab("Loop", isSelected = activeSubTab == 2) { activeSubTab = 2 }
                StudioMainTab("Keyframes (${keyframes.size})", isSelected = activeSubTab == 3) { activeSubTab = 3 }
            }

            HorizontalDivider(color = Color(0xFF222232), modifier = Modifier.padding(vertical = 4.dp))

            // Content per Sub Tab
            when (activeSubTab) {
                0 -> {
                    // Entry Animations List
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(AnimationRepository.ENTRY_ANIMATIONS) { anim ->
                            AnimationCard(anim = anim) {
                                onApplyEntryAnimation(anim)
                                Toast.makeText(context, "${anim.name} Entry Applied", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                1 -> {
                    // Exit Animations List
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(AnimationRepository.EXIT_ANIMATIONS) { anim ->
                            AnimationCard(anim = anim) {
                                onApplyExitAnimation(anim)
                                Toast.makeText(context, "${anim.name} Exit Applied", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                2 -> {
                    // Loop Animations List
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(AnimationRepository.LOOP_ANIMATIONS) { anim ->
                            AnimationCard(anim = anim) {
                                onApplyLoopAnimation(anim)
                                Toast.makeText(context, "${anim.name} Loop Applied", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                3 -> {
                    // Keyframe Control Panel
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Playhead: ${String.format("%.2f", currentTimeSec)}s", color = SoftPurpleGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                            Button(
                                onClick = {
                                    val kf = KeyframePoint(
                                        id = "kf_${System.currentTimeMillis()}",
                                        timeSec = currentTimeSec,
                                        translationX = kTransX,
                                        translationY = kTransY,
                                        scale = kScale,
                                        rotation = kRotation,
                                        opacity = kOpacity
                                    )
                                    onAddKeyframe(kf)
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                    Toast.makeText(context, "Keyframe added at ${String.format("%.2f", currentTimeSec)}s", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentViolet),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = TextMain, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Keyframe", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Sliders for Scale, Rotation, Opacity
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Scale: ${String.format("%.2f", kScale)}x", color = TextSub, fontSize = 10.sp)
                                Slider(
                                    value = kScale,
                                    onValueChange = { kScale = it },
                                    valueRange = 0.2f..3.0f,
                                    colors = SliderDefaults.colors(thumbColor = AccentViolet, activeTrackColor = BrightPurple),
                                    modifier = Modifier.height(18.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Rotation: ${kRotation.toInt()}°", color = TextSub, fontSize = 10.sp)
                                Slider(
                                    value = kRotation,
                                    onValueChange = { kRotation = it },
                                    valueRange = -180f..180f,
                                    colors = SliderDefaults.colors(thumbColor = AccentViolet, activeTrackColor = BrightPurple),
                                    modifier = Modifier.height(18.dp)
                                )
                            }
                        }

                        // Existing Keyframes List
                        if (keyframes.isNotEmpty()) {
                            Text("Active Keyframes", color = TextMain, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(keyframes) { kf ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(CardSurfaceSelected)
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Icon(Icons.Default.Key, contentDescription = null, tint = GoldStar, modifier = Modifier.size(12.dp))
                                            Text("${String.format("%.2f", kf.timeSec)}s", color = TextMain, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            IconButton(
                                                onClick = { onDeleteKeyframe(kf.id) },
                                                modifier = Modifier.size(16.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(10.dp))
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
}

@Composable
private fun AnimationCard(
    anim: ElementAnimationItem,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.width(84.dp),
        shape = RoundedCornerShape(12.dp),
        color = CardSurface,
        border = BorderStroke(1.dp, Color(0xFF2B2B3D))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2D2A4A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (anim.type) {
                        AnimationType.ENTRY -> Icons.Default.Login
                        AnimationType.EXIT -> Icons.Default.Logout
                        AnimationType.LOOP -> Icons.Default.Sync
                    },
                    contentDescription = null,
                    tint = SoftPurpleGlow,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(anim.name, color = TextMain, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
            Text(anim.category, color = TextSub, fontSize = 9.sp)
        }
    }
}
