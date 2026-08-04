package com.example.creatoracademy

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * MASTER PHASE 13 - Wishlink Creator Guide Level 13 View
 * AI Portfolio & Brand Ready Profile Builder:
 * Luxury Purple + White Theme, Glassmorphism, Apple Style, 94% Base Progress Ring.
 * 12 Modules:
 * 1. Creator Identity (Name, Username, Niche, Language, Target Audience)
 * 2. Professional Bio Builder (Instagram, YouTube, Wishlink in Short/Long/Professional/Luxury/Friendly)
 * 3. Creator Introduction (30s, 60s, Brand Collab)
 * 4. Portfolio Builder (About Me, Categories, Audience, Strengths, Experience, Achievements, Platforms, Goals)
 * 5. Content Showcase (Best Reels, Shorts, Posts, Reviews, Affiliate Content)
 * 6. Creator Skills Checklist (% progress)
 * 7. Brand Readiness Score (0-100)
 * 8. AI Improvement Plan (Profile, Content, Business)
 * 9. Media Kit Builder (Editable fields + Export PDF/Image/DOCX)
 * 10. Email & Pitch Generator (Brand Intro, Affiliate Collab, Review Request, Follow-up)
 * 11. Social Profile Audit (Instagram, YouTube, Telegram review)
 * 12. Today's Mission & Brand Ready Creator Badge (+900 XP)
 */

