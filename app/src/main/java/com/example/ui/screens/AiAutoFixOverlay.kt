package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.example.ui.components.ViriAction
import com.example.ui.components.ViriMascotWidget
import kotlinx.coroutines.delay

// Colors specified in DS-22: Apple Human Interface, Cyan #22D3EE primary, Soft Glass, NO PURPLE
private val AutoFixDark = Color(0xFF090B10)
private val AutoFixSurface = Color(0xFF11141C)
private val AutoFixCard = Color(0xFF181C27)
private val CyanAccent = Color(0xFF22D3EE)
private val CyanGlow = Color(0x3322D3EE)
private val GlassBorder = Color(0x3322D3EE)
private val EmeraldGreen = Color(0xFF10B981)
private val AmberYellow = Color(0xFFF59E0B)
private val RoseRed = Color(0xFFF43F5E)
private val TextWhite = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF94A3B8)

data class FixCheckitem(
    val id: Int,
    val title: String,
    val gainText: String,
    val initialChecked: Boolean = true
)

data class ThumbnailOption(
    val id: Int,
    val label: String,
    val timeMillis: Long,
    val faceScore: Int,
    val productScore: Int,
    val ctrPrediction: String
)

@Composable
fun AiAutoFixOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onCompletePackageExport: (ProjectSetupConfig) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val videoUri = config?.selectedMedia?.firstOrNull()?.uri

    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableFloatStateOf(0f) }

    // Section 1: Fix Checklist State
    val fixItems = remember {
        mutableStateListOf(
            FixCheckitem(1, "Remove first 0.8 sec silent delay", "+4% Viral Gain"),
            FixCheckitem(2, "Increase overall video brightness +9%", "+3% Viral Gain"),
            FixCheckitem(3, "Reduce acoustic background noise & hiss", "+5% Viral Gain"),
            FixCheckitem(4, "Add smooth zoom at 4.2 sec key moment", "+7% Viral Gain"),
            FixCheckitem(5, "Enhance product contrast & edge clarity", "+6% Viral Gain"),
            FixCheckitem(6, "Increase vocal speech clarity & presence", "+4% Viral Gain"),
            FixCheckitem(7, "Insert bold high-converting CTA badge", "+8% Viral Gain"),
            FixCheckitem(8, "Set high-CTR AI optimized thumbnail frame", "+9% Viral Gain")
        )
    }

    // Section 2: Split Preview Slider State (0.0 to 1.0)
    var splitPosition by remember { mutableFloatStateOf(0.5f) }

    // Section 3: Thumbnail Lab Options
    val thumbnailOptions = remember {
        listOf(
            ThumbnailOption(1, "Best Frame #1", 3500L, 98, 94, "High CTR (+22%)"),
            ThumbnailOption(2, "Best Frame #2", 1800L, 95, 91, "High CTR (+18%)"),
            ThumbnailOption(3, "Best Frame #3", 5200L, 91, 88, "Good CTR (+14%)"),
            ThumbnailOption(4, "Best Frame #4", 7500L, 89, 82, "Safe CTR (+10%)")
        )
    }
    var selectedThumbId by remember { mutableIntStateOf(1) }

    // Section 4: Title Lab State
    var selectedTitleTab by remember { mutableIntStateOf(0) } // 0: Titles, 1: Hooks, 2: CTAs, 3: Hashtags, 4: Comments
    val titlesList = remember {
        listOf(
            "Unboxing The Ultimate Game-Changer 🔥",
            "Must-Have Trending Product Under ₹999!",
            "Why Everyone Is Obsessed With This Device...",
            "Stop Buying Cheap Alternatives! Try This Instead",
            "10x Your Productivity With This Simple Hack",
            "Unfiltered Honest Review Of The Viral Product",
            "5 Reasons You Need This In Your Daily Life",
            "Is It Worth The Hype? Real Test Results!",
            "The Best Budget Discovery Of The Year 🌟",
            "Top Selling Viral Reel Trend Explained"
        )
    }
    val hooksList = remember {
        listOf(
            "Stop scrolling right now if you want to save money!",
            "Don't buy anything else until you watch this 15s clip...",
            "This secret feature changed the game completely!",
            "90% of people use this wrong... here is the fix!",
            "I tested the viral product so you don't have to!",
            "The one thing nobody tells you about this item!",
            "Wait for the end to see the mind-blowing result...",
            "Here is why this sold out 3 times this month!",
            "If you love smart gadgets, look at this design!",
            "This ₹499 item feels like ₹5000 quality!"
        )
    }
    val ctasList = remember {
        listOf(
            "Comment 'LINK' below and I'll DM you the direct purchase link!",
            "Tap the link in bio before the special discount code expires!",
            "Save this reel for later and share with your shopping partner!",
            "Follow for daily honest product reviews and budget deals!",
            "Limited stock available! Order now from the bio store link."
        )
    }
    val hashtagsList = remember {
        listOf(
            "#ViralReels #TrendingProducts #MeeshoFinds #Unboxing #MustHave #SmartShopping #AmazonDeals",
            "#ReelKaroFeelKaro #ProductReview #TechGadgets #DailyHacks #ShopLocal #DiscountDeals",
            "#BestBuys #TrendingNow #ContentCreator #ExplorePage #InstaDeals #QualityFirst",
            "#BudgetShopping #DealAlert #TopPick #StyleInspiration #ViralGadgets",
            "#SmartLiving #HomeEssentials #GiftIdeas #UnboxingVideo #ReviewIndia"
        )
    }
    val commentsList = remember {
        listOf(
            "👇 Drop 'WANT' in comments for instant direct buy link in DM!",
            "Stock is running out extremely fast! Tap the link in bio right now 🔥",
            "Use promo code 'VIRAL10' at checkout for extra 10% instant discount!",
            "Let me know in comments if you want a detailed part 2 comparison video!",
            "100% genuine product with Free Cash On Delivery (COD) across India!"
        )
    }

    // Section 5: Caption AI State
    var selectedCaptionType by remember { mutableIntStateOf(0) } // 0: Short, 1: Long, 2: Meesho, 3: Amazon, 4: Instagram, 5: Facebook
    var selectedLanguage by remember { mutableIntStateOf(2) } // 0: Hindi, 1: English, 2: Hinglish

    val captionsMap = remember {
        mapOf(
            "Hinglish_Short" to "Yeh viral product try nahi kiya toh kya kiya! DM us or comment 'LINK' for direct store link 🌟 #Trending #MustHave",
            "Hinglish_Long" to "Honest review time! Maine yeh product order kiya tha and honestly results 10/10 hain! Build quality super premium hai and price is under budget. Comment 'LINK' below to get instant buy link in your inbox. Don't forget to save this reel! 🔥",
            "Hinglish_Meesho" to "Meesho Code: IND-88219 🔥 Under ₹499 Deals! Free Cash on Delivery available. Search code on Meesho app or comment 'LINK' for direct link!",
            "Hinglish_Amazon" to "Amazon Choice Product! ⭐ 4.8 Rating with 10k+ Reviews. Tap link in bio to get fast 1-day Prime delivery with discount!",
            "Hinglish_Instagram" to "Tag someone who needs this ASAP! 👇 Save this reel for later & check bio link for 20% OFF coupon code! ✨ #ReelsIndia",
            "Hinglish_Facebook" to "Share this with your friends and family! Top trending product review. Click bio link to order online today with COD option.",

            "Hindi_Short" to "यह वायरल प्रोडक्ट आपके काम को 10x आसान बना देगा! डायरेक्ट लिंक के लिए 'LINK' कमेंट करें। 🌟 #Trending",
            "Hindi_Long" to "ईमानदार रिव्यू! मैंने यह प्रोडक्ट ऑर्डर किया और इसका रिजल्ट बहुत ही शानदार है। क्वालिटी 10/10 है और बजट में फिट बैठता है। बाय लिंक के लिए 'LINK' कमेंट करें और रील सेव कर लें! 🔥",
            "Hindi_Meesho" to "मीशो कोड: IND-88219 🔥 फ्री कैश ऑन डिलीवरी उपलब्ध है। डायरेक्ट लिंक पाने के लिए कमेंट करें!",
            "Hindi_Amazon" to "अमेज़न बेस्ट सेलर प्रोडक्ट! 4.8 स्टार रेटिंग। 1-डे फ़ास्ट डिलीवरी के लिए बायो लिंक पर क्लिक करें!",
            "Hindi_Instagram" to "जिसे इस प्रोडक्ट की ज़रूरत है उसे टैग करें! 👇 रील सेव करें और बायो लिंक चेक करें! ✨",
            "Hindi_Facebook" to "अपने दोस्तों के साथ शेयर करें! सबसे ट्रेंडिंग प्रोडक्ट रिव्यू। ऑनलाइन ऑर्डर के लिए बायो लिंक देखें।",

            "English_Short" to "Transform your daily routine with this viral setup! Comment 'LINK' for instant DM purchase link 🌟 #MustHave",
            "English_Long" to "Full review alert! I tested this viral item and it definitely lives up to the hype. 10/10 build quality and incredible value for money. Drop 'LINK' in comments for direct store link and save this post! 🔥",
            "English_Meesho" to "Meesho Code: IND-88219 🔥 Exclusive deals under ₹499. Free COD + Easy returns available!",
            "English_Amazon" to "Amazon Choice Award Winner ⭐ 4.8 Stars with 10k+ customer reviews. Tap bio link for 1-Day Prime Delivery!",
            "English_Instagram" to "Tag a friend who needs this in their life right now! 👇 Tap link in bio for exclusive discount code! ✨ #Trending",
            "English_Facebook" to "Share with your network! Top rated product review. Click bio link to grab yours while stock lasts."
        )
    }

    // Section 8: Upload Confidence
    val uploadConfidence = 94
    val isSafeToUpload = uploadConfidence >= 80

    // Export Handler
    LaunchedEffect(isExporting) {
        if (isExporting) {
            for (p in 1..100) {
                exportProgress = p / 100f
                delay(12)
            }
            delay(100)

            // Copy package summary to clipboard
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val currentCaptionKey = when (selectedLanguage) {
                0 -> "Hindi_"
                1 -> "English_"
                else -> "Hinglish_"
            } + when (selectedCaptionType) {
                0 -> "Short"
                1 -> "Long"
                2 -> "Meesho"
                3 -> "Amazon"
                4 -> "Instagram"
                else -> "Facebook"
            }
            val packageText = """
                ✨ AI AUTO FIX CREATOR PACKAGE ✨
                ---------------------------------
                📌 Selected Title: ${titlesList.first()}
                📌 Selected Hook: ${hooksList.first()}
                📌 Selected CTA: ${ctasList.first()}
                📌 Caption: ${captionsMap[currentCaptionKey]}
                📌 Hashtags: ${hashtagsList.first()}
                📌 Pinned Comment: ${commentsList.first()}
                📌 Selected Thumbnail: ${thumbnailOptions.find { it.id == selectedThumbId }?.label ?: "Frame #1"}
                📌 Upload Confidence: $uploadConfidence% (Safe To Upload)
            """.trimIndent()

            val clip = ClipData.newPlainText("AI Package", packageText)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(context, "✨ AI Package Exported & Copied to Clipboard!", Toast.LENGTH_LONG).show()

            config?.let { onCompletePackageExport(it) }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(AutoFixDark),
            color = AutoFixDark
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                if (isExporting) {
                    ExportingPackageView(progress = exportProgress)
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = CyanGlow,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.AutoFixHigh,
                                            contentDescription = null,
                                            tint = CyanAccent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "✨ AI Auto Fix",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextWhite,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "One tap. Smarter reel.",
                                        fontSize = 11.5.sp,
                                        color = CyanAccent,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(AutoFixSurface)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = TextWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Scrollable Content with 9 Sections
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // ==================================================
                            // SECTION 1: ONE TAP FIX LIST
                            // ==================================================
                            AppleCardContainer(title = "1. ONE TAP FIX CHECKLIST") {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Select recommended auto-fixes to boost viral reach:",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )

                                    fixItems.forEachIndexed { idx, item ->
                                        var isChecked by remember { mutableStateOf(item.initialChecked) }
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    isChecked = !isChecked
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            color = AutoFixCard,
                                            border = BorderStroke(1.dp, if (isChecked) CyanAccent.copy(alpha = 0.4f) else Color.Transparent)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    modifier = Modifier.weight(1f),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    Checkbox(
                                                        checked = isChecked,
                                                        onCheckedChange = {
                                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                            isChecked = it
                                                        },
                                                        colors = CheckboxDefaults.colors(
                                                            checkedColor = CyanAccent,
                                                            uncheckedColor = TextSecondary,
                                                            checkmarkColor = Color.Black
                                                        )
                                                    )
                                                    Text(
                                                        text = item.title,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = TextWhite
                                                    )
                                                }

                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = EmeraldGreen.copy(alpha = 0.18f),
                                                    border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.5f))
                                                ) {
                                                    Text(
                                                        text = item.gainText,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = EmeraldGreen,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==================================================
                            // SECTION 2: PREVIEW DIFFERENCE (SPLIT SLIDER)
                            // ==================================================
                            AppleCardContainer(title = "2. PREVIEW DIFFERENCE") {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Original Frame", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                        Text("Slide to compare", fontSize = 10.sp, color = CyanAccent)
                                        Text("AI Enhanced Frame", fontSize = 11.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
                                    }

                                    // Interactive Split View Container
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .border(BorderStroke(1.dp, GlassBorder), RoundedCornerShape(16.dp))
                                            .background(Color.Black)
                                    ) {
                                        val density = LocalDensity.current

                                        // Base Right Layer: AI Enhanced Frame
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(videoUri)
                                                    .videoFrameMillis(2500L)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = "AI Preview",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            // Brightness / Contrast Glow Overlay
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(CyanGlow.copy(alpha = 0.08f))
                                            )
                                            Surface(
                                                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                color = CyanAccent
                                            ) {
                                                Text(
                                                    text = "AI Enhanced ✨",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        // Top Left Layer: Original Frame clipped by splitPosition
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth(splitPosition)
                                                .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                                        ) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(videoUri)
                                                    .videoFrameMillis(2500L)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = "Original Frame",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            Surface(
                                                modifier = Modifier.align(Alignment.TopStart).padding(8.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color.Black.copy(alpha = 0.7f)
                                            ) {
                                                Text(
                                                    text = "Original",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        // Vertical Split Slider Handle Line
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth()
                                                .pointerInput(Unit) {
                                                    detectTransformGestures { _, pan, _, _ ->
                                                        val newPos = (splitPosition + pan.x / size.width).coerceIn(0.05f, 0.95f)
                                                        splitPosition = newPos
                                                    }
                                                }
                                        ) {
                                            val xOffsetFraction = splitPosition
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(xOffsetFraction)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .width(3.dp)
                                                        .fillMaxHeight()
                                                        .background(CyanAccent)
                                                )
                                                Surface(
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .offset(x = 12.dp)
                                                        .size(28.dp),
                                                    shape = CircleShape,
                                                    color = CyanAccent,
                                                    shadowElevation = 6.dp
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(
                                                            imageVector = Icons.Default.TrendingUp,
                                                            contentDescription = null,
                                                            tint = Color.Black,
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==================================================
                            // SECTION 3: THUMBNAIL LAB
                            // ==================================================
                            AppleCardContainer(title = "3. THUMBNAIL LAB") {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("AI auto-extracted high-CTR frame choices:", fontSize = 12.sp, color = TextSecondary)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        thumbnailOptions.forEach { thumb ->
                                            val isSelected = thumb.id == selectedThumbId
                                            Surface(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .clickable {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        selectedThumbId = thumb.id
                                                    },
                                                shape = RoundedCornerShape(12.dp),
                                                color = AutoFixCard,
                                                border = BorderStroke(
                                                    1.5.dp,
                                                    if (isSelected) CyanAccent else Color.Transparent
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(4.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(70.dp)
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(Color.Black)
                                                    ) {
                                                        AsyncImage(
                                                            model = ImageRequest.Builder(context)
                                                                .data(videoUri)
                                                                .videoFrameMillis(thumb.timeMillis)
                                                                .crossfade(true)
                                                                .build(),
                                                            contentDescription = thumb.label,
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize()
                                                        )

                                                        if (isSelected) {
                                                            Surface(
                                                                modifier = Modifier.align(Alignment.TopEnd).padding(3.dp),
                                                                shape = CircleShape,
                                                                color = CyanAccent
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Check,
                                                                    contentDescription = null,
                                                                    tint = Color.Black,
                                                                    modifier = Modifier.size(12.dp).padding(2.dp)
                                                                )
                                                            }
                                                        }
                                                    }

                                                    Text(
                                                        text = thumb.label,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) CyanAccent else TextWhite
                                                    )
                                                    Text(
                                                        text = "Face: ${thumb.faceScore}%",
                                                        fontSize = 8.5.sp,
                                                        color = TextSecondary
                                                    )
                                                    Text(
                                                        text = "Product: ${thumb.productScore}%",
                                                        fontSize = 8.5.sp,
                                                        color = TextSecondary
                                                    )
                                                    Surface(
                                                        shape = RoundedCornerShape(4.dp),
                                                        color = EmeraldGreen.copy(alpha = 0.2f)
                                                    ) {
                                                        Text(
                                                            text = thumb.ctrPrediction,
                                                            fontSize = 7.5.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = EmeraldGreen,
                                                            modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==================================================
                            // SECTION 4: TITLE LAB
                            // ==================================================
                            AppleCardContainer(title = "4. TITLE & HOOK LAB") {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    // Category Tab Selector
                                    ScrollableTabRow(
                                        selectedTabIndex = selectedTitleTab,
                                        edgePadding = 0.dp,
                                        containerColor = Color.Transparent,
                                        contentColor = CyanAccent,
                                        divider = {}
                                    ) {
                                        listOf("10 Titles", "10 Hooks", "5 CTAs", "5 Hashtags", "5 Comments").forEachIndexed { idx, tabTitle ->
                                            Tab(
                                                selected = selectedTitleTab == idx,
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    selectedTitleTab = idx
                                                },
                                                text = {
                                                    Text(
                                                        text = tabTitle,
                                                        fontSize = 11.5.sp,
                                                        fontWeight = if (selectedTitleTab == idx) FontWeight.Bold else FontWeight.Medium,
                                                        color = if (selectedTitleTab == idx) CyanAccent else TextSecondary
                                                    )
                                                }
                                            )
                                        }
                                    }

                                    // Display list based on active tab
                                    val currentList = when (selectedTitleTab) {
                                        0 -> titlesList
                                        1 -> hooksList
                                        2 -> ctasList
                                        3 -> hashtagsList
                                        else -> commentsList
                                    }

                                    currentList.forEachIndexed { i, textItem ->
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp),
                                            color = AutoFixCard
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "${i + 1}. $textItem",
                                                    fontSize = 11.5.sp,
                                                    color = TextWhite,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                IconButton(
                                                    onClick = {
                                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                        clipboard.setPrimaryClip(ClipData.newPlainText("Title Item", textItem))
                                                        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ContentCopy,
                                                        contentDescription = "Copy",
                                                        tint = CyanAccent,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==================================================
                            // SECTION 5: CAPTION AI
                            // ==================================================
                            AppleCardContainer(title = "5. CAPTION AI GENERATOR") {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    // Language Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        listOf("Hindi", "English", "Hinglish").forEachIndexed { lIdx, langName ->
                                            val isSel = selectedLanguage == lIdx
                                            Surface(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .clickable {
                                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                        selectedLanguage = lIdx
                                                    },
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (isSel) CyanAccent else AutoFixCard
                                            ) {
                                                Text(
                                                    text = langName,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSel) Color.Black else TextWhite,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.padding(vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }

                                    // Platform Caption Type
                                    ScrollableTabRow(
                                        selectedTabIndex = selectedCaptionType,
                                        edgePadding = 0.dp,
                                        containerColor = Color.Transparent,
                                        contentColor = CyanAccent,
                                        divider = {}
                                    ) {
                                        listOf("Short", "Long", "Meesho", "Amazon", "Instagram", "Facebook").forEachIndexed { cIdx, typeName ->
                                            Tab(
                                                selected = selectedCaptionType == cIdx,
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    selectedCaptionType = cIdx
                                                },
                                                text = {
                                                    Text(
                                                        text = typeName,
                                                        fontSize = 10.5.sp,
                                                        fontWeight = if (selectedCaptionType == cIdx) FontWeight.Bold else FontWeight.Medium,
                                                        color = if (selectedCaptionType == cIdx) CyanAccent else TextSecondary
                                                    )
                                                }
                                            )
                                        }
                                    }

                                    // Caption Box Output
                                    val langPrefix = when (selectedLanguage) {
                                        0 -> "Hindi_"
                                        1 -> "English_"
                                        else -> "Hinglish_"
                                    }
                                    val typeKey = when (selectedCaptionType) {
                                        0 -> "Short"
                                        1 -> "Long"
                                        2 -> "Meesho"
                                        3 -> "Amazon"
                                        4 -> "Instagram"
                                        else -> "Facebook"
                                    }
                                    val activeCaption = captionsMap[langPrefix + typeKey] ?: captionsMap["Hinglish_Short"]!!

                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        color = AutoFixCard,
                                        border = BorderStroke(1.dp, GlassBorder)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = activeCaption,
                                                fontSize = 12.sp,
                                                color = TextWhite,
                                                lineHeight = 17.sp
                                            )

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                Button(
                                                    onClick = {
                                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                        clipboard.setPrimaryClip(ClipData.newPlainText("Caption", activeCaption))
                                                        Toast.makeText(context, "Caption Copied!", Toast.LENGTH_SHORT).show()
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Copy Caption", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==================================================
                            // SECTION 6: UPLOAD CHECKLIST
                            // ==================================================
                            AppleCardContainer(title = "6. PRE-UPLOAD AI VERIFICATION") {
                                val checks = remember {
                                    listOf(
                                        "Face Visible & Eyeline Clean",
                                        "Product Centered in Safe Area",
                                        "Price Tag / Value Sticker Ready",
                                        "Brand Watermark & Logo Clean",
                                        "High-Converting Caption Generated",
                                        "High-CTR Thumbnail #1 Selected",
                                        "Acoustic Noise Filter Applied",
                                        "First 3-Second Hook Approved",
                                        "Call To Action (CTA) Included"
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    checks.forEach { checkName ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = EmeraldGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = checkName,
                                                fontSize = 11.5.sp,
                                                color = TextWhite,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }

                            // ==================================================
                            // SECTION 7: BEST POSTING TIME
                            // ==================================================
                            AppleCardContainer(title = "7. BEST POSTING TIME PREDICTION") {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(Icons.Default.Schedule, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
                                            Column {
                                                Text("Peak Window Today", fontSize = 11.sp, color = TextSecondary)
                                                Text("Thursday • 7:30 PM IST", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
                                            }
                                        }

                                        Surface(
                                            shape = CircleShape,
                                            color = EmeraldGreen.copy(alpha = 0.2f),
                                            border = BorderStroke(1.dp, EmeraldGreen)
                                        ) {
                                            Text(
                                                text = "88% Active",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldGreen,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        PostingStatTile("Competition", "Low", EmeraldGreen, Modifier.weight(1f))
                                        PostingStatTile("Expected Reach", "15K - 45K", CyanAccent, Modifier.weight(1f))
                                        PostingStatTile("Best Slot", "7PM - 9:30PM", TextWhite, Modifier.weight(1f))
                                    }
                                }
                            }

                            // ==================================================
                            // SECTION 8: UPLOAD CONFIDENCE
                            // ==================================================
                            AppleCardContainer(title = "8. UPLOAD CONFIDENCE SCORE") {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "$uploadConfidence%",
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Black,
                                            color = CyanAccent
                                        )

                                        Column {
                                            Text(
                                                text = if (isSafeToUpload) "✅ Safe to Upload" else "⚠️ Improve Hook First",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (isSafeToUpload) EmeraldGreen else AmberYellow
                                            )
                                            Text(
                                                text = "All virality factors verified by AI Engine",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }
                                }
                            }

                            // ==================================================
                            // SECTION 9: FINAL EXPORT BUTTON
                            // ==================================================
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                color = AutoFixSurface,
                                border = BorderStroke(1.5.dp, CyanAccent)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        ViriMascotWidget(
                                            action = ViriAction.CELEBRATING,
                                            size = 64.dp
                                        )

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Ready to Export AI Creator Package",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = "Includes Thumbnail, Caption, Titles, Hashtags & Upload Checklist",
                                                fontSize = 10.5.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            isExporting = true
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = CyanAccent,
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                                            Text(
                                                text = "✨ Export AI Package",
                                                fontWeight = FontWeight.Black,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppleCardContainer(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AutoFixSurface,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CyanAccent,
                letterSpacing = 0.8.sp
            )
            content()
        }
    }
}

@Composable
private fun PostingStatTile(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = AutoFixCard
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(label, fontSize = 9.sp, color = TextSecondary)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun ExportingPackageView(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ViriMascotWidget(action = ViriAction.CELEBRATING, size = 96.dp)

            Text(
                text = "Exporting AI Creator Package...",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            Text(
                text = "Bundling Thumbnails, Titles, Captions & Upload Metadata",
                fontSize = 11.5.sp,
                color = TextSecondary
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = CyanAccent,
                trackColor = AutoFixSurface
            )

            Text(
                text = "${(progress * 100).toInt()}% Complete",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
        }
    }
}
