package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.engine.AssetPack
import com.example.engine.AssetPackCategory
import com.example.engine.AssetPackManager
import com.example.engine.PackStatus
import java.util.Locale

// ============================================================================
// MASTER PHASE A-2 — DOWNLOADABLE ASSET PACK MARKETPLACE UI
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetPackMarketplaceSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val packManager = remember { AssetPackManager.getInstance(context) }

    val allPacks by packManager.allPacks.collectAsState()
    val packStatuses by packManager.packStatuses.collectAsState()
    val downloadTasks by packManager.downloadTasks.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("All") } // All, Installed, Not Installed, Trending, Popular, Free, Premium
    var selectedCategory by remember { mutableStateOf<AssetPackCategory?>(null) }
    var selectedPackForDetail by remember { mutableStateOf<AssetPack?>(null) }

    val filteredPacks by remember(searchQuery, selectedTab, selectedCategory, allPacks, packStatuses) {
        derivedStateOf {
            packManager.searchPacks(
                query = searchQuery,
                category = selectedCategory,
                filterTab = selectedTab
            )
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .shadow(24.dp),
        color = DarkPanelBg,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // TOP HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MintGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.DownloadForOffline, contentDescription = null, tint = MintPrimary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "CapCut Pro Asset Packs",
                            color = TextMainWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Downloadable Filters, FX, Transitions & Fonts",
                            color = TextMutedGray,
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CardSurfaceDark)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedGray, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(10.dp))

            // SEARCH BAR
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search packs (e.g. Cinematic, India, Glitch)...", color = TextMutedGray, fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = MintPrimary, modifier = Modifier.size(18.dp)) },
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
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            )

            Spacer(Modifier.height(8.dp))

            // FILTER TABS (All, Installed, Not Installed, Trending, Popular, Free, Premium)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val tabs = listOf("All", "Installed", "Not Installed", "Trending", "Popular", "Free", "Premium")
                items(tabs) { tab ->
                    val isSel = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSel) MintPrimary else CardSurfaceDark)
                            .border(BorderStroke(1.dp, if (isSel) MintPrimary else CardSurfaceBorder), RoundedCornerShape(14.dp))
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = tab,
                            color = if (isSel) PureBlackBg else TextMainWhite,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // CATEGORY SCROLL CHIPS
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    val isSel = selectedCategory == null
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) MintGlow else CardSurfaceDark)
                            .border(BorderStroke(1.dp, if (isSel) MintPrimary else CardSurfaceBorder), RoundedCornerShape(10.dp))
                            .clickable { selectedCategory = null }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("All Categories", color = if (isSel) MintPrimary else TextMutedGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    }
                }
                items(AssetPackCategory.values()) { cat ->
                    val isSel = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) MintGlow else CardSurfaceDark)
                            .border(BorderStroke(1.dp, if (isSel) MintPrimary else CardSurfaceBorder), RoundedCornerShape(10.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(cat.displayName, color = if (isSel) MintPrimary else TextMutedGray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // PACK CARDS LIST
            if (filteredPacks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No asset packs found matching criteria", color = TextMutedGray, fontSize = 12.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredPacks, key = { it.id }) { pack ->
                        val status = packStatuses[pack.id] ?: PackStatus.NOT_INSTALLED
                        val downloadTask = downloadTasks[pack.id]

                        AssetPackCardItem(
                            pack = pack,
                            status = status,
                            downloadTask = downloadTask,
                            onCardClick = { selectedPackForDetail = pack },
                            onInstall = { packManager.installPack(pack.id) },
                            onPause = { packManager.pauseInstall(pack.id) },
                            onResume = { packManager.resumeInstall(pack.id) },
                            onCancel = { packManager.cancelInstall(pack.id) },
                            onUninstall = { packManager.uninstallPack(pack.id) }
                        )
                    }
                }
            }
        }
    }

    // PACK DETAIL MODAL DIALOG
    selectedPackForDetail?.let { pack ->
        val status = packStatuses[pack.id] ?: PackStatus.NOT_INSTALLED
        val downloadTask = downloadTasks[pack.id]

        AlertDialog(
            onDismissRequest = { selectedPackForDetail = null },
            confirmButton = {
                Button(
                    onClick = {
                        if (status == PackStatus.NOT_INSTALLED) {
                            packManager.installPack(pack.id)
                        } else if (status == PackStatus.INSTALLED) {
                            packManager.uninstallPack(pack.id)
                        }
                        selectedPackForDetail = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (status == PackStatus.INSTALLED) Color(0xFFEF4444) else MintPrimary
                    )
                ) {
                    Text(
                        text = if (status == PackStatus.INSTALLED) "Uninstall Pack" else "Download Pack Now",
                        color = PureBlackBg,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPackForDetail = null }) {
                    Text("Close", color = TextMutedGray)
                }
            },
            containerColor = CardSurfaceDark,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(pack.name, color = TextMainWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    if (pack.isPremium) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GoldPro)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text("PRO", color = PureBlackBg, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(pack.coverImageUrl).crossfade(true).build(),
                        contentDescription = pack.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Text(pack.description, color = TextMutedGray, fontSize = 11.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Category: ${pack.category.displayName}", color = MintPrimary, fontSize = 10.sp)
                        Text("Assets: ${pack.assetCount} Items", color = TextMainWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Version: ${pack.version}", color = TextMutedGray, fontSize = 10.sp)
                        Text("Size: ${pack.packSizeBytes / (1024 * 1024)} MB", color = TextMutedGray, fontSize = 10.sp)
                    }
                }
            }
        )
    }
}

