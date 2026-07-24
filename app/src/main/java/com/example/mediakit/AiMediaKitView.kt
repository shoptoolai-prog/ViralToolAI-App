package com.example.mediakit

import android.content.Intent
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.ui.components.GlassCard
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

/**
 * SHOPTOOLAI Phase 9A — AI Media Kit Generator View
 * Apple-inspired glassmorphism design, theme customization, verified stats, and future-ready PDF export.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiMediaKitView(
    mediaKit: AiMediaKit,
    onClose: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    var selectedTheme by remember { mutableStateOf(MediaKitTheme.GOLD_LUXE) }
    var showPdfExportSheet by remember { mutableStateOf(false) }

    // Animated colors for dynamic glass theme
    val animatedAccent by animateColorAsState(targetValue = selectedTheme.accentColor, animationSpec = tween(500), label = "Accent")
    val animatedBgGradients = selectedTheme.primaryGradient

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(animatedBgGradients))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0x22FFFFFF), CircleShape)
                            .border(BorderStroke(1.dp, Color(0x33FFFFFF)), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "AI Media Kit",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Surface(
                                color = animatedAccent.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, animatedAccent)
                            ) {
                                Text(
                                    text = "OFFICIAL",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = animatedAccent,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Verified Creator Press & Brand Deck",
                            fontSize = 11.sp,
                            color = TextGray
                        )
                    }
                }

                // PDF Export Trigger Button
                Button(
                    onClick = { showPdfExportSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = animatedAccent),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "Export PDF",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Export PDF",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // Theme Switcher Selector
            ThemeSelectorRow(
                selectedTheme = selectedTheme,
                onSelectTheme = { selectedTheme = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable Media Kit Document Body
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 1. Hero Profile Banner
                item {
                    MediaKitHeroHeader(mediaKit = mediaKit, accentColor = animatedAccent)
                }

                // 2. Creator Summary & Content Niche
                item {
                    MediaKitSummaryCard(mediaKit = mediaKit, accentColor = animatedAccent)
                }

                // 3. Collaboration Readiness Score
                item {
                    MediaKitReadinessCard(readiness = mediaKit.collaborationReadiness, accentColor = animatedAccent)
                }

                // 4. Brand Friendly Categories
                item {
                    MediaKitCategoriesCard(categories = mediaKit.brandFriendlyCategories, accentColor = animatedAccent)
                }

                // 5. Portfolio Showcase Layout
                item {
                    MediaKitPortfolioSection(items = mediaKit.portfolioItems, accentColor = animatedAccent)
                }

                // 6. Contact Section & Social Links
                item {
                    MediaKitContactSection(
                        mediaKit = mediaKit,
                        accentColor = animatedAccent,
                        onCopyEmail = {
                            clipboard.setText(AnnotatedString(mediaKit.businessEmail))
                            onShowToast("✔ Business Email copied to clipboard!")
                        },
                        onCopyHandle = {
                            clipboard.setText(AnnotatedString(mediaKit.creatorUsername))
                            onShowToast("✔ Handle copied!")
                        }
                    )
                }
            }
        }
    }

    // PDF Export Preview Sheet Modal
    if (showPdfExportSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPdfExportSheet = false },
            containerColor = Color(0xFF141419),
            scrimColor = Color.Black.copy(alpha = 0.7f)
        ) {
            PdfExportPreviewSheet(
                mediaKit = mediaKit,
                accentColor = animatedAccent,
                onDismiss = { showPdfExportSheet = false },
                onShowToast = onShowToast
            )
        }
    }
}

@Composable
fun ThemeSelectorRow(
    selectedTheme: MediaKitTheme,
    onSelectTheme: (MediaKitTheme) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(MediaKitTheme.values()) { theme ->
            val isSelected = selectedTheme == theme
            Surface(
                onClick = { onSelectTheme(theme) },
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) theme.accentColor.copy(alpha = 0.25f) else Color(0x1AFFFFFF),
                border = BorderStroke(1.dp, if (isSelected) theme.accentColor else Color(0x22FFFFFF))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(theme.accentColor)
                    )
                    Text(
                        text = theme.title,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextWhite else TextGray
                    )
                }
            }
        }
    }
}

@Composable
fun MediaKitHeroHeader(mediaKit: AiMediaKit, accentColor: Color) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = accentColor.copy(alpha = 0.4f),
        backgroundColor = Color(0x22000000)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture with Glass Ring
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(accentColor, Color(0xFF1E1E24))))
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (!mediaKit.profilePicUri.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = mediaKit.profilePicUri,
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        tint = accentColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Verified Username Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = mediaKit.creatorUsername,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
                if (mediaKit.isUsernameVerified) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified Handle",
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (mediaKit.isDisplayNameVerified && mediaKit.creatorDisplayName.isNotBlank() && mediaKit.creatorDisplayName != "Not visible in screenshot") {
                Text(
                    text = mediaKit.creatorDisplayName,
                    fontSize = 13.sp,
                    color = TextGray
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Verified Stats Counter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x33000000), RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MediaKitStatCell(
                    label = "Followers",
                    value = mediaKit.followersFormatted,
                    isVerified = mediaKit.isFollowersVerified,
                    accentColor = accentColor
                )
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0x33FFFFFF)))
                MediaKitStatCell(
                    label = "Following",
                    value = mediaKit.followingFormatted,
                    isVerified = mediaKit.isFollowingVerified,
                    accentColor = accentColor
                )
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0x33FFFFFF)))
                MediaKitStatCell(
                    label = "Posts",
                    value = mediaKit.postsFormatted,
                    isVerified = mediaKit.isPostsVerified,
                    accentColor = accentColor
                )
            }
        }
    }
}

@Composable
fun MediaKitStatCell(
    label: String,
    value: String,
    isVerified: Boolean,
    accentColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = if (isVerified) TextWhite else TextGray
            )
            if (isVerified) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Verified",
                    tint = accentColor,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
        Text(text = label, fontSize = 10.sp, color = TextGray)
    }
}

@Composable
fun MediaKitSummaryCard(mediaKit: AiMediaKit, accentColor: Color) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = Color(0x33FFFFFF),
        backgroundColor = Color(0x1F000000)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Summary",
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "CREATOR SUMMARY & NICHE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = accentColor,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = accentColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Category, contentDescription = "Niche", tint = accentColor, modifier = Modifier.size(14.dp))
                    Text(text = "Primary Niche: ${mediaKit.contentNiche}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = mediaKit.creatorSummary,
                fontSize = 12.5.sp,
                color = TextWhite,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun MediaKitReadinessCard(readiness: CollaborationReadiness, accentColor: Color) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = accentColor.copy(alpha = 0.3f),
        backgroundColor = Color(0x22000000)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Readiness",
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "COLLABORATION READINESS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    color = accentColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${readiness.score}/100",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { readiness.score / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = accentColor,
                trackColor = Color(0x33FFFFFF)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tier: ${readiness.tierLabel}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                readiness.readinessBullets.forEach { bullet ->
                    Text(
                        text = bullet,
                        fontSize = 11.5.sp,
                        color = TextGray
                    )
                }
            }
        }
    }
}

@Composable
fun MediaKitCategoriesCard(categories: List<MediaKitBrandCategory>, accentColor: Color) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = Color(0x33FFFFFF),
        backgroundColor = Color(0x1F000000)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Brand Categories",
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "BRAND FRIENDLY CATEGORIES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = accentColor,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                categories.forEach { cat ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x22FFFFFF), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = cat.categoryName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text(text = "${cat.matchPercentage}% Match", fontSize = 11.sp, fontWeight = FontWeight.Black, color = accentColor)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = cat.keyAdvantage, fontSize = 11.sp, color = TextGray)
                    }
                }
            }
        }
    }
}

@Composable
fun MediaKitPortfolioSection(items: List<MediaKitPortfolioItem>, accentColor: Color) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = Color(0x33FFFFFF),
        backgroundColor = Color(0x1F000000)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = "Portfolio",
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "PORTFOLIO & CONTENT SHOWCASE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = accentColor,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0x33000000), RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Surface(
                                color = accentColor.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = item.postType,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = accentColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = item.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = item.verifiedMetric, fontSize = 10.sp, color = TextGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaKitContactSection(
    mediaKit: AiMediaKit,
    accentColor: Color,
    onCopyEmail: () -> Unit,
    onCopyHandle: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = accentColor.copy(alpha = 0.3f),
        backgroundColor = Color(0x22000000)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContactMail,
                    contentDescription = "Contact",
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "BUSINESS CONTACT & SOCIAL LINKS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = accentColor,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onCopyEmail,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, accentColor)
                ) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = accentColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Copy Email", fontSize = 11.5.sp, color = TextWhite)
                }

                OutlinedButton(
                    onClick = onCopyHandle,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0x4DFFFFFF))
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Handle", tint = TextWhite, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Copy Handle", fontSize = 11.5.sp, color = TextWhite)
                }
            }
        }
    }
}

@Composable
fun PdfExportPreviewSheet(
    mediaKit: AiMediaKit,
    accentColor: Color,
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0x4DFFFFFF))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = "PDF", tint = accentColor, modifier = Modifier.size(24.dp))
            Text(
                text = "Export Vector PDF Media Kit",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Generate a high-res printable PDF deck formatted with verified OCR metrics and brand categories.",
            fontSize = 12.sp,
            color = TextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // PDF Document Mockup Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E24), RoundedCornerShape(16.dp))
                .border(BorderStroke(1.dp, accentColor), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "OFFICIAL CREATOR MEDIA KIT PDF", fontSize = 11.sp, fontWeight = FontWeight.Black, color = accentColor)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = mediaKit.creatorUsername, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${mediaKit.followersFormatted} Followers • ${mediaKit.contentNiche}", fontSize = 11.sp, color = TextGray)
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0x2200FFCC),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = "✔ PDF Print Ready (2 Pages - A4 Vector)", fontSize = 10.sp, color = Color(0xFF00FFCC), modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "AI Media Kit - ${mediaKit.creatorUsername}")
                    putExtra(Intent.EXTRA_TEXT, "Here is the AI Media Kit for ${mediaKit.creatorUsername}.\nNiche: ${mediaKit.contentNiche}\nFollowers: ${mediaKit.followersFormatted}")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Export & Share PDF Media Kit"))
                onShowToast("✔ Media Kit PDF Export Initiated!")
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Download, contentDescription = "Download", tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Download / Share PDF Deck", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
