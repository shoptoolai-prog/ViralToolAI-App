package com.example.ai

import com.example.core.UniversalAiRequest
import com.example.core.UniversalAiResponse
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap

/**
 * SHOPTOOLAI Phase 8A — Local AI Response Cache
 * Caches successful AI responses locally to avoid duplicate requests & optimize latency/quota.
 */
object AiResponseCache {

    private data class CacheEntry(
        val response: UniversalAiResponse,
        val timestampMs: Long = System.currentTimeMillis()
    )

    private const val DEFAULT_TTL_MS = 60 * 60 * 1000L // 1 hour TTL
    private const val MAX_CACHE_SIZE = 100

    private val cache = ConcurrentHashMap<String, CacheEntry>()

    /**
     * Generates deterministic signature hash for request payload.
     */
    fun generateSignature(request: UniversalAiRequest): String {
        val rawKey = StringBuilder().apply {
            append(request.textPrompt?.trim() ?: "")
            append("|")
            append(request.promptCategory.name)
            append("|")
            append(request.language.name)
            append("|")
            append(request.shoppingUrl?.trim() ?: "")
            append("|")
            append(request.primaryImageUri?.trim() ?: "")
            append("|")
            append(request.preferredProvider?.name ?: "DEFAULT")
        }.toString()

        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(rawKey.toByteArray(Charsets.UTF_8))
            hashBytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            rawKey.hashCode().toString()
        }
    }

    /**
     * Retrieves valid cached response if available and not expired.
     */
    fun get(request: UniversalAiRequest, ttlMs: Long = DEFAULT_TTL_MS): UniversalAiResponse? {
        val key = generateSignature(request)
        val entry = cache[key] ?: return null
        val now = System.currentTimeMillis()

        if (now - entry.timestampMs > ttlMs) {
            cache.remove(key)
            return null
        }

        return entry.response
    }

    /**
     * Stores successful response in cache.
     */
    fun put(request: UniversalAiRequest, response: UniversalAiResponse) {
        if (!response.isSuccess) return

        if (cache.size >= MAX_CACHE_SIZE) {
            // Remove oldest entries
            val oldestKey = cache.entries.minByOrNull { it.value.timestampMs }?.key
            if (oldestKey != null) {
                cache.remove(oldestKey)
            }
        }

        val key = generateSignature(request)
        cache[key] = CacheEntry(response)
    }

    /**
     * Clears all cached responses.
     */
    fun clear() {
        cache.clear()
    }

    /**
     * Returns current cached entry count.
     */
    fun size(): Int = cache.size
}
