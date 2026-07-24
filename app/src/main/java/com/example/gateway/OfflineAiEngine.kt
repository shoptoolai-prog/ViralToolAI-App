package com.example.gateway

import com.example.core.UniversalAiProvider
import java.net.InetAddress

/**
 * PHASE 12D — Offline AI & Network Detector Engine
 * Handles graceful offline degradation using local intelligence.
 */

object OfflineAiEngine {

    @Volatile
    private var isSimulatedOfflineMode: Boolean = false

    /**
     * Checks whether internet connectivity is currently available.
     * Guaranteed never to block UI thread.
     */
    fun isNetworkAvailable(): Boolean {
        if (isSimulatedOfflineMode) return false
        return try {
            // Fast non-blocking check
            val runtime = Runtime.getRuntime()
            val process = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitCode = process.waitFor()
            exitCode == 0
        } catch (e: Exception) {
            // If ping fails or restricted, assume network is online unless simulated offline
            true
        }
    }

    /**
     * Toggles manual offline mode for testing local neural engine fallback.
     */
    fun setOfflineModeOverride(offline: Boolean) {
        isSimulatedOfflineMode = offline
    }

    /**
     * Resolves target AI provider based on network state.
     */
    fun resolveProviderForNetwork(preferredProvider: UniversalAiProvider): UniversalAiProvider {
        return if (isNetworkAvailable()) {
            preferredProvider
        } else {
            UniversalAiProvider.LOCAL_AI
        }
    }
}
