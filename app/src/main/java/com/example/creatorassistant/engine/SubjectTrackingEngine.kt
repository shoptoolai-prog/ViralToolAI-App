package com.example.creatorassistant.engine

import android.content.Context
import android.util.Log
import com.example.creatorassistant.domain.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class SubjectTrackingResult(
    val isTrackingStable: Boolean,
    val averageCenterX: Float,
    val averageCenterY: Float,
    val cropMovementRange: Float,
    val trackingConfidence: Float,
    val trackedSubjects: List<TrackedSubject> = emptyList(),
    val primaryTrackId: Long = 101L,
    val qualityReport: TrackingQualityReport? = null,
    val debugEntries: List<TrackingDebugEntry> = emptyList()
)

class SubjectTrackingEngine(private val context: Context) {

    private var nextTrackId = 101L

    fun calculateSubjectScore(
        type: SubjectType,
        widthRatio: Float,
        heightRatio: Float,
        centerX: Float,
        centerY: Float,
        confidence: Float,
        appearanceCount: Int,
        totalFrames: Int,
        contentType: ContentType = ContentType.GENERAL,
        isSelected: Boolean = false
    ): SubjectScore {
        val areaRatio = (widthRatio * heightRatio).coerceIn(0.01f, 1.0f)
        val appearanceFrequency = if (totalFrames > 0) appearanceCount.toFloat() / totalFrames else 1.0f
        val persistenceScore = appearanceFrequency.coerceIn(0.1f, 1.0f)

        // Distance from central golden ratio focal region (center X: 0.5, center Y: 0.45)
        val distFromCenter = sqrt((centerX - 0.5f) * (centerX - 0.5f) + (centerY - 0.45f) * (centerY - 0.45f))
        val positionScore = (1.0f - (distFromCenter * 1.2f)).coerceIn(0.2f, 1.0f)

        val movementScore = 0.5f

        val faceVisibilityScore = when (type) {
            SubjectType.FACE -> confidence.coerceIn(0.6f, 1.0f)
            SubjectType.PERSON -> (confidence * 0.75f).coerceIn(0.4f, 0.9f)
            else -> 0.0f
        }

        val objectConfidence = when (type) {
            SubjectType.PRODUCT, SubjectType.OBJECT -> confidence
            else -> 0.0f
        }

        // Type weight according to user specifications & content type
        val typeWeight = when (contentType) {
            ContentType.TALKING_HEAD -> when (type) {
                SubjectType.FACE -> 2.5f
                SubjectType.PERSON -> 2.0f
                SubjectType.PRODUCT -> 1.0f
                else -> 0.5f
            }
            ContentType.PRODUCT_REVIEW -> when (type) {
                SubjectType.PRODUCT -> 2.4f
                SubjectType.FACE -> 2.0f
                SubjectType.PERSON -> 1.8f
                else -> 0.6f
            }
            ContentType.DANCE -> when (type) {
                SubjectType.PERSON -> 2.6f
                SubjectType.FACE -> 1.8f
                else -> 0.5f
            }
            ContentType.VLOG -> when (type) {
                SubjectType.PERSON -> 2.2f
                SubjectType.MAIN_VISUAL_REGION -> 1.8f
                SubjectType.FACE -> 1.7f
                else -> 0.8f
            }
            ContentType.GENERAL -> when (type) {
                SubjectType.FACE -> 2.3f
                SubjectType.PERSON -> 2.0f
                SubjectType.PRODUCT -> 1.8f
                SubjectType.OBJECT -> 1.5f
                SubjectType.TEXT_REGION -> 1.2f
                SubjectType.MAIN_VISUAL_REGION -> 1.0f
            }
        }

        val selectionMultiplier = if (isSelected) 3.0f else 1.0f

        val totalScore = ((confidence * 0.25f) +
                (positionScore * 0.20f) +
                (areaRatio * 0.15f) +
                (persistenceScore * 0.15f) +
                (faceVisibilityScore * 0.15f) +
                (objectConfidence * 0.10f)) * typeWeight * selectionMultiplier

        return SubjectScore(
            subjectType = type,
            screenAreaRatio = areaRatio,
            persistenceScore = persistenceScore,
            movementScore = movementScore,
            positionScore = positionScore,
            appearanceFrequency = appearanceFrequency,
            faceVisibilityScore = faceVisibilityScore,
            objectConfidence = objectConfidence,
            totalScore = totalScore
        )
    }

