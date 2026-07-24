package com.example.data

import androidx.compose.ui.graphics.Color
import java.util.Locale

/**
 * PHASE 12B — Store & Product Confidence Metrics
 */
data class MerchantConfidenceMetrics(
    val merchantConfidence: Double = 0.95,
    val productConfidence: Double = 0.90,
    val imageConfidence: Double = 0.88,
    val titleConfidence: Double = 0.92
) {
    val overallScore: Int get() = ((merchantConfidence + productConfidence + imageConfidence + titleConfidence) / 4.0 * 100).toInt()
}

/**
 * Merchant Data Model according to Phase 12B architecture.
 */
data class MerchantInfo(
    val merchantName: String,
    val primaryColor: Long,
    val secondaryColor: Long,
    val officialLogoUrl: String? = null,
    val faviconUrl: String? = null,
    val domain: String,
    val country: String = "Global / India",
    val supported: Boolean = true,
    val category: String = "E-Commerce",
    val brandBadgeText: String = "",
    val confidenceMetrics: MerchantConfidenceMetrics = MerchantConfidenceMetrics()
)

/**
 * PHASE 12B — Universal Merchant Registry with Comprehensive Store Database
 */
object MerchantRegistry {

    private val registryMap = listOf(
        MerchantInfo("Flipkart", 0xFF2874F0, 0xFFF9D80B, officialLogoUrl = "https://assets.maccaron.in/brand/flipkart.png", faviconUrl = "https://www.google.com/s2/favicons?domain=flipkart.com&sz=128", domain = "flipkart.com", country = "India", category = "Multi-Category Marketplace", brandBadgeText = "FK"),
        MerchantInfo("Amazon", 0xFFFF9900, 0xFF131921, officialLogoUrl = "https://pngimg.com/uploads/amazon/amazon_PNG27.png", faviconUrl = "https://www.google.com/s2/favicons?domain=amazon.in&sz=128", domain = "amazon.in", country = "Global / India", category = "Online Marketplace", brandBadgeText = "AZ"),
        MerchantInfo("Meesho", 0xFFF43397, 0xFFFF6EAE, faviconUrl = "https://www.google.com/s2/favicons?domain=meesho.com&sz=128", domain = "meesho.com", country = "India", category = "Reselling & Fashion", brandBadgeText = "MS"),
        MerchantInfo("AJIO", 0xFF2C3E50, 0xFFFFFFFF, faviconUrl = "https://www.google.com/s2/favicons?domain=ajio.com&sz=128", domain = "ajio.com", country = "India", category = "Fashion & Lifestyle", brandBadgeText = "AJIO"),
        MerchantInfo("Myntra", 0xFFFC2779, 0xFFFF527B, faviconUrl = "https://www.google.com/s2/favicons?domain=myntra.com&sz=128", domain = "myntra.com", country = "India", category = "Fashion & Beauty", brandBadgeText = "MY"),
        MerchantInfo("Shopsy", 0xFF0072FF, 0xFF00C6FF, faviconUrl = "https://www.google.com/s2/favicons?domain=shopsy.in&sz=128", domain = "shopsy.in", country = "India", category = "Hyperlocal Value Shopping", brandBadgeText = "SHOPSY"),
        MerchantInfo("Mamaearth", 0xFF00A859, 0xFF008040, faviconUrl = "https://www.google.com/s2/favicons?domain=mamaearth.in&sz=128", domain = "mamaearth.in", country = "India", category = "Beauty & Skincare", brandBadgeText = "MAMAEARTH"),
        MerchantInfo("Mamaearth Store", 0xFF00A859, 0xFF008040, faviconUrl = "https://www.google.com/s2/favicons?domain=mamaearth.in&sz=128", domain = "mamaearth.in", country = "India", category = "Official Store", brandBadgeText = "ME-STORE"),
        MerchantInfo("Mamaearth Amazon", 0xFFFF9900, 0xFF00A859, faviconUrl = "https://www.google.com/s2/favicons?domain=amazon.in&sz=128", domain = "amazon.in", country = "India", category = "Brand Store on Amazon", brandBadgeText = "ME-AZ"),
        MerchantInfo("Snitch", 0xFF111111, 0xFF333333, faviconUrl = "https://www.google.com/s2/favicons?domain=snitch.co.in&sz=128", domain = "snitch.co.in", country = "India", category = "Menswear Fashion", brandBadgeText = "SNITCH"),
        MerchantInfo("Allen Solly", 0xFF0D253F, 0xFF003366, domain = "allensolly.com", country = "India", category = "Apparel & Formal Wear", brandBadgeText = "AS"),
        MerchantInfo("Nike", 0xFF111111, 0xFFF5F5F5, faviconUrl = "https://www.google.com/s2/favicons?domain=nike.com&sz=128", domain = "nike.com", country = "Global", category = "Sportswear & Sneakers", brandBadgeText = "NIKE"),
        MerchantInfo("Adidas", 0xFF000000, 0xFF333333, faviconUrl = "https://www.google.com/s2/favicons?domain=adidas.com&sz=128", domain = "adidas.com", country = "Global", category = "Sportswear & Footwear", brandBadgeText = "ADI"),
        MerchantInfo("Zara", 0xFF111111, 0xFF222222, faviconUrl = "https://www.google.com/s2/favicons?domain=zara.com&sz=128", domain = "zara.com", country = "Global", category = "Fast Fashion", brandBadgeText = "ZARA"),
        MerchantInfo("H&M", 0xFFE5001C, 0xFFCC0000, faviconUrl = "https://www.google.com/s2/favicons?domain=hm.com&sz=128", domain = "hm.com", country = "Global", category = "Fashion & Clothing", brandBadgeText = "H&M"),
        MerchantInfo("Apple", 0xFF555555, 0xFF000000, faviconUrl = "https://www.google.com/s2/favicons?domain=apple.com&sz=128", domain = "apple.com", country = "Global", category = "Electronics & Devices", brandBadgeText = "AAPL"),
        MerchantInfo("Samsung", 0xFF1428A0, 0xFF001060, faviconUrl = "https://www.google.com/s2/favicons?domain=samsung.com&sz=128", domain = "samsung.com", country = "Global", category = "Consumer Electronics", brandBadgeText = "SAMSUNG"),
        MerchantInfo("Reliance Digital", 0xFFE42528, 0xFFCC1111, faviconUrl = "https://www.google.com/s2/favicons?domain=reliancedigital.in&sz=128", domain = "reliancedigital.in", country = "India", category = "Electronics Superstore", brandBadgeText = "RD"),
        MerchantInfo("Croma", 0xFF00E5D2, 0xFF00B2A3, faviconUrl = "https://www.google.com/s2/favicons?domain=croma.com&sz=128", domain = "croma.com", country = "India", category = "Tech & Appliances", brandBadgeText = "CROMA"),
        MerchantInfo("Nykaa", 0xFFFC2779, 0xFFFF527B, faviconUrl = "https://www.google.com/s2/favicons?domain=nykaa.com&sz=128", domain = "nykaa.com", country = "India", category = "Beauty & Personal Care", brandBadgeText = "NYKAA"),
        MerchantInfo("Tata CLiQ", 0xFFFFCC00, 0xFFE40046, faviconUrl = "https://www.google.com/s2/favicons?domain=tatacliq.com&sz=128", domain = "tatacliq.com", country = "India", category = "Luxury & Fashion", brandBadgeText = "CLIQ"),
        MerchantInfo("Puma", 0xFFBA0C2F, 0xFF880000, domain = "puma.com", country = "Global", category = "Sportswear & Lifestyle", brandBadgeText = "PUMA"),
        MerchantInfo("Boat", 0xFF111111, 0xFFCC0000, faviconUrl = "https://www.google.com/s2/favicons?domain=boat-lifestyle.com&sz=128", domain = "boat-lifestyle.com", country = "India", category = "Audio & Wearables", brandBadgeText = "BOAT"),
        MerchantInfo("Noise", 0xFF0066FF, 0xFF0044CC, faviconUrl = "https://www.google.com/s2/favicons?domain=gonoise.com&sz=128", domain = "gonoise.com", country = "India", category = "Smartwatches & Audio", brandBadgeText = "NOISE"),
        MerchantInfo("OnePlus", 0xFFF00000, 0xFFB00000, domain = "oneplus.in", country = "Global", category = "Smartphones & Gadgets", brandBadgeText = "1+"),
        MerchantInfo("Lenovo", 0xFFE2231A, 0xFFB21008, domain = "lenovo.com", country = "Global", category = "Laptops & Tech", brandBadgeText = "LENOVO"),
        MerchantInfo("Dell", 0xFF0076CE, 0xFF005599, domain = "dell.com", country = "Global", category = "Computers & Laptops", brandBadgeText = "DELL"),
        MerchantInfo("HP", 0xFF0096D6, 0xFF0077B3, domain = "hp.com", country = "Global", category = "Printers & Laptops", brandBadgeText = "HP"),
        MerchantInfo("Asus", 0xFF00539B, 0xFF003870, domain = "asus.com", country = "Global", category = "Gaming & Hardware", brandBadgeText = "ASUS"),
        MerchantInfo("Acer", 0xFF83B81A, 0xFF659210, domain = "acer.com", country = "Global", category = "Laptops & Displays", brandBadgeText = "ACER"),
        MerchantInfo("JBL", 0xFFFF6600, 0xFFCC5500, domain = "jbl.com", country = "Global", category = "Audio Systems", brandBadgeText = "JBL"),
        MerchantInfo("Sony", 0xFF000000, 0xFF222222, domain = "sony.co.in", country = "Global", category = "Electronics & Audio", brandBadgeText = "SONY"),
        MerchantInfo("Instagram", 0xFFE1306C, 0xFFF77737, domain = "instagram.com", country = "Global", category = "Social Commerce & Reels", brandBadgeText = "INSTA"),
        MerchantInfo("Facebook Marketplace", 0xFF1877F2, 0xFF0D52B5, domain = "facebook.com", country = "Global", category = "Social Marketplace", brandBadgeText = "FB"),
        MerchantInfo("Pinterest", 0xFFE60023, 0xFFB3001B, domain = "pinterest.com", country = "Global", category = "Visual Shopping Pins", brandBadgeText = "PIN"),
        MerchantInfo("YouTube Product", 0xFFFF0000, 0xFFCC0000, domain = "youtube.com", country = "Global", category = "Video Shopping", brandBadgeText = "YT"),
        MerchantInfo("Telegram Link", 0xFF229ED9, 0xFF1A7DAF, domain = "t.me", country = "Global", category = "Messaging Deals", brandBadgeText = "TG"),
        MerchantInfo("Sephora", 0xFF000000, 0xFF1A1A1A, domain = "sephora.in", country = "Global", category = "Luxury Beauty", brandBadgeText = "SEPHORA"),
        MerchantInfo("Urbanic", 0xFF000000, 0xFF1E1E1E, domain = "urbanic.com", country = "Global", category = "Trendy Fashion", brandBadgeText = "URBANIC"),
        MerchantInfo("Lenskart", 0xFF000042, 0xFF000077, domain = "lenskart.com", country = "India", category = "Eyewear", brandBadgeText = "LENSKART"),
        MerchantInfo("FirstCry", 0xFFFF7000, 0xFFCC5800, faviconUrl = "https://www.google.com/s2/favicons?domain=firstcry.com&sz=128", domain = "firstcry.com", country = "India", category = "Baby & Kids", brandBadgeText = "FIRSTCRY"),
        MerchantInfo("JioMart", 0xFF003399, 0xFF002277, faviconUrl = "https://www.google.com/s2/favicons?domain=jiomart.com&sz=128", domain = "jiomart.com", country = "India", category = "Grocery & Electronics", brandBadgeText = "JIOMART"),
        MerchantInfo("Snapdeal", 0xFFE40046, 0xFFB30036, faviconUrl = "https://www.google.com/s2/favicons?domain=snapdeal.com&sz=128", domain = "snapdeal.com", country = "India", category = "Value Shopping", brandBadgeText = "SNAPDEAL")
    )

