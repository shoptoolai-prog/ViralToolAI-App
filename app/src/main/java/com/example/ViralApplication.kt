package com.example

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger

class ViralApplication : Application(), ImageLoaderFactory {

    private var customImageLoader: ImageLoader? = null

    override fun onCreate() {
        super.onCreate()
        // Initialize Live Cloud Management System (Firebase Remote Config, Firestore, Analytics, FCM, Offline Cache)
        com.example.cloud.LiveCloudManager.init(this)

        // Initialize Google Mobile Ads SDK exactly once on app startup
        try {
            com.google.android.gms.ads.MobileAds.initialize(this) { initializationStatus ->
                android.util.Log.d("AdMob", "Google Mobile Ads initialized: $initializationStatus")
            }
            com.example.ads.RewardedAdManager.init(this)
        } catch (e: Throwable) {
            android.util.Log.e("AdMob", "Failed to initialize Google Mobile Ads SDK: ${e.message}", e)
        }
    }

    override fun newImageLoader(): ImageLoader {
        val loader = ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // Max 25% app memory for bitmap cache
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(100 * 1024 * 1024) // 100 MB max disk cache
                    .build()
            }
            .respectCacheHeaders(false) // Optimize for immediate display
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
        customImageLoader = loader
        return loader
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        customImageLoader?.memoryCache?.trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        customImageLoader?.memoryCache?.clear()
    }
}