private val PurplePrimary13 = Color(0xFFC084FC)
private val PurpleDeepBg113 = Color(0xFF2B0A4D)
private val PurpleDeepBg213 = Color(0xFF17032D)
private val PurpleDeepBg313 = Color(0xFF0D011C)
private val GoldAccent13 = Color(0xFFFFD700)
private val TextWhite13 = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel13PortfolioView(
    userProfile: Map<String, String>,
    onCompleteLevel13: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Current Module index: 1 to 12
    var currentModule by remember { mutableIntStateOf(1) }

    // Module 1 State: Identity
    var creatorName by remember { mutableStateOf(userProfile["creatorName"] ?: "Aarav Sharma") }
    var username by remember { mutableStateOf(userProfile["username"] ?: "@aarav_creates") }
    var niche by remember { mutableStateOf(userProfile["niche"] ?: "Fashion & Lifestyle") }
    var language by remember { mutableStateOf(userProfile["language"] ?: "Hindi & English") }
    var targetAudience by remember { mutableStateOf(userProfile["targetAudience"] ?: "Gen Z & Young Professionals (18-28)") }

    // Module 2 State: Bio Builder
    var bioPlatform by remember { mutableStateOf("Instagram") }
    var bioStyle by remember { mutableStateOf("Luxury") }
    var generatedBio by remember {
        mutableStateOf("✨ $niche Creator | $creatorName\n📌 Curated Wishlink Store below ⬇️\n📩 Business: $username@creator.com")
    }

    // Module 3 State: Creator Introduction
    var introType by remember { mutableStateOf("30 Second Introduction") }
    var introScript by remember {
        mutableStateOf("Hey guys, I'm $creatorName! I create authentic $niche content for young shoppers. Over the past year, I've curated top fashion finds on Wishlink to make styling effortless.")
    }

    // Module 4 State: Portfolio Builder
    var aboutMe by remember { mutableStateOf("Passionate $niche content creator focused on delivering high conversion fashion recommendations and honest product reviews.") }
    var contentCategories by remember { mutableStateOf("Capsule Wardrobe, Seasonal Hauls, OOTD Reels, Budget Fits") }
    var audienceOverview by remember { mutableStateOf("72% Female, 28% Male • 18-28 Age Group • Top Cities: Mumbai, Delhi, Bengaluru") }
    var strengths by remember { mutableStateOf("High-engagement storytelling, crisp 4K color grading, high click-through rates on Wishlink store.") }
    var experience by remember { mutableStateOf("1.5 Years creating fashion & lifestyle content with 100+ Wishlink product recommendations.") }
    var achievements by remember { mutableStateOf("Wishlink Top Tier Creator • 150K+ Monthly Store Views • 8.4% Average Engagement Rate") }
    var platforms by remember { mutableStateOf("Instagram Reels, YouTube Shorts, Wishlink Storefront, Telegram Deals") }
    var futureGoals by remember { mutableStateOf("Launch a personal clothing preset line and hit ₹10L annual Wishlink affiliate sales.") }

    // Module 5 State: Content Showcase
    var bestReels by remember { mutableStateOf("5 Zara Summer Outfits Reel (240K Views)") }
    var bestShorts by remember { mutableStateOf("Top 3 H&M Shirts Shorts (180K Views)") }
    var bestPosts by remember { mutableStateOf("My Minimalist Capsule Wardrobe Guide") }
    var bestReviews by remember { mutableStateOf("Honest Myntra Sneaker Review (45K Views)") }
    var bestAffiliateContent by remember { mutableStateOf("Monsoons Fashion Lookbook - 1.2K Wishlink Clicks") }

    // Module 6 State: Creator Skills (8 skills)
    var skillProductReviews by remember { mutableStateOf(true) }
    var skillVideoEditing by remember { mutableStateOf(true) }
    var skillStorytelling by remember { mutableStateOf(true) }
    var skillPhotography by remember { mutableStateOf(true) }
    var skillCommunication by remember { mutableStateOf(true) }
    var skillAffiliateMarketing by remember { mutableStateOf(true) }
    var skillAnalytics by remember { mutableStateOf(true) }
    var skillBrandCollab by remember { mutableStateOf(true) }

    val totalSkillsChecked = listOf(
        skillProductReviews, skillVideoEditing, skillStorytelling, skillPhotography,
        skillCommunication, skillAffiliateMarketing, skillAnalytics, skillBrandCollab
    ).count { it }
    val skillsPercent = (totalSkillsChecked * 100) / 8

    // Module 7 State: Brand Readiness Score
    val profileQuality = 92
    val contentQuality = 95
    val portfolioQuality = 90
    val professionalism = 98
    val overallScore = (profileQuality + contentQuality + portfolioQuality + professionalism) / 4

    // Module 9 State: Media Kit Builder
    var mkFollowers by remember { mutableStateOf("45K Followers") }
    var mkAvgViews by remember { mutableStateOf("32K Avg Views") }
    var mkLocation by remember { mutableStateOf("India (Tier 1 & 2 Cities)") }
    var mkContactEmail by remember { mutableStateOf("collabs.$username@gmail.com") }
    var mkServicesOffered by remember { mutableStateOf("Instagram Reel, Story Series, Wishlink Store Feature, Dedicated UGC Video") }
    var mkNotes by remember { mutableStateOf("Open to long-term ambassadorships and affiliate integrations.") }

    // Module 10 State: Pitch Generator
    var pitchType by remember { mutableStateOf("Brand Introduction Email") }
    var pitchText by remember {
        mutableStateOf("Subject: Collaboration Proposal: $creatorName x [Brand Name]\n\nHi [Brand Team],\n\nMy name is $creatorName ($username), a $niche creator. I love your latest collection and would love to feature it in an upcoming Wishlink haul video. Here is my media kit for your review!\n\nBest,\n$creatorName")
    }

    // Module 11 State: Social Profile Audit
    var igHandle by remember { mutableStateOf("@$username") }
    var ytHandle by remember { mutableStateOf("youtube.com/@$creatorName") }
    var tgHandle by remember { mutableStateOf("t.me/$username") }
    var auditRan by remember { mutableStateOf(false) }

    // Module 12 State: Achievement
    var isAchievementUnlocked13 by remember { mutableStateOf(false) }

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL13")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL13"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL13"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg113,
                        PurpleDeepBg213,
                        PurpleDeepBg313
                    )
                )
            )
    ) {
        // BACKGROUND: Luxury Purple Gradient, Floating Portfolio Cards, Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33C084FC), radius = w * 0.62f, center = Offset(w * 0.85f, h * 0.15f))
            drawCircle(Color(0x229C27B0), radius = w * 0.70f, center = Offset(w * 0.15f, h * 0.80f))

            // Golden Particles
            drawCircle(GoldAccent13.copy(alpha = 0.55f), radius = 12.dp.toPx(), center = Offset(w * 0.12f, h * 0.22f + floatY))
            drawCircle(GoldAccent13.copy(alpha = 0.40f), radius = 16.dp.toPx(), center = Offset(w * 0.88f, h * 0.48f - floatY))
            drawCircle(PurplePrimary13.copy(alpha = 0.50f), radius = 14.dp.toPx(), center = Offset(w * 0.20f, h * 0.75f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 94% BASE ANIMATED PROGRESS RING
            WishlinkLevel13Header(
                currentModule = currentModule,
                totalModules = 12,
                progressPercent = 94 + ((currentModule - 1) * 1 / 2), // 94% to 100%
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD (1200+ conversation styles)
            WishlinkLevel13AiMentorCard(
                currentModule = currentModule,
                language = language,
                floatY = floatY
            )

            // MODULE CONTENT CONTAINER
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentModule,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "moduleContentTransitionL13"
                ) { module ->
                    when (module) {
                        1 -> Level13Module1CreatorIdentityView(
                            creatorName = creatorName,
                            onNameChange = { creatorName = it },
                            username = username,
                            onUsernameChange = { username = it },
                            niche = niche,
                            onNicheChange = { niche = it },
                            language = language,
                            onLanguageChange = { language = it },
                            targetAudience = targetAudience,
                            onTargetAudienceChange = { targetAudience = it },
                            onContinue = { currentModule = 2 }
                        )
                        2 -> Level13Module2BioBuilderView(
                            creatorName = creatorName,
                            niche = niche,
                            bioPlatform = bioPlatform,
                            onBioPlatformChange = { bioPlatform = it },
                            bioStyle = bioStyle,
                            onBioStyleChange = { bioStyle = it },
                            generatedBio = generatedBio,
                            onGenerateBio = {
                                generatedBio = generateBioText(creatorName, username, niche, bioPlatform, bioStyle)
                            },
                            onContinue = { currentModule = 3 }
                        )
                        3 -> Level13Module3CreatorIntroView(
                            creatorName = creatorName,
                            niche = niche,
                            introType = introType,
                            onIntroTypeChange = { introType = it },
                            introScript = introScript,
                            onGenerateScript = {
                                introScript = generateIntroScript(creatorName, niche, introType)
                            },
                            onContinue = { currentModule = 4 }
                        )
                        4 -> Level13Module4PortfolioBuilderView(
                            aboutMe = aboutMe, onAboutMeChange = { aboutMe = it },
                            contentCategories = contentCategories, onCategoriesChange = { contentCategories = it },
                            audienceOverview = audienceOverview, onAudienceChange = { audienceOverview = it },
                            strengths = strengths, onStrengthsChange = { strengths = it },
                            experience = experience, onExperienceChange = { experience = it },
                            achievements = achievements, onAchievementsChange = { achievements = it },
                            platforms = platforms, onPlatformsChange = { platforms = it },
                            futureGoals = futureGoals, onGoalsChange = { futureGoals = it },
                            onContinue = { currentModule = 5 }
                        )
                        5 -> Level13Module5ContentShowcaseView(
                            bestReels = bestReels, onBestReelsChange = { bestReels = it },
                            bestShorts = bestShorts, onBestShortsChange = { bestShorts = it },
                            bestReviews = bestReviews, onBestReviewsChange = { bestReviews = it },
                            bestAffiliateContent = bestAffiliateContent, onBestAffiliateChange = { bestAffiliateContent = it },
                            onContinue = { currentModule = 6 }
                        )
                        6 -> Level13Module6CreatorSkillsView(
                            s1 = skillProductReviews, onS1Change = { skillProductReviews = it },
                            s2 = skillVideoEditing, onS2Change = { skillVideoEditing = it },
                            s3 = skillStorytelling, onS3Change = { skillStorytelling = it },
                            s4 = skillPhotography, onS4Change = { skillPhotography = it },
                            s5 = skillCommunication, onS5Change = { skillCommunication = it },
                            s6 = skillAffiliateMarketing, onS6Change = { skillAffiliateMarketing = it },
                            s7 = skillAnalytics, onS7Change = { skillAnalytics = it },
                            s8 = skillBrandCollab, onS8Change = { skillBrandCollab = it },
                            percent = skillsPercent,
                            onContinue = { currentModule = 7 }
                        )
                        7 -> Level13Module7ReadinessScoreView(
                            profileQ = profileQuality,
                            contentQ = contentQuality,
                            portfolioQ = portfolioQuality,
                            profQ = professionalism,
                            overall = overallScore,
                            onContinue = { currentModule = 8 }
                        )
                        8 -> Level13Module8ImprovementPlanView(
                            niche = niche,
                            onContinue = { currentModule = 9 }
                        )
                        9 -> Level13Module9MediaKitView(
                            creatorName = creatorName,
                            niche = niche,
                            followers = mkFollowers, onFollowersChange = { mkFollowers = it },
                            avgViews = mkAvgViews, onAvgViewsChange = { mkAvgViews = it },
                            location = mkLocation, onLocationChange = { mkLocation = it },
                            contactEmail = mkContactEmail, onContactEmailChange = { mkContactEmail = it },
                            services = mkServicesOffered, onServicesChange = { mkServicesOffered = it },
                            notes = mkNotes, onNotesChange = { mkNotes = it },
                            onExport = { format ->
                                Toast.makeText(context, "Exporting Media Kit as $format...", Toast.LENGTH_SHORT).show()
                            },
                            onContinue = { currentModule = 10 }
                        )
                        10 -> Level13Module10PitchGeneratorView(
                            creatorName = creatorName,
                            pitchType = pitchType,
                            onPitchTypeChange = { pitchType = it },
                            pitchText = pitchText,
                            onGeneratePitch = {
                                pitchText = generatePitchText(creatorName, username, niche, pitchType)
                            },
                            onContinue = { currentModule = 11 }
                        )
                        11 -> Level13Module11ProfileAuditView(
                            igHandle = igHandle, onIgChange = { igHandle = it },
                            ytHandle = ytHandle, onYtChange = { ytHandle = it },
                            tgHandle = tgHandle, onTgChange = { tgHandle = it },
                            auditRan = auditRan,
                            onRunAudit = { auditRan = true },
                            onContinue = { currentModule = 12 }
                        )
                        12 -> Level13Module12MissionAchievementView(
                            isUnlocked = isAchievementUnlocked13,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked13 = true
                                CreatorAcademyPrefs.saveWishlinkLevel13Data(
                                    context = context,
                                    score = 100,
                                    progress = 100,
                                    portfolioJson = "aboutMe:$aboutMe|strengths:$strengths",
                                    mediaKitJson = "followers:$mkFollowers|email:$mkContactEmail"
                                )
                            },
                            onCompleteLevel = onCompleteLevel13
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 13 HEADER WITH 94% BASE PROGRESS RING
 */
@Composable
private fun WishlinkLevel13Header(
    currentModule: Int,
    totalModules: Int,
    progressPercent: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0x22FFFFFF))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextWhite13,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FolderSpecial,
                    contentDescription = null,
                    tint = PurplePrimary13,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Creator Portfolio Builder",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite13
                )
            }
            Text(
                text = "Become Brand Ready • Module $currentModule/$totalModules",
                fontSize = 10.5.sp,
                color = Color(0xFFE1BEE7)
            )
        }

        Box(
            modifier = Modifier.size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 3.dp.toPx()
                drawCircle(
                    color = Color(0x33C084FC),
                    style = Stroke(width = strokeWidth)
                )
                val sweep = (progressPercent / 100f) * 360f
                drawArc(
                    color = GoldAccent13,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }
            Text(
                text = "$progressPercent%",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent13
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 1200+ CONVERSATION STYLES
 */
@Composable
private fun WishlinkLevel13AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel13Module(currentModule, language)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x332B0A4D))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary13.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer { translationY = floatY * 0.5f }
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(PurplePrimary13, PurpleDeepBg113)))
                    .border(BorderStroke(1.5.dp, GoldAccent13), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = "AI Portfolio Mentor",
                    tint = GoldAccent13,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Brand Advisor",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent13
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "PORTFOLIO BUILDER", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite13,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForLevel13Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi" || lang.contains("Hindi")

    return when (module) {
        1 -> if (isHindi) "Brand collaboration milne se pehle... Tumhara profile hi tumhari pehli impression hoti hai. Aaj hum ek professional creator identity banayenge." else "Before getting a brand deal... Your profile is your first impression. Today we build your professional identity."
        2 -> if (isHindi) "Professional Bio Builder: Custom bios generate karo Instagram, YouTube aur Wishlink store ke liye." else "Professional Bio Builder: Generate custom bios tailored for Instagram, YouTube, and Wishlink."
        3 -> if (isHindi) "Creator Introduction: 30s aur 60s introduction pitch tailor karo natural conversational tone mein." else "Creator Introduction: Craft 30s and 60s introduction scripts in a confident natural tone."
        4 -> if (isHindi) "Portfolio Builder: Strengths, achievements aur target audience setup karo." else "Portfolio Builder: Highlight strengths, achievements, and target audience overview."
        5 -> if (isHindi) "Content Showcase: Apne best Reels, Shorts aur Wishlink reviews highlight karo." else "Content Showcase: Showcase your top performing Reels, Shorts, and product reviews."
        6 -> if (isHindi) "Creator Skills: Product review, editing aur affiliate marketing skills evaluate karo." else "Creator Skills: Evaluate your proficiency across editing, storytelling, and affiliate marketing."
        7 -> if (isHindi) "Brand Readiness Score: AI tumhari profile quality aur readiness index evaluate karta hai." else "Brand Readiness Score: AI evaluates your brand readiness index across 4 core pillars."
        8 -> if (isHindi) "AI Improvement Plan: Top profile, content aur business improvements ka action plan." else "AI Improvement Plan: Actionable steps to upgrade profile, content, and monetization."
        9 -> if (isHindi) "Media Kit Builder: Professional Media Kit generate karo PDF & Image export options ke saath." else "Media Kit Builder: Generate a sleek Media Kit ready for brand pitch exports."
        10 -> if (isHindi) "Email & Pitch Generator: Brands ko mail karne ke liye high-converting templates." else "Email & Pitch Generator: Pitch templates crafted for brand collabs and affiliate outreach."
        11 -> if (isHindi) "Social Profile Audit: Profile picture, highlights aur link placement audit karo." else "Social Profile Audit: Review bio, profile picture, highlights, and store link placement."
        12 -> if (isHindi) "Awesome! Tumne Creator Portfolio complete kar liya! Brand Ready Creator Badge & +900 XP claim karo!" else "Outstanding! Claim your Brand Ready Creator Badge and unlock +900 XP!"
        else -> "Build your brand identity!"
    }
}