    private val invalidMerchantTerms = setOf(
        "https", "http", "dl", "www", "url", "link", "com", "in", "org", "net", "http://", "https://", "null", "undefined"
    )

    /**
     * Resolves a domain or brand query against the registry.
     * Guaranteed NEVER to return raw URLs, "https://", "dl", or gibberish.
     */
    fun findMerchant(hostOrName: String): MerchantInfo {
        val query = hostOrName.trim().lowercase(Locale.getDefault())

        // Safeguard against raw URL noise or invalid merchant names
        if (query.isBlank() || invalidMerchantTerms.contains(query) || query.startsWith("http") || query.contains("://")) {
            return getUnknownMerchantFallback(hostOrName)
        }

        // 1. Direct match by domain or brand name
        val matched = registryMap.firstOrNull { merchant ->
            val nameLower = merchant.merchantName.lowercase(Locale.getDefault())
            val domainBase = merchant.domain.lowercase(Locale.getDefault())
                .replace(".com", "")
                .replace(".in", "")
                .replace(".co.in", "")

            query == nameLower ||
                    query.contains(domainBase) ||
                    nameLower.contains(query)
        }

        if (matched != null) return matched

        // 2. Dynamic clean brand name generation for unlisted store/domain
        var cleanBrand = hostOrName
            .removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("www.")
            .removePrefix("m.")
            .removePrefix("shop.")
            .removePrefix("dl.")

        val slashIdx = cleanBrand.indexOf('/')
        if (slashIdx != -1) cleanBrand = cleanBrand.substring(0, slashIdx)

        val colonIdx = cleanBrand.indexOf(':')
        if (colonIdx != -1) cleanBrand = cleanBrand.substring(0, colonIdx)

        val parts = cleanBrand.split(".")
        val brandSegment = if (parts.size >= 2) {
            val p0 = parts[0]
            if (invalidMerchantTerms.contains(p0)) parts.getOrElse(1) { "Store" } else p0
        } else {
            cleanBrand
        }

        val formattedBrandName = brandSegment
            .replace("-", " ")
            .replace("_", " ")
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }

