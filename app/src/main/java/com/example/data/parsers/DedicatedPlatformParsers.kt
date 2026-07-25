package com.example.data.parsers

import com.example.data.extraction.RawExtractedMetadata
import java.util.regex.Pattern

/**
 * MASTER PHASE V4.5 — Platform Dedicated Parsers
 * Each e-commerce platform has its own dedicated parser.
 */

// 1. Flipkart Dedicated Parser
object FlipkartParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.FLIPKART

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractTitleFromHtml(htmlContent)
        val price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractPriceFromHtml(htmlContent)
        val image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractImageFromHtml(htmlContent)
        val brand = rawMeta?.brand ?: extractBrandFromTitle(title)
        val rawOrigPrice = rawMeta?.originalPrice
        val origPrice = if (rawOrigPrice != null && price != null && rawOrigPrice > price) rawOrigPrice else null
        val delivery = extractFlipkartDelivery(htmlContent)
        val rating = rawMeta?.rating?.let { if (it in 1.0..5.0) it else null }
        val reviews = rawMeta?.reviewsCount?.let { if (it > 0) it else null }

        val isVerified = !title.isNullOrBlank() && price != null && price > 0.0
        val confidence = if (isVerified) (if (!image.isNullOrBlank()) 98 else 85) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = brand,
            rating = rating,
            reviewsCount = reviews,
            availability = if (isVerified) "In Stock" else "Unavailable",
            deliveryInfo = delivery,
            merchantName = "Flipkart",
            isVerified = isVerified,
            confidenceScore = confidence,
            extractionSource = "FLIPKART_DEDICATED_PARSER"
        )
    }

    private fun extractTitleFromHtml(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("<span[^>]*class=[\"'][^\"']*B_NuT2[^\"']*[\"'][^>]*>(.*?)</span>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<h1[^>]*class=[\"'][^\"']*_6ERyO1[^\"']*[\"'][^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<span[^>]*class=[\"'][^\"']*_35KyA6[^\"']*[\"'][^>]*>(.*?)</span>", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractPriceFromHtml(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<div[^>]*class=[\"'][^\"']*_30jeq3[^\"']*[\"'][^>]*>₹?([0-9,]+)</div>", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toDoubleOrNull()
        }
        return null
    }

    private fun extractImageFromHtml(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<img[^>]*class=[\"'][^\"']*_396cs4[^\"']*[\"'][^>]*src=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val url = m.group(1)?.trim()
            if (isValidImageUrl(url)) return url
        }
        return null
    }

    private fun extractFlipkartDelivery(html: String?): String? {
        if (html.isNullOrBlank()) return null
        if (html.contains("Free delivery", ignoreCase = true)) return "Free Delivery"
        if (html.contains("Delivery by", ignoreCase = true)) {
            val p = Pattern.compile("Delivery by\\s*([^<.,]+)", Pattern.CASE_INSENSITIVE)
            val m = p.matcher(html)
            if (m.find()) return "Delivery by ${m.group(1)?.trim()}"
        }
        return null
    }
}

// 2. Amazon Dedicated Parser
object AmazonParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.AMAZON

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val asin = extractAsinFromUrl(url)
        var title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractTitleFromHtml(htmlContent)
        var price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractPriceFromHtml(htmlContent)
        var origPrice = rawMeta?.originalPrice?.let { if (it > (price ?: 0.0)) it else null } ?: extractMrpFromHtml(htmlContent, price)
        var image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractImageFromHtml(htmlContent)
        var brand = rawMeta?.brand ?: extractBrandFromHtml(htmlContent) ?: extractBrandFromTitle(title)
        var rating = rawMeta?.rating?.let { if (it in 1.0..5.0) it else null } ?: extractRatingFromHtml(htmlContent)
        var reviews = rawMeta?.reviewsCount?.let { if (it > 0) it else null } ?: extractReviewsFromHtml(htmlContent)
        var availability = extractAvailabilityFromHtml(htmlContent) ?: (if (!title.isNullOrBlank() && price != null && price > 0.0) "In Stock" else "Unavailable")
        var delivery = extractAmazonDelivery(htmlContent) ?: rawMeta?.deliveryInfo ?: "Free Amazon Delivery Available"
        var seller = extractSellerFromHtml(htmlContent) ?: rawMeta?.merchantName ?: "Amazon"

        // High resolution image URL optimization for Amazon
        image = optimizeAmazonImageUrl(image)

        // Validation check: Title must be non-empty and not a raw URL, price must be numeric & > 0
        val isValid = !title.isNullOrBlank() && price != null && price > 0.0

        val confidence = if (isValid) (if (!image.isNullOrBlank()) 98 else 85) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = brand,
            rating = rating,
            reviewsCount = reviews,
            availability = availability,
            deliveryInfo = delivery,
            merchantName = if (!seller.isNullOrBlank()) seller else "Amazon",
            modelNumber = asin,
            isVerified = isValid,
            confidenceScore = confidence,
            extractionSource = "AMAZON_DEDICATED_PARSER"
        )
    }

    private fun optimizeAmazonImageUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return url.replace(Regex("\\._AC_[A-Z0-9_,]+_\\."), "._AC_SL1500_.")
            .replace(Regex("\\._SX[0-9]+_\\."), "._AC_SL1500_.")
            .replace(Regex("\\._SY[0-9]+_\\."), "._AC_SL1500_.")
    }

    private fun extractAsinFromUrl(url: String): String? {
        val p = Pattern.compile("/(?:dp|gp/product|asin)/([A-Z0-9]{10})", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(url)
        if (m.find()) return m.group(1)
        val p2 = Pattern.compile("[?&]asin=([A-Z0-9]{10})", Pattern.CASE_INSENSITIVE)
        val m2 = p2.matcher(url)
        return if (m2.find()) m2.group(1) else null
    }

    private fun extractTitleFromHtml(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("id=[\"']productTitle[\"'][^>]*>(.*?)</", Pattern.CASE_INSENSITIVE or Pattern.DOTALL),
            Pattern.compile("id=[\"']title[\"'][^>]*>(.*?)</", Pattern.CASE_INSENSITIVE or Pattern.DOTALL),
            Pattern.compile("<meta[^>]*property=[\"']og:title[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.replace("&amp;", "&")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractPriceFromHtml(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("class=[\"'][^\"']*a-price-whole[^\"']*[\"'][^>]*>([0-9,]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("id=[\"']priceblock_ourprice[\"'][^>]*>₹?\\s*([0-9,]+(?:\\.[0-9]{2})?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("id=[\"']priceblock_dealprice[\"'][^>]*>₹?\\s*([0-9,]+(?:\\.[0-9]{2})?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("class=[\"']a-offscreen[\"'][^>]*>₹?\\s*([0-9,]+(?:\\.[0-9]{2})?)", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val priceStr = m.group(1)?.replace(",", "")?.trim()
                val parsed = priceStr?.toDoubleOrNull()
                if (parsed != null && parsed > 0) return parsed
            }
        }
        return null
    }

    private fun extractMrpFromHtml(html: String?, currentPrice: Double?): Double? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("class=[\"'][^\"']*a-text-price[^\"']*[\"'][^>]*>.*?class=[\"']a-offscreen[\"'][^>]*>₹?\\s*([0-9,]+(?:\\.[0-9]{2})?)", Pattern.CASE_INSENSITIVE or Pattern.DOTALL),
            Pattern.compile("class=[\"'][^\"']*basisPrice[^\"']*[\"'][^>]*>.*?class=[\"']a-offscreen[\"'][^>]*>₹?\\s*([0-9,]+(?:\\.[0-9]{2})?)", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val priceStr = m.group(1)?.replace(",", "")?.trim()
                val parsed = priceStr?.toDoubleOrNull()
                if (parsed != null && (currentPrice == null || parsed > currentPrice)) return parsed
            }
        }
        return null
    }

    private fun extractImageFromHtml(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("id=[\"']landingImage[\"'][^>]*src=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
            Pattern.compile("id=[\"']imgBlkFront[\"'][^>]*src=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
            Pattern.compile("data-old-hires=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<meta[^>]*property=[\"']og:image[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val img = m.group(1)?.trim()
                if (isValidImageUrl(img)) return img
            }
        }
        return null
    }

    private fun extractBrandFromHtml(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("id=[\"']bylineInfo[\"'][^>]*>(.*?)</", Pattern.CASE_INSENSITIVE or Pattern.DOTALL),
            Pattern.compile("class=[\"'][^\"']*po-brand[^\"']*[\"'][^>]*>(.*?)</", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val b = m.group(1)?.replace(Regex("<[^>]*>"), "")?.replace("Visit the", "")?.replace("Brand:", "")?.replace("Store", "")?.trim()
                if (!b.isNullOrBlank() && b.length in 2..40) return b
            }
        }
        return null
    }

    private fun extractRatingFromHtml(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("([0-9]\\.[0-9])\\s*out of 5 stars", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.toDoubleOrNull()
        }
        return null
    }

    private fun extractReviewsFromHtml(html: String?): Int? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("id=[\"']acrCustomerReviewText[\"'][^>]*>([0-9,]+)\\s*ratings?", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toIntOrNull()
        }
        return null
    }

    private fun extractAvailabilityFromHtml(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("id=[\"']availability[\"'][^>]*>(.*?)</", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        val m = p.matcher(html)
        if (m.find()) {
            val avail = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
            if (!avail.isNullOrBlank()) return avail
        }
        return null
    }

    private fun extractAmazonDelivery(html: String?): String? {
        if (html.isNullOrBlank()) return null
        if (html.contains("FREE delivery", ignoreCase = true)) return "FREE Delivery"
        val p = Pattern.compile("FREE delivery\\s*<b>([^<]+)</b>", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) return "FREE Delivery ${m.group(1)?.trim()}"
        return null
    }

    private fun extractSellerFromHtml(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("id=[\"']merchant-info[\"'][^>]*>(.*?)</", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        val m = p.matcher(html)
        if (m.find()) {
            val seller = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
            if (!seller.isNullOrBlank()) return seller
        }
        return "Amazon"
    }
}

// 3. Meesho Dedicated Parser
object MeeshoParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.MEESHO

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractHtmlTitle(htmlContent)
        val price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractHtmlPrice(htmlContent)
        val origPrice = rawMeta?.originalPrice?.let { if (it > (price ?: 0.0)) it else null } ?: extractHtmlMrp(htmlContent, price)
        val image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractHtmlImage(htmlContent)

        val isVerified = !title.isNullOrBlank() && price != null && price > 0.0
        val confidence = if (isVerified) (if (!image.isNullOrBlank()) 95 else 80) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = rawMeta?.brand ?: "Meesho Seller",
            rating = rawMeta?.rating,
            reviewsCount = rawMeta?.reviewsCount,
            availability = if (isVerified) "In Stock" else "Unavailable",
            deliveryInfo = rawMeta?.deliveryInfo ?: "Standard Meesho Delivery",
            merchantName = "Meesho",
            isVerified = isVerified,
            confidenceScore = confidence,
            extractionSource = "MEESHO_DEDICATED_PARSER"
        )
    }

    private fun extractHtmlTitle(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("<h1[^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL),
            Pattern.compile("<meta[^>]*property=[\"']og:title[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractHtmlPrice(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("₹\\s*([0-9,]+)", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toDoubleOrNull()
        }
        return null
    }

    private fun extractHtmlMrp(html: String?, currentPrice: Double?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("MRP\\s*₹?\\s*([0-9,]+)", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val parsed = m.group(1)?.replace(",", "")?.toDoubleOrNull()
            if (parsed != null && (currentPrice == null || parsed > currentPrice)) return parsed
        }
        return null
    }

    private fun extractHtmlImage(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:image[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val img = m.group(1)?.trim()
            if (isValidImageUrl(img)) return img
        }
        return null
    }
}

