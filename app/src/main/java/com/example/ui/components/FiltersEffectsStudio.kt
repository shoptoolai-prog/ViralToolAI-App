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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

// ============================================================================
// MASTER PHASE 10: CAPCUT PRO ASSET MARKETPLACE & STUDIO ENGINE
// ============================================================================

private val PureBlackBg = Color(0xFF000000)
private val DarkPanelBg = Color(0xFF0D0E15)
private val CardSurfaceDark = Color(0xFF151722)
private val CardSurfaceBorder = Color(0xFF222536)
private val MintPrimary = Color(0xFF38E8A5)
private val MintGlow = Color(0x3338E8A5)
private val GoldPro = Color(0xFFF59E0B)
private val TextMainWhite = Color(0xFFFFFFFF)
private val TextMutedGray = Color(0xFF9CA3AF)

enum class AssetType { FILTER, EFFECT, TRANSITION, ANIMATION }

data class MarketAsset(
    val id: String,
    val name: String,
    val type: AssetType,
    val category: String,
    val previewUrl: String,
    val isPremium: Boolean = false,
    val isDownloaded: Boolean = true,
    val rating: Float = 4.9f,
    val colorMatrix: ColorMatrix? = null,
    val effectOverlayColor: Color = Color.Transparent,
    val description: String = ""
)

// Legacy compatibility models
data class FilterItem(
    val id: String,
    val name: String,
    val category: String,
    val colorMatrix: ColorMatrix,
    val defaultIntensity: Float = 1.0f,
    val description: String = ""
)

data class EffectItem(
    val id: String,
    val name: String,
    val category: String,
    val effectType: String,
    val defaultIntensity: Float = 0.8f,
    val defaultDuration: Float = 3.0f,
    val defaultBlend: Float = 0.5f,
    val defaultOpacity: Float = 1.0f,
    val colorOverlay: Color = Color.Transparent,
    val description: String = ""
)

object FilterMatrixRepository {
    val NONE = FilterItem("none", "Original", "Trending", ColorMatrix())
    val ALL_FILTERS = listOf(
        NONE,
        FilterItem("f_teal_orange", "Teal & Orange", "Cinematic", ColorMatrix(floatArrayOf(1.2f,0.1f,-0.1f,0f,15f, 0f,1f,0.2f,0f,5f, -0.2f,0.2f,1.3f,0f,-10f, 0f,0f,0f,1f,0f))),
        FilterItem("f_cyber_neon", "Cyber Neon", "Neon", ColorMatrix(floatArrayOf(1.3f,0f,0.4f,0f,20f, 0f,0.8f,0.5f,0f,0f, 0.3f,0.1f,1.4f,0f,30f, 0f,0f,0f,1f,0f))),
        FilterItem("f_golden_hour", "Golden Hour", "India", ColorMatrix(floatArrayOf(1.3f,0.2f,0f,0f,25f, 0.1f,1.1f,0f,0f,15f, -0.1f,0f,0.8f,0f,-10f, 0f,0f,0f,1f,0f))),
        FilterItem("f_retro_90s", "Retro 90s", "Vintage", ColorMatrix(floatArrayOf(1.1f,0.1f,0.1f,0f,10f, 0.1f,0.9f,0.1f,0f,5f, 0.1f,0.2f,0.8f,0f,15f, 0f,0f,0f,1f,0f))),
        FilterItem("f_monsoon_rain", "Monsoon Rain", "Monsoon", ColorMatrix(floatArrayOf(0.9f,0.1f,0.2f,0f,-5f, 0f,1.05f,0.2f,0f,5f, 0.1f,0.2f,1.25f,0f,15f, 0f,0f,0f,1f,0f))),
        FilterItem("f_ladakh_cold", "Ladakh Blue", "Mountains", ColorMatrix(floatArrayOf(0.85f,0.1f,0.1f,0f,-10f, 0f,1.1f,0.2f,0f,10f, 0.1f,0.2f,1.35f,0f,25f, 0f,0f,0f,1f,0f))),
        FilterItem("f_kashmir_mist", "Kashmir Haze", "Nature", ColorMatrix(floatArrayOf(1.05f,0.1f,0.1f,0f,12f, 0.1f,1.05f,0.1f,0f,10f, 0.1f,0.1f,1.15f,0f,15f, 0f,0f,0f,1f,0f))),
        FilterItem("f_jaipur_royal", "Jaipur Royal", "India", ColorMatrix(floatArrayOf(1.25f,0.15f,0f,0f,20f, 0.1f,1.1f,0f,0f,12f, 0f,0f,0.8f,0f,-5f, 0f,0f,0f,1f,0f))),
        FilterItem("f_black_matte", "Black Matte", "Black & White", ColorMatrix(floatArrayOf(0.33f,0.59f,0.11f,0f,5f, 0.33f,0.59f,0.11f,0f,5f, 0.33f,0.59f,0.11f,0f,5f, 0f,0f,0f,1f,0f)))
    )

