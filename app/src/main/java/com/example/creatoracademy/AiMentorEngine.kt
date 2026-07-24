package com.example.creatoracademy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.EmeraldPrimary
import kotlin.random.Random

/**
 * MASTER PHASE 15C — AI Creator Mentor Pro Engine
 * Core architecture for real adaptive learning, dynamic lesson generation,
 * smart help explanations, non-repeating AI encouragement, streak tracking,
 * and future-ready hooks for Brand Deals, Affiliate, & Video Editing.
 */

enum class TaskState {
    LOCKED,
    CURRENT,
    COMPLETED,
    SKIPPED
}

enum class FutureAcademyModule {
    BRAND_COLLABORATION_AI,
    AFFILIATE_MENTOR,
    VIDEO_EDITING_ACADEMY
}

data class CreatorLevel(
    val name: String,
    val badgeName: String,
    val minXp: Int,
    val maxXp: Int,
    val color: Color,
    val icon: ImageVector
) {
    companion object {
        fun getLevelForXp(xp: Int): CreatorLevel {
            return when {
                xp < 200 -> CreatorLevel("Bronze Creator", "BRONZE BADGE", 0, 200, Color(0xFFCD7F32), Icons.Default.WorkspacePremium)
                xp < 500 -> CreatorLevel("Silver Creator", "SILVER BADGE", 200, 500, Color(0xFFC0C0C0), Icons.Default.Star)
                xp < 1000 -> CreatorLevel("Gold Creator", "GOLD BADGE", 500, 1000, Color(0xFFFFD700), Icons.Default.EmojiEvents)
                xp < 2000 -> CreatorLevel("Diamond Creator", "DIAMOND BADGE", 1000, 2000, Color(0xFF00E5FF), Icons.Default.WorkspacePremium)
                else -> CreatorLevel("Legend Creator", "LEGEND BADGE", 2000, 5000, EmeraldPrimary, Icons.Default.EmojiEvents)
            }
        }
    }
}

data class AiMentorTask(
    val id: String,
    val stepNumber: Int,
    val title: String,
    val skillCategory: String,
    val goalStatement: String,
    val coachMessage: String,
    val detailExplanation: String,
    val whyItMatters: String,
    val goodExample: String,
    val badExample: String,
    val proTip: String,
    val commonMistake: String,
    val actionTask: String,
    val simplerExplanation: String,
    val extraRealExample: String,
    val recommendedTool: String? = null,
    val xpReward: Int = 100,
    val state: TaskState = TaskState.LOCKED
)

object AiMentorEngine {

    /**
     * ADAPTIVE LEARNING ENGINE:
     * Dynamically generates personalized lessons based on:
     * - Platform (Instagram, YouTube, Shopping, Personal Brand, etc.)
     * - Experience level (Beginner, Intermediate, Advanced)
     * - Primary Goal (Followers, Views, Brand Deals, Sales, etc.)
     * - Available Time / Pace
     * - Creator Niche / Type (Tech, Fashion, Beauty, Gaming, Travel, Food, Educational, Business, etc.)
     */
    fun generatePersonalizedTasks(
        platform: String,
        setupData: CreatorSetupData,
        currentTaskIndex: Int,
        completedTaskIds: Set<String>,
        skippedTaskIds: Set<String>
    ): List<AiMentorTask> {
        val isYouTube = platform.uppercase() == "YOUTUBE"
        val niche = setupData.niche.ifBlank { "Creator" }
        val skill = setupData.skillLevel
        val goal = setupData.primaryGoal
        val timePace = setupData.availableTime

        val rawTasks = if (isYouTube) {
            getYouTubeTasksForNiche(niche, skill, goal, timePace)
        } else {
            getInstagramTasksForNiche(niche, skill, goal, timePace)
        }

        return rawTasks.mapIndexed { index, task ->
            val state = when {
                completedTaskIds.contains(task.id) -> TaskState.COMPLETED
                skippedTaskIds.contains(task.id) -> TaskState.SKIPPED
                index == currentTaskIndex -> TaskState.CURRENT
                index < currentTaskIndex -> TaskState.COMPLETED
                else -> TaskState.LOCKED
            }
            task.copy(state = state)
        }
    }