/**
 * MODULE BADGE HELPER
 */
@Composable
private fun Level13ModuleBadge(moduleNum: Int, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33C084FC))
                .border(BorderStroke(1.dp, PurplePrimary13), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = "MODULE $moduleNum", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent13)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite13)
    }
}

/**
 * GLASS SHINE BUTTON
 */
@Composable
private fun GlassShineButton13(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF6A1B9A),
                        Color(0xFFAB47BC)
                    )
                )
            )
            .border(BorderStroke(1.dp, GoldAccent13.copy(alpha = 0.6f)), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13
        )
    }
}

/**
 * MODULE 1: Creator Identity
 */
@Composable
private fun Level13Module1CreatorIdentityView(
    creatorName: String, onNameChange: (String) -> Unit,
    username: String, onUsernameChange: (String) -> Unit,
    niche: String, onNicheChange: (String) -> Unit,
    language: String, onLanguageChange: (String) -> Unit,
    targetAudience: String, onTargetAudienceChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 1, title = "Creator Identity")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Define Your Core Brand Identity",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = creatorName, onValueChange = onNameChange,
            label = { Text("Creator Full Name", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username, onValueChange = onUsernameChange,
            label = { Text("Primary Handle / Username", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = niche, onValueChange = onNicheChange,
            label = { Text("Content Niche", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = language, onValueChange = onLanguageChange,
            label = { Text("Primary Languages", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = targetAudience, onValueChange = onTargetAudienceChange,
            label = { Text("Target Audience Demographic", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Build Professional Bios →", onClick = onContinue)
    }
}

/**
 * MODULE 2: Professional Bio Builder
 */
@Composable
private fun Level13Module2BioBuilderView(
    creatorName: String,
    niche: String,
    bioPlatform: String, onBioPlatformChange: (String) -> Unit,
    bioStyle: String, onBioStyleChange: (String) -> Unit,
    generatedBio: String,
    onGenerateBio: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 2, title = "Professional Bio Builder")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Generate High-Converting Platform Bios",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = "Select Platform:", fontSize = 12.sp, color = GoldAccent13, modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Instagram", "YouTube", "Wishlink").forEach { platform ->
                val sel = bioPlatform == platform
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (sel) PurplePrimary13 else Color(0x22FFFFFF))
                        .clickable { onBioPlatformChange(platform) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = platform, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (sel) Color.Black else TextWhite13)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Select Tone & Style:", fontSize = 12.sp, color = GoldAccent13, modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Professional", "Luxury", "Friendly").forEach { style ->
                val sel = bioStyle == style
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (sel) GoldAccent13 else Color(0x22FFFFFF))
                        .clickable { onBioStyleChange(style) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = style, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (sel) Color.Black else TextWhite13)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x33000000))
                .border(BorderStroke(1.dp, PurplePrimary13), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(text = "Generated $bioPlatform Bio ($bioStyle):", fontSize = 11.sp, color = GoldAccent13)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = generatedBio, fontSize = 13.sp, color = TextWhite13, lineHeight = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33C084FC))
                .clickable { onGenerateBio() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent13, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Regenerate Variation ✨", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite13)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Creator Introduction Script →", onClick = onContinue)
    }
}

private fun generateBioText(name: String, handle: String, niche: String, platform: String, style: String): String {
    return when (style) {
        "Luxury" -> "✨ $niche Curator | $name\n📍 Curated luxury fits & essentials\n🛍️ Shop my exact Wishlink store below 👇"
        "Professional" -> "👔 $niche Specialist • $name\n📈 Helping young shoppers style modern outfits\n💼 Business Collabs: $handle@creator.com\n🔗 Wishlink Storefront:"
        else -> "Hey shoppers! I'm $name 👋\nSharing honest $niche finds every single week!\nTap my Wishlink link to get exact outfit codes ⬇️"
    }
}

/**
 * MODULE 3: Creator Introduction
 */
@Composable
private fun Level13Module3CreatorIntroView(
    creatorName: String,
    niche: String,
    introType: String, onIntroTypeChange: (String) -> Unit,
    introScript: String,
    onGenerateScript: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 3, title = "Creator Introduction")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Master Your Pitch & Audio Introductions",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("30 Second Intro", "60 Second Intro", "Brand Collab Pitch").forEach { type ->
                val sel = introType.contains(type.take(5))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (sel) PurplePrimary13 else Color(0x22FFFFFF))
                        .clickable { onIntroTypeChange(type) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = type, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (sel) Color.Black else TextWhite13)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x33000000))
                .border(BorderStroke(1.dp, GoldAccent13), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(text = "$introType Script:", fontSize = 11.sp, color = GoldAccent13)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = introScript, fontSize = 12.5.sp, color = TextWhite13, lineHeight = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33C084FC))
                .clickable { onGenerateScript() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent13, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Refresh Script Pitch ✨", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite13)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Build Detailed Portfolio →", onClick = onContinue)
    }
}