// 4. Myntra Dedicated Parser
object MyntraParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.MYNTRA

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractHtmlTitle(htmlContent)
        val price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractHtmlPrice(htmlContent)
        val origPrice = rawMeta?.originalPrice?.let { if (it > (price ?: 0.0)) it else null }
        val image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractHtmlImage(htmlContent)

        val isVerified = !title.isNullOrBlank() && price != null && price > 0.0
        val confidence = if (isVerified) (if (!image.isNullOrBlank()) 95 else 80) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = rawMeta?.brand ?: extractBrandFromTitle(title),
            rating = rawMeta?.rating,
            reviewsCount = rawMeta?.reviewsCount,
            availability = if (isVerified) "In Stock" else "Unavailable",
            deliveryInfo = rawMeta?.deliveryInfo ?: "Express Myntra Shipping",
            merchantName = "Myntra",
            isVerified = isVerified,
            confidenceScore = confidence,
            extractionSource = "MYNTRA_DEDICATED_PARSER"
        )
    }

    private fun extractHtmlTitle(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("<h1[^>]*class=[\"'][^\"']*pdp-name[^\"']*[\"'][^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<h1[^>]*class=[\"'][^\"']*pdp-title[^\"']*[\"'][^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<meta[^>]*property=[\"']og:title[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractHtmlPrice(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("class=[\"'][^\"']*pdp-price[^\"']*[\"'][^>]*>.*?₹?\\s*([0-9,]+)", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toDoubleOrNull()
        }
        return null
    }

    private fun extractHtmlImage(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:image[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val img = m.group(1)?.trim()
            if (isValidImageUrl(img)) return img
        }
        return null
    }
}

