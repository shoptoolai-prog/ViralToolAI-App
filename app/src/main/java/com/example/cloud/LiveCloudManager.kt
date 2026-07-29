package com.example.cloud

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

/**
 * Live Cloud Management System (PRE-PUBLISH)
 * Handles Remote Config, Firestore Collections, Analytics, Crashlytics, Storage, and Offline Fallbacks.
 */
object LiveCloudManager {

    private const val TAG = "LiveCloudManager"
    private const val PREFS_NAME = "shoptool_cloud_cache"

    private lateinit var prefs: SharedPreferences
    private val scope = CoroutineScope(Dispatchers.IO)

    private var isFirebaseInitialized = false
    private var remoteConfig: FirebaseRemoteConfig? = null
    private var firestore: FirebaseFirestore? = null
    private var analytics: FirebaseAnalytics? = null
    private var crashlytics: FirebaseCrashlytics? = null
    private var storage: FirebaseStorage? = null
    private var auth: FirebaseAuth? = null

    // State Flows for Remote Config
    private val _maintenanceMode = MutableStateFlow(false)
    val maintenanceMode: StateFlow<Boolean> = _maintenanceMode.asStateFlow()

    private val _forceUpdate = MutableStateFlow(false)
    val forceUpdate: StateFlow<Boolean> = _forceUpdate.asStateFlow()

    private val _minimumSupportedVersion = MutableStateFlow("1.0.0")
    val minimumSupportedVersion: StateFlow<String> = _minimumSupportedVersion.asStateFlow()

    private val _playstoreUrl = MutableStateFlow("https://play.google.com/store/apps/details?id=com.aistudio.shoptoolai.fpxwkr")
    val playstoreUrl: StateFlow<String> = _playstoreUrl.asStateFlow()

    private val _announcementConfig = MutableStateFlow(AnnouncementConfig())
    val announcementConfig: StateFlow<AnnouncementConfig> = _announcementConfig.asStateFlow()

    private val _brandAmbassadorConfig = MutableStateFlow(BrandAmbassadorConfig())
    val brandAmbassadorConfig: StateFlow<BrandAmbassadorConfig> = _brandAmbassadorConfig.asStateFlow()

    private val _festivalBannerConfig = MutableStateFlow(FestivalBannerConfig())
    val festivalBannerConfig: StateFlow<FestivalBannerConfig> = _festivalBannerConfig.asStateFlow()

    private val _toolFlags = MutableStateFlow<Map<String, String>>(
        mapOf(
            "tool_instagram" to "enabled",
            "tool_youtube" to "enabled",
            "tool_capcut" to "enabled",
            "tool_vn" to "enabled",
            "tool_edits" to "enabled",
            "tool_creator_academy" to "enabled",
            "tool_brand_collaboration" to "enabled",
            "tool_shopping_ai" to "enabled"
        )
    )
    val toolFlags: StateFlow<Map<String, String>> = _toolFlags.asStateFlow()

    // State Flows for Firestore Live Collections
    private val _announcements = MutableStateFlow<List<AnnouncementItem>>(emptyList())
    val announcements: StateFlow<List<AnnouncementItem>> = _announcements.asStateFlow()

    private val _creatorCourses = MutableStateFlow<List<CourseItem>>(emptyList())
    val creatorCourses: StateFlow<List<CourseItem>> = _creatorCourses.asStateFlow()

    private val _videoEditingCourses = MutableStateFlow<List<CourseItem>>(emptyList())
    val videoEditingCourses: StateFlow<List<CourseItem>> = _videoEditingCourses.asStateFlow()

    private val _motivationalQuotes = MutableStateFlow<List<MotivationalQuote>>(emptyList())
    val motivationalQuotes: StateFlow<List<MotivationalQuote>> = _motivationalQuotes.asStateFlow()

    private val _releaseNotes = MutableStateFlow<List<ReleaseNote>>(emptyList())
    val releaseNotes: StateFlow<List<ReleaseNote>> = _releaseNotes.asStateFlow()

    private val _toolUpdates = MutableStateFlow<List<ToolUpdate>>(emptyList())
    val toolUpdates: StateFlow<List<ToolUpdate>> = _toolUpdates.asStateFlow()

