package com.example.creatoracademy

import com.example.reports.ReportLanguage

enum class ContentTypeMatchStatus {
    MATCHED,
    PARTIALLY_MATCHED,
    NOT_MATCHED,
    UNCERTAIN
}

data class DetectedContentTypeEvidence(
    val typeId: String,
    val displayName: String,
    val confidencePercent: Int,
    val matchStatus: ContentTypeMatchStatus,
    val evidenceText: String,
    val timestamps: String? = null
)

data class ContentTypeVerificationResult(
    val userSelectedTypes: List<String>,
    val actualDetectedTypes: List<DetectedContentTypeEvidence>,
    val overallMatchStatus: ContentTypeMatchStatus,
    val summaryExplanation: String
)

object ContentTypeVerificationEngine {

    fun verifyContentTypes(
        userSelectedTypes: List<String>,
        context: UniversalDetectionContext?,
        language: ReportLanguage = ReportLanguage.ENGLISH
    ): ContentTypeVerificationResult {
        if (context == null) {
            return ContentTypeVerificationResult(
                userSelectedTypes = userSelectedTypes,
                actualDetectedTypes = emptyList(),
                overallMatchStatus = ContentTypeMatchStatus.UNCERTAIN,
                summaryExplanation = getUncertainSummary(language)
            )
        }

        // Analyze evidence from all detectors in UniversalDetectionContext
        val human = context.human
        val scene = context.scene
        val product = context.product
        val objects = context.objects
        val audio = context.audio
        val speech = context.speech
        val ocr = context.ocr
        val editing = context.editing

        val detectedEvidences = mutableListOf<DetectedContentTypeEvidence>()

        // Analyze TALKING HEAD
        val isTalkingHeadFacing = human.headAngle == "Direct Facing" || human.headAngle == "Slight Angle"
        val hasFaceAndSpeech = (human.faceType != FaceDetectionType.NO_FACE) && speech.hasSpeech
        if (hasFaceAndSpeech && isTalkingHeadFacing) {
            val conf = ((human.faceVisibilityPercent + speech.speechConfidence) / 2).coerceIn(60, 98)
            val text = when (language) {
                ReportLanguage.HINDI -> "चेहरा स्पष्ट रूप से दिखाई दे रहा है और स्पीच डिटेक्ट हुई है"
                ReportLanguage.HINGLISH -> "Face clearly visible + speech detected"
                else -> "Face clearly visible + direct speech detected"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "talking_head",
                    displayName = "Talking Head",
                    confidencePercent = conf,
                    matchStatus = ContentTypeMatchStatus.MATCHED,
                    evidenceText = text,
                    timestamps = "00:00.4–00:${context.durationSeconds.toInt().coerceAtLeast(5)}.0"
                )
            )
        }

        // Analyze DANCE
        val isDancingPosture = human.bodyPosture.equals("Dancing", ignoreCase = true)
        val hasRhythmicMotion = context.timestampedObservations.any { 
            it.observation.contains("dance", ignoreCase = true) || 
            it.observation.contains("rhythm", ignoreCase = true) || 
            it.observation.contains("movement intensity", ignoreCase = true) 
        } || (human.peopleCount >= 1 && audio.hasMusic && editing.editPacingScore > 65)