// 5. Ajio Dedicated Parser
object AjioParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.AJIO

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractHtmlTitle(htmlContent)
        val price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractHtmlPrice(htmlContent)
        val origPrice = rawMeta?.originalPrice?.let { if (it > (price ?: 0.0)) it else null }
        val image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractHtmlImage(htmlContent)

        val isVerified = !title.isNullOrBlank() && price != null && price > 0.0
        val confidence = if (isVerified) (if (!image.isNullOrBlank()) 95 else 80) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = rawMeta?.brand ?: extractBrandFromTitle(title),
            rating = rawMeta?.rating,
            reviewsCount = rawMeta?.reviewsCount,
            availability = if (isVerified) "In Stock" else "Unavailable",
            deliveryInfo = rawMeta?.deliveryInfo ?: "Ajio Direct Delivery",
            merchantName = "AJIO",
            isVerified = isVerified,
            confidenceScore = confidence,
            extractionSource = "AJIO_DEDICATED_PARSER"
        )
    }

    private fun extractHtmlTitle(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("<h1[^>]*class=[\"'][^\"']*prod-name[^\"']*[\"'][^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<meta[^>]*property=[\"']og:title[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractHtmlPrice(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("class=[\"'][^\"']*prod-sp[^\"']*[\"'][^>]*>.*?₹?\\s*([0-9,]+)", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toDoubleOrNull()
        }
        return null
    }

    private fun extractHtmlImage(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:image[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val img = m.group(1)?.trim()
            if (isValidImageUrl(img)) return img
        }
        return null
    }
}

