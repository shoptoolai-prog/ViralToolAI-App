package com.example.gateway

import kotlinx.coroutines.delay
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * PHASE 12D — Rate Limit & Queue Manager
 * Prepares architecture for API limits, request queueing, retries, and cooldowns.
 */

object AiRateLimitManager {

    private const val MAX_REQUESTS_PER_MINUTE = 30
    private const val COOLDOWN_DURATION_MS = 10000L

    private val requestCountWindow = AtomicInteger(0)
    private var windowStartTimeMs = System.currentTimeMillis()
    private val cooldownMap = ConcurrentHashMap<String, Long>()

    /**
     * Checks if provider or gateway is currently rate limited or in cooldown.
     */
    fun isRateLimited(providerKey: String = "GLOBAL"): Boolean {
        val now = System.currentTimeMillis()

        // Check provider cooldown
        val cooldownEnd = cooldownMap[providerKey] ?: 0L
        if (now < cooldownEnd) {
            return true
        }

        // Check sliding window
        if (now - windowStartTimeMs > 60000L) {
            windowStartTimeMs = now
            requestCountWindow.set(0)
        }

        return requestCountWindow.get() >= MAX_REQUESTS_PER_MINUTE
    }

    /**
     * Registers a new outgoing AI request and enforces cooldown if threshold reached.
     */
    fun registerRequest(providerKey: String = "GLOBAL") {
        val now = System.currentTimeMillis()
        if (now - windowStartTimeMs > 60000L) {
            windowStartTimeMs = now
            requestCountWindow.set(0)
        }

        val count = requestCountWindow.incrementAndGet()
        if (count >= MAX_REQUESTS_PER_MINUTE) {
            cooldownMap[providerKey] = now + COOLDOWN_DURATION_MS
        }
    }

    /**
     * Trigger explicit cooldown on 429 Too Many Requests error.
     */
    fun triggerCooldown(providerKey: String, durationMs: Long = COOLDOWN_DURATION_MS) {
        cooldownMap[providerKey] = System.currentTimeMillis() + durationMs
    }

    /**
     * Helper suspend function for exponential backoff delay before retrying failed requests.
     */
    suspend fun applyBackoffDelay(attempt: Int) {
        val delayMs = (200L * (1 shl (attempt - 1))).coerceAtMost(2000L)
        delay(delayMs)
    }
}
