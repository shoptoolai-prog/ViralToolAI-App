package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.StickerTrackItem
import com.example.ui.screens.TextTrackItem
import kotlinx.coroutines.delay

// Dark Luxury Apple Aesthetic Color Palette
private val LuxuryDarkBg = Color(0xFF0D0D11)
private val LuxuryCardBg = Color(0xFF15151B)
private val LuxuryItemBg = Color(0xFF1D1D26)
private val LuxuryBorderColor = Color(0x33FFFFFF)
private val PrimaryPurple = Color(0xFF7C3AED)
private val BrightPurple = Color(0xFFA78BFA)
private val EmeraldGreen = Color(0xFF10B981)
private val BrightEmerald = Color(0xFF34D399)
private val AccentGold = Color(0xFFF59E0B)
private val WarningRed = Color(0xFFEF4444)
private val TextWhite = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF94A3B8)

// Data Models for Affiliate Assistant
data class DetectedProductInfo(
    val mainProduct: String = "Women's Designer Saree",
    val category: String = "Fashion / Ethnic Wear",
    val brand: String = "Kanjivaram Silks",
    val dominantColors: String = "Royal Magenta & Gold",
    val productPosition: String = "Center Frame (Safe)",
    val confidencePercent: Int = 96
)

data class PriceStickerConfig(
    val currentPrice: String = "₹399",
    val oldPrice: String = "₹799",
    val offer: String = "Flat 50% OFF",
    val coupon: String = "MEESHO50",
    val delivery: String = "Free Delivery",
    val cod: String = "Cash on Delivery"
)

enum class EcommercePlatform(val displayName: String, val brandColor: Color, val logoEmoji: String) {
    MEESHO("Meesho", Color(0xFFE91E63), "🛍️"),
    AMAZON("Amazon", Color(0xFFFF9900), "📦"),
    FLIPKART("Flipkart", Color(0xFF2874F0), "⚡"),
    MYNTRA("Myntra", Color(0xFFE0005E), "👗"),
    AJIO("Ajio", Color(0xFF2C3E50), "✨"),
    NYKAA("Nykaa", Color(0xFFFC2779), "💄")
}

enum class StylePack(val label: String, val emoji: String, val primaryColor: Color, val desc: String) {
    FASHION("Fashion", "👗", Color(0xFFEC4899), "Elegant Rose & Gold Accent"),
    BEAUTY("Beauty", "💄", Color(0xFFF43F5E), "Soft Glow & Pastel Tones"),
    SKINCARE("Skincare", "✨", Color(0xFF14B8A6), "Fresh Teal & Clean Mint"),
    KITCHEN("Kitchen", "🍳", Color(0xFFF97316), "Warm Amber & Vibrant Orange"),
    ELECTRONICS("Electronics", "⚡", Color(0xFF3B82F6), "Neon Cyber Blue Tech"),
    HOME_DECOR("Home Decor", "🏡", Color(0xFF8B5CF6), "Warm Earth & Gold Tones"),
    JEWELLERY("Jewellery", "💎", Color(0xFFA855F7), "Royal Violet & Diamond Sparkle")
}