        if (isDancingPosture || hasRhythmicMotion) {
            val conf = if (isDancingPosture) 88 else 72
            val text = when (language) {
                ReportLanguage.HINDI -> "मानव शरीर की गति और लयबद्ध मूवमेंट की पुष्टि हुई है"
                ReportLanguage.HINGLISH -> "Rhythmic body movement & dance pose transitions verified"
                else -> "Rhythmic body movement & choreography transitions verified"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "dance",
                    displayName = "Dance",
                    confidencePercent = conf,
                    matchStatus = ContentTypeMatchStatus.MATCHED,
                    evidenceText = text,
                    timestamps = "00:01.0–00:${(context.durationSeconds * 0.8f).toInt().coerceAtLeast(3)}.0"
                )
            )
        } else {
            // Unconfirmed for Dance
            val text = when (language) {
                ReportLanguage.HINDI -> "डांस से संबंधित पर्याप्त लयबद्ध हलचल या मूवमेंट नहीं मिला"
                ReportLanguage.HINGLISH -> "Verified visual evidence does not support dance content"
                else -> "No verified rhythmic body movement or choreography detected"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "dance",
                    displayName = "Dance",
                    confidencePercent = 18,
                    matchStatus = ContentTypeMatchStatus.NOT_MATCHED,
                    evidenceText = text,
                    timestamps = null
                )
            )
        }

        // Analyze TRAVEL
        val isOutdoor = scene.environment.equals("Outdoor", ignoreCase = true) || scene.timeOfDay.contains("Day", ignoreCase = true)
        val hasTravelObjects = objects.detectedObjects.any { 
            it.contains("car", true) || it.contains("mountain", true) || it.contains("tree", true) || 
            it.contains("beach", true) || it.contains("building", true) || it.contains("landscape", true)
        }
        val isMultiSceneTravel = scene.sceneCount >= 3 && isOutdoor
        if (isMultiSceneTravel || (isOutdoor && hasTravelObjects)) {
            val conf = if (isMultiSceneTravel) 91 else 75
            val text = when (language) {
                ReportLanguage.HINDI -> "स्थान परिवर्तन और आउटडोर लोकेशन का प्रमाण मिला है"
                ReportLanguage.HINGLISH -> "Outdoor environment changes & travel landmarks verified"
                else -> "Outdoor location progression & environmental shift verified"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "travel",
                    displayName = "Travel",
                    confidencePercent = conf,
                    matchStatus = ContentTypeMatchStatus.MATCHED,
                    evidenceText = text,
                    timestamps = "00:02.0–00:${context.durationSeconds.toInt().coerceAtLeast(5)}.0"
                )
            )
        } else {
            val text = when (language) {
                ReportLanguage.HINDI -> "ट्रैवल/आउटडोर लोकेशन या लैंडमार्क के प्रमाण नहीं मिले"
                ReportLanguage.HINGLISH -> "No travel landmarks or outdoor environment progression detected"
                else -> "No verified travel landmarks or location shifts detected"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "travel",
                    displayName = "Travel",
                    confidencePercent = 22,
                    matchStatus = ContentTypeMatchStatus.NOT_MATCHED,
                    evidenceText = text,
                    timestamps = null
                )
            )
        }

        // Analyze PRODUCT REVIEW
        if (product.productExists) {
            val conf = product.confidence.coerceIn(65, 96)
            val text = when (language) {
                ReportLanguage.HINDI -> "प्रोडक्ट स्पष्ट रूप से दिखाया और प्रदर्शित किया गया है"
                ReportLanguage.HINGLISH -> "Product visibly presented & demonstrated on screen"
                else -> "Product visibly presented & demonstrated on screen"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "product_review",
                    displayName = "Product Review",
                    confidencePercent = conf,
                    matchStatus = ContentTypeMatchStatus.MATCHED,
                    evidenceText = text,
                    timestamps = "00:00.8–00:${product.screenTimeSeconds.toInt().coerceAtLeast(4)}.0"
                )
            )
        } else {
            val text = when (language) {
                ReportLanguage.HINDI -> "स्क्रीन पर कोई स्पष्ट प्रोडक्ट प्रदर्शन नहीं मिला"
                ReportLanguage.HINGLISH -> "No product demonstration or visible product found"
                else -> "No product visibly presented or demonstrated on screen"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "product_review",
                    displayName = "Product Review",
                    confidencePercent = 15,
                    matchStatus = ContentTypeMatchStatus.NOT_MATCHED,
                    evidenceText = text,
                    timestamps = null
                )
            )
        }

        // Analyze FASHION / BEAUTY
        val hasFashionObjects = objects.detectedObjects.any { 
            it.contains("dress", true) || it.contains("shirt", true) || it.contains("outfit", true) || 
            it.contains("shoes", true) || it.contains("makeup", true) || it.contains("person", true)
        }
        val isFashionVisual = human.faceType != FaceDetectionType.NO_FACE && (hasFashionObjects || human.bodyPosture == "Standing")
        if (isFashionVisual) {
            val text = when (language) {
                ReportLanguage.HINDI -> "आउटफ़िट और पर्सन की विजिबिलिटी की पुष्टि हुई"
                ReportLanguage.HINGLISH -> "Person outfit & styling presentation verified"
                else -> "Outfit & styling demonstration verified on screen"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "fashion_beauty",
                    displayName = "Fashion & Beauty",
                    confidencePercent = 78,
                    matchStatus = ContentTypeMatchStatus.MATCHED,
                    evidenceText = text,
                    timestamps = "00:01.0–00:${context.durationSeconds.toInt().coerceAtLeast(4)}.0"
                )
            )
        }

        // Analyze VLOG
        if (scene.sceneCount >= 3) {
            val text = when (language) {
                ReportLanguage.HINDI -> "विभिन्न सीन और एक्टिविटी की पुष्टि हुई"
                ReportLanguage.HINGLISH -> "Multiple scenes & activity progression verified"
                else -> "Multiple scene transitions & activity narrative verified"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "vlog",
                    displayName = "Vlog",
                    confidencePercent = 82,
                    matchStatus = ContentTypeMatchStatus.MATCHED,
                    evidenceText = text,
                    timestamps = "00:00.0–00:${context.durationSeconds.toInt()}.0"
                )
            )
        }

        // Analyze GAMING
        val hasGamingUI = ocr.captionsDetected.any { it.contains("score", true) || it.contains("level", true) || it.contains("hp", true) }
        if (hasGamingUI) {
            val text = when (language) {
                ReportLanguage.HINDI -> "गेमिंग UI और HUD विजिबल है"
                ReportLanguage.HINGLISH -> "Gameplay screen & HUD UI verified"
                else -> "Gameplay screen & HUD UI detected"
            }
            detectedEvidences.add(
                DetectedContentTypeEvidence(
                    typeId = "gaming",
                    displayName = "Gaming",
                    confidencePercent = 89,
                    matchStatus = ContentTypeMatchStatus.MATCHED,
                    evidenceText = text,
                    timestamps = "00:00.0–00:${context.durationSeconds.toInt()}.0"
                )
            )
        }

        // If no user types selected
        if (userSelectedTypes.isEmpty()) {
            val primaryDetected = detectedEvidences.firstOrNull { it.matchStatus == ContentTypeMatchStatus.MATCHED }
            val summary = when (language) {
                ReportLanguage.HINDI -> "ऑटो-डिटेक्टेड टाइप: ${primaryDetected?.displayName ?: "जनरल कंटेंट"}"
                ReportLanguage.HINGLISH -> "Auto-detected content type: ${primaryDetected?.displayName ?: "General Content"}"
                else -> "Auto-detected primary content type: ${primaryDetected?.displayName ?: "General Content"}"
            }
            return ContentTypeVerificationResult(
                userSelectedTypes = emptyList(),
                actualDetectedTypes = detectedEvidences,
                overallMatchStatus = ContentTypeMatchStatus.MATCHED,
                summaryExplanation = summary
            )
        }

        // Compare user selections vs AI detections
        var matchedCount = 0
        var notMatchedCount = 0
        var uncertainCount = 0

        userSelectedTypes.forEach { selected ->
            val normSelected = selected.lowercase().trim()
            val match = detectedEvidences.find { 
                it.typeId.lowercase() == normSelected || 
                it.displayName.lowercase().contains(normSelected) ||
                normSelected.contains(it.typeId.lowercase())
            }

            if (match == null) {
                // Not in detection list -> evaluate if evidence exists or uncertain
                val text = when (language) {
                    ReportLanguage.HINDI -> "चुने गए $selected टाइप का पर्याप्त प्रमाण नहीं मिला"
                    ReportLanguage.HINGLISH -> "Not enough verified evidence to classify this as $selected"
                    else -> "Not enough verified evidence to classify this as $selected"
                }
                detectedEvidences.add(
                    DetectedContentTypeEvidence(
                        typeId = selected,
                        displayName = selected.replaceFirstChar { it.uppercase() },
                        confidencePercent = 30,
                        matchStatus = ContentTypeMatchStatus.UNCERTAIN,
                        evidenceText = text,
                        timestamps = null
                    )
                )
                uncertainCount++
            } else {
                when (match.matchStatus) {
                    ContentTypeMatchStatus.MATCHED -> matchedCount++
                    ContentTypeMatchStatus.NOT_MATCHED -> notMatchedCount++
                    ContentTypeMatchStatus.UNCERTAIN -> uncertainCount++
                    else -> {}
                }
            }
        }

        val overallStatus = when {
            matchedCount == userSelectedTypes.size -> ContentTypeMatchStatus.MATCHED
            matchedCount > 0 -> ContentTypeMatchStatus.PARTIALLY_MATCHED
            notMatchedCount > 0 -> ContentTypeMatchStatus.NOT_MATCHED
            else -> ContentTypeMatchStatus.UNCERTAIN
        }

        val summary = when (overallStatus) {
            ContentTypeMatchStatus.MATCHED -> when (language) {
                ReportLanguage.HINDI -> "चयनित कंटेंट टाइप वीडियो कंटेंट से पूरी तरह मेल खाता है।"
                ReportLanguage.HINGLISH -> "Selected content type matches verified video evidence."
                else -> "Selected content type fully matches detected video content."
            }
            ContentTypeMatchStatus.PARTIALLY_MATCHED -> when (language) {
                ReportLanguage.HINDI -> "चयनित कंटेंट टाइप आंशिक रूप से मेल खाता है।"
                ReportLanguage.HINGLISH -> "Selected types partially match detected video content."
                else -> "Selected content types partially match detected video evidence."
            }
            ContentTypeMatchStatus.NOT_MATCHED -> when (language) {
                ReportLanguage.HINDI -> "चयनित कंटेंट टाइप वीडियो कंटेंट से मेल नहीं खाता।"
                ReportLanguage.HINGLISH -> "Selected type doesn't match the detected video content."
                else -> "Selected type doesn't match the detected video content."
            }
            ContentTypeMatchStatus.UNCERTAIN -> when (language) {
                ReportLanguage.HINDI -> "वर्गीकृत करने के लिए पर्याप्त प्रमाण उपलब्ध नहीं है।"
                ReportLanguage.HINGLISH -> "Not enough verified evidence to confirm content type."
                else -> "Not enough verified evidence to confirm selected content type."
            }
        }

        return ContentTypeVerificationResult(
            userSelectedTypes = userSelectedTypes,
            actualDetectedTypes = detectedEvidences,
            overallMatchStatus = overallStatus,
            summaryExplanation = summary
        )
    }

    private fun getUncertainSummary(language: ReportLanguage): String = when (language) {
        ReportLanguage.HINDI -> "वीडियो का विश्लेषण जारी है या पर्याप्त डेटा नहीं है।"
        ReportLanguage.HINGLISH -> "Video analysis incomplete or insufficient data."
        else -> "Insufficient analysis data for content type verification."
    }
}
