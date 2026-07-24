package com.example.data.extraction

import com.example.data.MerchantDetector
import com.example.data.MerchantRegistry
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLDecoder
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * PHASE 12E — Real Metadata Extractor
 * Strictly extracts product metadata from live HTML source using priority:
 * 1. JSON-LD Product Schema
 * 2. Open Graph Metadata
 * 3. Twitter Card Metadata
 * 4. Meta Tags
 * 5. HTML Title
 * 6. Canonical URL
 * 7. Favicon
 */

data class RawExtractedMetadata(
    val title: String? = null,
    val imageUrl: String? = null,
    val currentPrice: Double? = null,
    val originalPrice: Double? = null,
    val currency: String? = null,
    val brand: String? = null,
    val merchantName: String? = null,
    val rating: Double? = null,
    val reviewsCount: Int? = null,
    val availability: String? = null,
    val canonicalUrl: String? = null,
    val faviconUrl: String? = null,
    val extractionSource: String = "HTML_METADATA"
)

object ShoppingMetadataExtractor {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    private const val BROWSER_USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    /**
     * Downloads live HTML for normalized URL and parses metadata using 7-tier priority sources.
     */
    fun fetchAndExtractMetadata(url: String): RawExtractedMetadata {
        val normalizedUrl = MerchantDetector.normalizeUrl(url)
        val merchantAnalysis = MerchantDetector.analyzeUrl(normalizedUrl)
        val defaultMerchant = merchantAnalysis.merchantInfo.merchantName

        var htmlContent: String? = null
        var finalEffectiveUrl = normalizedUrl

        try {
            val request = Request.Builder()
                .url(if (!normalizedUrl.startsWith("http")) "https://$normalizedUrl" else normalizedUrl)
                .header("User-Agent", BROWSER_USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9")
                .build()

            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    finalEffectiveUrl = response.request.url.toString()
                    htmlContent = response.body?.string()
                }
            }
        } catch (e: Exception) {
            htmlContent = null
        }

        if (htmlContent.isNullOrBlank()) {
            return extractFromUrlSlugFallback(normalizedUrl, defaultMerchant)
        }

        val html = htmlContent!!

        // 1. Priority 1: JSON-LD Product Schema
        val jsonLdData = parseJsonLdProductSchema(html)
        if (jsonLdData?.title != null) {
            return jsonLdData.copy(
                merchantName = jsonLdData.merchantName ?: defaultMerchant,
                canonicalUrl = jsonLdData.canonicalUrl ?: finalEffectiveUrl,
                faviconUrl = jsonLdData.faviconUrl ?: merchantAnalysis.merchantInfo.faviconUrl
            )
        }

        // 2. Priority 2: Open Graph Metadata
        val ogData = parseOpenGraphMetadata(html)

        // 3. Priority 3: Twitter Card Metadata
        val twitterData = parseTwitterCardMetadata(html)

        // 4. Priority 4: Meta Tags & Title Tag
        val metaData = parseMetaAndTitleTags(html)

        // Merge extracted sources prioritizing highest tier
        val mergedTitle = ogData.title ?: twitterData.title ?: metaData.title ?: extractTitleFromSlug(normalizedUrl)
        val mergedImage = ogData.imageUrl ?: twitterData.imageUrl ?: metaData.imageUrl
        val mergedPrice = ogData.currentPrice ?: twitterData.currentPrice ?: metaData.currentPrice
        val mergedBrand = ogData.brand ?: metaData.brand
        val mergedRating = ogData.rating
        val mergedReviews = ogData.reviewsCount
        val canonical = ogData.canonicalUrl ?: metaData.canonicalUrl ?: finalEffectiveUrl
        val favicon = merchantAnalysis.merchantInfo.faviconUrl

