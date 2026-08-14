package com.example.engine

data class AudioTrackItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String = "Background Track",
    val category: String = "Music",
    val durationSec: Double = 30.0,
    val volume: Float = 1.0f,
    val fadeInSec: Float = 0.5f,
    val fadeOutSec: Float = 0.5f,
    val balance: Float = 0.0f,
    val speed: Float = 1.0f,
    val pitchSemitones: Float = 0f,
    val pitchLock: Boolean = true,
    val isMuted: Boolean = false,
    val isNormalized: Boolean = true,
    val bassDb: Float = 0f,
    val trebleDb: Float = 0f,
    val echoLevel: Float = 0f,
    val isLimiterEnabled: Boolean = false,
    val voiceEnhanceEnabled: Boolean = false,
    val voiceEffect: String = "None",
    val noiseReductionEnabled: Boolean = false,
    val noiseReductionLevel: Float = 0.5f,
    val startSec: Double = 0.0
)

data class TextTrackItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String = "Sample Text",
    val startSec: Double = 0.0,
    val durationSec: Double = 5.0,
    val textColorHex: String = "#FFFFFF",
    val styleName: String = "Default",
    val fontSizeSp: Float = 24f,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val alignment: String = "Center",
    val opacity: Float = 1.0f,
    val letterSpacingSp: Float = 0f,
    val lineHeightSp: Float = 28f,
    val strokeColorHex: String = "#000000",
    val strokeWidthDp: Float = 0f,
    val bgColorHex: String = "Transparent",
    val bgRadiusDp: Float = 0f,
    val shadowColorHex: String = "#000000",
    val shadowBlurDp: Float = 0f,
    val glowColorHex: String = "#000000",
    val glowRadiusDp: Float = 0f,
    val isGradient: Boolean = false,
    val gradientSecondaryHex: String = "#FF0000",
    val entryAnimation: String = "Fade",
    val exitAnimation: String = "Fade",
    val loopAnimation: String = "None",
    val categoryType: String = "Title",
    val positionX: Float = 0.5f,
    val positionY: Float = 0.5f
)

data class StickerTrackItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "Sticker",
    val category: String = "General",
    val startSec: Double = 0.0,
    val durationSec: Double = 5.0,
    val positionX: Float = 0.5f,
    val positionY: Float = 0.5f,
    val stickerEmoji: String = "⭐",
    val label: String = "Sticker"
)

data class EffectTrackItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "Effect",
    val startSec: Double = 0.0,
    val durationSec: Double = 5.0,
    val effectType: String = "general",
    val colorHex: String = "#10B981"
)

data class DrawingTrackItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "Drawing",
    val startSec: Double = 0.0,
    val durationSec: Double = 5.0,
    val strokeColorHex: String = "#FFFFFF",
    val strokeWidthDp: Float = 4f,
    val toolType: String = "Pen",
    val points: List<Pair<Float, Float>> = emptyList()
)

data class Anchor(
    val index: Int = 0,
    val id: String = ""
)
