package com.example.creatoracademy

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 15D — Brand Collaboration AI Mentor
 * Pure educational mentorship for brand deals, pitching, pricing, contracts,
 * practice mode, and future-ready tool integrations.
 */

data class BrandCollabOnboardingData(
    val experience: String = "Never", // Never, 1–5 Collaborations, 5–20, 20+
    val followers: String = "1k-10k",
    val platform: String = "Instagram", // Instagram, YouTube, Both
    val niche: String = "Technology"
)

data class BrandCollabChapter(
    val id: Int,
    val title: String,
    val summary: String,
    val detailExplanation: String,
    val whyItMatters: String,
    val realExample: String,
    val commonMistakes: String,
    val practiceTask: String,
    val simplerHelp: String
)

object BrandCollabData {
    val chapters = listOf(
        BrandCollabChapter(
            id = 1,
            title = "Chapter 1: What is Brand Collaboration?",
            summary = "Understanding sponsorship, gifted collabs, and paid partnerships.",
            detailExplanation = "A brand collaboration is a business arrangement where a creator promotes a brand's product or service in exchange for monetary compensation, free products, or affiliate commissions.",
            whyItMatters = "Brands partner with creators because creator content delivers 4x higher trust and engagement than traditional banner ads.",
            realExample = "Gifted Collab: Receiving a $200 microphone in exchange for an honest Reel review. Paid Collab: Receiving $500 flat fee for a 30-second integrated YouTube Short.",
            commonMistakes = "Thinking only creators with 100k+ followers can get brand deals. Micro-creators (2k-10k followers) often have higher engagement rates!",
            practiceTask = "Identify 3 brands in your niche that currently sponsor micro-creators.",
            simplerHelp = "Brand collaboration is simple: you create great content that shows a product to your audience, and the company pays or gifts you for your time."
        ),
        BrandCollabChapter(
            id = 2,
            title = "Chapter 2: How Brands Choose Creators",
            summary = "Target audience alignment, engagement rate, and visual quality.",
            detailExplanation = "Brands prioritize 3 key factors: Audience Demographics (country/age match), Engagement Rate (likes/comments vs followers), and Content Quality.",
            whyItMatters = "A creator with 5,000 active, highly engaged followers in a specific niche is worth more to a brand than an account with 50,000 passive followers.",
            realExample = "A tech brand chooses a creator with 4,000 followers because 80% of their audience actively asks for gadget recommendations in comments.",
            commonMistakes = "Buying fake followers or engagement. Brands run automated audit tools (HypeAuditor, Modash) that flag artificial metrics instantly.",
            practiceTask = "Calculate your engagement rate: (Average Likes + Comments per post) / Total Followers * 100.",
            simplerHelp = "Brands don't just count followers; they want to see that people actually care, comment, and trust your recommendations!"
        ),
        BrandCollabChapter(
            id = 3,
            title = "Chapter 3: When Should You Pitch?",
            summary = "Readiness signals, audience size, and timing your outreach.",
            detailExplanation = "You are ready to pitch as soon as you have a clear niche, at least 15-20 high-quality posts, and a consistent posting frequency.",
            whyItMatters = "Waiting for brands to find you in DMs takes months. Proactive pitching puts you directly in front of Marketing Managers.",
            realExample = "Pitching a new productivity app right before back-to-school season or Q4 holiday shopping peaks.",
            commonMistakes = "Pitching with an incomplete profile, missing bio contact info, or irregular posting history.",
            practiceTask = "Ensure your Instagram/YouTube profile has a professional contact email listed in the bio.",
            simplerHelp = "Don't wait for brands to email you! If you have consistent content and a clear topic, you can start pitching today."
        ),
        BrandCollabChapter(
            id = 4,
            title = "Chapter 4: Media Kit Essentials",
            summary = "Creating a 1-page digital press kit that closes deals.",
            detailExplanation = "A Media Kit is a creator's resume. It includes your Bio, Audience Demographics (Age, Top Countries, Gender), Recent Engagement Stats, and Case Studies.",
            whyItMatters = "A clean PDF or digital Media Kit makes you look professional and speeds up brand budget approvals.",
            realExample = "Media Kit showing: 10k Followers, 6.2% Engagement, 65% US Audience, past results with 2 sample brands.",
            commonMistakes = "Creating a cluttered 10-page document. Keep your Media Kit to 1 concise, beautifully designed page.",
            practiceTask = "Draft a 3-bullet list of your audience demographics from your platform analytics dashboard.",
            simplerHelp = "A Media Kit is your 1-page creator resume. It shows brands who watches your content and why they should hire you!"
        ),
        BrandCollabChapter(
            id = 5,
            title = "Chapter 5: Portfolio & Case Studies",
            summary = "Showcasing past work and content samples effectively.",
            detailExplanation = "A portfolio displays your best video clips, lighting quality, and past sponsorship deliverables so brand managers can visualize their product in your content.",
            whyItMatters = "Brand managers need proof that you can deliver clean audio, crisp video, and clear messaging.",
            realExample = "Embedding 3 unboxing video links in your pitch email showing professional lighting and voiceover.",
            commonMistakes = "Sending raw, unedited footage or broken links that require brand managers to request permissions.",
            practiceTask = "Select your top 3 highest-quality videos and save direct links to a dedicated portfolio folder.",
            simplerHelp = "Your portfolio is your show-and-tell. Show brands your 3 best videos so they can see how great their product will look!"
        ),
        BrandCollabChapter(
            id = 6,
            title = "Chapter 6: Negotiation & Pricing Factors",
            summary = "Calculating rates based on deliverables, usage rights, and scope.",
            detailExplanation = "Base your pricing on Deliverables (Reels, Shorts, Stories), Usage Rights (Is the brand running paid ads with your video?), and Exclusivity.",
            whyItMatters = "Undercharging leads to burnout, while overcharging without justification causes brands to walk away.",
            realExample = "Standard Reel = $300. Reel + 30-day Paid Ad Usage Rights = $300 + $200 (60% usage fee) = $500 total.",
            commonMistakes = "Giving away perpetual usage rights or paid ad whitelisting for free without extra fees.",
            practiceTask = "Calculate your baseline rate for 1 Reel based on your creation hours and production overhead.",
            simplerHelp = "Charge for your time, your editing skills, and the value of your audience access. Never sell your content rights forever for cheap!"
        ),
        BrandCollabChapter(
            id = 7,
            title = "Chapter 7: Contracts & Protect Your Rights",
            summary = "Understanding key contract clauses, revision caps, and payment terms.",
            detailExplanation = "Always review 4 key terms before signing: Payment Terms (Net 30/60), Revision Caps (Max 2 minor edits), Exclusivity timeframe, and IP Ownership.",
            whyItMatters = "A clear contract prevents scope creep (brands requesting 10 re-shoots) and guarantees you get paid on time.",
            realExample = "Clause: 'Creator provides up to 2 rounds of edits. Additional edits requested by Brand billed at $50/hr.'",
            commonMistakes = "Starting work or publishing content before signing a written agreement or agreement letter.",
            practiceTask = "Review a sample contract checklist and highlight where payment terms and revision limits are specified.",
            simplerHelp = "Contracts are safety rules for both sides. They ensure you get paid on time and prevent the brand from asking for endless changes!"
        ),
        BrandCollabChapter(
            id = 8,
            title = "Chapter 8: Professional Communication & Outreach",
            summary = "Writing cold pitches that get opened and answered.",
            detailExplanation = "Structure cold pitches with: 1) Compliment/Hook (proof you know the brand), 2) Value Proposition (what you offer), 3) Social Proof, 4) Soft Call to Action.",
            whyItMatters = "Marketing directors receive 50+ pitch emails daily. Short, personalized, value-first emails get opened.",
            realExample = "Subject: Love the new [Product] launch + Quick Reel idea for [Brand]!",
            commonMistakes = "Sending generic 'Dear Brand, give me free stuff' mass spam messages.",
            practiceTask = "Draft a 100-word cold outreach email using the AI Pitch Coach template.",
            simplerHelp = "Keep your email short, friendly, and focused on how YOU can help THEM showcase their new product!"
        ),
        BrandCollabChapter(
            id = 9,
            title = "Chapter 9: Delivering Campaign Value & Analytics",
            summary = "Post-campaign reporting to secure recurring brand deals.",
            detailExplanation = "After publishing, send a post-campaign performance summary report within 7 days showing Impressions, Reach, Engagements, and Link Clicks.",
            whyItMatters = "Sending analytics reports turns one-off $300 sponsorships into $3,000 quarterly retainer contracts.",
            realExample = "Email report: 'Hi Sarah! Here are the stats for our Reel: 45k Reach, 3.2k Engagements, 420 Link Clicks. Audience loved the unboxing!'",
            commonMistakes = "Ghosting the brand manager as soon as you receive payment.",
            practiceTask = "Create a template for post-campaign reporting showing screenshots of insights.",
            simplerHelp = "After you post, send the brand manager a quick thank-you email with your view counts. They will love your professionalism!"
        ),
        BrandCollabChapter(
            id = 10,
            title = "Chapter 10: Building Long-Term Brand Retainers",
            summary = "Transitioning from single posts to multi-month ambassador retainers.",
            detailExplanation = "Propose package deals (e.g. 2 Reels/month for 3 months) at a slight bundle discount to secure predictable recurring income.",
            whyItMatters = "Retainers provide financial stability and eliminate the need to constantly cold pitch new brands every week.",
            realExample = "Single Reel = $400. 3-Month Package (6 Reels) = $2,100 ($350/Reel with guaranteed monthly exposure).",
            commonMistakes = "Not offering monthly package deals when a brand manager expresses happiness with initial results.",
            practiceTask = "Outline a 3-month retainer proposal structure for your top favorite brand.",
            simplerHelp = "Offer brands a multi-month deal. It gives you steady monthly income and gives them consistent exposure to your audience!"
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollaborationAiDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var onboardingDone by remember { mutableStateOf(false) }
    var onboardingData by remember { mutableStateOf(BrandCollabOnboardingData()) }

    var selectedChapterIndex by remember { mutableIntStateOf(0) }
    var showSmartHelp by remember { mutableStateOf(false) }
    var showExtraExample by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf("LESSONS") } // LESSONS, PITCH_COACH, PRACTICE, FUTURE_TOOLS

    val currentChapter = BrandCollabData.chapters.getOrElse(selectedChapterIndex) { BrandCollabData.chapters.first() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF12121E),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x2210B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Handshake,
                                contentDescription = "Brand Collab",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Brand Collaboration AI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Personal Sponsorship Mentor",
                                fontSize = 11.sp,
                                color = EmeraldPrimary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhite.copy(alpha = 0.6f),
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!onboardingDone) {
                    // ==================================================
                    // BRAND COLLAB ONBOARDING FLOW
                    // ==================================================
                    Text(
                        text = "🎯 Mentor Onboarding Setup",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Customize your brand deal mentorship strategy",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Have you worked with brands before?", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Never", "1–5 Collaborations", "5–20", "20+").forEach { opt ->
                            ChipOption(
                                label = opt,
                                isSelected = onboardingData.experience == opt,
                                onClick = { onboardingData = onboardingData.copy(experience = opt) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Current Followers / Subscribers:", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("<1k", "1k-10k", "10k-50k", "50k+").forEach { opt ->
                            ChipOption(
                                label = opt,
                                isSelected = onboardingData.followers == opt,
                                onClick = { onboardingData = onboardingData.copy(followers = opt) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Primary Platform:", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Instagram", "YouTube", "Both").forEach { opt ->
                            ChipOption(
                                label = opt,
                                isSelected = onboardingData.platform == opt,
                                onClick = { onboardingData = onboardingData.copy(platform = opt) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(EmeraldPrimary)
                            .clickable {
                                onboardingDone = true
                                Toast.makeText(context, "Brand Mentor Tailored!", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Start Brand Deal Mentorship 🚀",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmoledBlack
                        )
                    }
                } else {
                    // ==================================================
                    // MAIN BRAND COLLAB MENTOR DASHBOARD
                    // ==================================================

                    // Navigation Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x12FFFFFF))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TabButton("Lessons", activeTab == "LESSONS") { activeTab = "LESSONS" }
                        TabButton("Pitch Coach", activeTab == "PITCH_COACH") { activeTab = "PITCH_COACH" }
                        TabButton("Practice", activeTab == "PRACTICE") { activeTab = "PRACTICE" }
                        TabButton("Tools", activeTab == "FUTURE_TOOLS") { activeTab = "FUTURE_TOOLS" }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    when (activeTab) {
                        "LESSONS" -> {
                            // Chapter Dropdown Selector
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x0EFFFFFF))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Chapter ${currentChapter.id} of 10",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (selectedChapterIndex > 0) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0x22FFFFFF))
                                                .clickable { selectedChapterIndex--; showSmartHelp = false; showExtraExample = false }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(text = "Prev", fontSize = 10.sp, color = TextWhite)
                                        }
                                    }
                                    if (selectedChapterIndex < BrandCollabData.chapters.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(EmeraldPrimary)
                                                .clickable { selectedChapterIndex++; showSmartHelp = false; showExtraExample = false }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(text = "Next", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = currentChapter.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Chapter Details
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x10FFFFFF))
                                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(14.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(text = "📖 Explanation:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = currentChapter.detailExplanation, fontSize = 12.sp, color = TextWhite, lineHeight = 17.sp)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(text = "💡 Why It Matters:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = currentChapter.whyItMatters, fontSize = 12.sp, color = TextWhite.copy(alpha = 0.85f), lineHeight = 17.sp)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(text = "✨ Real Example:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = currentChapter.realExample, fontSize = 12.sp, color = TextWhite.copy(alpha = 0.85f), lineHeight = 17.sp)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(text = "⚠️ Common Mistake:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = currentChapter.commonMistakes, fontSize = 11.5.sp, color = TextWhite.copy(alpha = 0.8f))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Smart Help Section
                            if (showSmartHelp) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x2210B981))
                                        .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text(text = "🧠 Simplified Explanation:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = currentChapter.simplerHelp, fontSize = 12.sp, color = TextWhite)

                                        if (showExtraExample) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(text = "🎯 Extra Real Example:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "Imagine a brand gives you $500 for a 30s Reel. If you post it without a contract, they can use it in TV ads forever without paying you more!", fontSize = 11.5.sp, color = TextWhite)
                                        } else {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "Still confused? Give me another example",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldPrimary,
                                                modifier = Modifier.clickable { showExtraExample = true }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x18FFFFFF))
                                        .clickable { showSmartHelp = !showSmartHelp }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (showSmartHelp) "Hide Help" else "Need Help? 💡",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(EmeraldPrimary)
                                        .clickable {
                                            if (selectedChapterIndex < BrandCollabData.chapters.size - 1) {
                                                selectedChapterIndex++
                                                showSmartHelp = false
                                                showExtraExample = false
                                                Toast.makeText(context, "Chapter Completed! +100 XP", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "🎉 You Completed Brand Collab Mentor Academy!", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Complete Task ✅",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmoledBlack
                                    )
                                }
                            }
                        }

                        "PITCH_COACH" -> {
                            AiPitchCoachSection(onboardingData)
                        }

                        "PRACTICE" -> {
                            BrandPitchPracticeSection()
                        }

                        "FUTURE_TOOLS" -> {
                            FutureReadyBrandToolsSection()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AiPitchCoachSection(data: BrandCollabOnboardingData) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var selectedChannel by remember { mutableStateOf("EMAIL") } // EMAIL, INSTAGRAM_DM, LINKEDIN, FOLLOW_UP

    val pitchText = when (selectedChannel) {
        "INSTAGRAM_DM" -> "Hi [Brand Name] team! 👋 I've been creating ${data.niche} content on ${data.platform} for my ${data.followers} followers.\n\n" +
                "I recently used your [Product Name] and love it! I'd love to put together a dedicated Reel showcasing a unique tutorial.\n\n" +
                "Could I send over my 1-page Media Kit & proposal?"

        "LINKEDIN" -> "Hi [Name], hope you're having a great week!\n\n" +
                "I lead a growing ${data.niche} content channel on ${data.platform} (${data.followers} followers with strong US/UK demographics).\n\n" +
                "I'm reaching out to explore potential sponsored content or product review partnerships for Q3.\n\n" +
                "Would you be open to a quick 5-minute chat or reviewing our Media Kit?"

        "FOLLOW_UP" -> "Hi [Name],\n\n" +
                "Following up on my note below regarding a potential ${data.niche} collaboration on ${data.platform}.\n\n" +
                "We recently wrapped up a partnership that achieved a 6.8% engagement rate, and I'd love to share similar results with [Brand Name].\n\n" +
                "Let me know if you have a moment this week to review our Media Kit!\n\n" +
                "Best,\n[Your Name]"

        else -> "Subject: Partnership Proposal: ${data.niche} Creator x [Brand Name]\n\n" +
                "Hi [Marketing Manager Name],\n\n" +
                "My name is [Your Name], a digital creator in the ${data.niche} space on ${data.platform} (${data.followers} engaged followers).\n\n" +
                "I've been a huge fan of [Brand Name] and would love to collaborate on a dedicated video showcasing how [Product] solves [Key Audience Problem].\n\n" +
                "Attached is my Media Kit outlining our audience demographics and past campaign results.\n\n" +
                "Are you open to reviewing a quick 3-point content proposal?\n\n" +
                "Best regards,\n[Your Name]"
    }

    Column {
        Text(text = "✉️ AI Pitch Coach Templates", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Text(text = "Professional outreach templates tailored for your niche", fontSize = 11.sp, color = TextWhite.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("EMAIL", "INSTAGRAM_DM", "LINKEDIN", "FOLLOW_UP").forEach { ch ->
                ChipOption(
                    label = ch.replace("_", " "),
                    isSelected = selectedChannel == ch,
                    onClick = { selectedChannel = ch }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x10FFFFFF))
                .padding(12.dp)
        ) {
            Column {
                Text(text = pitchText, fontSize = 11.5.sp, color = TextWhite, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmeraldPrimary)
                        .clickable {
                            clipboardManager.setText(AnnotatedString(pitchText))
                            Toast.makeText(context, "Pitch Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = AmoledBlack, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Copy Pitch Template", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                }
            }
        }
    }
}

@Composable
private fun BrandPitchPracticeSection() {
    var userPitchInput by remember { mutableStateOf("") }
    var aiFeedback by remember { mutableStateOf<String?>(null) }

    Column {
        Text(text = "🎙️ AI Pitch Practice Mode", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Text(text = "Type or paste your brand pitch to receive AI feedback on Tone, Grammar, & Professionalism", fontSize = 11.sp, color = TextWhite.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = userPitchInput,
            onValueChange = { userPitchInput = it },
            placeholder = { Text("Paste your pitch email or DM here...", fontSize = 11.5.sp, color = TextWhite.copy(alpha = 0.4f)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = Color(0x33FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(EmeraldPrimary)
                .clickable {
                    if (userPitchInput.isBlank()) {
                        aiFeedback = "💡 Feedback: Pitch is empty! Write a 2-sentence intro introducing your niche and asking for a Media Kit review."
                    } else {
                        aiFeedback = "✅ Professionalism: 9/10\n" +
                                "✅ Tone: Enthusiastic & Value-Focused\n" +
                                "💡 Suggestion: Add a direct Call-To-Action asking if they are the right contact person for creator partnerships!"
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Analyze My Pitch ✨", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
        }

        if (aiFeedback != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x2210B981))
                    .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(text = aiFeedback!!, fontSize = 11.5.sp, color = TextWhite, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
private fun FutureReadyBrandToolsSection() {
    val context = LocalContext.current

    Column {
        Text(text = "🛠️ Future-Ready Brand Tool Suite", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Text(text = "Architecture prepared for next-gen automation modules", fontSize = 11.sp, color = TextWhite.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(12.dp))

        val tools = listOf(
            Triple("Media Kit Generator", "Auto-build 1-page PDF press kits", Icons.Default.Work),
            Triple("Proposal Generator", "Generate customized brand campaign proposals", Icons.Default.Send),
            Triple("Invoice Generator", "Create Net-30 compliant sponsorship invoices", Icons.Default.MonetizationOn),
            Triple("Contract Assistant", "Review contract clauses for red flags", Icons.Default.Shield)
        )

        tools.forEach { (name, desc, icon) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x10FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(12.dp))
                    .clickable {
                        Toast.makeText(context, "$name module architecture ready for full API sync!", Toast.LENGTH_SHORT).show()
                    }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0x2210B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = name, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = name, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = desc, fontSize = 10.5.sp, color = TextWhite.copy(alpha = 0.6f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipOption(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) EmeraldPrimary else Color(0x18FFFFFF))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) AmoledBlack else TextWhite
        )
    }
}

@Composable
private fun TabButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) EmeraldPrimary else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) AmoledBlack else TextWhite.copy(alpha = 0.7f)
        )
    }
}