    fun getFilterById(id: String): FilterItem {
        return ALL_FILTERS.find { it.id == id } ?: NONE
    }
}

object EffectRepository {
    val NONE = EffectItem("e_none", "None", "Trending", "None", 0f, 0f, 0f, 0f)
    val ALL_EFFECTS = listOf(
        NONE,
        EffectItem("e_rgb_split", "RGB Split", "Glitch", "RGB", 0.8f, 3f, 0.6f, 0.9f, Color(0x33FF00FF)),
        EffectItem("e_zoom_blur", "Zoom Blur", "Motion", "Motion", 0.7f, 2.5f, 0.5f, 0.8f, Color(0x228B5CF6)),
        EffectItem("e_flash_leak", "Flash Leak", "Light FX", "Light", 0.85f, 1.5f, 0.7f, 0.95f, Color(0x44FFA500)),
        EffectItem("e_vhs_glitch", "VHS Glitch", "Glitch", "Glitch", 0.9f, 4f, 0.8f, 0.9f, Color(0x3300FFFF)),
        EffectItem("e_neon_pulse", "Neon Pulse", "Neon", "Neon", 0.75f, 3f, 0.65f, 0.85f, Color(0x33A78BFA)),
        EffectItem("e_gold_embers", "Gold Embers", "Fire", "Fire", 0.85f, 4f, 0.7f, 0.9f, Color(0x44EF4444)),
        EffectItem("e_cyber_rain", "Cyber Rain", "Rain", "Rain", 0.75f, 4f, 0.6f, 0.85f, Color(0x3338BDF8))
    )

    fun getEffectById(id: String): EffectItem {
        return ALL_EFFECTS.find { it.id == id } ?: NONE
    }
}

// Comprehensive Marketplace Catalog Repository (Generating 500+ Filters, 1000+ Effects, 700+ Transitions, 500+ Animations)
object CapCutProAssetMarketplaceRepository {

    val CATEGORIES = listOf(
        "Featured", "Trending", "Cinematic", "Portrait", "Nature", "Travel", "India",
        "Monsoon", "Mountains", "Forest", "Temple", "City", "Night", "Food", "Wedding",
        "Festival", "Street", "Vintage", "Film", "Black & White", "HDR", "Neon", "Moody",
        "Aesthetic", "Luxury", "Gaming", "Anime", "Vlog", "Shorts", "Reels", "YouTube",
        "Instagram", "AI Generated", "Latest"
    )

    private val THUMBNAIL_URLS = listOf(
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80", // Mountains
        "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=400&q=80", // Taj Mahal
        "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=400&q=80", // Kerala
        "https://images.unsplash.com/photo-1570168007204-dfb528c6958f?auto=format&fit=crop&w=400&q=80", // Mumbai
        "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=400&q=80", // Sunset
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80", // Portrait
        "https://images.unsplash.com/photo-1518173946687-a4c8a383392e?auto=format&fit=crop&w=400&q=80", // Film
        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=400&q=80", // Food
        "https://images.unsplash.com/photo-1448375240586-882707db888b?auto=format&fit=crop&w=400&q=80", // Forest
        "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?auto=format&fit=crop&w=400&q=80", // Cyber
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=400&q=80"  // Beach
    )