private fun generateIntroScript(name: String, niche: String, type: String): String {
    return when {
        type.contains("30") -> "Hey everyone! I'm $name, a $niche creator. My mission is to help my audience find high quality fashion at affordable prices. Every week I test and review top collections on my Wishlink store so my followers get instant styling codes."
        type.contains("60") -> "Hello! I'm $name. Over the past year, I built an engaged community in $niche. By focusing on honest product reviews and high conversion Wishlink outfit links, I turn casual viewers into repeat shoppers. I work closely with fashion brands to drive tangible sales."
        else -> "Hi Team! I'm $name, $niche creator behind my page. My audience consists of style-conscious young adults actively seeking outfit inspiration. I integrate Wishlink store tags seamlessly into my Reels, driving over 10K+ monthly store clicks."
    }
}

/**
 * MODULE 4: Portfolio Builder
 */
@Composable
private fun Level13Module4PortfolioBuilderView(
    aboutMe: String, onAboutMeChange: (String) -> Unit,
    contentCategories: String, onCategoriesChange: (String) -> Unit,
    audienceOverview: String, onAudienceChange: (String) -> Unit,
    strengths: String, onStrengthsChange: (String) -> Unit,
    experience: String, onExperienceChange: (String) -> Unit,
    achievements: String, onAchievementsChange: (String) -> Unit,
    platforms: String, onPlatformsChange: (String) -> Unit,
    futureGoals: String, onGoalsChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 4, title = "Portfolio Builder")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "8 Core Portfolio Sections",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        val fields = listOf(
            Triple("1. About Me", aboutMe, onAboutMeChange),
            Triple("2. Content Categories", contentCategories, onCategoriesChange),
            Triple("3. Audience Overview", audienceOverview, onAudienceChange),
            Triple("4. Strengths & Tone", strengths, onStrengthsChange),
            Triple("5. Experience & History", experience, onExperienceChange),
            Triple("6. Achievements & Badges", achievements, onAchievementsChange),
            Triple("7. Active Platforms", platforms, onPlatformsChange),
            Triple("8. Future Goals & Roadmap", futureGoals, onGoalsChange)
        )

        fields.forEach { (label, valStr, onChange) ->
            OutlinedTextField(
                value = valStr, onValueChange = onChange,
                label = { Text(label, color = PurplePrimary13) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Manage Content Showcase →", onClick = onContinue)
    }
}

/**
 * MODULE 5: Content Showcase
 */
@Composable
private fun Level13Module5ContentShowcaseView(
    bestReels: String, onBestReelsChange: (String) -> Unit,
    bestShorts: String, onBestShortsChange: (String) -> Unit,
    bestReviews: String, onBestReviewsChange: (String) -> Unit,
    bestAffiliateContent: String, onBestAffiliateChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 5, title = "Content Showcase")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Highlight Your Best Performing Media",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = bestReels, onValueChange = onBestReelsChange,
            label = { Text("Best Performing Reels", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bestShorts, onValueChange = onBestShortsChange,
            label = { Text("Best YouTube Shorts", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bestReviews, onValueChange = onBestReviewsChange,
            label = { Text("Top Product Review Videos", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bestAffiliateContent, onValueChange = onBestAffiliateChange,
            label = { Text("High Conversion Wishlink Content", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Creator Skills Checklist →", onClick = onContinue)
    }
}

/**
 * MODULE 6: Creator Skills Checklist
 */
@Composable
private fun Level13Module6CreatorSkillsView(
    s1: Boolean, onS1Change: (Boolean) -> Unit,
    s2: Boolean, onS2Change: (Boolean) -> Unit,
    s3: Boolean, onS3Change: (Boolean) -> Unit,
    s4: Boolean, onS4Change: (Boolean) -> Unit,
    s5: Boolean, onS5Change: (Boolean) -> Unit,
    s6: Boolean, onS6Change: (Boolean) -> Unit,
    s7: Boolean, onS7Change: (Boolean) -> Unit,
    s8: Boolean, onS8Change: (Boolean) -> Unit,
    percent: Int,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 6, title = "Creator Skills Checklist")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Skill Proficiency Breakdown ($percent% Mastered)",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        val skillList = listOf(
            Pair("Product Reviews & Honest Unboxing", s1 to onS1Change),
            Pair("4K Mobile Video Editing & Color Grading", s2 to onS2Change),
            Pair("Hook Storytelling & Scripting", s3 to onS3Change),
            Pair("Aesthetic Photography & Lighting", s4 to onS4Change),
            Pair("Audience & Brand Communication", s5 to onS5Change),
            Pair("Wishlink Store & Affiliate Marketing", s6 to onS6Change),
            Pair("Analytics & CTR Optimization", s7 to onS7Change),
            Pair("Brand Collaboration Deal Negotiation", s8 to onS8Change)
        )

        skillList.forEach { (text, pair) ->
            val (checked, onChange) = pair
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (checked) Color(0x44C084FC) else Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, if (checked) GoldAccent13 else Color(0x33C084FC)), RoundedCornerShape(12.dp))
                    .clickable { onChange(!checked) }
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (checked) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (checked) GoldAccent13 else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = text, fontSize = 12.sp, color = TextWhite13)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Calculate Readiness Score →", onClick = onContinue)
    }
}

/**
 * MODULE 7: Brand Readiness Score
 */
@Composable
private fun Level13Module7ReadinessScoreView(
    profileQ: Int,
    contentQ: Int,
    portfolioQ: Int,
    profQ: Int,
    overall: Int,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 7, title = "Brand Readiness Score")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "AI Evaluation: Overall $overall/100",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0x33C084FC))
                .border(BorderStroke(3.dp, GoldAccent13), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$overall", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent13)
                Text(text = "BRAND READY", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextWhite13)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        val metrics = listOf(
            Pair("Profile Quality", profileQ),
            Pair("Content Quality", contentQ),
            Pair("Portfolio Quality", portfolioQ),
            Pair("Professionalism", profQ)
        )

        metrics.forEach { (label, score) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary13), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite13)
                    Text(text = "$score/100", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent13)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "*Educational assessment based on completeness of identity, bio clarity, and portfolio assets.",
            fontSize = 10.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "View AI Improvement Plan →", onClick = onContinue)
    }
}