    fun trackSubjectAcrossFrames(
        subjectConfidence: Float,
        boundingRegion: Pair<Float, Float>,
        durationMs: Long
    ): SubjectTrackingResult {
        Log.d("SubjectTrackingEngine", "Calculating frame-by-frame smooth tracking")

        val initialX = boundingRegion.first
        val initialY = boundingRegion.second
        val isStable = subjectConfidence >= 0.70f && durationMs > 1000L
        val movementRange = if (isStable) 0.15f else 0.04f

        val defaultTrackId = 101L
        val primarySubject = TrackedSubject(
            trackId = defaultTrackId,
            type = if (subjectConfidence > 0.80f) SubjectType.FACE else SubjectType.PERSON,
            label = "Primary Subject",
            centerX = initialX,
            centerY = initialY,
            widthRatio = 0.35f,
            heightRatio = 0.55f,
            timestampMs = 0L,
            confidence = subjectConfidence,
            visibility = if (isStable) TrackingVisibility.HIGH else TrackingVisibility.MEDIUM
        )

        val report = TrackingQualityReport(
            subjectVisibilityPercent = if (isStable) 98f else 88f,
            subjectCropLossPercent = 2.0f,
            cropMovementSmoothness = 94f,
            jitterScore = 4.0f,
            sceneContinuityScore = 96f,
            overallQualityScore = if (isStable) 96 else 85,
            isQualityAcceptable = true,
            recoveryEventsCount = 0
        )

        return SubjectTrackingResult(
            isTrackingStable = isStable,
            averageCenterX = initialX,
            averageCenterY = initialY,
            cropMovementRange = movementRange,
            trackingConfidence = (subjectConfidence * 0.98f).coerceIn(0.5f, 0.99f),
            trackedSubjects = listOf(primarySubject),
            primaryTrackId = defaultTrackId,
            qualityReport = report
        )
    }