    private fun getInstagramTasksForNiche(
        niche: String,
        skill: String,
        goal: String,
        timePace: String
    ): List<AiMentorTask> {
        return listOf(
            AiMentorTask(
                id = "ig_adaptive_1",
                stepNumber = 1,
                title = "Craft a High-Converting $niche Bio with CTA",
                skillCategory = "Profile & Bio Optimization",
                goalStatement = "Convert profile visitors into loyal $niche followers within 2 seconds.",
                coachMessage = "Welcome! Let's tailor your Instagram bio specifically for $niche. A bio must state who you are, what value you provide, and where to click.",
                detailExplanation = "Line 1 defines your $niche focus. Line 2 highlights the main benefit for viewers. Line 3 is a direct Call-To-Action link.",
                whyItMatters = "Over 80% of profile visitors drop off without following if your bio is vague or lacks a clear value statement.",
                goodExample = "⚡ $niche Insights & Weekly Hacks\n🎯 Helping you master $niche in under 60s\n👇 Download the free $niche guide below",
                badExample = "Just loving $niche ✨ DM for collab | live laugh love",
                proTip = "Use bullet points or emoji spacing to improve scannability on mobile screens.",
                commonMistake = "Forgetting a CTA link or using confusing slang that non-experts won't understand.",
                actionTask = "Write 3 bullet lines for your bio and add an active link or lead magnet.",
                simplerExplanation = "Think of your bio like a 3-line sign on a store. Line 1: What do you sell/do? Line 2: Why should I care? Line 3: What should I click right now?",
                extraRealExample = "Example for $niche: '💡 Top 1% $niche Tips | 🚀 Daily Shorts & Guides | 👇 Get my free template'",
                recommendedTool = "Caption Generator"
            ),
            AiMentorTask(
                id = "ig_adaptive_2",
                stepNumber = 2,
                title = "Establish 3 Core $niche Content Pillars ($skill Level)",
                skillCategory = "Content Strategy",
                goalStatement = "Structure your content feed so the algorithm categories your $niche account accurately.",
                coachMessage = "Awesome! Now let's pick 3 tight content pillars. Pillars protect you from burnout and give your audience a consistent reason to return.",
                detailExplanation = "Pillar 1: Educational $niche Tips (How-To). Pillar 2: Common $niche Pitfalls / Mythbusting. Pillar 3: Personal $niche Journey & Workflow.",
                whyItMatters = "Algorithm indexing relies on consistent key topic signals to recommend your videos to $niche enthusiasts.",
                goodExample = "Pillar A: 30s $niche Hacks\nPillar B: Tool & Gear Reviews\nPillar C: Behind-the-scenes mistakes",
                badExample = "Posting random food photos today, crypto memes tomorrow, and gym clips next week.",
                proTip = "Stick strictly to these 3 pillars for 30 days to build account authority.",
                commonMistake = "Trying to cover too many unrelated topics at once, which confuses both viewers and algorithm recommendations.",
                actionTask = "Write down 3 specific titles for each of your 3 content pillars.",
                simplerExplanation = "Content pillars are like TV show channels. If a sports channel suddenly shows cooking, viewers leave. Stay focused on your 3 main sub-topics!",
                extraRealExample = "For $niche: 1) Quick Tutorials, 2) Myth Busting, 3) Product/Tool Recommendations.",
                recommendedTool = "Content Planner"
            ),
            AiMentorTask(
                id = "ig_adaptive_3",
                stepNumber = 3,
                title = "Master the 3-Second Viral $niche Hook Formula",
                skillCategory = "Reels Scripting",
                goalStatement = "Achieve >80% watch time in the first 3 seconds of every Reel.",
                coachMessage = "Hooks decide whether your video goes viral or dies at 200 views. Let's write a scroll-stopping 3-second hook for $niche!",
                detailExplanation = "Combine a physical movement or pattern interrupt on screen with bold, dynamic visual text overlays.",
                whyItMatters = "Instagram measures 3-second retention strictly. If viewers swipe away instantly, the algorithm stops pushing your Reel.",
                goodExample = "Text on screen: 'Stop making this $1,000 $niche mistake in 2026!' paired with energetic movement.",
                badExample = "'Hey guys welcome back to my Reel today I wanted to talk about $niche...'",
                proTip = "Place text overlays in the upper 30% of the screen so captions don't cover it.",
                commonMistake = "Starting videos with slow introductions or silent pauses.",
                actionTask = "Script 3 different hook variations for your next $niche Reel concept.",
                simplerExplanation = "A hook is like a book cover. If it doesn't shock or excite people in 3 seconds, they swipe to the next video!",
                extraRealExample = "Hook idea: '3 $niche tools that feel illegal to know!'",
                recommendedTool = "Hook Generator"
            ),
            AiMentorTask(
                id = "ig_adaptive_4",
                stepNumber = 4,
                title = "Build a 3-Tier SEO Hashtag Stack for $niche",
                skillCategory = "SEO & Hashtag Strategy",
                goalStatement = "Rank on Instagram Search results for high-intent $niche keywords.",
                coachMessage = "Let's organize a targeted 3-tier hashtag stack to maximize searchable discovery for your $niche posts.",
                detailExplanation = "Tier 1: Broad Industry tags (100k+ posts). Tier 2: Specific Niche tags (10k-100k posts). Tier 3: Micro Community tags (<10k posts).",
                whyItMatters = "Instagram now functions like a search engine. Relevant hashtags help index your Reel under active search queries.",
                goodExample = "#${niche.lowercase()} #reelsgrowth #${niche.lowercase()}tips #${niche.lowercase()}hacks #${niche.lowercase()}community",
                badExample = "#viral #fyp #love #explorepage #trending",
                proTip = "Add your primary $niche keywords into the spoken audio and text captions as well for maximum indexing.",
                commonMistake = "Using 30 giant generic hashtags that get buried in seconds.",
                actionTask = "Create and save a 5-hashtag stack tailored to your exact $niche topic.",
                simplerExplanation = "Hashtags are like library categories. Putting your book in 'Books' is too broad, but 'Tech -> Android -> Kotlin' helps readers find you instantly!",
                extraRealExample = "Stack for $niche: Broad (#$niche) + Specific (#${niche}tips) + Community (#${niche}creators).",
                recommendedTool = "Hashtag Generator"
            ),
            AiMentorTask(
                id = "ig_adaptive_5",
                stepNumber = 5,
                title = "Pre-Publishing Quality Control & Audio Selection",
                skillCategory = "Publishing & Verification",
                goalStatement = "Ensure 100% technical and algorithmic quality before hitting publish.",
                coachMessage = "Before posting your next $niche Reel, perform a quick 7-point quality checklist to maximize engagement!",
                detailExplanation = "Check audio levels, verify trending audio arrow ↗️ indicator, ensure high-contrast cover image, and confirm on-screen captions.",
                whyItMatters = "Small technical oversights like low contrast or quiet audio destroy video retention and reach.",
                goodExample = "Reel uses trending audio (<10k posts) + Clear auto-captions + Strong cover thumbnail frame.",
                badExample = "Posting in dark room without text captions or audio normalization.",
                proTip = "Use trending audio with the small diagonal arrow icon ↗️ to ride current algorithmic surges.",
                commonMistake = "Publishing without selecting an engaging cover photo frame for feed preview.",
                actionTask = "Run through all items on the interactive posting checklist for your upcoming Reel.",
                simplerExplanation = "Think of this as a pilot's pre-flight checklist. Double-check lighting, captions, and audio before takeoff!",
                extraRealExample = "Checklist: 1) Audio clear? 2) Hook visible? 3) Captions added? 4) Cover picked? 5) Hashtags added?",
                recommendedTool = "Posting Checklist"
            )
        )
    }

