package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.MintGlow
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintDark
import com.example.ui.theme.MintSurface
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

data class MediaPickerItem(
    val id: String,
    val uri: Uri?,
    val title: String,
    val durationFormatted: String,
    val durationSeconds: Long,
    val isVideo: Boolean,
    val albumName: String,
    val mimeType: String,
    val dateAddedSeconds: Long = 0,
    val resolutionLabel: String = "1080p",
    val width: Int = 1920,
    val height: Int = 1080,
    val fileSizeFormatted: String = "12 MB",
    val fileSizeBytes: Long = 0L,
    val frameRateLabel: String = "30 FPS"
)

enum class MediaSortOrder {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LARGEST_SIZE,
    SMALLEST_SIZE,
    LONGEST_DURATION,
    NAME_ALPHABETICAL
}

/**
 * VIRALTOOLAI MASTER PHASE 1 — FLAGSHIP MEDIA PICKER & GALLERY
 * CapCut-level workflow & Apple Human Interface polish.
 * Deep Black (#050507) background, Mint Green accents, 4-column grid, MediaStore & SAF support.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPickerScreen(
    onClose: () -> Unit,
    onNext: (List<MediaPickerItem>) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    // Permissions to request depending on Android API level
    val permissionsToRequest = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    // Storage Access Framework SAF launcher for browsing system files
    val systemGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val importedItems = uris.mapIndexed { idx, uri ->
                MediaPickerItem(
                    id = "sys_${idx}_${System.currentTimeMillis()}",
                    uri = uri,
                    title = "Storage Item #${idx + 1}",
                    durationFormatted = "0:30",
                    durationSeconds = 30,
                    isVideo = true,
                    albumName = "Downloads & Storage",
                    mimeType = "video/*",
                    dateAddedSeconds = System.currentTimeMillis() / 1000,
                    resolutionLabel = "1080p",
                    width = 1920,
                    height = 1080,
                    fileSizeFormatted = "Storage File",
                    fileSizeBytes = 15000000L,
                    frameRateLabel = "30 FPS"
                )
            }
            onNext(importedItems)
        }
    }

    // Permission State
    var hasPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    var permissionRequestedOnce by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionRequestedOnce = true
        val granted = permissions.values.any { it }
        hasPermission = granted
    }

    // FIRST OPEN AUTOMATIC NATIVE PERMISSION TRIGGER
    LaunchedEffect(Unit) {
        if (!hasPermission && !permissionRequestedOnce) {
            permissionRequestedOnce = true
            permissionLauncher.launch(permissionsToRequest)
        }
    }

    // Device MediaStore items
    var deviceMediaItems by remember { mutableStateOf<List<MediaPickerItem>>(emptyList()) }
    var isLoadingMediaStore by remember { mutableStateOf(false) }

    // Scan real MediaStore as soon as permission is granted
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            isLoadingMediaStore = true
            withContext(Dispatchers.IO) {
                val videos = queryMediaStoreVideos(context)
                val images = queryMediaStoreImages(context)
                val combined = (videos + images).sortedByDescending { it.dateAddedSeconds }
                withContext(Dispatchers.Main) {
                    deviceMediaItems = combined
                    isLoadingMediaStore = false
                }
            }
        }
    }

    // Filter, Album and Sort State
    var selectedTab by remember { mutableStateOf("All") } // "All", "Videos", "Photos", "Downloads"
    var selectedAlbum by remember { mutableStateOf("All Albums") }
    var sortOrder by remember { mutableStateOf(MediaSortOrder.NEWEST_FIRST) }
    var showAlbumDropdown by remember { mutableStateOf(false) }
    var showSortDropdown by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Preview Dialog State
    var previewItem by remember { mutableStateOf<MediaPickerItem?>(null) }

    // Multi-select list
    var selectedItems by remember { mutableStateOf<List<MediaPickerItem>>(emptyList()) }

    // Albums mapping with counts
    val albumCountsMap = remember(deviceMediaItems) {
        val counts = mutableMapOf<String, Int>()
        deviceMediaItems.forEach { item ->
            val album = if (item.albumName.isBlank()) "Camera" else item.albumName
            counts[album] = (counts[album] ?: 0) + 1
        }
        counts
    }

    val availableAlbums = remember(albumCountsMap) {
        listOf("All Albums") + albumCountsMap.keys.sorted()
    }

    // Filter and Sort Logic
    val filteredItems = remember(deviceMediaItems, selectedTab, selectedAlbum, sortOrder, searchQuery) {
        val filtered = deviceMediaItems.filter { item ->
            val matchesTab = when (selectedTab) {
                "Videos" -> item.isVideo
                "Photos" -> !item.isVideo
                "Downloads" -> item.albumName.lowercase().contains("download") || item.title.lowercase().contains("download")
                else -> true
            }
            val matchesAlbum = if (selectedAlbum == "All Albums") true else item.albumName.equals(selectedAlbum, ignoreCase = true)
            val matchesSearch = if (searchQuery.isBlank()) true else item.title.contains(searchQuery, ignoreCase = true) || item.albumName.contains(searchQuery, ignoreCase = true)
            matchesTab && matchesAlbum && matchesSearch
        }

        when (sortOrder) {
            MediaSortOrder.NEWEST_FIRST -> filtered.sortedByDescending { it.dateAddedSeconds }
            MediaSortOrder.OLDEST_FIRST -> filtered.sortedBy { it.dateAddedSeconds }
            MediaSortOrder.LARGEST_SIZE -> filtered.sortedByDescending { it.fileSizeBytes }
            MediaSortOrder.SMALLEST_SIZE -> filtered.sortedBy { it.fileSizeBytes }
            MediaSortOrder.LONGEST_DURATION -> filtered.sortedByDescending { it.durationSeconds }
            MediaSortOrder.NAME_ALPHABETICAL -> filtered.sortedBy { it.title.lowercase() }
        }
    }

    // Selection Summary calculations
    val totalDurationSeconds = remember(selectedItems) {
        selectedItems.sumOf { it.durationSeconds }
    }

    val totalDurationFormatted = remember(totalDurationSeconds) {
        val mins = totalDurationSeconds / 60
        val secs = totalDurationSeconds % 60
        if (mins > 0) "${mins}m ${secs}s" else "${secs}s"
    }

    val totalSizeBytes = remember(selectedItems) {
        selectedItems.sumOf { it.fileSizeBytes }
    }

    val totalSizeFormatted = remember(totalSizeBytes) {
        val sizeMb = totalSizeBytes / (1024f * 1024f)
        if (sizeMb > 1024f) String.format(Locale.US, "%.1f GB", sizeMb / 1024f)
        else String.format(Locale.US, "%.1f MB", sizeMb)
    }

    // Check if permanently denied ("Don't ask again")
    val isPermanentlyDenied = remember(hasPermission, permissionRequestedOnce) {
        permissionRequestedOnce && !hasPermission && activity != null && permissionsToRequest.all { perm ->
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ==================================================
            // FLAGSHIP TOP HEADER BAR
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF090B10),
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Close Button (Glassmorphism circle)
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onClose()
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x1AFFFFFF))
                                .border(BorderStroke(0.5.dp, Color(0x30FFFFFF)), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Album Selector Dropdown Button
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0x1AFFFFFF))
                                    .border(BorderStroke(1.dp, MintPrimary.copy(alpha = 0.5f)), RoundedCornerShape(18.dp))
                                    .clickable { showAlbumDropdown = true }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = MintGlow,
                                    modifier = Modifier.size(15.dp)
                                )
                                val count = if (selectedAlbum == "All Albums") deviceMediaItems.size else (albumCountsMap[selectedAlbum] ?: 0)
                                Text(
                                    text = if (count > 0) "$selectedAlbum ($count)" else selectedAlbum,
                                    color = TextWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = TextGray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showAlbumDropdown,
                                onDismissRequest = { showAlbumDropdown = false },
                                modifier = Modifier
                                    .background(Color(0xFF10131D))
                                    .border(BorderStroke(1.dp, MintPrimary.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                            ) {
                                availableAlbums.forEach { album ->
                                    val count = if (album == "All Albums") deviceMediaItems.size else (albumCountsMap[album] ?: 0)
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = if (album == selectedAlbum) Icons.Default.CheckCircle else Icons.Default.Folder,
                                                        contentDescription = null,
                                                        tint = if (album == selectedAlbum) MintGlow else TextGray,
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                    Text(
                                                        text = album,
                                                        color = if (album == selectedAlbum) MintGlow else TextWhite,
                                                        fontSize = 12.sp,
                                                        fontWeight = if (album == selectedAlbum) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                }
                                                Text(
                                                    text = "$count",
                                                    color = TextGray,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        },
                                        onClick = {
                                            selectedAlbum = album
                                            showAlbumDropdown = false
                                        }
                                    )
                                }
                                HorizontalDivider(color = Color(0xFF222636))
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.FolderZip,
                                                contentDescription = null,
                                                tint = MintGlow,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Text(
                                                text = "Browse Storage (SAF)...",
                                                color = MintGlow,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    },
                                    onClick = {
                                        showAlbumDropdown = false
                                        systemGalleryLauncher.launch("video/*")
                                    }
                                )
                            }
                        }

                        // Search Toggle & Sort Dropdown Buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Sort Dropdown Button
                            Box {
                                IconButton(
                                    onClick = { showSortDropdown = true },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x1AFFFFFF))
                                        .border(BorderStroke(0.5.dp, Color(0x30FFFFFF)), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Sort,
                                        contentDescription = "Sort",
                                        tint = TextWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showSortDropdown,
                                    onDismissRequest = { showSortDropdown = false },
                                    modifier = Modifier
                                        .background(Color(0xFF10131D))
                                        .border(BorderStroke(1.dp, MintPrimary.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Newest First", color = if (sortOrder == MediaSortOrder.NEWEST_FIRST) MintGlow else TextWhite, fontSize = 12.sp) },
                                        onClick = { sortOrder = MediaSortOrder.NEWEST_FIRST; showSortDropdown = false }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Oldest First", color = if (sortOrder == MediaSortOrder.OLDEST_FIRST) MintGlow else TextWhite, fontSize = 12.sp) },
                                        onClick = { sortOrder = MediaSortOrder.OLDEST_FIRST; showSortDropdown = false }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Largest Size", color = if (sortOrder == MediaSortOrder.LARGEST_SIZE) MintGlow else TextWhite, fontSize = 12.sp) },
                                        onClick = { sortOrder = MediaSortOrder.LARGEST_SIZE; showSortDropdown = false }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Smallest Size", color = if (sortOrder == MediaSortOrder.SMALLEST_SIZE) MintGlow else TextWhite, fontSize = 12.sp) },
                                        onClick = { sortOrder = MediaSortOrder.SMALLEST_SIZE; showSortDropdown = false }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Longest Duration", color = if (sortOrder == MediaSortOrder.LONGEST_DURATION) MintGlow else TextWhite, fontSize = 12.sp) },
                                        onClick = { sortOrder = MediaSortOrder.LONGEST_DURATION; showSortDropdown = false }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Name (A-Z)", color = if (sortOrder == MediaSortOrder.NAME_ALPHABETICAL) MintGlow else TextWhite, fontSize = 12.sp) },
                                        onClick = { sortOrder = MediaSortOrder.NAME_ALPHABETICAL; showSortDropdown = false }
                                    )
                                }
                            }

                            // Search Button
                            IconButton(
                                onClick = {
                                    isSearchActive = !isSearchActive
                                    if (!isSearchActive) searchQuery = ""
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSearchActive) MintPrimary.copy(alpha = 0.3f) else Color(0x1AFFFFFF))
                                    .border(BorderStroke(0.5.dp, if (isSearchActive) MintGlow else Color(0x30FFFFFF)), CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = if (isSearchActive) MintGlow else TextWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    // Search Input Field
                    AnimatedVisibility(
                        visible = isSearchActive,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search by filename or album...", color = TextGray, fontSize = 12.sp) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MintGlow,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = null, tint = TextGray, modifier = Modifier.size(15.dp))
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF10131F),
                                unfocusedContainerColor = Color(0xFF10131F),
                                focusedBorderColor = MintPrimary,
                                unfocusedBorderColor = Color(0xFF222638),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Compact Category Tabs (All, Videos, Photos, Downloads)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F111A))
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("All", "Videos", "Photos", "Downloads").forEach { tab ->
                            val isTabSelected = selectedTab == tab
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .then(
                                        if (isTabSelected) Modifier.background(MintPrimary)
                                        else Modifier.background(Color.Transparent)
                                    )
                                    .clickable {
                                        selectedTab = tab
                                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                                    }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = when (tab) {
                                            "Videos" -> Icons.Default.Videocam
                                            "Photos" -> Icons.Default.Image
                                            "Downloads" -> Icons.Default.Download
                                            else -> Icons.Default.GridOn
                                        },
                                        contentDescription = null,
                                        tint = if (isTabSelected) TextWhite else TextGray,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = tab,
                                        color = if (isTabSelected) TextWhite else TextGray,
                                        fontSize = 11.sp,
                                        fontWeight = if (isTabSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sub-header Info Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF07080D))
                    .padding(horizontal = 14.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isLoadingMediaStore) "Scanning device media..." else "${filteredItems.size} items available",
                    color = TextGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Pick from Storage",
                    color = MintGlow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { systemGalleryLauncher.launch("*/*") }
                )
            }

            // ==================================================
            // MAIN CONTENT AREA: PERMISSION / LOADING / MEDIA GRID
            // ==================================================
            if (!hasPermission) {
                // ELEGANT PERMISSION EXPLANATION CARD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x1AFFFFFF),
                        border = BorderStroke(1.dp, Color(0x20FFFFFF))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(MintSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    tint = MintGlow,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Text(
                                text = "Media Permission Required",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Grant access to videos and photos on your device so ViralToolAi can import and process media.",
                                color = TextGray,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (isPermanentlyDenied) {
                                    Button(
                                        onClick = {
                                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data = Uri.fromParts("package", context.packageName, null)
                                            }
                                            context.startActivity(intent)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Open Settings", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            permissionLauncher.launch(permissionsToRequest)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Grant Permission", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                OutlinedButton(
                                    onClick = { onClose() },
                                    border = BorderStroke(1.dp, Color(0xFF282B3E)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Not Now", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                            }

                            TextButton(
                                onClick = { systemGalleryLauncher.launch("*/*") }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.FolderOpen, contentDescription = null, tint = MintGlow, modifier = Modifier.size(15.dp))
                                    Text("Browse Storage Files (SAF)", color = MintGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else if (isLoadingMediaStore) {
                // Scanning Progress Indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(color = MintGlow, modifier = Modifier.size(32.dp))
                        Text("Loading device videos and photos...", color = TextGray, fontSize = 12.sp)
                    }
                }
            } else if (filteredItems.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF141722)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = null,
                                tint = TextGray,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Text(
                            text = "No media found in gallery",
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = if (searchQuery.isNotBlank()) "No files match '$searchQuery'."
                            else "Use Storage Access Framework to import videos or photos directly from your file manager.",
                            color = TextGray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = { systemGalleryLauncher.launch("*/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.FolderZip, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick from Storage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // 4-COLUMN ADAPTIVE MEDIA GRID
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        val selectedIndex = selectedItems.indexOfFirst { it.id == item.id }
                        val isSelected = selectedIndex >= 0

                        MediaGridCard(
                            item = item,
                            isSelected = isSelected,
                            selectionNumber = if (isSelected) selectedIndex + 1 else null,
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                selectedItems = if (isSelected) {
                                    selectedItems.filterNot { it.id == item.id }
                                } else {
                                    selectedItems + item
                                }
                            },
                            onLongClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                previewItem = item
                            }
                        )
                    }
                }
            }

            // ==================================================
            // BOTTOM FLOATING SELECTION BAR
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0B0D14),
                shadowElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Selected Count & Estimated Storage Summary
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedItems.isNotEmpty()) MintPrimary else Color(0xFF222638)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${selectedItems.size}",
                                    color = TextWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Text(
                                text = if (selectedItems.isEmpty()) "No media selected" else "${selectedItems.size} Selected",
                                color = TextWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (selectedItems.isNotEmpty()) {
                            Text(
                                text = "Duration: $totalDurationFormatted • Size: ~$totalSizeFormatted",
                                color = MintGlow,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Text(
                                text = "Tap items to select • Long press to preview",
                                color = TextGray,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Floating Glassmorphism Add Button
                    Button(
                        onClick = {
                            if (selectedItems.isNotEmpty()) {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onNext(selectedItems)
                            } else {
                                Toast.makeText(context, "Select at least 1 media item to proceed", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = selectedItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintPrimary,
                            disabledContainerColor = Color(0xFF1B1E2B)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (selectedItems.isNotEmpty()) "Add (${selectedItems.size})" else "Import",
                                color = if (selectedItems.isNotEmpty()) TextWhite else TextGray,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = if (selectedItems.isNotEmpty()) TextWhite else TextGray,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }

        // Long Press Media Preview Modal Dialog
        previewItem?.let { item ->
            MediaPreviewModal(
                item = item,
                isSelected = selectedItems.any { it.id == item.id },
                onToggleSelect = {
                    val exists = selectedItems.any { it.id == item.id }
                    selectedItems = if (exists) selectedItems.filterNot { it.id == item.id }
                    else selectedItems + item
                },
                onDismiss = { previewItem = null }
            )
        }
    }
}

/**
 * COMPACT 4-COLUMN MEDIA GRID CARD
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaGridCard(
    item: MediaPickerItem,
    isSelected: Boolean,
    selectionNumber: Int?,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF121520))
            .border(
                border = if (isSelected) BorderStroke(2.dp, MintGlow)
                else BorderStroke(0.5.dp, Color(0x20FFFFFF)),
                shape = RoundedCornerShape(8.dp)
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        // Thumbnail Image
        if (item.uri != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.uri)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF1E2130), Color(0xFF0F111A)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.isVideo) Icons.Default.PlayCircleFilled else Icons.Default.Image,
                    contentDescription = null,
                    tint = MintGlow.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Selected Tint Overlay
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MintPrimary.copy(alpha = 0.3f))
            )
        }

        // Top-Left Format Badge (e.g., 4K / 1080p / Photo)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(horizontal = 3.dp, vertical = 1.dp)
        ) {
            Text(
                text = item.resolutionLabel,
                color = TextWhite,
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Top-Right Checkmark Number Badge
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(18.dp)
                .clip(CircleShape)
                .then(
                    if (isSelected) Modifier.background(MintPrimary)
                    else Modifier.background(Color.Black.copy(alpha = 0.5f))
                )
                .border(
                    BorderStroke(1.dp, if (isSelected) TextWhite else TextGray.copy(alpha = 0.5f)),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected && selectionNumber != null) {
                Text(
                    text = "$selectionNumber",
                    color = TextWhite,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Bottom Right Duration / Play Badge
        if (item.isVideo) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(8.dp)
                    )
                    Text(
                        text = item.durationFormatted,
                        color = TextWhite,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * MEDIA PREVIEW MODAL DIALOG FOR LONG PRESS
 */
@Composable
private fun MediaPreviewModal(
    item: MediaPickerItem,
    isSelected: Boolean,
    onToggleSelect: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color(0xFF10121C),
            border = BorderStroke(1.dp, MintPrimary.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextGray, modifier = Modifier.size(18.dp))
                    }
                }

                // Media Preview Frame
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.uri != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.uri)
                                .build(),
                            contentDescription = item.title,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = MintGlow, modifier = Modifier.size(48.dp))
                    }

                    if (item.isVideo) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextWhite, modifier = Modifier.size(28.dp))
                        }
                    }
                }

                // Metadata Details Sheet
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF181B29))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Album", color = TextGray, fontSize = 9.sp)
                        Text(item.albumName, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Resolution", color = TextGray, fontSize = 9.sp)
                        Text("${item.width}x${item.height}", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Duration", color = TextGray, fontSize = 9.sp)
                        Text(if (item.isVideo) item.durationFormatted else "Photo", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Size", color = TextGray, fontSize = 9.sp)
                        Text(item.fileSizeFormatted, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Action Button
                Button(
                    onClick = {
                        onToggleSelect()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFFDC2626) else MintPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.RemoveCircle else Icons.Default.AddCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSelected) "Remove from Selection" else "Add to Selection",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ============================================================================
// REAL MEDIASTORE QUERY FUNCTIONS
// ============================================================================

private suspend fun queryMediaStoreVideos(context: Context): List<MediaPickerItem> {
    val items = mutableListOf<MediaPickerItem>()
    try {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.DATE_ADDED
        )
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
            val bucketCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val widthCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
            val heightCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol) ?: "Video #$id"
                val durationMs = cursor.getLong(durCol)
                val mimeType = cursor.getString(mimeCol) ?: "video/mp4"
                val album = cursor.getString(bucketCol) ?: "Camera"
                val sizeBytes = cursor.getLong(sizeCol)
                val w = cursor.getInt(widthCol)
                val h = cursor.getInt(heightCol)
                val dateAdded = cursor.getLong(dateCol)

                val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                val durationSec = durationMs / 1000
                val mins = durationSec / 60
                val secs = durationSec % 60
                val formattedDur = String.format(Locale.US, "%d:%02d", mins, secs)

                val resLabel = if (w >= 3840 || h >= 2160) "4K"
                else if (w >= 1920 || h >= 1080) "1080p"
                else if (w > 0) "${w}p"
                else "HD"

                val sizeMb = sizeBytes / (1024f * 1024f)
                val sizeFormatted = if (sizeMb > 1024f) String.format(Locale.US, "%.1f GB", sizeMb / 1024f)
                else String.format(Locale.US, "%.1f MB", sizeMb)

                items.add(
                    MediaPickerItem(
                        id = "video_$id",
                        uri = uri,
                        title = name,
                        durationFormatted = formattedDur,
                        durationSeconds = durationSec,
                        isVideo = true,
                        albumName = album,
                        mimeType = mimeType,
                        dateAddedSeconds = dateAdded,
                        resolutionLabel = resLabel,
                        width = if (w > 0) w else 1920,
                        height = if (h > 0) h else 1080,
                        fileSizeFormatted = sizeFormatted,
                        fileSizeBytes = sizeBytes,
                        frameRateLabel = if (name.lowercase().contains("60")) "60 FPS" else "30 FPS"
                    )
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return items
}

private suspend fun queryMediaStoreImages(context: Context): List<MediaPickerItem> {
    val items = mutableListOf<MediaPickerItem>()
    try {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.DATE_ADDED
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            val bucketCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val widthCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol) ?: "Image #$id"
                val mimeType = cursor.getString(mimeCol) ?: "image/jpeg"
                val album = cursor.getString(bucketCol) ?: "Camera"
                val sizeBytes = cursor.getLong(sizeCol)
                val w = cursor.getInt(widthCol)
                val h = cursor.getInt(heightCol)
                val dateAdded = cursor.getLong(dateCol)

                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                val sizeMb = sizeBytes / (1024f * 1024f)
                val sizeFormatted = String.format(Locale.US, "%.1f MB", sizeMb)

                items.add(
                    MediaPickerItem(
                        id = "image_$id",
                        uri = uri,
                        title = name,
                        durationFormatted = "Photo",
                        durationSeconds = 0,
                        isVideo = false,
                        albumName = album,
                        mimeType = mimeType,
                        dateAddedSeconds = dateAdded,
                        resolutionLabel = if (w > 0 && h > 0) "${w}x${h}" else "Photo",
                        width = if (w > 0) w else 1080,
                        height = if (h > 0) h else 1080,
                        fileSizeFormatted = sizeFormatted,
                        fileSizeBytes = sizeBytes,
                        frameRateLabel = "Still"
                    )
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return items
}
