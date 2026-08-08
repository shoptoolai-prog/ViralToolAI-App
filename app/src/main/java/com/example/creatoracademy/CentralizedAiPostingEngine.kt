package com.example.creatoracademy

data class PostingWindowInfo(
    val platformName: String,
    val primaryWindow: String,
    val secondaryWindow: String,
    val avoidWindow: String,
    val competitionLevel: String, // "Low", "Medium", "High"
    val audienceActivityPct: Int, // e.g. 84
    val confidencePct: Int, // e.g. 96
    val reasoningText: String
)

object CentralizedAiPostingEngine {

    val SUPPORTED_PLATFORMS = listOf(
        "Instagram",
        "YouTube Shorts",
        "Facebook",
        "TikTok",
        "Meesho",
        "Amazon",
        "Flipkart"
    )

    fun getPostingWindow(category: String, platform: String): PostingWindowInfo {
        val cat = category.lowercase()
        val plat = platform.lowercase()

        val primary: String
        val secondary: String
        val avoid = "1:30 AM – 5:30 AM"
        val competition: String
        val activityPct: Int
        val confidencePct: Int
        val reason: String

        when {
            cat.contains("fashion") || cat.contains("beauty") || cat.contains("skincare") -> {
                primary = if (plat.contains("meesho") || plat.contains("amazon") || plat.contains("flipkart")) "8:00 PM – 9:30 PM" else "6:30 PM – 8:00 PM"
                secondary = "11:30 AM – 1:00 PM"
                competition = "High"
                activityPct = 86
                confidencePct = 95
                reason = "Fashion & beauty reels peak during early evening post-work browsing, yielding up to 24% higher organic impression velocity on $platform."
            }
            cat.contains("food") -> {
                primary = "1:00 PM – 2:30 PM"
                secondary = "7:30 PM – 9:00 PM"
                competition = "Medium"
                activityPct = 82
                confidencePct = 94
                reason = "Food & culinary content experiences peak lunch and dinner impulse viewing on $platform."
            }
            cat.contains("motivation") || cat.contains("education") || cat.contains("tutorial") -> {
                primary = if (cat.contains("motivation")) "7:00 AM – 8:30 AM" else "8:00 PM – 9:30 PM"
                secondary = "12:15 PM – 1:15 PM"
                competition = "Medium"
                activityPct = 88
                confidencePct = 96
                reason = "Educational & motivational content captures highest save-rate during morning start or post-dinner learning sessions on $platform."
            }
            cat.contains("gaming") || cat.contains("meme") -> {
                primary = "9:30 PM – 11:30 PM"
                secondary = "4:00 PM – 5:30 PM"
                competition = "High"
                activityPct = 91
                confidencePct = 93
                reason = "Gaming & meme shorts achieve late-night binge engagement on $platform."
            }
            cat.contains("product") || cat.contains("unboxing") || cat.contains("affiliate") -> {
                primary = "7:30 PM – 9:00 PM"
                secondary = "12:30 PM – 1:45 PM"
                competition = "Medium"
                activityPct = 85
                confidencePct = 96
                reason = "E-commerce product reviews & unboxings convert highest during prime evening shopping hours on $platform."
            }
            cat.contains("talking") || cat.contains("face") || cat.contains("podcast") || cat.contains("storytelling") -> {
                primary = "7:00 PM – 8:30 PM"
                secondary = "12:00 PM – 1:30 PM"
                competition = "Medium"
                activityPct = 84
                confidencePct = 95
                reason = "Talking head and storytelling reels capture maximum focused attention during relaxed evening slots on $platform."
            }
            cat.contains("cinematic") || cat.contains("travel") || cat.contains("vlog") || cat.contains("lifestyle") -> {
                primary = "5:30 PM – 7:00 PM"
                secondary = "11:00 AM – 12:30 PM"
                competition = "Low"
                activityPct = 83
                confidencePct = 93
                reason = "Cinematic travel & lifestyle content thrives during evening leisure hours with high save & share intent on $platform."
            }
            else -> {
                primary = "6:00 PM – 7:30 PM"
                secondary = "12:00 PM – 1:30 PM"
                competition = "Medium"
                activityPct = 80
                confidencePct = 92
                reason = "Optimal general audience activity slot for $category content on $platform."
            }
        }

        return PostingWindowInfo(
            platformName = platform,
            primaryWindow = primary,
            secondaryWindow = secondary,
            avoidWindow = avoid,
            competitionLevel = competition,
            audienceActivityPct = activityPct,
            confidencePct = confidencePct,
            reasoningText = reason
        )
    }
}