@Composable
private fun AssetPackCardItem(
    pack: AssetPack,
    status: PackStatus,
    downloadTask: com.example.engine.PackDownloadTask?,
    onCardClick: () -> Unit,
    onInstall: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit,
    onUninstall: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurfaceDark)
            .border(BorderStroke(1.dp, CardSurfaceBorder), RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // COVER IMAGE
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(pack.coverImageUrl).crossfade(true).build(),
                    contentDescription = pack.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if (pack.isPremium) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(GoldPro)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text("PRO", color = PureBlackBg, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.width(10.dp))

            // INFO
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = pack.name,
                        color = TextMainWhite,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                Text(
                    text = "${pack.category.displayName} • ${pack.assetCount} Assets • ${pack.packSizeBytes / (1024 * 1024)} MB",
                    color = MintPrimary,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = pack.description,
                    color = TextMutedGray,
                    fontSize = 8.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (status == PackStatus.DOWNLOADING && downloadTask != null) {
                    Spacer(Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { downloadTask.progress },
                        color = MintPrimary,
                        trackColor = CardSurfaceBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                    Text(
                        text = "${downloadTask.currentStep} (${(downloadTask.progress * 100).toInt()}%) • ${downloadTask.remainingTimeSec}s left",
                        color = MintPrimary,
                        fontSize = 8.sp
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            // ACTION BUTTON
            when (status) {
                PackStatus.NOT_INSTALLED -> {
                    IconButton(
                        onClick = onInstall,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MintGlow)
                    ) {
                        Icon(Icons.Outlined.Download, contentDescription = "Download", tint = MintPrimary, modifier = Modifier.size(18.dp))
                    }
                }
                PackStatus.DOWNLOADING -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        IconButton(
                            onClick = onPause,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(CardSurfaceBorder)
                        ) {
                            Icon(Icons.Default.Pause, contentDescription = "Pause", tint = TextMainWhite, modifier = Modifier.size(14.dp))
                        }
                        IconButton(
                            onClick = onCancel,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(CardSurfaceBorder)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color(0xFFEF4444), modifier = Modifier.size(14.dp))
                        }
                    }
                }
                PackStatus.PAUSED -> {
                    IconButton(
                        onClick = onResume,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MintGlow)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Resume", tint = MintPrimary, modifier = Modifier.size(18.dp))
                    }
                }
                PackStatus.INSTALLED -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MintPrimary.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintPrimary, modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(3.dp))
                            Text("Ready", color = MintPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                PackStatus.UPDATE_AVAILABLE -> {
                    Button(
                        onClick = onInstall,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPro),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text("Update", color = PureBlackBg, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
                else -> {
                    IconButton(onClick = onInstall) {
                        Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = MintPrimary)
                    }
                }
            }
        }
    }
}