// 6. Nykaa Dedicated Parser
object NykaaParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.NYKAA

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractHtmlTitle(htmlContent)
        val price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractHtmlPrice(htmlContent)
        val origPrice = rawMeta?.originalPrice?.let { if (it > (price ?: 0.0)) it else null }
        val image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractHtmlImage(htmlContent)

        val isVerified = !title.isNullOrBlank() && price != null && price > 0.0
        val confidence = if (isVerified) (if (!image.isNullOrBlank()) 95 else 80) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = rawMeta?.brand ?: extractBrandFromTitle(title),
            rating = rawMeta?.rating,
            reviewsCount = rawMeta?.reviewsCount,
            availability = if (isVerified) "In Stock" else "Unavailable",
            deliveryInfo = rawMeta?.deliveryInfo ?: "Nykaa Beauty Express",
            merchantName = "Nykaa",
            isVerified = isVerified,
            confidenceScore = confidence,
            extractionSource = "NYKAA_DEDICATED_PARSER"
        )
    }

    private fun extractHtmlTitle(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("<h1[^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL),
            Pattern.compile("<meta[^>]*property=[\"']og:title[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractHtmlPrice(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("₹\\s*([0-9,]+)", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toDoubleOrNull()
        }
        return null
    }

    private fun extractHtmlImage(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:image[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val img = m.group(1)?.trim()
            if (isValidImageUrl(img)) return img
        }
        return null
    }
}

