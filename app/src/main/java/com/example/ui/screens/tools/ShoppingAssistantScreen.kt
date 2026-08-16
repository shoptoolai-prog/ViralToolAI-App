package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val ScreenBg = Color(0xFF07090E)
private val GlassCardBg = Color(0xFF101522)
private val ElevatedCardBg = Color(0xFF161D2E)
private val BorderSubtle = Color(0xFF22D9E8).copy(alpha = 0.22f)
private val NeonCyan = Color(0xFF20D9E8)
private val NeonPurple = Color(0xFFA855F7)
private val GoldAccent = Color(0xFFFFB800)
private val EmeraldAccent = Color(0xFF10B981)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingAssistantScreen(
    onBackClick: () -> Unit,
    initialUrl: String? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    var inputUrl by remember { mutableStateOf(initialUrl ?: "") }
    var isAnalyzing by remember { mutableStateOf(false) }
    var scanProgress by remember { mutableStateOf(0f) }
    var scanStatusText by remember { mutableStateOf("Initializing AI Scanner...") }
    var shoppingResult by remember { mutableStateOf<ShoppingResult?>(null) }
    var isWishlisted by remember { mutableStateOf(false) }
    var selectedSpecTab by remember { mutableIntStateOf(0) }

    // Quick product examples
    val sampleProducts = remember {
        listOf(
            "Sony WH-1000XM5" to "https://www.amazon.in/Sony-WH-1000XM5-Wireless-Cancelling-Headphones/dp/B09XS7JWHH",
            "Apple MacBook Air M3" to "https://www.flipkart.com/apple-macbook-air-m3-8-gb-256-gb-ssd-macos-sonoma-mxd13hn-a/p/itm12345",
            "Samsung S24 Ultra" to "https://www.amazon.in/Samsung-Galaxy-Ultra-Titanium-Storage/dp/B0CS5X82LM",
            "Nike Air Jordan Retro" to "https://www.myntra.com/shoes/nike/nike-air-jordan-1-retro-high-og/26471822/buy",
            "Zara Oversized Blazer" to "https://www.ajio.com/zara-men-structured-blazer/p/465192837"
        )
    }

    fun analyzeLink(urlToAnalyze: String) {
        if (urlToAnalyze.isBlank()) {
            Toast.makeText(context, "Please enter or paste a valid product link", Toast.LENGTH_SHORT).show()
            return
        }
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        isAnalyzing = true
        scanProgress = 0.05f
        scanStatusText = "Connecting to AI Neural Shopping Engine..."

        coroutineScope.launch {
            delay(350)
            scanProgress = 0.25f
            scanStatusText = "Extracting product metadata & attributes..."
            delay(400)
            scanProgress = 0.55f
            scanStatusText = "Comparing 6+ live merchant marketplaces..."
            delay(450)
            scanProgress = 0.85f
            scanStatusText = "Evaluating deal score, warranty & price trends..."
            delay(350)
            scanProgress = 1f
            scanStatusText = "Finalizing AI verification report..."
            delay(200)

            val result = generateResultData(urlToAnalyze)
            shoppingResult = result
            isAnalyzing = false
        }
    }

    // Auto-analyze if initial URL was passed
    LaunchedEffect(initialUrl) {
        if (!initialUrl.isNullOrBlank()) {
            analyzeLink(initialUrl)
        }
    }

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "AI Shopping Assistant",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NeonCyan.copy(alpha = 0.15f),
                                border = BorderStroke(0.6.dp, NeonCyan.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "PRO AI",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NeonCyan,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Find. Compare. Buy Smarter.",
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onBackClick()
                        },
                        modifier = Modifier.testTag("btn_shopping_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (shoppingResult != null) {
                        IconButton(
                            onClick = {
                                isWishlisted = !isWishlisted
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                Toast.makeText(
                                    context,
                                    if (isWishlisted) "Added to Wishlist!" else "Removed from Wishlist",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Icon(
                                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (isWishlisted) Color(0xFFFF2A55) else Color.White.copy(alpha = 0.8f)
                            )
                        }
                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "🛍️ Check this AI Price Comparison for ${shoppingResult?.productName ?: "this product"}:\nBest Price: ₹${shoppingResult?.bestPrice?.toInt()}\nCompared across Flipkart, Amazon & Myntra!"
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Price Report"))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Report",
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBg
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 36.dp)
        ) {
            // ==========================================
            // 1. INPUT & SEARCH BAR CARD
            // ==========================================
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GlassCardBg,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Paste Product URL or Reel Link",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        OutlinedTextField(
                            value = inputUrl,
                            onValueChange = { inputUrl = it },
                            placeholder = {
                                Text(
                                    "https://amazon.in/... or Instagram Reel link",
                                    fontSize = 12.5.sp,
                                    color = Color.White.copy(alpha = 0.4f)
                                )
                            },
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (inputUrl.isNotEmpty()) {
                                        IconButton(onClick = { inputUrl = "" }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear",
                                                tint = Color.White.copy(alpha = 0.6f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                            val clip = clipboard?.primaryClip
                                            if (clip != null && clip.itemCount > 0) {
                                                val text = clip.getItemAt(0).text?.toString()
                                                if (!text.isNullOrBlank()) {
                                                    inputUrl = text
                                                    Toast.makeText(context, "Link Pasted!", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentPaste,
                                            contentDescription = "Paste",
                                            tint = NeonCyan,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = NeonCyan
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_shopping_url")
                        )

                        // Sample preset pills
                        Text(
                            text = "Quick Try Examples:",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Medium
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sampleProducts) { (title, sampleUrl) ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = ElevatedCardBg,
                                    border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.12f)),
                                    modifier = Modifier.clickable {
                                        inputUrl = sampleUrl
                                        analyzeLink(sampleUrl)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = NeonCyan,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = title,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        // Main action button
                        Button(
                            onClick = { analyzeLink(inputUrl) },
                            enabled = !isAnalyzing,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan,
                                disabledContainerColor = NeonCyan.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_analyze_shopping")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (isAnalyzing) {
                                    CircularProgressIndicator(
                                        color = Color.Black,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    text = if (isAnalyzing) "Scanning Marketplaces..." else "Analyze & Compare Deals",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 2. SCANNING / LOADING STATE
            // ==========================================
            if (isAnalyzing) {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = ElevatedCardBg,
                        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            val infiniteTransition = rememberInfiniteTransition(label = "scan_pulse")
                            val pulseAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.4f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "pulse"
                            )

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan.copy(alpha = 0.15f * pulseAlpha))
                                    .border(2.dp, NeonCyan.copy(alpha = pulseAlpha), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TravelExplore,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Text(
                                text = scanStatusText,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                            LinearProgressIndicator(
                                progress = { scanProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = NeonCyan,
                                trackColor = Color.White.copy(alpha = 0.1f)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                val stores = listOf("Amazon", "Flipkart", "Myntra", "Ajio", "Meesho")
                                stores.forEach { store ->
                                    Text(
                                        text = "✓ $store",
                                        fontSize = 11.sp,
                                        color = NeonCyan.copy(alpha = 0.8f),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 3. PRODUCT RESULT & COMPARISON
            // ==========================================
            val result = shoppingResult
            if (result != null && !isAnalyzing) {
                // Main Product Overview Card
                item {
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = GlassCardBg,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Product Image Banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                                    .background(Color(0xFF0B0F19))
                            ) {
                                AsyncImage(
                                    model = result.imageUrl,
                                    contentDescription = result.productName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color(0xFF101522))
                                            )
                                        )
                                )

                                // Merchant & Confidence Badges
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.Black.copy(alpha = 0.75f),
                                        border = BorderStroke(0.8.dp, NeonCyan.copy(alpha = 0.8f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = null,
                                                tint = NeonCyan,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text(
                                                text = "${result.detectionConfidence}% AI MATCH",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = NeonCyan
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(result.accentColor).copy(alpha = 0.85f)
                                    ) {
                                        Text(
                                            text = result.detectedStore.uppercase(),
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            // Product Title & Quick Stats
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = result.brand.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan,
                                    letterSpacing = 1.sp
                                )

                                Text(
                                    text = result.productName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = GoldAccent.copy(alpha = 0.15f),
                                        border = BorderStroke(0.6.dp, GoldAccent.copy(alpha = 0.4f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = GoldAccent,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text(
                                                text = "${result.rating}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GoldAccent
                                            )
                                        }
                                    }

                                    Text(
                                        text = "(${result.reviewsCount}+ verified reviews)",
                                        fontSize = 11.5.sp,
                                        color = Color.White.copy(alpha = 0.55f)
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = result.availability,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = EmeraldAccent
                                    )
                                }

                                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                                // Best Price Banner
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "BEST VERIFIED PRICE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White.copy(alpha = 0.5f),
                                            letterSpacing = 0.8.sp
                                        )
                                        Row(
                                            verticalAlignment = Alignment.Bottom,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "₹${result.bestPrice.toInt()}",
                                                fontSize = 26.sp,
                                                fontWeight = FontWeight.Black,
                                                color = NeonCyan
                                            )
                                            if (result.highestSellerPrice > result.bestPrice) {
                                                Text(
                                                    text = "₹${result.highestSellerPrice.toInt()}",
                                                    fontSize = 14.sp,
                                                    color = Color.White.copy(alpha = 0.4f),
                                                    textDecoration = TextDecoration.LineThrough,
                                                    modifier = Modifier.padding(bottom = 3.dp)
                                                )
                                            }
                                        }
                                    }

                                    if (result.savingsAmount > 0) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = EmeraldAccent.copy(alpha = 0.18f),
                                            border = BorderStroke(1.dp, EmeraldAccent.copy(alpha = 0.6f))
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "SAVE ₹${result.savingsAmount.toInt()}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = EmeraldAccent
                                                )
                                                Text(
                                                    text = "${result.savingsPercent}% lower",
                                                    fontSize = 9.5.sp,
                                                    color = EmeraldAccent.copy(alpha = 0.8f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // 4. PRICE COMPARISON MATRIX (All Stores)
                // ==========================================
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Live Store Comparison",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${result.priceComparison.size} Stores Compared",
                                fontSize = 11.5.sp,
                                color = NeonCyan
                            )
                        }

                        result.priceComparison.forEach { storeItem ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (storeItem.isBest) ElevatedCardBg else GlassCardBg,
                                border = BorderStroke(
                                    if (storeItem.isBest) 1.2.dp else 0.8.dp,
                                    if (storeItem.isBest) NeonCyan else Color.White.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Store Logo Char Avatar
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(storeItem.accentColor)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = storeItem.logoChar.toString(),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = storeItem.store,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            if (storeItem.isBest) {
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = NeonCyan.copy(alpha = 0.2f)
                                                ) {
                                                    Text(
                                                        text = "BEST DEAL",
                                                        fontSize = 8.5.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = NeonCyan,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "${storeItem.deliveryEstimate} • ${storeItem.returnPolicy}",
                                            fontSize = 10.5.sp,
                                            color = Color.White.copy(alpha = 0.55f)
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "₹${storeItem.price.toInt()}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (storeItem.isBest) NeonCyan else Color.White
                                        )
                                        Button(
                                            onClick = {
                                                try {
                                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(storeItem.url))
                                                    context.startActivity(browserIntent)
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Opening store: ${storeItem.store}", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (storeItem.isBest) NeonCyan else Color.White.copy(alpha = 0.15f)
                                            ),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                                            ) {
                                                Text(
                                                    text = "Buy",
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (storeItem.isBest) Color.Black else Color.White
                                                )
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                                    contentDescription = null,
                                                    tint = if (storeItem.isBest) Color.Black else Color.White,
                                                    modifier = Modifier.size(11.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // 5. AI BUYING INTELLIGENCE & DEAL SCORE
                // ==========================================
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = GlassCardBg,
                        border = BorderStroke(1.dp, NeonPurple.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = NeonPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "AI Buying Intelligence",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = NeonPurple.copy(alpha = 0.2f),
                                    border = BorderStroke(0.6.dp, NeonPurple.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "${result.dealScore}/100 DEAL SCORE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = NeonPurple,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Text(
                                text = result.aiRecommendation,
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )

                            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                            // Pros & Cons
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "VERIFIED ADVANTAGES:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldAccent,
                                    letterSpacing = 0.8.sp
                                )
                                result.pros.take(3).forEach { pro ->
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text("✓", color = EmeraldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(pro, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
                                    }
                                }
                            }

                            if (result.cons.isNotEmpty()) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "THINGS TO CONSIDER:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldAccent,
                                        letterSpacing = 0.8.sp
                                    )
                                    result.cons.take(2).forEach { con ->
                                        Row(
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text("•", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(con, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // 6. COUPONS & BANK OFFERS
                // ==========================================
                if (result.coupons.isNotEmpty()) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Exclusive Coupons & Bank Offers",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            result.coupons.forEach { coupon ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = ElevatedCardBg,
                                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.35f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = GoldAccent.copy(alpha = 0.15f),
                                                    border = BorderStroke(0.6.dp, GoldAccent)
                                                ) {
                                                    Text(
                                                        text = coupon.code,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = GoldAccent,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                                Text(
                                                    text = coupon.discountAmountText,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = EmeraldAccent
                                                )
                                            }
                                            Text(
                                                text = coupon.description,
                                                fontSize = 11.sp,
                                                color = Color.White.copy(alpha = 0.6f),
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                                val clip = ClipData.newPlainText("Coupon Code", coupon.code)
                                                clipboard?.setPrimaryClip(clip)
                                                Toast.makeText(context, "Copied code: ${coupon.code}", Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy Coupon",
                                                tint = GoldAccent,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // 7. SPECIFICATIONS TABLE
                // ==========================================
                if (result.specifications.isNotEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = GlassCardBg,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Technical Specifications",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                result.specifications.forEachIndexed { index, spec ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = spec.title,
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.55f),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = spec.value,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White,
                                            modifier = Modifier.weight(1.5f),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                    if (index < result.specifications.size - 1) {
                                        HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
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