    private val _brandCollaborations = MutableStateFlow<List<BrandCollab>>(emptyList())
    val brandCollaborations: StateFlow<List<BrandCollab>> = _brandCollaborations.asStateFlow()

    private val _shoppingTips = MutableStateFlow<List<ShoppingTip>>(emptyList())
    val shoppingTips: StateFlow<List<ShoppingTip>> = _shoppingTips.asStateFlow()

    private val _featureFlags = MutableStateFlow<Map<String, String>>(emptyMap())
    val featureFlags: StateFlow<Map<String, String>> = _featureFlags.asStateFlow()

    private val _futureTools = MutableStateFlow<List<FutureTool>>(emptyList())
    val futureTools: StateFlow<List<FutureTool>> = _futureTools.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Instant load from local cache (< 10ms execution)
        loadCachedState()

        // 2. Initialize Firebase safely
        scope.launch {
            try {
                val appContext = context.applicationContext
                if (FirebaseApp.getApps(appContext).isEmpty()) {
                    FirebaseApp.initializeApp(appContext)
                }

                if (FirebaseApp.getApps(appContext).isEmpty()) {
                    Log.i(TAG, "Firebase configuration not present - proceeding in offline fallback mode")
                    isFirebaseInitialized = false
                    return@launch
                }

                isFirebaseInitialized = true

                // Firebase Services
                auth = runCatching { FirebaseAuth.getInstance() }.getOrNull()
                remoteConfig = runCatching { FirebaseRemoteConfig.getInstance() }.getOrNull()
                firestore = runCatching { FirebaseFirestore.getInstance() }.getOrNull()
                analytics = runCatching { FirebaseAnalytics.getInstance(appContext) }.getOrNull()
                crashlytics = runCatching { FirebaseCrashlytics.getInstance() }.getOrNull()
                storage = runCatching { FirebaseStorage.getInstance() }.getOrNull()

                // Configure Firestore persistence
                try {
                    val settings = FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build()
                    firestore?.firestoreSettings = settings
                } catch (e: Exception) {
                    Log.w(TAG, "Firestore persistence setting already applied or failed", e)
                }

                // Anonymous Auth
                auth?.signInAnonymously()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Firebase Anonymous Auth success: ${auth?.currentUser?.uid}")
                    }
                }

                // Remote Config Setup
                setupRemoteConfig()