/**
 * MODULE 8: AI Improvement Plan
 */
@Composable
private fun Level13Module8ImprovementPlanView(
    niche: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 8, title = "AI Improvement Plan")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Personalized 15-Point Growth Roadmap",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val plans = listOf(
            Triple("Top 5 Profile Upgrades", "1. Add clear Wishlink store CTA in Instagram bio\n2. Pin top 3 high-converting Reels\n3. Use consistent color palette across thumbnails\n4. Add business contact email in bio\n5. Clean non-active highlights", Icons.Default.Person),
            Triple("Top 5 Content Upgrades", "1. Improve lighting in first 2 seconds\n2. Use text overlays for Wishlink item codes\n3. A/B test voiceover vs trending audio\n4. Include call-to-action in every caption\n5. Post 4 times per week consistently", Icons.Default.Movie),
            Triple("Top 5 Business Upgrades", "1. Refresh out-of-stock Wishlink links weekly\n2. Group products by seasonal collections\n3. Send media kit to 3 brands every Monday\n4. Track CTR in Wishlink analytics dashboard\n5. Build a Telegram deal channel", Icons.Default.Analytics)
        )

        plans.forEach { (title, desc, icon) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary13), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row {
                    Icon(imageVector = icon, contentDescription = null, tint = GoldAccent13, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent13)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = desc, fontSize = 11.5.sp, color = TextWhite13, lineHeight = 16.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Open Media Kit Builder →", onClick = onContinue)
    }
}