    val ALL_MARKET_ASSETS: List<MarketAsset> by lazy {
        val list = mutableListOf<MarketAsset>()

        // 1. Generate 520 Filters
        val filterNames = listOf(
            "Teal & Orange", "Cyber Neon", "Golden Hour", "Retro 90s", "Monsoon Rain",
            "Ladakh Blue", "Kashmir Haze", "Jaipur Royal", "Black Matte", "Kodak Chrome",
            "Fuji Film", "Vivid HDR", "Cinematic Mood", "Nordic Crisp", "Peachy Blush",
            "Monaco Luxury", "Midnight Onyx", "Desert Gold", "Tropical Teal", "Porcelain Glow",
            "Sunburst Warmth", "Arctic Cyan", "Velvet Dark", "Dream Pastel", "AI Auto Tone"
        )
        for (i in 1..520) {
            val name = "${filterNames[(i - 1) % filterNames.size]} ${if (i > filterNames.size) "#${i / filterNames.size + 1}" else ""}".trim()
            val cat = CATEGORIES[(i - 1) % CATEGORIES.size]
            val img = THUMBNAIL_URLS[(i - 1) % THUMBNAIL_URLS.size]
            val filterObj = FilterMatrixRepository.ALL_FILTERS.getOrElse(i % FilterMatrixRepository.ALL_FILTERS.size) { FilterMatrixRepository.NONE }
            list.add(
                MarketAsset(
                    id = "filter_$i",
                    name = name,
                    type = AssetType.FILTER,
                    category = cat,
                    previewUrl = img,
                    isPremium = i % 4 == 0,
                    isDownloaded = i % 3 != 0,
                    colorMatrix = filterObj.colorMatrix,
                    description = "Professional color grade filter for $cat reels"
                )
            )
        }

        // 2. Generate 1020 Effects
        val effectNames = listOf(
            "RGB Split", "Zoom Blur", "Flash Leak", "VHS Glitch", "Neon Pulse",
            "Gold Embers", "Cyber Rain", "Light Rays", "Smoke Fog", "Diamond Glint",
            "Film Dust", "Ethereal Glow", "Camera Shake", "Anamorphic Flare", "Bokeh Circles",
            "Particle Stardust", "Prism Wave", "Hologram Grid", "Matrix Data", "Thermal FX"
        )
        for (i in 1..1020) {
            val name = "${effectNames[(i - 1) % effectNames.size]} ${if (i > effectNames.size) "Pro #${i / effectNames.size + 1}" else ""}".trim()
            val cat = CATEGORIES[(i - 1) % CATEGORIES.size]
            val img = THUMBNAIL_URLS[(i * 3) % THUMBNAIL_URLS.size]
            list.add(
                MarketAsset(
                    id = "effect_$i",
                    name = name,
                    type = AssetType.EFFECT,
                    category = cat,
                    previewUrl = img,
                    isPremium = i % 3 == 0,
                    isDownloaded = i % 2 == 0,
                    effectOverlayColor = if (i % 2 == 0) Color(0x3338E8A5) else Color(0x33FF00FF),
                    description = "Dynamic GPU video effect for $cat clips"
                )
            )
        }

        // 3. Generate 720 Transitions
        val transitionNames = listOf(
            "Smooth Push", "Glitch Spin", "Zoom Whip", "Camera Flash", "Directional Blur",
            "Film Burn", "Paper Tear", "Glass Shatter", "Cube 3D", "Liquid Wipe",
            "Mask Circle", "Page Flip", "AI Morph", "Light Leak Pass", "Seamless Fade"
        )
        for (i in 1..720) {
            val name = "${transitionNames[(i - 1) % transitionNames.size]} ${if (i > transitionNames.size) "v${i / transitionNames.size + 1}" else ""}".trim()
            val cat = CATEGORIES[(i - 1) % CATEGORIES.size]
            val img = THUMBNAIL_URLS[(i * 5) % THUMBNAIL_URLS.size]
            list.add(
                MarketAsset(
                    id = "trans_$i",
                    name = name,
                    type = AssetType.TRANSITION,
                    category = cat,
                    previewUrl = img,
                    isPremium = i % 5 == 0,
                    isDownloaded = true,
                    description = "Seamless CapCut Pro style transition"
                )
            )
        }

        // 4. Generate 520 Animations
        val animNames = listOf(
            "Elastic Pop In", "Smooth Slide Up", "3D Flip Entrance", "Typewriter Neon", "Glitch Bounce",
            "Zoom Spring Out", "Swing Loop", "Camera Roll In", "Luxury Gold Fade", "Instagram Reel Pop"
        )
        for (i in 1..520) {
            val name = "${animNames[(i - 1) % animNames.size]} ${if (i > animNames.size) "#${i / animNames.size + 1}" else ""}".trim()
            val cat = CATEGORIES[(i - 1) % CATEGORIES.size]
            val img = THUMBNAIL_URLS[(i * 7) % THUMBNAIL_URLS.size]
            list.add(
                MarketAsset(
                    id = "anim_$i",
                    name = name,
                    type = AssetType.ANIMATION,
                    category = cat,
                    previewUrl = img,
                    isPremium = i % 6 == 0,
                    isDownloaded = true,
                    description = "Keyframe motion animation for video/text"
                )
            )
        }

        list
    }
}