                // Firestore Sync
                syncFirestoreCollections()

            } catch (e: Exception) {
                Log.e(TAG, "Firebase initialization error - proceeding in offline fallback mode", e)
                isFirebaseInitialized = false
            }
        }
    }

    private fun loadCachedState() {
        try {
            _maintenanceMode.value = prefs.getBoolean("maintenance_mode", false)
            _forceUpdate.value = prefs.getBoolean("force_update", false)
            _minimumSupportedVersion.value = prefs.getString("minimum_supported_version", "1.0.0") ?: "1.0.0"
            _playstoreUrl.value = prefs.getString("playstore_url", _playstoreUrl.value) ?: _playstoreUrl.value

            val annJson = prefs.getString("announcement_config_json", null)
            if (!annJson.isNullOrBlank()) {
                val json = JSONObject(annJson)
                _announcementConfig.value = AnnouncementConfig(
                    enabled = json.optBoolean("enabled", false),
                    title = json.optString("title", ""),
                    message = json.optString("message", ""),
                    image = json.optString("image", ""),
                    buttonText = json.optString("button_text", "OK"),
                    buttonAction = json.optString("button_action", "")
                )
            }

            val baJson = prefs.getString("brand_ambassador_json", null)
            if (!baJson.isNullOrBlank()) {
                val json = JSONObject(baJson)
                val savedImg = json.optString("image", "")
                val finalImg = if (savedImg.isBlank() || savedImg.contains("Picsart") || savedImg.contains("a7996a261d91d703ea1e41a90cba30233d85b80a") || savedImg.contains("unsplash")) {
                    "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/1785321241752.png"
                } else {
                    savedImg
                }
                _brandAmbassadorConfig.value = BrandAmbassadorConfig(
                    enabled = json.optBoolean("enabled", true),
                    image = finalImg,
                    durationMs = json.optLong("duration", 5000L)
                )
            }

            val festJson = prefs.getString("festival_banner_json", null)
            if (!festJson.isNullOrBlank()) {
                val json = JSONObject(festJson)
                _festivalBannerConfig.value = FestivalBannerConfig(
                    enabled = json.optBoolean("enabled", true),
                    image = json.optString("image", _festivalBannerConfig.value.image),
                    title = json.optString("title", _festivalBannerConfig.value.title),
                    subtitle = json.optString("subtitle", _festivalBannerConfig.value.subtitle)
                )
            }

            // Populate initial course fallback items if cache is empty
            loadDefaultCourseFallbacks()

        } catch (e: Exception) {
            Log.e(TAG, "Error loading cached cloud state", e)
            loadDefaultCourseFallbacks()
        }
    }

    private fun loadDefaultCourseFallbacks() {
        if (_creatorCourses.value.isEmpty()) {
            _creatorCourses.value = listOf(
                CourseItem("c1", "Viral Reels Blueprint 2026", "Master 3-second hooks, pacing, and retention algorithms for Instagram & YouTube Shorts.", "Short Form", "12 mins", "Beginner", "", "", listOf("Hook Strategy", "Audio Pacing", "Call to Action"), true),
                CourseItem("c2", "Creator Monetization & Brand Deals", "Learn how to pitch to brands, build media kits, and secure ₹10k-₹1 Lakh deals.", "Monetization", "18 mins", "Intermediate", "", "", listOf("Media Kit Creation", "Rate Card Setup", "Brand Outreach"), true),
                CourseItem("c3", "AI Scriptwriting & Storyboarding", "Generate viral video ideas, scripts, and captions using Gemini AI.", "AI Tools", "15 mins", "All Levels", "", "", listOf("Prompt Engineering", "Hook Generation", "SEO Captions"), false)
            )
        }

        if (_videoEditingCourses.value.isEmpty()) {
            _videoEditingCourses.value = listOf(
                CourseItem("v1", "CapCut Pro Speed Ramping & Text Effects", "Step-by-step masterclass on keyframe animation, velocity edits, and 3D text overlays.", "CapCut", "14 mins", "Beginner", "", "", listOf("Speed Curves", "Text Tracking", "Sound FX Sync"), true),
                CourseItem("v2", "VN Video Editor Color Grading Masterclass", "Transform raw smartphone video into cinematic color palettes with LUTs.", "VN Editor", "16 mins", "Intermediate", "", "", listOf("LUT Presets", "Skin Tone Correction", "HDR Color"), true),
                CourseItem("v3", "AI Auto-Captions & Sound Effects Sync", "Automate subtitles and audio transitions for maximum audience engagement.", "Audio & Subtitles", "10 mins", "Beginner", "", "", listOf("Auto Subtitles", "Whoosh FX", "Pop-up Graphics"), false)
            )
        }

        if (_motivationalQuotes.value.isEmpty()) {
            _motivationalQuotes.value = listOf(
                MotivationalQuote("q1", "Consistency turns raw creativity into unstoppable momentum.", "ShopTool AI", "Mindset"),
                MotivationalQuote("q2", "Your best viral video is always the next one you upload.", "Creator Academy", "Growth")
            )
        }

        if (_brandCollaborations.value.isEmpty()) {
            _brandCollaborations.value = listOf(
                BrandCollab("b1", "TechNova Gadgets", "", "₹15,000 - ₹45,000", "Tech & Gaming", "Min 3K Followers"),
                BrandCollab("b2", "UrbanPulse Fashion", "", "₹8,000 - ₹25,000", "Lifestyle & Fashion", "Min 2K Followers"),
                BrandCollab("b3", "FitLife Nutrition", "", "₹10,000 - ₹35,000", "Health & Fitness", "Min 5K Followers")
            )
        }
    }

    private fun setupRemoteConfig() {
        val rc = remoteConfig ?: return

        val defaults = mapOf<String, Any>(
            "maintenance_mode" to false,
            "force_update" to false,
            "minimum_supported_version" to "1.0.0",
            "playstore_url" to "https://play.google.com/store/apps/details?id=com.aistudio.shoptoolai.fpxwkr",
            "announcement_enabled" to false,
            "announcement_title" to "",
            "announcement_message" to "",
            "announcement_image" to "",
            "announcement_button_text" to "OK",
            "announcement_button_action" to "",
            "brand_ambassador_enabled" to true,
            "brand_ambassador_image" to _brandAmbassadorConfig.value.image,
            "brand_ambassador_duration" to 5000L,
            "festival_banner_enabled" to true,
            "festival_banner_image" to _festivalBannerConfig.value.image,
            "festival_banner_title" to _festivalBannerConfig.value.title,
            "festival_banner_subtitle" to _festivalBannerConfig.value.subtitle,
            "tool_instagram" to "enabled",
            "tool_youtube" to "enabled",
            "tool_capcut" to "enabled",
            "tool_vn" to "enabled",
            "tool_edits" to "enabled",
            "tool_creator_academy" to "enabled",
            "tool_brand_collaboration" to "enabled",
            "tool_shopping_ai" to "enabled"
        )

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(30L)
            .build()

        rc.setConfigSettingsAsync(configSettings)
        rc.setDefaultsAsync(defaults)

        rc.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Remote Config fetched and activated successfully")
                applyRemoteConfigValues(rc)
            } else {
                Log.w(TAG, "Remote config fetch failed, using local/default values")
            }
        }
    }

    private fun applyRemoteConfigValues(rc: FirebaseRemoteConfig) {
        try {
            val isMaint = rc.getBoolean("maintenance_mode")
            val isForce = rc.getBoolean("force_update")
            val minVer = rc.getString("minimum_supported_version")
            val playUrl = rc.getString("playstore_url")

            _maintenanceMode.value = isMaint
            _forceUpdate.value = isForce
            if (minVer.isNotBlank()) _minimumSupportedVersion.value = minVer
            if (playUrl.isNotBlank()) _playstoreUrl.value = playUrl

            val annConfig = AnnouncementConfig(
                enabled = rc.getBoolean("announcement_enabled"),
                title = rc.getString("announcement_title"),
                message = rc.getString("announcement_message"),
                image = rc.getString("announcement_image"),
                buttonText = rc.getString("announcement_button_text").ifBlank { "OK" },
                buttonAction = rc.getString("announcement_button_action")
            )
            _announcementConfig.value = annConfig

            val rcBaImg = rc.getString("brand_ambassador_image").ifBlank { _brandAmbassadorConfig.value.image }
            val finalBaImg = if (rcBaImg.contains("Picsart") || rcBaImg.contains("a7996a261d91d703ea1e41a90cba30233d85b80a") || rcBaImg.contains("unsplash")) {
                "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/1785321241752.png"
            } else {
                rcBaImg
            }
            val baConfig = BrandAmbassadorConfig(
                enabled = rc.getBoolean("brand_ambassador_enabled"),
                image = finalBaImg,
                durationMs = rc.getLong("brand_ambassador_duration").let { if (it <= 0) 5000L else it }
            )
            _brandAmbassadorConfig.value = baConfig

            val festConfig = FestivalBannerConfig(
                enabled = rc.getBoolean("festival_banner_enabled"),
                image = rc.getString("festival_banner_image").ifBlank { _festivalBannerConfig.value.image },
                title = rc.getString("festival_banner_title").ifBlank { _festivalBannerConfig.value.title },
                subtitle = rc.getString("festival_banner_subtitle").ifBlank { _festivalBannerConfig.value.subtitle }
            )
            _festivalBannerConfig.value = festConfig

            val flags = mapOf(
                "tool_instagram" to rc.getString("tool_instagram").ifBlank { "enabled" },
                "tool_youtube" to rc.getString("tool_youtube").ifBlank { "enabled" },
                "tool_capcut" to rc.getString("tool_capcut").ifBlank { "enabled" },
                "tool_vn" to rc.getString("tool_vn").ifBlank { "enabled" },
                "tool_edits" to rc.getString("tool_edits").ifBlank { "enabled" },
                "tool_creator_academy" to rc.getString("tool_creator_academy").ifBlank { "enabled" },
                "tool_brand_collaboration" to rc.getString("tool_brand_collaboration").ifBlank { "enabled" },
                "tool_shopping_ai" to rc.getString("tool_shopping_ai").ifBlank { "enabled" }
            )
            _toolFlags.value = flags

            // Save to local cache
            saveToPrefs("maintenance_mode", isMaint)
            saveToPrefs("force_update", isForce)
            saveToPrefs("minimum_supported_version", minVer)
            saveToPrefs("playstore_url", playUrl)

        } catch (e: Exception) {
            Log.e(TAG, "Failed applying remote config values", e)
        }
    }

    private fun syncFirestoreCollections() {
        val fs = firestore ?: return

        // 1. Announcements collection
        fs.collection("announcements").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    AnnouncementItem(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        message = doc.getString("message") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: doc.getString("image") ?: "",
                        actionUrl = doc.getString("actionUrl") ?: "",
                        category = doc.getString("category") ?: "General",
                        timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _announcements.value = list
        }

        // 2. Creator Courses collection
        fs.collection("creator_courses").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    val takeaways = (doc.get("keyTakeaways") as? List<*>)?.mapNotNull { it.toString() } ?: emptyList()
                    CourseItem(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        category = doc.getString("category") ?: "General",
                        duration = doc.getString("duration") ?: "10 mins",
                        level = doc.getString("level") ?: "Beginner",
                        iconUrl = doc.getString("iconUrl") ?: "",
                        videoUrl = doc.getString("videoUrl") ?: "",
                        keyTakeaways = takeaways,
                        isFeatured = doc.getBoolean("isFeatured") ?: false
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _creatorCourses.value = list
        }

        // 3. Video Editing Courses collection
        fs.collection("video_editing_courses").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    val takeaways = (doc.get("keyTakeaways") as? List<*>)?.mapNotNull { it.toString() } ?: emptyList()
                    CourseItem(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        category = doc.getString("category") ?: "General",
                        duration = doc.getString("duration") ?: "10 mins",
                        level = doc.getString("level") ?: "Beginner",
                        iconUrl = doc.getString("iconUrl") ?: "",
                        videoUrl = doc.getString("videoUrl") ?: "",
                        keyTakeaways = takeaways,
                        isFeatured = doc.getBoolean("isFeatured") ?: false
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _videoEditingCourses.value = list
        }

        // 4. Motivational Quotes
        fs.collection("motivational_quotes").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    MotivationalQuote(
                        id = doc.id,
                        quote = doc.getString("quote") ?: "",
                        author = doc.getString("author") ?: "ShopTool AI",
                        category = doc.getString("category") ?: "Growth"
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _motivationalQuotes.value = list
        }

        // 5. Release Notes
        fs.collection("release_notes").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    val features = (doc.get("features") as? List<*>)?.mapNotNull { it.toString() } ?: emptyList()
                    ReleaseNote(
                        id = doc.id,
                        version = doc.getString("version") ?: "1.0.0",
                        title = doc.getString("title") ?: "Release Note",
                        features = features,
                        releaseDate = doc.getLong("releaseDate") ?: System.currentTimeMillis()
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _releaseNotes.value = list
        }

        // 6. Tool Updates
        fs.collection("tool_updates").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    ToolUpdate(
                        id = doc.id,
                        toolId = doc.getString("toolId") ?: "",
                        toolName = doc.getString("toolName") ?: "",
                        updateMessage = doc.getString("updateMessage") ?: "",
                        timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _toolUpdates.value = list
        }

        // 7. Brand Collaborations
        fs.collection("brand_collaborations").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    BrandCollab(
                        id = doc.id,
                        brandName = doc.getString("brandName") ?: "",
                        logoUrl = doc.getString("logoUrl") ?: "",
                        payoutRange = doc.getString("payoutRange") ?: "",
                        category = doc.getString("category") ?: "",
                        requirements = doc.getString("requirements") ?: "",
                        applyUrl = doc.getString("applyUrl") ?: ""
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _brandCollaborations.value = list
        }

        // 8. Shopping Tips
        fs.collection("shopping_tips").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    ShoppingTip(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        tip = doc.getString("tip") ?: "",
                        merchant = doc.getString("merchant") ?: "Flipkart",
                        savingsPotential = doc.getString("savingsPotential") ?: ""
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _shoppingTips.value = list
        }

        // 9. Feature Flags
        fs.collection("feature_flags").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val map = mutableMapOf<String, String>()
            snapshot.documents.forEach { doc ->
                doc.getString("status")?.let { map[doc.id] = it }
            }
            if (map.isNotEmpty()) {
                val current = _toolFlags.value.toMutableMap()
                current.putAll(map)
                _toolFlags.value = current
            }
        }

        // 10. Future Tools
        fs.collection("future_tools").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { doc ->
                try {
                    FutureTool(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        targetReleaseDate = doc.getString("targetReleaseDate") ?: "",
                        votes = (doc.getLong("votes") ?: 0L).toInt()
                    )
                } catch (ex: Exception) { null }
            }
            if (list.isNotEmpty()) _futureTools.value = list
        }
    }

    /**
     * Resolves Firebase Storage paths (gs:// or relative) or returns standard HTTP image URLs.
     */
    fun resolveImageUrl(pathOrUrl: String): String {
        if (pathOrUrl.isBlank()) return "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80"
        if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) return pathOrUrl

        val st = storage ?: return "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80"
        return try {
            val ref = if (pathOrUrl.startsWith("gs://")) st.getReferenceFromUrl(pathOrUrl) else st.reference.child(pathOrUrl)
            ref.downloadUrl.result?.toString() ?: "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80"
        } catch (e: Exception) {
            "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80"
        }
    }

    /**
     * Checks status of a tool remotely.
     */
    fun getToolStatus(toolKey: String): ToolStatus {
        val statusStr = _toolFlags.value[toolKey]
            ?: _featureFlags.value[toolKey]
            ?: remoteConfig?.getString(toolKey)
            ?: "enabled"
        return ToolStatus.fromString(statusStr)
    }

    // Analytics & Crashlytics Event Tracking
    fun logToolOpen(toolId: String, toolName: String) {
        try {
            val bundle = android.os.Bundle().apply {
                putString("tool_id", toolId)
                putString("tool_name", toolName)
                putLong("timestamp", System.currentTimeMillis())
            }
            analytics?.logEvent("tool_opened", bundle)
            crashlytics?.log("Tool opened: $toolId - $toolName")
        } catch (e: Exception) {
            Log.w(TAG, "Analytics log error", e)
        }
    }

    fun logCourseCompletion(courseId: String, courseTitle: String) {
        try {
            val bundle = android.os.Bundle().apply {
                putString("course_id", courseId)
                putString("course_title", courseTitle)
            }
            analytics?.logEvent("course_completed", bundle)
        } catch (e: Exception) {
            Log.w(TAG, "Analytics log error", e)
        }
    }

    fun logSessionTime(durationSeconds: Long) {
        try {
            val bundle = android.os.Bundle().apply {
                putLong("session_duration_sec", durationSeconds)
            }
            analytics?.logEvent("session_duration", bundle)
        } catch (e: Exception) {
            Log.w(TAG, "Analytics log error", e)
        }
    }

    fun logFeatureUsage(featureName: String, action: String) {
        try {
            val bundle = android.os.Bundle().apply {
                putString("feature_name", featureName)
                putString("action", action)
            }
            analytics?.logEvent("feature_used", bundle)
        } catch (e: Exception) {
            Log.w(TAG, "Analytics log error", e)
        }
    }

    fun logCrashEvent(exception: Throwable, contextInfo: String) {
        try {
            crashlytics?.setCustomKey("context_info", contextInfo)
            crashlytics?.recordException(exception)
        } catch (e: Exception) {
            Log.w(TAG, "Crashlytics log error", e)
        }
    }

    private fun saveToPrefs(key: String, value: Any) {
        if (!::prefs.isInitialized) return
        when (value) {
            is Boolean -> prefs.edit().putBoolean(key, value).apply()
            is String -> prefs.edit().putString(key, value).apply()
            is Long -> prefs.edit().putLong(key, value).apply()
            is Int -> prefs.edit().putInt(key, value).apply()
        }
    }
}