/**
 * MODULE 9: Media Kit Builder
 */
@Composable
private fun Level13Module9MediaKitView(
    creatorName: String,
    niche: String,
    followers: String, onFollowersChange: (String) -> Unit,
    avgViews: String, onAvgViewsChange: (String) -> Unit,
    location: String, onLocationChange: (String) -> Unit,
    contactEmail: String, onContactEmailChange: (String) -> Unit,
    services: String, onServicesChange: (String) -> Unit,
    notes: String, onNotesChange: (String) -> Unit,
    onExport: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 9, title = "Media Kit Builder")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Generate & Export Brand Media Kit",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = followers, onValueChange = onFollowersChange,
            label = { Text("Total Follower Count", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = avgViews, onValueChange = onAvgViewsChange,
            label = { Text("Average Views per Reel/Video", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = location, onValueChange = onLocationChange,
            label = { Text("Audience Geographic Location", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = contactEmail, onValueChange = onContactEmailChange,
            label = { Text("Business Contact Email", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = services, onValueChange = onServicesChange,
            label = { Text("Services Offered", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Export Options:", fontSize = 12.sp, color = GoldAccent13)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("PDF", "Image", "DOCX").forEach { format ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x33C084FC))
                        .border(BorderStroke(1.dp, GoldAccent13), RoundedCornerShape(10.dp))
                        .clickable { onExport(format) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = GoldAccent13, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Export $format", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite13)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Email & Pitch Generator →", onClick = onContinue)
    }
}

/**
 * MODULE 10: Email & Pitch Generator
 */
@Composable
private fun Level13Module10PitchGeneratorView(
    creatorName: String,
    pitchType: String, onPitchTypeChange: (String) -> Unit,
    pitchText: String,
    onGeneratePitch: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 10, title = "Email & Pitch Generator")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Generate High-Converting Brand Outreaches",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            listOf("Brand Intro Email", "Affiliate Collab Pitch", "Product Review Request", "Follow-up Email").forEach { type ->
                val sel = pitchType == type
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (sel) PurplePrimary13 else Color(0x22FFFFFF))
                        .clickable {
                            onPitchTypeChange(type)
                            onGeneratePitch()
                        }
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Text(text = type, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (sel) Color.Black else TextWhite13)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x33000000))
                .border(BorderStroke(1.dp, GoldAccent13), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(text = "Generated $pitchType Template:", fontSize = 11.sp, color = GoldAccent13)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = pitchText, fontSize = 12.sp, color = TextWhite13, lineHeight = 17.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Run Social Profile Audit →", onClick = onContinue)
    }
}

private fun generatePitchText(name: String, handle: String, niche: String, type: String): String {
    return when (type) {
        "Affiliate Collab Pitch" -> "Subject: Wishlink Store Integration Request: $name\n\nHi [Brand Team],\n\nI love your new collection! I regularly curate $niche outfits on my Wishlink store. I'd love to generate trackable Wishlink product links for your catalog to feature in my upcoming Reels.\n\nBest,\n$name"
        "Product Review Request" -> "Subject: Honest Review Proposal for [Product Name]\n\nHi [PR Team],\n\nI'm $name ($handle). My audience trusts my honest $niche reviews. I'd love to review your latest release and share exact buy links with my 40K+ followers via Wishlink.\n\nBest regards,\n$name"
        "Follow-up Email" -> "Subject: Following up: $name x [Brand Name]\n\nHi [Name],\n\nHope you're having a great week! Just following up on my previous collaboration note. Attached is my latest Media Kit showcasing our 8.4% engagement rate.\n\nBest,\n$name"
        else -> "Subject: Collaboration Proposal: $name x [Brand Name]\n\nHi [Brand Team],\n\nMy name is $name ($handle), a $niche creator. I love your brand aesthetic and would love to feature your top items in my Wishlink haul video.\n\nBest,\n$name"
    }
}

/**
 * MODULE 11: Social Profile Audit
 */
@Composable
private fun Level13Module11ProfileAuditView(
    igHandle: String, onIgChange: (String) -> Unit,
    ytHandle: String, onYtChange: (String) -> Unit,
    tgHandle: String, onTgChange: (String) -> Unit,
    auditRan: Boolean,
    onRunAudit: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 11, title = "Social Profile Audit")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Audit Your Public Creator Accounts",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = igHandle, onValueChange = onIgChange,
            label = { Text("Instagram Handle", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = ytHandle, onValueChange = onYtChange,
            label = { Text("YouTube Channel Link", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = tgHandle, onValueChange = onTgChange,
            label = { Text("Telegram / WhatsApp Channel", color = PurplePrimary13) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary13, unfocusedBorderColor = Color(0x44FFFFFF), focusedTextColor = TextWhite13, unfocusedTextColor = TextWhite13)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33C084FC))
                .border(BorderStroke(1.dp, GoldAccent13), RoundedCornerShape(12.dp))
                .clickable { onRunAudit() }
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Analytics, contentDescription = null, tint = GoldAccent13, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Run AI Social Audit ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite13)
            }
        }

        if (auditRan) {
            Spacer(modifier = Modifier.height(12.dp))
            val auditItems = listOf(
                "Bio Optimization: Clear Wishlink CTA detected ✅",
                "Username Consistency: Matches across Instagram & YouTube ✅",
                "Profile Picture: Professional lighting and high-contrast composition ✅",
                "Story Highlights: Wishlink collections organized cleanly ✅",
                "Link Placement: Direct link to store in main bio link field ✅"
            )
            auditItems.forEach { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x22FFFFFF))
                        .padding(8.dp)
                ) {
                    Text(text = item, fontSize = 11.5.sp, color = TextWhite13)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton13(text = "Complete Today's Mission →", onClick = onContinue)
    }
}

/**
 * MODULE 12: Mission & Achievement
 */
@Composable
private fun Level13Module12MissionAchievementView(
    isUnlocked: Boolean,
    shineAnim: Float,
    onUnlockAchievement: () -> Unit,
    onCompleteLevel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level13ModuleBadge(moduleNum = 12, title = "Today's Mission")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Complete Your Creator Portfolio",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite13,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Estimated Time: 20 Minutes • All 12 Modules Finalized",
            fontSize = 11.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // BADGE CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            PurplePrimary13.copy(alpha = 0.4f),
                            Color(0x442B0A4D)
                        )
                    )
                )
                .border(BorderStroke(2.dp, GoldAccent13), RoundedCornerShape(24.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(GoldAccent13, Color(0xFFFF8F00))))
                        .border(BorderStroke(3.dp, TextWhite13), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Brand Ready Badge",
                        tint = PurpleDeepBg113,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Brand Ready Creator",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent13
                )

                Text(
                    text = "Mastered AI Portfolio & Brand Ready Profile Builder",
                    fontSize = 11.sp,
                    color = TextWhite13,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x3300E676))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "+900 XP REWARD UNLOCKED",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00E676)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!isUnlocked) {
            GlassShineButton13(
                text = "Claim Badge & Complete Level 13 🏆",
                onClick = onUnlockAchievement
            )
        } else {
            GlassShineButton13(
                text = "Return to Creator Dashboard ✓",
                onClick = onCompleteLevel
            )
        }
    }
}