// ============================================================================
// COMPONENT: LEGACY ADAPTER & HELPER COMPONENTS
// ============================================================================

@Composable
fun StudioMainTab(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = title,
        color = if (isSelected) MintPrimary else TextMutedGray,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    )
}

@Composable
fun CategoryChip(label: String, isSelected: Boolean, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onSelect),
        color = if (isSelected) MintPrimary else CardSurfaceDark,
        border = if (isSelected) null else BorderStroke(1.dp, CardSurfaceBorder)
    ) {
        Text(
            text = label,
            color = if (isSelected) PureBlackBg else TextMainWhite,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun FiltersEffectsStudioSheet(
    activeMediaUri: Uri?,
    currentFilterId: String,
    currentFilterIntensity: Float,
    currentEffectId: String,
    currentEffectIntensity: Float,
    currentEffectDuration: Float,
    currentEffectBlend: Float,
    currentEffectOpacity: Float,
    onPreviewFilter: (FilterItem?, Float) -> Unit,
    onPreviewEffect: (EffectItem?, Float, Float, Float, Float) -> Unit,
    onComparePressChanged: (Boolean) -> Unit,
    onApplyFilter: (FilterItem, Float) -> Unit,
    onApplyEffect: (EffectItem, Float, Float, Float, Float) -> Unit,
    onCancel: () -> Unit,
    onClose: () -> Unit
) {
    CapCutProMarketplaceSheet(
        initialTab = AssetType.FILTER,
        activeFilterId = currentFilterId,
        activeEffectId = currentEffectId,
        onSelectAsset = { asset ->
            when (asset.type) {
                AssetType.FILTER -> {
                    val filterItem = FilterMatrixRepository.getFilterById(asset.id)
                    onApplyFilter(filterItem, 1.0f)
                }
                AssetType.EFFECT -> {
                    val effectItem = EffectRepository.getEffectById(asset.id)
                    onApplyEffect(effectItem, 0.8f, 3.0f, 0.5f, 1.0f)
                }
                else -> {
                    onClose()
                }
            }
        },
        onClose = onClose
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CapCutProMarketplaceSheet(
    initialTab: AssetType = AssetType.FILTER,
    activeFilterId: String? = null,
    activeEffectId: String? = null,
    activeAnimId: String? = null,
    onSelectAsset: (MarketAsset) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(initialTab) }
    var selectedCategory by remember { mutableStateOf("Featured") }
    var selectedSubFilter by remember { mutableStateOf("All") } // All, Favorites, Downloads, AI Recommended, Trending, Free, Premium

    // Local Download & Favorite State tracking
    var downloadedAssetIds by remember { mutableStateOf(setOf<String>()) }
    var downloadingAssetIds by remember { mutableStateOf(mapOf<String, Float>()) } // id -> progress
    var favoriteAssetIds by remember { mutableStateOf(setOf<String>("filter_1", "filter_3", "effect_1", "effect_2", "trans_1")) }

    // Compare mode state
    var isHoldingCompare by remember { mutableStateOf(false) }
    var holdCompareAsset by remember { mutableStateOf<MarketAsset?>(null) }

    // Filter Assets dynamically
    val filteredAssets by remember(searchQuery, selectedType, selectedCategory, selectedSubFilter, downloadedAssetIds, favoriteAssetIds) {
        derivedStateOf {
            CapCutProAssetMarketplaceRepository.ALL_MARKET_ASSETS.filter { asset ->
                // Type match
                val matchType = asset.type == selectedType

                // Category match
                val matchCat = selectedCategory == "Featured" || selectedCategory == "Trending" || asset.category.equals(selectedCategory, ignoreCase = true)

                // SubFilter match
                val matchSub = when (selectedSubFilter) {
                    "Favorites" -> favoriteAssetIds.contains(asset.id)
                    "Downloads" -> asset.isDownloaded || downloadedAssetIds.contains(asset.id)
                    "AI Recommended" -> asset.isPremium || asset.category == "India" || asset.category == "Cinematic"
                    "Trending" -> asset.category == "Trending" || asset.rating >= 4.9f
                    "Premium" -> asset.isPremium
                    "Free" -> !asset.isPremium
                    else -> true
                }

                // Search query match
                val matchSearch = searchQuery.isEmpty() ||
                        asset.name.contains(searchQuery, ignoreCase = true) ||
                        asset.category.contains(searchQuery, ignoreCase = true) ||
                        asset.type.name.contains(searchQuery, ignoreCase = true)

                matchType && matchCat && matchSub && matchSearch
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp)
            .shadow(16.dp),
        color = DarkPanelBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            // TOP HEADER: SEARCH & TYPE TABS
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Search Input Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search 2700+ Pro Assets...", color = TextMutedGray, fontSize = 11.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MintPrimary, modifier = Modifier.size(16.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TextMutedGray,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { searchQuery = "" }
                            )
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardSurfaceDark,
                        unfocusedContainerColor = CardSurfaceDark,
                        focusedBorderColor = MintPrimary,
                        unfocusedBorderColor = CardSurfaceBorder,
                        focusedTextColor = TextMainWhite,
                        unfocusedTextColor = TextMainWhite
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                )

                Spacer(Modifier.width(8.dp))

                // Compare Status Indicator if holding
                if (isHoldingCompare) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEF4444))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("ORIGINAL PREVIEW", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(6.dp))
                }

                // Close Button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MintPrimary.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Done", tint = MintPrimary, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(6.dp))

            // MAIN TYPE TABS (FILTERS, EFFECTS, TRANSITIONS, ANIMATIONS)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PureBlackBg)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AssetType.values().forEach { type ->
                    val isSel = selectedType == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) MintPrimary else Color.Transparent)
                            .clickable { selectedType = type }
                            .padding(vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (type) {
                                AssetType.FILTER -> "Filters"
                                AssetType.EFFECT -> "Effects"
                                AssetType.TRANSITION -> "Transitions"
                                AssetType.ANIMATION -> "Animations"
                            },
                            color = if (isSel) PureBlackBg else TextMainWhite,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // QUICK FILTER CHIPS (Favorites, Downloads, AI Rec, Trending, Premium)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val subFilters = listOf("All", "Favorites", "Downloads", "AI Recommended", "Trending", "Free", "Premium")
                items(subFilters) { sub ->
                    val isSel = selectedSubFilter == sub
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSel) MintGlow else CardSurfaceDark)
                            .border(BorderStroke(1.dp, if (isSel) MintPrimary else CardSurfaceBorder), RoundedCornerShape(12.dp))
                            .clickable { selectedSubFilter = sub }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (sub == "Favorites") {
                                Icon(Icons.Default.Star, contentDescription = null, tint = GoldPro, modifier = Modifier.size(11.dp))
                                Spacer(Modifier.width(3.dp))
                            } else if (sub == "Downloads") {
                                Icon(Icons.Outlined.Download, contentDescription = null, tint = MintPrimary, modifier = Modifier.size(11.dp))
                                Spacer(Modifier.width(3.dp))
                            }
                            Text(
                                text = sub,
                                color = if (isSel) MintPrimary else TextMainWhite,
                                fontSize = 10.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // CATEGORY SCROLL ROW
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(CapCutProAssetMarketplaceRepository.CATEGORIES) { cat ->
                    val isSel = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) MintPrimary.copy(alpha = 0.25f) else Color(0xFF131520))
                            .border(BorderStroke(1.dp, if (isSel) MintPrimary else CardSurfaceBorder), RoundedCornerShape(8.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSel) MintPrimary else TextMutedGray,
                            fontSize = 10.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // 2-COLUMN GRID (110dp x 150dp rounded 18dp cards)
            if (filteredAssets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No assets found matching filter criteria",
                        color = TextMutedGray,
                        fontSize = 11.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredAssets, key = { it.id }) { asset ->
                        val isDownloaded = asset.isDownloaded || downloadedAssetIds.contains(asset.id)
                        val downloadingProgress = downloadingAssetIds[asset.id]
                        val isFavorite = favoriteAssetIds.contains(asset.id)

                        val isActive = when (asset.type) {
                            AssetType.FILTER -> activeFilterId == asset.id
                            AssetType.EFFECT -> activeEffectId == asset.id
                            AssetType.ANIMATION -> activeAnimId == asset.id
                            else -> false
                        }

                        // CapCut Pro Asset Card (Rounded 18dp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(CardSurfaceDark)
                                .border(
                                    border = if (isActive) BorderStroke(2.5.dp, MintPrimary) else BorderStroke(1.dp, CardSurfaceBorder),
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            if (!isDownloaded && downloadingProgress == null) {
                                                // Start download simulation
                                                scope.launch {
                                                    for (p in 1..10) {
                                                        delay(80)
                                                        downloadingAssetIds = downloadingAssetIds + (asset.id to (p * 0.1f))
                                                    }
                                                    downloadingAssetIds = downloadingAssetIds - asset.id
                                                    downloadedAssetIds = downloadedAssetIds + asset.id
                                                    onSelectAsset(asset)
                                                }
                                            } else {
                                                onSelectAsset(asset)
                                            }
                                        },
                                        onLongPress = {
                                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                            isHoldingCompare = true
                                            holdCompareAsset = asset
                                        }
                                    )
                                }
                        ) {
                            // Thumbnail Preview
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(asset.previewUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = asset.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Effect Overlay Tint if present
                            if (asset.effectOverlayColor != Color.Transparent) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(asset.effectOverlayColor)
                                )
                            }

                            // Bottom Gradient Dark Scrim
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                                            startY = 50f
                                        )
                                    )
                            )

                            // Top End: Favorite Button
                            IconButton(
                                onClick = {
                                    favoriteAssetIds = if (isFavorite) favoriteAssetIds - asset.id else favoriteAssetIds + asset.id
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .padding(3.dp)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) GoldPro else Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Top Start: PRO Tag or Download Button
                            if (asset.isPremium) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GoldPro)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("PRO", color = PureBlackBg, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
                                }
                            } else if (!isDownloaded) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(4.dp)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.6f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (downloadingProgress != null) {
                                        CircularProgressIndicator(
                                            progress = { downloadingProgress },
                                            color = MintPrimary,
                                            strokeWidth = 1.5.dp,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    } else {
                                        Icon(
                                            Icons.Outlined.Download,
                                            contentDescription = "Download",
                                            tint = MintPrimary,
                                            modifier = Modifier.size(11.dp)
                                        )
                                    }
                                }
                            }

                            // Bottom Content: Name & Live Badge
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(6.dp)
                            ) {
                                if (isActive) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MintPrimary)
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text("LIVE PREVIEW", color = PureBlackBg, fontSize = 6.5.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(Modifier.height(2.dp))
                                }

                                Text(
                                    text = asset.name,
                                    color = if (isActive) MintPrimary else TextMainWhite,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = asset.category,
                                    color = TextMutedGray,
                                    fontSize = 7.5.sp,
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
}