@Composable
fun AiAffiliateCreatorStudioPanel(
    videoDurationSec: Double = 15.0,
    onDismiss: () -> Unit,
    onApplyToTimeline: (List<TextTrackItem>, List<StickerTrackItem>) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Panel collapse/expand state
    var isPanelCollapsed by remember { mutableStateOf(false) }

    // Section 2: Smart Product Detection State
    var isAnalyzing by remember { mutableStateOf(false) }
    var detectedProduct by remember { mutableStateOf(DetectedProductInfo()) }

    // Section 3: Price Sticker Generator State
    var priceConfig by remember { mutableStateOf(PriceStickerConfig()) }
    var selectedStickerStyleIndex by remember { mutableIntStateOf(0) }

    // Section 4: Smart Logo Placement State
    var selectedPlatform by remember { mutableStateOf(EcommercePlatform.MEESHO) }
    var logoPosition by remember { mutableStateOf("Top Right") }
    var isLogoApproved by remember { mutableStateOf(false) }

    // Section 5: Smart Text Placement State
    var selectedTextPosition by remember { mutableStateOf("Top Safety Area") }

    // Section 6: CTA Generator State
    var selectedCtaText by remember { mutableStateOf("Buy Now") }
    var customCtaInput by remember { mutableStateOf("Link in Bio") }

    // Section 7: Style Packs State
    var selectedStylePack by remember { mutableStateOf(StylePack.FASHION) }

    // Section 8: Auto Check State (Validation flags)
    var isTextCoveringProduct by remember { mutableStateOf(false) }
    var isLogoCoveringFace by remember { mutableStateOf(false) }
    var isPriceHidden by remember { mutableStateOf(false) }
    var isCtaTooSmall by remember { mutableStateOf(false) }
    var isLowReadability by remember { mutableStateOf(false) }

    // Section 9: Preview Mode State
    var isBeforeAfterMode by remember { mutableStateOf(false) }
    var isShowingAfterPreview by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    // Price sticker presets
    val stickerPresets = remember(priceConfig) {
        listOf(
            "₹${priceConfig.currentPrice.replace("₹", "")}" to "Classic Gold Pill",
            "Only ${priceConfig.currentPrice}" to "Minimal Dark Badge",
            priceConfig.offer to "Flame Red Tag",
            priceConfig.cod to "Emerald COD Badge",
            priceConfig.delivery to "Express Shipping",
            "BEST SELLER" to "Hot Trending Badge"
        )
    }

    // Available CTAs
    val ctaOptions = listOf(
        "Buy Now",
        "Link in Bio",
        "Available Now",
        "Limited Stock",
        "Order Today",
        "COD Available"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .border(
                BorderStroke(1.dp, LuxuryBorderColor),
                RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ),
        color = LuxuryDarkBg,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            // ==================================================
            // SECTION 1: HEADER & PANEL CONTROLS
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = LuxuryCardBg
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    // Drag Handle Bar
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f))
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Small AI Badge
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = PrimaryPurple.copy(alpha = 0.22f),
                                border = BorderStroke(1.dp, BrightPurple.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = BrightEmerald,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "AI CREATOR",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = BrightPurple,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Text(
                                text = "AI Creator Assistant",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Collapse / Expand Toggle
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isPanelCollapsed = !isPanelCollapsed
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPanelCollapsed) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                                    contentDescription = "Collapse/Expand",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Dismiss Close
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (!isPanelCollapsed) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 520.dp)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // ==================================================
                    // SECTION 9: PREVIEW MODE SWITCH (BEFORE / AFTER)
                    // ==================================================
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .border(BorderStroke(1.dp, LuxuryBorderColor), RoundedCornerShape(18.dp)),
                        color = LuxuryCardBg
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Compare,
                                    contentDescription = null,
                                    tint = BrightPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "Interactive Preview Mode",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = if (isBeforeAfterMode) "Compare raw vs AI shopping frame" else "Real-time AI Overlay Preview",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (!isShowingAfterPreview) PrimaryPurple else LuxuryItemBg,
                                    modifier = Modifier.clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isBeforeAfterMode = true
                                        isShowingAfterPreview = false
                                    }
                                ) {
                                    Text(
                                        text = "BEFORE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (!isShowingAfterPreview) TextWhite else TextSecondary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = if (isShowingAfterPreview) BrightPurple else LuxuryItemBg,
                                    modifier = Modifier.clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isBeforeAfterMode = true
                                        isShowingAfterPreview = true
                                    }
                                ) {
                                    Text(
                                        text = "AFTER",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isShowingAfterPreview) Color.Black else TextSecondary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }

                    // ==================================================
                    // SECTION 2: SMART PRODUCT DETECTION
                    // ==================================================
                    SectionCard(
                        title = "SMART PRODUCT DETECTION",
                        badgeText = "${detectedProduct.confidencePercent}% Confidence",
                        icon = Icons.Default.CenterFocusWeak
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = detectedProduct.mainProduct,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "Category: ${detectedProduct.category}",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }

                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isAnalyzing = true
                                    },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryItemBg),
                                    border = BorderStroke(1.dp, LuxuryBorderColor),
                                    shape = CircleShape
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = null,
                                            tint = BrightPurple,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (isAnalyzing) "Scanning..." else "Re-Analyze",
                                            fontSize = 11.sp,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            // Product details grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MiniDetailPill(
                                    label = "Brand",
                                    value = detectedProduct.brand,
                                    icon = Icons.Default.Verified,
                                    modifier = Modifier.weight(1f)
                                )
                                MiniDetailPill(
                                    label = "Colors",
                                    value = detectedProduct.dominantColors,
                                    icon = Icons.Default.Palette,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MiniDetailPill(
                                    label = "Position",
                                    value = detectedProduct.productPosition,
                                    icon = Icons.Default.CropFree,
                                    modifier = Modifier.weight(1f)
                                )
                                MiniDetailPill(
                                    label = "Safe Zone",
                                    value = "100% Face Clear",
                                    icon = Icons.Default.Security,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // ==================================================
                    // SECTION 7: STYLE PACKS
                    // ==================================================
                    SectionCard(
                        title = "AFFILIATE STYLE PACKS",
                        badgeText = selectedStylePack.label,
                        icon = Icons.Default.Style
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Select product niche to optimize colors, typography & sticker animations:",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StylePack.entries.take(4).forEach { pack ->
                                    StylePackChip(
                                        pack = pack,
                                        isSelected = selectedStylePack == pack,
                                        onSelect = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedStylePack = pack
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StylePack.entries.drop(4).forEach { pack ->
                                    StylePackChip(
                                        pack = pack,
                                        isSelected = selectedStylePack == pack,
                                        onSelect = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedStylePack = pack
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // ==================================================
                    // SECTION 3: PRICE STICKER GENERATOR
                    // ==================================================
                    SectionCard(
                        title = "PRICE STICKER GENERATOR",
                        badgeText = "6 Styles Ready",
                        icon = Icons.Default.Sell
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Inputs Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = priceConfig.currentPrice,
                                    onValueChange = { priceConfig = priceConfig.copy(currentPrice = it) },
                                    label = { Text("Current Price", fontSize = 10.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BrightPurple,
                                        unfocusedBorderColor = LuxuryBorderColor,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite
                                    )
                                )
                                OutlinedTextField(
                                    value = priceConfig.oldPrice,
                                    onValueChange = { priceConfig = priceConfig.copy(oldPrice = it) },
                                    label = { Text("Old Price (Opt)", fontSize = 10.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BrightPurple,
                                        unfocusedBorderColor = LuxuryBorderColor,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite
                                    )
                                )
                                OutlinedTextField(
                                    value = priceConfig.offer,
                                    onValueChange = { priceConfig = priceConfig.copy(offer = it) },
                                    label = { Text("Offer Tag", fontSize = 10.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BrightPurple,
                                        unfocusedBorderColor = LuxuryBorderColor,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite
                                    )
                                )
                            }

                            // Delivery & COD options
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MiniToggleOption(
                                    label = priceConfig.delivery,
                                    icon = Icons.Default.LocalShipping,
                                    modifier = Modifier.weight(1f)
                                )
                                MiniToggleOption(
                                    label = priceConfig.cod,
                                    icon = Icons.Default.Payments,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Text(
                                text = "Preview Sticker Styles (Tap style to select):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            // Sticker Previews
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                stickerPresets.chunked(2).forEach { pair ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        pair.forEachIndexed { idxInPair, (stickerText, styleLabel) ->
                                            val globalIdx = stickerPresets.indexOfFirst { it.first == stickerText }
                                            StickerStylePreviewCard(
                                                text = stickerText,
                                                styleName = styleLabel,
                                                isSelected = selectedStickerStyleIndex == globalIdx,
                                                accentColor = selectedStylePack.primaryColor,
                                                onSelect = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    selectedStickerStyleIndex = globalIdx
                                                },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ==================================================
                    // SECTION 4: SMART LOGO PLACEMENT
                    // ==================================================
                    SectionCard(
                        title = "SMART LOGO PLACEMENT",
                        badgeText = if (isLogoApproved) "Approved" else "Preview Only",
                        icon = Icons.Default.LocalOffer
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Select Platform Brand Logo:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            // Platform Selector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                EcommercePlatform.entries.forEach { platform ->
                                    val isSelected = selectedPlatform == platform
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) platform.brandColor.copy(alpha = 0.25f) else LuxuryItemBg,
                                        border = BorderStroke(
                                            1.dp,
                                            if (isSelected) platform.brandColor else LuxuryBorderColor
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                selectedPlatform = platform
                                                isLogoApproved = false
                                            }
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = platform.logoEmoji, fontSize = 16.sp)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = platform.displayName,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) TextWhite else TextSecondary
                                            )
                                        }
                                    }
                                }
                            }

                            // Logo Position & Approval Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Suggested Position: $logoPosition",
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "AI verified — Face & Product clear",
                                        fontSize = 11.sp,
                                        color = EmeraldGreen
                                    )
                                }

                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isLogoApproved = !isLogoApproved
                                        Toast.makeText(
                                            context,
                                            if (isLogoApproved) "${selectedPlatform.displayName} Logo Approved!" else "Logo Preview Reset",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isLogoApproved) EmeraldGreen else PrimaryPurple
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isLogoApproved) Icons.Default.CheckCircle else Icons.Default.Add,
                                            contentDescription = null,
                                            tint = TextWhite,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = if (isLogoApproved) "Applied" else "Apply Logo",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ==================================================
                    // SECTION 5: SMART TEXT PLACEMENT
                    // ==================================================
                    SectionCard(
                        title = "SMART TEXT PLACEMENT",
                        badgeText = "Safe Areas Found",
                        icon = Icons.Default.FormatAlignLeft
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "AI automatically guarantees zero obstruction of critical video regions:",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )

                            val positions = listOf(
                                "Top Safety Area" to "Recommended for Title",
                                "Bottom Banner Space" to "Best for Price & CTA",
                                "Center Offset Right" to "Side product review",
                                "Sub-Header Below Product" to "Category caption"
                            )

                            positions.forEach { (posTitle, posDesc) ->
                                val isSelected = selectedTextPosition == posTitle
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSelected) PrimaryPurple.copy(alpha = 0.2f) else LuxuryItemBg,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) BrightPurple else LuxuryBorderColor
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedTextPosition = posTitle
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (isSelected) BrightPurple else TextSecondary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = posTitle,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite
                                                )
                                                Text(
                                                    text = posDesc,
                                                    fontSize = 11.sp,
                                                    color = TextSecondary
                                                )
                                            }
                                        }

                                        Surface(
                                            shape = CircleShape,
                                            color = EmeraldGreen.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = "Safe",
                                                fontSize = 10.sp,
                                                color = BrightEmerald,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ==================================================
                    // SECTION 6: CTA GENERATOR
                    // ==================================================
                    SectionCard(
                        title = "CALL-TO-ACTION (CTA) GENERATOR",
                        badgeText = "Editable",
                        icon = Icons.Default.TouchApp
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "High-converting affiliate Call-To-Action buttons:",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )

                            // CTA Chips
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                ctaOptions.chunked(3).forEach { row ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        row.forEach { cta ->
                                            val isSelected = selectedCtaText == cta
                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = if (isSelected) selectedStylePack.primaryColor else LuxuryItemBg,
                                                border = BorderStroke(
                                                    1.dp,
                                                    if (isSelected) Color.White else LuxuryBorderColor
                                                ),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        selectedCtaText = cta
                                                        customCtaInput = cta
                                                    }
                                            ) {
                                                Text(
                                                    text = cta,
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.padding(vertical = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Custom CTA Input
                            OutlinedTextField(
                                value = customCtaInput,
                                onValueChange = {
                                    customCtaInput = it
                                    selectedCtaText = it
                                },
                                label = { Text("Custom CTA Label", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrightPurple,
                                    unfocusedBorderColor = LuxuryBorderColor,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )
                        }
                    }

                    // ==================================================
                    // SECTION 8: AUTO CHECK (SAFETY & QUALITY ENGINE)
                    // ==================================================
                    SectionCard(
                        title = "AUTO CHECK & READABILITY INSPECTOR",
                        badgeText = "5 Checks",
                        icon = Icons.Default.FactCheck
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            AutoCheckRow(
                                title = "Text covers product",
                                isWarning = isTextCoveringProduct,
                                onToggle = { isTextCoveringProduct = !isTextCoveringProduct }
                            )
                            AutoCheckRow(
                                title = "Logo covers face",
                                isWarning = isLogoCoveringFace,
                                onToggle = { isLogoCoveringFace = !isLogoCoveringFace }
                            )
                            AutoCheckRow(
                                title = "Price hidden or low contrast",
                                isWarning = isPriceHidden,
                                onToggle = { isPriceHidden = !isPriceHidden }
                            )
                            AutoCheckRow(
                                title = "CTA button too small (<12sp)",
                                isWarning = isCtaTooSmall,
                                onToggle = { isCtaTooSmall = !isCtaTooSmall }
                            )
                            AutoCheckRow(
                                title = "Low background readability",
                                isWarning = isLowReadability,
                                onToggle = { isLowReadability = !isLowReadability }
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            val warningCount = listOf(
                                isTextCoveringProduct,
                                isLogoCoveringFace,
                                isPriceHidden,
                                isCtaTooSmall,
                                isLowReadability
                            ).count { it }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (warningCount == 0) EmeraldGreen.copy(alpha = 0.15f) else WarningRed.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, if (warningCount == 0) EmeraldGreen else WarningRed)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (warningCount == 0) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (warningCount == 0) BrightEmerald else WarningRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = if (warningCount == 0)
                                            "100% Clear — All 5 Safety Checks Passed!"
                                        else
                                            "$warningCount potential issue(s) detected. Adjust layout before applying.",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (warningCount == 0) BrightEmerald else WarningRed
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // ==================================================
            // BOTTOM USER APPROVAL & ACTION BAR
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = LuxuryCardBg
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, LuxuryBorderColor)
                    ) {
                        Text(text = "Discard", color = TextSecondary, fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                            // Compile generated items for video timeline
                            val generatedCaptions = listOf(
                                TextTrackItem(
                                    id = "txt_aff_title_${System.currentTimeMillis()}",
                                    text = detectedProduct.mainProduct,
                                    startSec = 0.5,
                                    durationSec = videoDurationSec.coerceAtLeast(4.0),
                                    styleName = "${selectedStylePack.label} Header",
                                    positionY = -180f
                                ),
                                TextTrackItem(
                                    id = "txt_aff_cta_${System.currentTimeMillis()}",
                                    text = customCtaInput.ifBlank { "Buy Now" },
                                    startSec = 1.0,
                                    durationSec = videoDurationSec.coerceAtLeast(5.0),
                                    styleName = "Glow CTA Button",
                                    positionY = 190f
                                )
                            )

                            val generatedStickers = mutableListOf(
                                StickerTrackItem(
                                    id = "stk_price_${System.currentTimeMillis()}",
                                    stickerEmoji = "🏷️",
                                    startSec = 0.5,
                                    durationSec = videoDurationSec,
                                    label = priceConfig.currentPrice,
                                    category = "Price",
                                    positionX = 110f,
                                    positionY = -130f
                                ),
                                StickerTrackItem(
                                    id = "stk_offer_${System.currentTimeMillis()}",
                                    stickerEmoji = "🔥",
                                    startSec = 1.0,
                                    durationSec = videoDurationSec,
                                    label = priceConfig.offer,
                                    category = "Offer",
                                    positionX = -110f,
                                    positionY = -130f
                                )
                            )

                            if (isLogoApproved) {
                                generatedStickers.add(
                                    StickerTrackItem(
                                        id = "stk_brand_${System.currentTimeMillis()}",
                                        stickerEmoji = selectedPlatform.logoEmoji,
                                        startSec = 0.0,
                                        durationSec = videoDurationSec,
                                        label = selectedPlatform.displayName,
                                        category = "Brand Logo",
                                        positionX = 120f,
                                        positionY = -210f
                                    )
                                )
                            }

                            onApplyToTimeline(generatedCaptions, generatedStickers)
                            Toast.makeText(context, "AI Creator enhancements applied to video!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Apply AI Enhancements",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================================================
// HELPER COMPONENTS
// ==================================================
@Composable
private fun SectionCard(
    title: String,
    badgeText: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(BorderStroke(1.dp, LuxuryBorderColor), RoundedCornerShape(20.dp)),
        color = LuxuryCardBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BrightPurple,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = title,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = PrimaryPurple.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, BrightPurple.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrightPurple,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            content()
        }
    }
}

@Composable
private fun MiniDetailPill(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        color = LuxuryItemBg,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrightEmerald,
                modifier = Modifier.size(15.dp)
            )
            Column {
                Text(
                    text = label,
                    fontSize = 9.5.sp,
                    color = TextSecondary
                )
                Text(
                    text = value,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun StylePackChip(
    pack: StylePack,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) pack.primaryColor.copy(alpha = 0.25f) else LuxuryItemBg,
        border = BorderStroke(
            1.dp,
            if (isSelected) pack.primaryColor else LuxuryBorderColor
        ),
        modifier = modifier.clickable { onSelect() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = pack.emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = pack.label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) TextWhite else TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MiniToggleOption(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = LuxuryItemBg,
        border = BorderStroke(1.dp, LuxuryBorderColor),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StickerStylePreviewCard(
    text: String,
    styleName: String,
    isSelected: Boolean,
    accentColor: Color,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) accentColor.copy(alpha = 0.2f) else LuxuryItemBg,
        border = BorderStroke(
            1.5.dp,
            if (isSelected) accentColor else LuxuryBorderColor
        ),
        modifier = modifier.clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = text,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) TextWhite else TextWhite.copy(alpha = 0.9f)
                )
                Text(
                    text = styleName,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun AutoCheckRow(
    title: String,
    isWarning: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (isWarning) Icons.Default.Warning else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isWarning) WarningRed else EmeraldGreen,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = TextWhite,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = if (isWarning) "Issue Found" else "Pass",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isWarning) WarningRed else EmeraldGreen
        )
    }
}