        return RawExtractedMetadata(
            title = mergedTitle,
            imageUrl = mergedImage,
            currentPrice = mergedPrice,
            originalPrice = ogData.originalPrice,
            currency = ogData.currency ?: "INR",
            brand = mergedBrand,
            merchantName = defaultMerchant,
            rating = mergedRating,
            reviewsCount = mergedReviews,
            availability = ogData.availability ?: "In Stock",
            canonicalUrl = canonical,
            faviconUrl = favicon,
            extractionSource = "LIVE_HTML_PARSER"
        )
    }

    /**
     * Priority 1: Parse JSON-LD (<script type="application/ld+json">) Product Schema.
     */
    private fun parseJsonLdProductSchema(html: String): RawExtractedMetadata? {
        val jsonLdPattern = Pattern.compile(
            "<script[^>]*type=[\"']application/ld\\+json[\"'][^>]*>(.*?)</script>",
            Pattern.DOTALL or Pattern.CASE_INSENSITIVE
        )
        val matcher = jsonLdPattern.matcher(html)

        while (matcher.find()) {
            val jsonText = matcher.group(1)?.trim() ?: continue
            try {
                if (jsonText.startsWith("[")) {
                    val array = JSONArray(jsonText)
                    for (i in 0 until array.length()) {
                        val obj = array.optJSONObject(i) ?: continue
                        val parsed = extractProductFromJsonLdObject(obj)
                        if (parsed?.title != null) return parsed
                    }
                } else if (jsonText.startsWith("{")) {
                    val obj = JSONObject(jsonText)
                    if (obj.has("@graph")) {
                        val graphArray = obj.optJSONArray("@graph")
                        if (graphArray != null) {
                            for (i in 0 until graphArray.length()) {
                                val item = graphArray.optJSONObject(i) ?: continue
                                val parsed = extractProductFromJsonLdObject(item)
                                if (parsed?.title != null) return parsed
                            }
                        }
                    } else {
                        val parsed = extractProductFromJsonLdObject(obj)
                        if (parsed?.title != null) return parsed
                    }
                }
            } catch (e: Exception) {
                // Ignore JSON parse errors in snippet
            }
        }
        return null
    }

    private fun extractProductFromJsonLdObject(obj: JSONObject): RawExtractedMetadata? {
        val type = obj.optString("@type", "")
        if (!type.equals("Product", ignoreCase = true) && !type.contains("Product", ignoreCase = true)) {
            return null
        }

        val name = obj.optString("name", "").takeIf { it.isNotBlank() } ?: return null

        // Image
        var imgUrl: String? = null
        if (obj.has("image")) {
            val imgOpt = obj.get("image")
            if (imgOpt is String) imgUrl = imgOpt
            else if (imgOpt is JSONArray && imgOpt.length() > 0) imgUrl = imgOpt.optString(0)
            else if (imgOpt is JSONObject) imgUrl = imgOpt.optString("url")
        }

        // Brand
        var brandName: String? = null
        if (obj.has("brand")) {
            val brandOpt = obj.get("brand")
            if (brandOpt is JSONObject) brandName = brandOpt.optString("name")
            else if (brandOpt is String) brandName = brandOpt
        }

        // Offers
        var priceVal: Double? = null
        var curr: String? = null
        var avail: String? = null

        if (obj.has("offers")) {
            val offersOpt = obj.get("offers")
            val offerObj = if (offersOpt is JSONObject) offersOpt
            else if (offersOpt is JSONArray && offersOpt.length() > 0) offersOpt.optJSONObject(0)
            else null

            if (offerObj != null) {
                val rawPrice = offerObj.optString("price", offerObj.optString("lowPrice", ""))
                priceVal = rawPrice.toDoubleOrNull()
                curr = offerObj.optString("priceCurrency", "INR")
                avail = offerObj.optString("availability", "In Stock").replace("http://schema.org/", "")
            }
        }

        // Rating
        var ratingVal: Double? = null
        var reviewsCountVal: Int? = null

        if (obj.has("aggregateRating")) {
            val aggObj = obj.optJSONObject("aggregateRating")
            if (aggObj != null) {
                ratingVal = aggObj.optString("ratingValue", "").toDoubleOrNull()
                reviewsCountVal = aggObj.optString("reviewCount", aggObj.optString("ratingCount", "")).toIntOrNull()
            }
        }

        return RawExtractedMetadata(
            title = cleanTitle(name),
            imageUrl = imgUrl,
            currentPrice = priceVal,
            currency = curr,
            brand = brandName,
            rating = ratingVal,
            reviewsCount = reviewsCountVal,
            availability = avail,
            extractionSource = "JSON_LD_SCHEMA"
        )
    }

    /**
     * Priority 2: Parse Open Graph (<meta property="og:...">) tags.
     */
    private fun parseOpenGraphMetadata(html: String): RawExtractedMetadata {
        val title = extractMetaTagContent(html, "og:title")
        val image = extractMetaTagContent(html, "og:image") ?: extractMetaTagContent(html, "og:image:secure_url")
        val price = extractMetaTagContent(html, "og:price:amount") ?: extractMetaTagContent(html, "product:price:amount")
        val currency = extractMetaTagContent(html, "og:price:currency") ?: extractMetaTagContent(html, "product:price:currency")
        val brand = extractMetaTagContent(html, "og:brand") ?: extractMetaTagContent(html, "product:brand")
        val siteName = extractMetaTagContent(html, "og:site_name")

        return RawExtractedMetadata(
            title = cleanTitle(title),
            imageUrl = image,
            currentPrice = price?.toDoubleOrNull(),
            currency = currency ?: "INR",
            brand = brand,
            merchantName = siteName,
            extractionSource = "OPEN_GRAPH"
        )
    }

    /**
     * Priority 3: Parse Twitter Cards (<meta name="twitter:...">).
     */
    private fun parseTwitterCardMetadata(html: String): RawExtractedMetadata {
        val title = extractMetaTagContent(html, "twitter:title")
        val image = extractMetaTagContent(html, "twitter:image")
        val price = extractMetaTagContent(html, "twitter:data1")?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()

        return RawExtractedMetadata(
            title = cleanTitle(title),
            imageUrl = image,
            currentPrice = price,
            extractionSource = "TWITTER_CARD"
        )
    }

    /**
     * Priority 4: Parse standard <meta> tags and <title> tag.
     */
    private fun parseMetaAndTitleTags(html: String): RawExtractedMetadata {
        val metaTitle = extractMetaTagContent(html, "title") ?: extractMetaTagContent(html, "description")
        val tagTitle = extractHtmlTagContent(html, "title")

        val title = cleanTitle(tagTitle ?: metaTitle)
        val canonical = extractMetaTagContent(html, "canonical") ?: extractLinkTagHref(html, "canonical")

        return RawExtractedMetadata(
            title = title,
            canonicalUrl = canonical,
            extractionSource = "HTML_META_TAGS"
        )
    }

    private fun extractMetaTagContent(html: String, attributeValue: String): String? {
        val p1 = Pattern.compile("<meta[^>]*property=[\"']$attributeValue[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        var m = p1.matcher(html)
        if (m.find()) return m.group(1)?.trim()

        val p2 = Pattern.compile("<meta[^>]*name=[\"']$attributeValue[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        m = p2.matcher(html)
        if (m.find()) return m.group(1)?.trim()

        val p3 = Pattern.compile("<meta[^>]*content=[\"'](.*?)[\"'][^>]*property=[\"']$attributeValue[\"']", Pattern.CASE_INSENSITIVE)
        m = p3.matcher(html)
        if (m.find()) return m.group(1)?.trim()

        val p4 = Pattern.compile("<meta[^>]*content=[\"'](.*?)[\"'][^>]*name=[\"']$attributeValue[\"']", Pattern.CASE_INSENSITIVE)
        m = p4.matcher(html)
        if (m.find()) return m.group(1)?.trim()

        return null
    }

    private fun extractHtmlTagContent(html: String, tagName: String): String? {
        val pattern = Pattern.compile("<$tagName[^>]*>(.*?)</$tagName>", Pattern.DOTALL or Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(html)
        return if (matcher.find()) matcher.group(1)?.trim() else null
    }

    private fun extractLinkTagHref(html: String, relValue: String): String? {
        val pattern = Pattern.compile("<link[^>]*rel=[\"']$relValue[\"'][^>]*href=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(html)
        return if (matcher.find()) matcher.group(1)?.trim() else null
    }

    /**
     * Cleans page titles by stripping site name suffixes (e.g., "| Amazon.in", "- Flipkart").
     */
    private fun cleanTitle(rawTitle: String?): String? {
        if (rawTitle.isNullOrBlank()) return null
        var title = rawTitle.trim()

        val separators = listOf(" | ", " - ", " : ", " — ", " :: ")
        for (sep in separators) {
            if (title.contains(sep)) {
                val parts = title.split(sep)
                if (parts.first().length >= 5) {
                    title = parts.first().trim()
                    break
                }
            }
        }
        return title.takeIf { it.length >= 3 }
    }

    /**
     * Fallback URL Slug Parser if live HTML download fails or is blocked.
     */
    private fun extractFromUrlSlugFallback(url: String, merchantName: String): RawExtractedMetadata {
        val slugTitle = extractTitleFromSlug(url)
        return RawExtractedMetadata(
            title = slugTitle,
            merchantName = merchantName,
            canonicalUrl = url,
            extractionSource = "URL_SLUG_PARSER"
        )
    }

    private fun extractTitleFromSlug(url: String): String? {
        return try {
            val segments = url.split("/").filter { it.isNotBlank() }
            var rawSlug = ""

            val dpIndex = segments.indexOfFirst { it in listOf("dp", "gp", "product", "products", "p", "buy", "item", "pdp") }
            if (dpIndex > 0) {
                rawSlug = segments[dpIndex - 1]
            } else if (dpIndex == 0 && segments.size > 1) {
                rawSlug = segments[1]
            } else {
                val candidateSegments = segments.filter { seg ->
                    !seg.contains(".") && !seg.contains("?") && seg.length > 3 && seg !in listOf("p", "dp", "buy", "item", "product")
                }
                rawSlug = candidateSegments.maxByOrNull { it.length } ?: ""
            }

            if (rawSlug.contains("?")) rawSlug = rawSlug.substringBefore("?")
            if (rawSlug.isBlank()) return null

            var decoded = URLDecoder.decode(rawSlug, "UTF-8")
                .replace("-", " ")
                .replace("_", " ")
                .replace(Regex("\\b[A-Z0-9]{10}\\b"), "")
                .trim()

            decoded = decoded.split(" ").filter { it.isNotBlank() }.joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            decoded.takeIf { it.length >= 3 }
        } catch (e: Exception) {
            null
        }
    }
}