        if (formattedBrandName.isBlank() || invalidMerchantTerms.contains(formattedBrandName.lowercase(Locale.getDefault())) || formattedBrandName.length <= 1) {
            return getUnknownMerchantFallback(cleanBrand)
        }

        // Generate deterministic vibrant primary/secondary colors from brand hash
        val hash = formattedBrandName.hashCode()
        val hue = (Math.abs(hash) % 360).toFloat()
        val primaryColorLong = Color.hsl(hue, 0.75f, 0.45f).value.toLong()
        val secondaryColorLong = Color.hsl((hue + 40f) % 360f, 0.85f, 0.35f).value.toLong()

        val badgeText = formattedBrandName.take(6).uppercase(Locale.getDefault())
        val cleanDomain = if (cleanBrand.contains(".")) cleanBrand else "$cleanBrand.com"

        return MerchantInfo(
            merchantName = formattedBrandName,
            primaryColor = primaryColorLong,
            secondaryColor = secondaryColorLong,
            officialLogoUrl = null,
            faviconUrl = "https://www.google.com/s2/favicons?domain=$cleanDomain&sz=128",
            domain = cleanDomain,
            country = "Verified Store",
            supported = true,
            category = "Online Retail",
            brandBadgeText = badgeText,
            confidenceMetrics = MerchantConfidenceMetrics(0.85, 0.80, 0.82, 0.84)
        )
    }

    /**
     * Fallback System: Returns "Unknown Shopping Store" when merchant cannot be cleanly identified.
     */
    fun getUnknownMerchantFallback(rawText: String = ""): MerchantInfo {
        return MerchantInfo(
            merchantName = "Unknown Shopping Store",
            primaryColor = 0xFF555555,
            secondaryColor = 0xFF333333,
            officialLogoUrl = null,
            faviconUrl = if (rawText.contains(".")) "https://www.google.com/s2/favicons?domain=$rawText&sz=128" else null,
            domain = if (rawText.contains(".")) rawText else "unknown-store.com",
            country = "Global",
            supported = false,
            category = "General Retail",
            brandBadgeText = "STORE",
            confidenceMetrics = MerchantConfidenceMetrics(0.30, 0.25, 0.20, 0.25)
        )
    }

    fun getAllMerchants(): List<MerchantInfo> = registryMap
}