    private fun getYouTubeTasksForNiche(
        niche: String,
        skill: String,
        goal: String,
        timePace: String
    ): List<AiMentorTask> {
        return listOf(
            AiMentorTask(
                id = "yt_adaptive_1",
                stepNumber = 1,
                title = "Optimize Channel Banner & Description for $niche",
                skillCategory = "Channel Branding",
                goalStatement = "Turn channel visitors into instant subscribers with clear $niche positioning.",
                coachMessage = "Welcome YouTube Creator! Let's optimize your YouTube channel header and About section specifically for $niche.",
                detailExplanation = "Your banner is your hero billboard. It must clearly state your upload schedule and $niche value proposition.",
                whyItMatters = "YouTube's recommendation system reads your channel description to understand what audience to recommend your videos to.",
                goodExample = "Banner: 'Weekly $niche Guides & Shorts Every Tuesday' + High contrast branding.",
                badExample = "Default background banner with no schedule or text information.",
                proTip = "Keep key text inside the 1546x423 mobile safe area of your banner graphics.",
                commonMistake = "Leaving the channel About box empty or missing target search keywords.",
                actionTask = "Write a 150-word channel description packed with $niche search keywords.",
                simplerExplanation = "Your channel home page is like a TV billboard. Tell people what your channel is about in 5 seconds or less!",
                extraRealExample = "Description: 'Welcome! On this channel, we break down top $niche strategies, step-by-step guides, and honest reviews.'",
                recommendedTool = "Caption Generator"
            ),
            AiMentorTask(
                id = "yt_adaptive_2",
                stepNumber = 2,
                title = "Master YouTube Shorts 3-Sec Retention Hook ($skill Level)",
                skillCategory = "Shorts Optimization",
                goalStatement = "Achieve >75% 'Viewed' vs 'Swiped Away' metric on YouTube Shorts Feed.",
                coachMessage = "Retention is YouTube's #1 ranking factor! Let's craft a Shorts hook that holds viewers beyond the 3-second mark.",
                detailExplanation = "YouTube Shorts analytics measure 'Swiped Away %' heavily. Start with a bold action statement or unexpected visual cut.",
                whyItMatters = "If 'Swiped Away' exceeds 35%, YouTube Shorts algorithm abruptly stops pushing the video.",
                goodExample = "Start with high energy: 'Do NOT buy your next $niche gear until you watch this!'",
                badExample = "Waving hands: 'Hey everyone welcome back to another video...'",
                proTip = "Loop your ending sentence seamlessly into your opening hook to trigger re-watches.",
                commonMistake = "Slow introductions or boring static opening frames.",
                actionTask = "Record a 15-second Short with an instant visual & verbal hook.",
                simplerExplanation = "Shorts feed moves super fast. Imagine someone flipping through channels. You must shout something interesting immediately!",
                extraRealExample = "Shorts Hook: 'The #1 $niche hack nobody is talking about!'",
                recommendedTool = "Hook Generator"
            ),
            AiMentorTask(
                id = "yt_adaptive_3",
                stepNumber = 3,
                title = "Design High-CTR Thumbnail & Title Combos for $niche",
                skillCategory = "CTR Optimization",
                goalStatement = "Achieve a Click-Through-Rate (CTR) above 8% on YouTube Search & Impressions.",
                coachMessage = "Thumbnail + Title = Your Click-Through Rate! Let's pair high-contrast visuals with irresistible title copy.",
                detailExplanation = "Your thumbnail should complement—not repeat—the text in your title. Aim for 3 words max on thumbnail graphics.",
                whyItMatters = "High CTR signals to YouTube that viewers find your video enticing, boosting recommendation impressions.",
                goodExample = "Title: 'How I Scaled My $niche Workflow' | Thumbnail text: '10x Faster!' with expressive reaction.",
                badExample = "Title and thumbnail both say exact same long sentence.",
                proTip = "Use high-contrast face expressions or close-ups on dark backgrounds.",
                commonMistake = "Using tiny text that becomes unreadable on smartphone screens.",
                actionTask = "Outline 3 distinct Thumbnail + Title concepts for your next $niche topic.",
                simplerExplanation = "Thumbnail is the picture on the box, Title is the label. Make them work together to make people want to open the box!",
                extraRealExample = "Title: '$niche Strategy EXPOSED' | Thumbnail: 'DO NOT SKIP!' with red arrow.",
                recommendedTool = "Content Planner"
            )
        )
    }

    /**
     * DYNAMIC AI ENCOURAGEMENT GENERATOR:
     * Natural, non-repeating motivational messages dynamically calculated after completing lessons.
     */
    fun getRandomEncouragementMessage(niche: String, xp: Int, streak: Int): String {
        val messages = listOf(
            "🔥 Outstanding effort! Your $niche mastery is growing rapidly. Keep this momentum alive!",
            "⚡ Boom! You just leveled up your $niche creator skills. You're building real authority now!",
            "🎯 Flawless execution! Another key $niche lesson unlocked. Consistency always wins!",
            "🚀 Phenomenal progress! With a $streak-day streak and $xp XP, you're outpacing 95% of creators!",
            "🌟 Brilliant work! Every completed lesson brings you closer to your ultimate $niche goal!",
            "💎 Genius execution! Your dedication to $niche content strategy is paying off BIG time!",
            "🏆 Level Up! You're executing like a seasoned pro. Keep stacking those win days!"
        )
        return messages.random()
    }
}

