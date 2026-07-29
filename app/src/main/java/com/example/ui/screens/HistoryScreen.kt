package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShoppingItem
import com.example.data.WishlistStorageManager
import com.example.ui.components.GlassCard
import com.example.ui.components.materialSharedBounds
import com.example.ui.theme.*

@Composable
fun HistoryScreen(
    historyList: List<ShoppingItem>,
    onClearHistory: () -> Unit,
    onDeleteHistoryItem: (String) -> Unit = {},
    onNavigateToHome: () -> Unit,
    onReopenReport: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: History, 1: Wishlist
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showClearWishlistDialog by remember { mutableStateOf(false) }
    val wishlistItems = WishlistStorageManager.getWishlistItems()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header with title and clear button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = if (selectedTab == 0) "Analysis History" else "Saved Wishlist",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = com.example.core.TaglineEngine.getTagline(com.example.core.AppModule.HISTORY),
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                if (selectedTab == 0 && historyList.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearHistoryDialog = true },
                        modifier = Modifier
                            .background(Color(0x1F2C2C2C), RoundedCornerShape(10.dp))
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear History",
                            tint = CrimsonRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else if (selectedTab == 1 && wishlistItems.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearWishlistDialog = true },
                        modifier = Modifier
                            .background(Color(0x1F2C2C2C), RoundedCornerShape(10.dp))
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear Wishlist",
                            tint = CrimsonRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Tab Selector (History vs Saved Wishlist)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x12FFFFFF))
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Tab 0: History Log
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedTab == 0) EmeraldPrimary else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.History, contentDescription = null, tint = if (selectedTab == 0) AmoledBlack else TextWhite, modifier = Modifier.size(16.dp))
                            Text(
                                text = "History (${historyList.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 0) AmoledBlack else TextWhite
                            )
                        }
                    }

                    // Tab 1: Wishlist
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedTab == 1) EmeraldPrimary else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = if (selectedTab == 1) AmoledBlack else TextWhite, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Wishlist (${wishlistItems.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) AmoledBlack else TextWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTab == 0) {
                // TAB 0: HISTORY LIST
                if (historyList.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            borderColor = GlassCardBorder,
                            backgroundColor = GlassCardBg
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp, horizontal = 16.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .background(
                                                Brush.radialGradient(listOf(EmeraldPrimary.copy(alpha = 0.35f), Color.Transparent)),
                                                CircleShape
                                            )
                                    )
                                    Icon(Icons.Default.History, contentDescription = null, tint = EmeraldGlow, modifier = Modifier.size(48.dp))
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                Text("No Scanned Products Yet", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Paste any product link to unlock deep price comparisons and deal intelligence.",
                                    fontSize = 12.sp, color = TextGray, textAlign = TextAlign.Center, lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(
                                    onClick = onNavigateToHome,
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = AmoledBlack, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Launch Product Analyzer", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(historyList, key = { it.id }) { item ->
                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .materialSharedBounds("history_item_${item.id}")
                                    .clickable { onReopenReport(item.url) },
                                borderColor = GlassCardBorder,
                                backgroundColor = GlassCardBg
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(Brush.horizontalGradient(listOf(CrimsonRed, Color(0xFF8B0000))), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text((item.merchant ?: item.platform).uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF2ECC71).copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("SCORE ${item.dealScore ?: 92}/100", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                                        }

                                        if (!item.aiRecommendation.isNullOrEmpty()) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .background(CrimsonRed.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(item.aiRecommendation.uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Black, color = CrimsonLight)
                                            }
                                        }

                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(listOfNotNull(item.date, item.time).joinToString(" ").ifEmpty { item.timestamp }, fontSize = 10.sp, color = TextGray)

                                        Spacer(modifier = Modifier.width(6.dp))
                                        IconButton(
                                            onClick = { onDeleteHistoryItem(item.id) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextGray, modifier = Modifier.size(16.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        if (!item.thumbnailUrl.isNullOrEmpty()) {
                                            coil.compose.SubcomposeAsyncImage(
                                                model = item.thumbnailUrl,
                                                contentDescription = "Thumbnail",
                                                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(8.dp)).background(Color(0x1AFFFFFF)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            if (!item.brand.isNullOrEmpty()) {
                                                Text(item.brand.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, color = CrimsonLight, letterSpacing = 1.sp)
                                            }
                                            Text(item.productName ?: item.url, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                            if (item.price != null && item.price > 0) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text("₹${String.format("%,.0f", item.price)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Text("ID: ${item.id.take(8).uppercase()}", fontSize = 10.sp, color = TextGray)
                                        Spacer(modifier = Modifier.weight(1f))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable { onReopenReport(item.url) }
                                        ) {
                                            Text("Reopen Report", fontSize = 11.sp, color = CrimsonLight, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.Analytics, contentDescription = "Reopen", tint = CrimsonLight, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // TAB 1: SAVED WISHLIST
                if (wishlistItems.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            borderColor = GlassCardBorder,
                            backgroundColor = GlassCardBg
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp, horizontal = 16.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(90.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .background(
                                                Brush.radialGradient(listOf(EmeraldPrimary.copy(alpha = 0.35f), Color.Transparent)),
                                                CircleShape
                                            )
                                    )
                                    Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = EmeraldGlow, modifier = Modifier.size(48.dp))
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Your Wishlist is Empty", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Tap the Heart / Wishlist button on any product result card to save items locally.", fontSize = 12.sp, color = TextGray, textAlign = TextAlign.Center)
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(wishlistItems, key = { it.id }) { wishItem ->
                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                borderColor = GlassCardBorder,
                                backgroundColor = GlassCardBg
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF2874F0).copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(wishItem.merchant.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF2874F0))
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF2ECC71).copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("SCORE ${wishItem.dealScore}/100", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                                        }

                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(wishItem.dateSaved, fontSize = 10.sp, color = TextGray)

                                        Spacer(modifier = Modifier.width(8.dp))
                                        IconButton(
                                            onClick = { WishlistStorageManager.removeItem(wishItem.id) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextGray, modifier = Modifier.size(16.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        if (!wishItem.thumbnailUrl.isNullOrEmpty()) {
                                            coil.compose.SubcomposeAsyncImage(
                                                model = wishItem.thumbnailUrl,
                                                contentDescription = "Thumbnail",
                                                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(8.dp)).background(Color(0x1AFFFFFF)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(wishItem.productName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("₹${String.format("%,.0f", wishItem.price)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Button(
                                            onClick = {
                                                try {
                                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(wishItem.url))
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {}
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = TextWhite),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Quick Open", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // CONFIRMATION DIALOG: CLEAR HISTORY
        if (showClearHistoryDialog) {
            AlertDialog(
                onDismissRequest = { showClearHistoryDialog = false },
                containerColor = Color(0xFF101018),
                shape = RoundedCornerShape(24.dp),
                title = { Text("Clear All History?", color = TextWhite, fontWeight = FontWeight.Black) },
                text = { Text("Are you sure you want to delete all locally stored shopping search history? This action cannot be undone.", color = TextGray) },
                confirmButton = {
                    Button(
                        onClick = {
                            onClearHistory()
                            showClearHistoryDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Clear All", fontWeight = FontWeight.Bold, color = AmoledBlack)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearHistoryDialog = false }) {
                        Text("Cancel", color = TextGray)
                    }
                }
            )
        }

        // CONFIRMATION DIALOG: CLEAR WISHLIST
        if (showClearWishlistDialog) {
            AlertDialog(
                onDismissRequest = { showClearWishlistDialog = false },
                containerColor = Color(0xFF101018),
                shape = RoundedCornerShape(24.dp),
                title = { Text("Clear Wishlist?", color = TextWhite, fontWeight = FontWeight.Black) },
                text = { Text("Are you sure you want to clear all saved wishlist items?", color = TextGray) },
                confirmButton = {
                    Button(
                        onClick = {
                            WishlistStorageManager.clearWishlist()
                            showClearWishlistDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Clear Wishlist", fontWeight = FontWeight.Bold, color = AmoledBlack)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearWishlistDialog = false }) {
                        Text("Cancel", color = TextGray)
                    }
                }
            )
        }
    }
}
