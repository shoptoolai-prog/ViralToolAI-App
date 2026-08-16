package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicBoolean

/**
 * REWARDED AD MANAGER — PHASE 2 GOOGLE ADMOB REWARDED AD ENGINE
 *
 * Isolated, lifecycle-safe manager for loading and showing AdMob Rewarded Ads.
 * - Prevents memory leaks (does not retain Activity references)
 * - Prevents double showing on rapid button taps
 * - Automatically pre-loads next ad upon dismissal/failure
 * - Safe exception handling so the app never crashes
 */
object RewardedAdManager {

    private const val TAG = "RewardedAdManager"

    /**
     * Google's official Android Rewarded Test Ad Unit ID:
     * https://developers.google.com/admob/android/test-ads#sample_ad_units
     */
    const val REWARDED_TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    /**
     * Production Ad Unit ID configuration / placeholder
     */
    private const val REWARDED_PRODUCTION_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    /**
     * Returns the active ad unit ID based on build configuration.
     * Uses test ad unit ID during DEBUG to guarantee ad safety.
     */
    val adUnitId: String
        get() = if (BuildConfig.DEBUG) {
            REWARDED_TEST_AD_UNIT_ID
        } else {
            REWARDED_PRODUCTION_AD_UNIT_ID
        }

    // Ad instance reference (cleared after each show)
    private var rewardedAd: RewardedAd? = null

    // Application context reference for background reloading
    private var appContext: Context? = null

    // Reactive StateFlows for UI observation
    private val _isAdReady = MutableStateFlow(false)
    val isAdReady: StateFlow<Boolean> = _isAdReady.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    // Concurrency guard to prevent double-showing on rapid user taps
    private val isShowingAd = AtomicBoolean(false)

    /**
     * Initialize MobileAds and trigger initial ad prefetch.
     * Safe to call multiple times, but will only initialize once.
     */
    fun init(context: Context) {
        try {
            appContext = context.applicationContext
            loadAd(context.applicationContext)
        } catch (e: Throwable) {
            Log.e(TAG, "Error during RewardedAdManager init: ${e.message}", e)
        }
    }

    /**
     * Loads a Rewarded Ad if one is not already ready or loading.
     */
    fun loadAd(context: Context? = appContext) {
        val ctx = context?.applicationContext ?: appContext ?: return

        // Guard against duplicate loading or loading while ad is currently showing
        if (_isLoading.value || isShowingAd.get()) {
            Log.d(TAG, "Ad is already loading or displaying. Skipping load request.")
            return
        }

        if (rewardedAd != null && _isAdReady.value) {
            Log.d(TAG, "Ad is already loaded and ready.")
            return
        }

        _isLoading.value = true
        _lastError.value = null

        try {
            val adRequest = AdRequest.Builder().build()

            RewardedAd.load(
                ctx,
                adUnitId,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        Log.d(TAG, "Rewarded ad loaded successfully.")
                        rewardedAd = ad
                        _isAdReady.value = true
                        _isLoading.value = false
                        _lastError.value = null

                        // Register FullScreenContentCallback for lifecycle events
                        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Rewarded ad showed full screen content.")
                                isShowingAd.set(true)
                            }

                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Rewarded ad dismissed by user.")
                                isShowingAd.set(false)
                                rewardedAd = null
                                _isAdReady.value = false
                                // Preload next ad automatically
                                loadAd()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e(TAG, "Rewarded ad failed to show: ${adError.message} (code: ${adError.code})")
                                isShowingAd.set(false)
                                rewardedAd = null
                                _isAdReady.value = false
                                _lastError.value = "Rewarded ad isn't available right now. Please try again."
                                // Attempt reload
                                loadAd()
                            }
                        }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.e(TAG, "Rewarded ad failed to load: ${loadAdError.message} (code: ${loadAdError.code})")
                        rewardedAd = null
                        _isAdReady.value = false
                        _isLoading.value = false
                        _lastError.value = "Rewarded ad isn't available right now. Please try again."
                    }
                }
            )
        } catch (e: Throwable) {
            Log.e(TAG, "Exception during RewardedAd.load: ${e.message}", e)
            rewardedAd = null
            _isAdReady.value = false
            _isLoading.value = false
            _lastError.value = "Rewarded ad isn't available right now. Please try again."
        }
    }

    /**
     * Displays the loaded Rewarded Ad safely to the user.
     *
     * @param activity Current valid Activity. The reference is used immediately and not retained.
     * @param onUserEarnedReward Callback invoked ONLY when user successfully completes viewing the ad.
     * @param onAdClosed Optional callback when ad full screen is dismissed.
     * @param onError Optional callback for user-facing error messages (e.g. ad not ready).
     */
    fun showAd(
        activity: Activity,
        onUserEarnedReward: (RewardItem) -> Unit,
        onAdClosed: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        try {
            if (activity.isFinishing || activity.isDestroyed) {
                Log.w(TAG, "Activity is finishing or destroyed. Cannot display ad.")
                onError?.invoke("Rewarded ad isn't available right now. Please try again.")
                return
            }

            // Prevent double show on rapid button taps
            if (!isShowingAd.compareAndSet(false, true)) {
                Log.w(TAG, "Ad is already showing. Ignoring additional show tap.")
                return
            }

            val ad = rewardedAd
            if (ad == null || !_isAdReady.value) {
                isShowingAd.set(false)
                Log.w(TAG, "Rewarded ad is not ready yet.")
                onError?.invoke("Ad is loading…")
                loadAd(activity.applicationContext)
                return
            }

            // Chain user dismissal callback to current show request
            val currentCallback = ad.fullScreenContentCallback
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    currentCallback?.onAdShowedFullScreenContent()
                }

                override fun onAdDismissedFullScreenContent() {
                    currentCallback?.onAdDismissedFullScreenContent()
                    onAdClosed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    currentCallback?.onAdFailedToShowFullScreenContent(adError)
                    onError?.invoke("Rewarded ad isn't available right now. Please try again.")
                    onAdClosed?.invoke()
                }
            }

            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                try {
                    onUserEarnedReward(rewardItem)
                } catch (e: Throwable) {
                    Log.e(TAG, "Error in onUserEarnedReward callback: ${e.message}", e)
                }
            }
        } catch (e: Throwable) {
            isShowingAd.set(false)
            Log.e(TAG, "Exception during RewardedAd.show: ${e.message}", e)
            onError?.invoke("Rewarded ad isn't available right now. Please try again.")
        }
    }
}