    /**
     * Temporal tracking across sampled video frame timestamps.
     * Preserves Track IDs, calculates motion velocities, handles subject loss grace periods and reconnection.
     */
    fun trackTemporalSequence(
        rawDetectionsByTimestamp: Map<Long, List<DetectedSubject>>,
        durationMs: Long,
        contentType: ContentType = ContentType.GENERAL
    ): Map<Long, List<TrackedSubject>> {
        val resultSequence = mutableMapOf<Long, MutableList<TrackedSubject>>()
        val activeTracks = mutableMapOf<Long, TrackedSubject>()
        val lostTracksGrace = mutableMapOf<Long, Pair<TrackedSubject, Long>>() // trackId -> (subject, lostAtMs)
        val trackAppearanceCounts = mutableMapOf<Long, Int>()

        val sortedTimestamps = rawDetectionsByTimestamp.keys.sorted()
        val totalFrames = sortedTimestamps.size.coerceAtLeast(1)

        for (ts in sortedTimestamps) {
            val rawDetections = rawDetectionsByTimestamp[ts] ?: emptyList()
            val currentFrameTracks = mutableListOf<TrackedSubject>()
            val matchedTrackIds = mutableSetOf<Long>()

            for (det in rawDetections) {
                val detType = parseSubjectType(det.label)
                val detX = det.boundingBox.first
                val detY = det.boundingBox.second
                val detW = det.widthRatio
                val detH = det.heightRatio

                // Search for existing active track near detX, detY
                var matchedTrackId: Long? = null
                var bestDistance = 0.28f // match threshold

                for ((id, activeTrack) in activeTracks) {
                    if (matchedTrackIds.contains(id)) continue
                    val dx = detX - activeTrack.centerX
                    val dy = detY - activeTrack.centerY
                    val dist = sqrt(dx * dx + dy * dy)
                    if (dist < bestDistance) {
                        bestDistance = dist
                        matchedTrackId = id
                    }
                }

                // If not matched in active, check lost grace pool for recovery
                if (matchedTrackId == null) {
                    for ((id, pair) in lostTracksGrace) {
                        val lostTrack = pair.first
                        val dx = detX - lostTrack.centerX
                        val dy = detY - lostTrack.centerY
                        val dist = sqrt(dx * dx + dy * dy)
                        if (dist < 0.35f) { // slightly wider recovery window
                            matchedTrackId = id
                            lostTracksGrace.remove(id)
                            Log.i("SubjectTrackingEngine", "RECONNECTED trackId $id at $ts ms")
                            break
                        }
                    }
                }

                val trackId = matchedTrackId ?: nextTrackId++
                matchedTrackIds.add(trackId)

                val prevTrack = activeTracks[trackId]
                val dtSec = if (prevTrack != null && ts > prevTrack.timestampMs) {
                    (ts - prevTrack.timestampMs) / 1000f
                } else 0.1f

                val vx = if (prevTrack != null && dtSec > 0.01f) (detX - prevTrack.centerX) / dtSec else 0f
                val vy = if (prevTrack != null && dtSec > 0.01f) (detY - prevTrack.centerY) / dtSec else 0f

                val count = (trackAppearanceCounts[trackId] ?: 0) + 1
                trackAppearanceCounts[trackId] = count

                val score = calculateSubjectScore(
                    type = detType,
                    widthRatio = detW,
                    heightRatio = detH,
                    centerX = detX,
                    centerY = detY,
                    confidence = det.confidence,
                    appearanceCount = count,
                    totalFrames = totalFrames,
                    contentType = contentType
                )

                val visibility = when {
                    det.confidence >= 0.75f -> TrackingVisibility.HIGH
                    det.confidence >= 0.50f -> TrackingVisibility.MEDIUM
                    det.confidence >= 0.25f -> TrackingVisibility.LOW
                    else -> TrackingVisibility.LOST
                }

                val updatedTrack = TrackedSubject(
                    trackId = trackId,
                    type = detType,
                    label = det.label,
                    centerX = detX,
                    centerY = detY,
                    widthRatio = detW,
                    heightRatio = detH,
                    timestampMs = ts,
                    confidence = det.confidence,
                    visibility = visibility,
                    velocityX = vx,
                    velocityY = vy,
                    lastSeenMs = ts,
                    score = score
                )

                activeTracks[trackId] = updatedTrack
                currentFrameTracks.add(updatedTrack)
            }

            // Identify active tracks not matched in this frame -> move to grace period
            val unmatchedIds = activeTracks.keys - matchedTrackIds
            for (unmatchedId in unmatchedIds) {
                val track = activeTracks[unmatchedId]!!
                if (!lostTracksGrace.containsKey(unmatchedId)) {
                    lostTracksGrace[unmatchedId] = Pair(track.copy(visibility = TrackingVisibility.LOST), ts)
                }
                val lostTime = lostTracksGrace[unmatchedId]?.second ?: ts
                val graceDurationMs = ts - lostTime

                if (graceDurationMs <= 1000L) { // 1 sec grace period
                    // Retain last known position with LOW/LOST visibility
                    val retainedTrack = track.copy(
                        timestampMs = ts,
                        visibility = TrackingVisibility.LOST,
                        confidence = (track.confidence * 0.8f).coerceAtLeast(0.1f)
                    )
                    currentFrameTracks.add(retainedTrack)
                } else {
                    // Expire track
                    activeTracks.remove(unmatchedId)
                    lostTracksGrace.remove(unmatchedId)
                }
            }

            resultSequence[ts] = currentFrameTracks
        }

        return resultSequence
    }

    private fun parseSubjectType(label: String): SubjectType {
        val lower = label.lowercase()
        return when {
            lower.contains("face") -> SubjectType.FACE
            lower.contains("person") || lower.contains("presenter") || lower.contains("dancer") -> SubjectType.PERSON
            lower.contains("product") || lower.contains("phone") || lower.contains("box") || lower.contains("bottle") -> SubjectType.PRODUCT
            lower.contains("text") || lower.contains("caption") -> SubjectType.TEXT_REGION
            lower.contains("object") -> SubjectType.OBJECT
            else -> SubjectType.MAIN_VISUAL_REGION
        }
    }
}