// 7. Snapdeal Dedicated Parser
object SnapdealParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.SNAPDEAL

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractHtmlTitle(htmlContent)
        val price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractHtmlPrice(htmlContent)
        val origPrice = rawMeta?.originalPrice?.let { if (it > (price ?: 0.0)) it else null }
        val image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractHtmlImage(htmlContent)

        val isVerified = !title.isNullOrBlank() && price != null && price > 0.0
        val confidence = if (isVerified) (if (!image.isNullOrBlank()) 95 else 80) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = rawMeta?.brand ?: extractBrandFromTitle(title),
            rating = rawMeta?.rating,
            reviewsCount = rawMeta?.reviewsCount,
            availability = if (isVerified) "In Stock" else "Unavailable",
            deliveryInfo = rawMeta?.deliveryInfo ?: "Snapdeal Delivery",
            merchantName = "Snapdeal",
            isVerified = isVerified,
            confidenceScore = confidence,
            extractionSource = "SNAPDEAL_DEDICATED_PARSER"
        )
    }

    private fun extractHtmlTitle(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("<h1[^>]*class=[\"'][^\"']*pdp-e-i-head[^\"']*[\"'][^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<meta[^>]*property=[\"']og:title[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractHtmlPrice(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("class=[\"'][^\"']*payBlkSSP[^\"']*[\"'][^>]*>.*?₹?\\s*([0-9,]+)", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toDoubleOrNull()
        }
        return null
    }

    private fun extractHtmlImage(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:image[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val img = m.group(1)?.trim()
            if (isValidImageUrl(img)) return img
        }
        return null
    }
}

// 8. Shopify Store Parser
object ShopifyParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.SHOPIFY

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractHtmlTitle(htmlContent)
        val price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractHtmlPrice(htmlContent)
        val origPrice = rawMeta?.originalPrice?.let { if (it > (price ?: 0.0)) it else null }
        val image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractHtmlImage(htmlContent)
        val storeName = rawMeta?.merchantName ?: extractStoreNameFromUrl(url)

        val isVerified = !title.isNullOrBlank() && price != null && price > 0.0
        val confidence = if (isVerified) (if (!image.isNullOrBlank()) 90 else 75) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = rawMeta?.brand ?: storeName,
            rating = rawMeta?.rating,
            reviewsCount = rawMeta?.reviewsCount,
            availability = if (isVerified) "In Stock" else "Unavailable",
            deliveryInfo = rawMeta?.deliveryInfo ?: "Standard Store Delivery",
            merchantName = storeName,
            isVerified = isVerified,
            confidenceScore = confidence,
            extractionSource = "SHOPIFY_DEDICATED_PARSER"
        )
    }

    private fun extractHtmlTitle(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("<meta[^>]*property=[\"']og:title[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<h1[^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractHtmlPrice(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:price:amount[\"'][^>]*content=[\"']([0-9.,]+)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toDoubleOrNull()
        }
        return null
    }

    private fun extractHtmlImage(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:image[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val img = m.group(1)?.trim()
            if (isValidImageUrl(img)) return img
        }
        return null
    }

    private fun extractStoreNameFromUrl(url: String): String {
        return try {
            val host = java.net.URI(url).host ?: ""
            host.removePrefix("www.").substringBefore(".myshopify.com").substringBefore(".").capitalize()
        } catch (e: Exception) {
            "Shopify Store"
        }
    }
}

// 9. Official Brand Store Parser
object OfficialBrandStoreParser : IShoppingPlatformParser {
    override val platform = ShoppingPlatform.OFFICIAL_BRAND

    override fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData {
        val title = rawMeta?.title?.takeIf { isValidTitle(it) } ?: extractHtmlTitle(htmlContent)
        val price = rawMeta?.currentPrice?.let { if (it > 0.0) it else null } ?: extractHtmlPrice(htmlContent)
        val origPrice = rawMeta?.originalPrice?.let { if (it > (price ?: 0.0)) it else null }
        val image = rawMeta?.imageUrl?.takeIf { isValidImageUrl(it) } ?: extractHtmlImage(htmlContent)
        val storeName = rawMeta?.merchantName ?: extractStoreNameFromUrl(url)

        val isVerified = !title.isNullOrBlank() && price != null && price > 0.0
        val confidence = if (isVerified) (if (!image.isNullOrBlank()) 85 else 70) else 0

        return ExtractedProductData(
            platform = platform,
            title = title,
            currentPrice = price,
            originalPrice = origPrice,
            imageUrl = image,
            brand = rawMeta?.brand ?: storeName,
            rating = rawMeta?.rating,
            reviewsCount = rawMeta?.reviewsCount,
            availability = if (isVerified) "In Stock" else "Unavailable",
            deliveryInfo = rawMeta?.deliveryInfo ?: "Brand Store Shipping",
            merchantName = storeName,
            isVerified = isVerified,
            confidenceScore = confidence,
            extractionSource = "OFFICIAL_BRAND_PARSER"
        )
    }

    private fun extractHtmlTitle(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val patterns = listOf(
            Pattern.compile("<meta[^>]*property=[\"']og:title[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(html)
            if (m.find()) {
                val t = m.group(1)?.replace(Regex("<[^>]*>"), "")?.trim()
                if (isValidTitle(t)) return t
            }
        }
        return null
    }

    private fun extractHtmlPrice(html: String?): Double? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:price:amount[\"'][^>]*content=[\"']([0-9.,]+)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            return m.group(1)?.replace(",", "")?.toDoubleOrNull()
        }
        return null
    }

    private fun extractHtmlImage(html: String?): String? {
        if (html.isNullOrBlank()) return null
        val p = Pattern.compile("<meta[^>]*property=[\"']og:image[\"'][^>]*content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(html)
        if (m.find()) {
            val img = m.group(1)?.trim()
            if (isValidImageUrl(img)) return img
        }
        return null
    }

    private fun extractStoreNameFromUrl(url: String): String {
        return try {
            val host = java.net.URI(url).host ?: ""
            host.removePrefix("www.").substringBefore(".").capitalize()
        } catch (e: Exception) {
            "Official Store"
        }
    }
}

// Helper validation functions
private fun isValidTitle(title: String?): Boolean {
    if (title.isNullOrBlank()) return false
    val t = title.trim()
    if (t.length < 3) return false
    if (t.startsWith("http://") || t.startsWith("https://") || t.contains("://") || t.startsWith("www.")) return false
    val invalid = setOf("null", "undefined", "access denied", "page not found", "404", "error", "untitled", "product")
    if (invalid.any { t.equals(it, ignoreCase = true) }) return false
    return true
}

private fun isValidImageUrl(img: String?): Boolean {
    if (img.isNullOrBlank()) return false
    val i = img.trim()
    if (!i.startsWith("http://") && !i.startsWith("https://")) return false
    if (i.contains("pixel") || i.contains("spacer") || i.contains("1x1")) return false
    return true
}

private fun extractBrandFromTitle(title: String?): String? {
    if (title.isNullOrBlank()) return null
    val firstWord = title.trim().split(" ").firstOrNull()
    return if (!firstWord.isNullOrBlank() && firstWord.length >= 2) firstWord else null
}
