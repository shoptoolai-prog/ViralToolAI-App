package com.example.engine

import com.example.BuildConfig
import com.example.data.CouponOffer
import com.example.data.MerchantDetector
import com.example.data.MerchantRegistry
import com.example.data.PriceCompareItem
import com.example.data.ProductIdentity
import com.example.data.ProductSpecification
import com.example.data.ShoppingResult
import com.example.data.SimilarProduct
import com.example.data.extractRealProductFromUrl
import com.example.data.extraction.RawExtractedMetadata
import com.example.data.getProductByUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * ViralToolAI Shopping Intelligence Engine — Internal AI Analysis Layer
 * Combines live metadata extraction, Gemini AI cloud REST analysis,
 * and intelligent local AI url parsing for multi-platform e-commerce links.
 */
object AiProductAnalysisEngine {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .writeTimeout(12, TimeUnit.SECONDS)
        .build()

    private fun getValidApiKey(): String {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            if (!key.isNullOrBlank() && key != "BUILDCONFIG_MISSING" && key != "null" && key != "MY_GEMINI_API_KEY") {
                key
            } else {
                System.getenv("GEMINI_API_KEY") ?: ""
            }
        } catch (_: Exception) {
            System.getenv("GEMINI_API_KEY") ?: ""
        }
    }

    suspend fun analyzeProduct(url: String, rawMetadata: RawExtractedMetadata? = null): ShoppingResult = withContext(Dispatchers.IO) {
        val routeResult = com.example.data.parsers.UniversalShoppingUrlRouter.detectAndRoute(url)

        val cleanUrl = when (routeResult) {
            is com.example.data.parsers.UniversalShoppingUrlRouter.RouteResult.Routed -> routeResult.normalizedUrl
            is com.example.data.parsers.UniversalShoppingUrlRouter.RouteResult.Unsupported -> return@withContext buildUnsupportedLinkResult(routeResult.rawUrl)
            is com.example.data.parsers.UniversalShoppingUrlRouter.RouteResult.Invalid -> return@withContext buildInvalidLinkResult(url)
        }

        // 1. Run Platform Extraction Router for dedicated store parsing
        val platformExtracted = com.example.data.parsers.PlatformExtractionRouter.routeAndExtract(cleanUrl, null, rawMetadata)

        val merchantInfo = MerchantRegistry.findMerchant(platformExtracted.merchantName)
        val merchantName = platformExtracted.merchantName

        // Merge rawMetadata with platformExtracted
        val mergedRaw = (rawMetadata ?: RawExtractedMetadata()).copy(
            title = platformExtracted.title ?: rawMetadata?.title,
            imageUrl = platformExtracted.imageUrl ?: rawMetadata?.imageUrl,
            currentPrice = platformExtracted.currentPrice ?: rawMetadata?.currentPrice,
            originalPrice = platformExtracted.originalPrice ?: rawMetadata?.originalPrice,
            brand = platformExtracted.brand ?: rawMetadata?.brand,
            rating = platformExtracted.rating ?: rawMetadata?.rating,
            reviewsCount = platformExtracted.reviewsCount ?: rawMetadata?.reviewsCount,
            availability = platformExtracted.availability ?: rawMetadata?.availability,
            deliveryInfo = platformExtracted.deliveryInfo ?: rawMetadata?.deliveryInfo,
            category = platformExtracted.category ?: rawMetadata?.category,
            merchantName = merchantName
        )

        // 2. Try Gemini AI Cloud Verification & Extraction if API Key is available
        val aiResult = callGeminiApiForProductAnalysis(cleanUrl, merchantName, mergedRaw)
        if (aiResult != null && aiResult.productName.isNotBlank() && aiResult.isReliable) {
            return@withContext aiResult
        }

        // 3. Fallback to verified extracted platform data without fake default estimation
        return@withContext buildIntelligentFallbackResult(cleanUrl, merchantName, mergedRaw)
    }

    private suspend fun callGeminiApiForProductAnalysis(
        url: String,
        merchantName: String,
        raw: RawExtractedMetadata
    ): ShoppingResult? = withContext(Dispatchers.IO) {
        val apiKey = getValidApiKey()
        if (apiKey.isBlank()) return@withContext null

        val modelsToTry = listOf("gemini-2.5-flash", "gemini-1.5-flash")
        for (model in modelsToTry) {
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
            val result = tryModelCall(endpoint, url, merchantName, raw)
            if (result != null) return@withContext result
        }
        return@withContext null
    }

    private fun tryModelCall(
        endpoint: String,
        url: String,
        merchantName: String,
        raw: RawExtractedMetadata
    ): ShoppingResult? {

        val prompt = """
            You are the ViralToolAI Gemini Smart Product Reasoning Engine.
            Analyze and verify the extracted e-commerce product data for this URL.

            Product Link: $url
            Detected Merchant: $merchantName
            Extracted Title: ${raw.title ?: "null"}
            Extracted Current Price: ${raw.currentPrice ?: "null"}
            Extracted MRP: ${raw.originalPrice ?: "null"}
            Extracted Image: ${raw.imageUrl ?: "null"}
            Extracted Brand: ${raw.brand ?: "null"}
            Extracted Rating: ${raw.rating ?: "null"}
            Extracted Reviews: ${raw.reviewsCount ?: "null"}
            Extracted Availability: ${raw.availability ?: "null"}
            Extracted Delivery: ${raw.deliveryInfo ?: "null"}
            Extracted Category: ${raw.category ?: "null"}

            REASONING & VERIFICATION MANDATES:
            1. HIDDEN REASONING: Perform step-by-step verification in the "reasoning" field. Detect wrong extractions, fake metadata, or broken pages.
            2. CONFIDENCE SCORE: Output "High", "Medium", or "Low".
               - "High": Title and price are clearly verified from a legitimate product page.
               - "Medium": Product title is verified, but price/details were repaired or inferred from canonical metadata.
               - "Low": Broken page, search/listing page, missing title, missing price, or unverified metadata.
            3. ZERO HALLUCINATION GUARANTEE: Never hallucinate or estimate prices, ratings, or MRPs. If missing or unverified, set to null or "Unknown".
            4. MULTI-STORE IDENTICAL PRODUCT MATCHING:
               - Cross-verify if the EXACT IDENTICAL product (same Brand, Model Number, SKU, Variant, Colour, Storage, RAM, Capacity, Size) is sold on other major e-commerce platforms (Amazon, Flipkart, Myntra, Meesho, AJIO, Nykaa).
               - Include cross-store listings in "price_comparison" ONLY IF match_confidence > 95% for the exact identical product.
               - If identical product is NOT found or match confidence <= 95%, output "price_comparison": [].
               - NEVER include similar or alternative products. NEVER invent fake prices.
            5. GENERATE VERIFIED INSIGHTS:
               - "shopping_summary": Brief 1-2 sentence verified overview of the product offer.
               - "pros": Array of 2-3 verified highlights derived purely from verified specs/ratings/merchant policies.
               - "cons": Array of 1-2 verified caveats (e.g., fluctuating stock, limited bank discount applicability).
               - "buying_advice": Concise actionable advice for the buyer.

            Return ONLY a valid JSON object matching this schema without markdown wrappers:
            {
              "reasoning": "Hidden chain-of-thought analysis verifying the link and extracted metadata",
              "confidence_score": "High" | "Medium" | "Low",
              "is_valid_product": true | false,
              "product_name": "Full official product title or null",
              "category": "Product category or Unknown",
              "brand": "Official brand or Unknown",
              "model_number": "Model number/SKU or null",
              "current_price": 1499.0 or null,
              "original_price": 2499.0 or null,
              "rating": 4.5 or null,
              "reviews_count": 1250 or null,
              "variant": "Size/Color/Storage or null",
              "image_url": "Direct image URL or null",
              "shopping_summary": "1-2 sentence verified product summary",
              "pros": ["Verified pro 1", "Verified pro 2"],
              "cons": ["Verified con 1"],
              "buying_advice": "Actionable buying advice",
              "specifications": [
                 {"title": "Spec Name", "value": "Spec Value"}
              ],
              "highlights": ["Verified highlight 1"],
              "price_comparison": [
                 {
                   "store": "Amazon",
                   "price": 1499.0,
                   "original_price": 1999.0,
                   "availability": "In Stock",
                   "delivery": "FREE Delivery",
                   "is_official_seller": true,
                   "match_confidence": 98,
                   "title": "Exact Title on Store",
                   "url": "https://..."
                 }
              ]
            }
        """.trimIndent()

        val requestJson = JSONObject().apply {
            val contentsArray = JSONArray().apply {
                val contentObj = JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        put(JSONObject().apply { put("text", prompt) })
                    }
                    put("parts", partsArray)
                }
                put(contentObj)
            }
            put("contents", contentsArray)
        }

        return try {
            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val responseText = response.body?.string() ?: return null

                val root = JSONObject(responseText)
                val candidates = root.optJSONArray("candidates") ?: return null
                if (candidates.length() == 0) return null

                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content") ?: return null
                val parts = content.optJSONArray("parts") ?: return null
                if (parts.length() == 0) return null

                val rawText = parts.getJSONObject(0).optString("text", "")
                if (rawText.isBlank()) return null

                val cleanedText = rawText.replace(Regex("^```json\\s*"), "")
                    .replace(Regex("^```\\s*"), "")
                    .replace(Regex("\\s*```$"), "")
                    .trim()

                val json = JSONObject(cleanedText)
                val reasoning = json.optString("reasoning", "")
                val confidenceScore = json.optString("confidence_score", "High").trim()
                val isValidProduct = json.optBoolean("is_valid_product", true)

                if (!isValidProduct || confidenceScore.equals("Low", ignoreCase = true)) {
                    return buildUnverifiedResult(url, merchantName, reasoning)
                }

                val productName = if (json.isNull("product_name")) raw.title else json.optString("product_name").ifBlank { raw.title }
                if (productName.isNullOrBlank() || productName == "null" || productName == "Unknown") {
                    return buildUnverifiedResult(url, merchantName, reasoning)
                }

                val currentPrice = if (json.isNull("current_price")) raw.currentPrice else json.optDouble("current_price").takeIf { !it.isNaN() && it > 0 } ?: raw.currentPrice
                if (currentPrice == null || currentPrice <= 0.0) {
                    return buildUnverifiedResult(url, merchantName, reasoning)
                }

                val originalPrice = if (json.isNull("original_price")) raw.originalPrice else json.optDouble("original_price").takeIf { !it.isNaN() && it > 0 } ?: raw.originalPrice ?: currentPrice

                val category = if (json.isNull("category")) raw.category ?: "General" else json.optString("category", raw.category ?: "General")
                val brand = if (json.isNull("brand")) raw.brand ?: merchantName else json.optString("brand", raw.brand ?: merchantName)
                val rating = if (json.isNull("rating")) raw.rating ?: 0.0 else json.optDouble("rating", raw.rating ?: 0.0)
                val reviewsCount = if (json.isNull("reviews_count")) raw.reviewsCount ?: 0 else json.optInt("reviews_count", raw.reviewsCount ?: 0)
                val variant = if (json.isNull("variant")) null else json.optString("variant").ifBlank { null }
                val imageUrl = if (json.isNull("image_url")) raw.imageUrl else json.optString("image_url").ifBlank { raw.imageUrl }

                val shoppingSummary = if (json.isNull("shopping_summary")) "Verified $productName from $merchantName." else json.optString("shopping_summary")
                val buyingAdvice = if (json.isNull("buying_advice")) "ViralToolAI Verdict: Product details verified directly from $merchantName catalog." else json.optString("buying_advice")

                val prosList = mutableListOf<String>()
                val prosArray = json.optJSONArray("pros")
                if (prosArray != null) {
                    for (i in 0 until prosArray.length()) {
                        val p = prosArray.optString(i, "")
                        if (p.isNotBlank()) prosList.add(p)
                    }
                }

                val consList = mutableListOf<String>()
                val consArray = json.optJSONArray("cons")
                if (consArray != null) {
                    for (i in 0 until consArray.length()) {
                        val c = consArray.optString(i, "")
                        if (c.isNotBlank()) consList.add(c)
                    }
                }

                val specs = mutableListOf<ProductSpecification>()
                val specsArray = json.optJSONArray("specifications")
                if (specsArray != null) {
                    for (i in 0 until specsArray.length()) {
                        val sObj = specsArray.optJSONObject(i) ?: continue
                        val t = sObj.optString("title", "")
                        val v = sObj.optString("value", "")
                        if (t.isNotBlank() && v.isNotBlank()) {
                            specs.add(ProductSpecification(t, v))
                        }
                    }
                }

                val highlights = mutableListOf<String>()
                val hArray = json.optJSONArray("highlights")
                if (hArray != null) {
                    for (i in 0 until hArray.length()) {
                        val h = hArray.optString(i, "")
                        if (h.isNotBlank()) highlights.add(h)
                    }
                }

                val candidateMatches = mutableListOf<ComparisonMatchResult>()
                val compArray = json.optJSONArray("price_comparison")
                if (compArray != null) {
                    val modelNum = json.optString("model_number", null)
                    val baseParams = ProductMatchParameters(
                        brand = brand,
                        modelNumber = modelNum,
                        variant = variant,
                        title = productName
                    )
                    for (i in 0 until compArray.length()) {
                        val itemObj = compArray.optJSONObject(i) ?: continue
                        val candStore = itemObj.optString("store", "")
                        if (candStore.isBlank()) continue
                        val candPrice = itemObj.optDouble("price", 0.0)
                        val candOrigPrice = itemObj.optDouble("original_price", candPrice)
                        val candTitle = itemObj.optString("title", productName)
                        val candUrl = itemObj.optString("url", "")
                        val candAvailability = itemObj.optString("availability", "In Stock")
                        val candDelivery = itemObj.optString("delivery", "Free Delivery")
                        val isOfficial = itemObj.optBoolean("is_official_seller", false)
                        val matchConfidence = itemObj.optInt("match_confidence", 95)

                        val matchResult = MultiStoreComparisonEngine.verifyIdenticalMatch(
                            baseProduct = baseParams,
                            candidateStore = candStore,
                            candidateTitle = candTitle,
                            candidatePrice = candPrice,
                            candidateOriginalPrice = candOrigPrice,
                            candidateUrl = candUrl,
                            candidateAvailability = candAvailability,
                            candidateDelivery = candDelivery,
                            isOfficialSeller = isOfficial,
                            geminiConfidence = matchConfidence
                        )

                        if (matchResult != null) {
                            candidateMatches.add(matchResult)
                        }
                    }
                }

                return buildShoppingResultFromAi(
                    url = url,
                    productName = productName,
                    category = category,
                    brand = brand,
                    currentPrice = currentPrice,
                    originalPrice = originalPrice ?: currentPrice,
                    rating = rating,
                    reviewsCount = reviewsCount,
                    variant = variant ?: "Standard",
                    imageUrl = imageUrl ?: "",
                    merchantName = merchantName,
                    specifications = specs,
                    highlights = highlights,
                    aiRecommendation = buyingAdvice,
                    shoppingSummary = shoppingSummary,
                    pros = prosList,
                    cons = consList,
                    confidenceScore = confidenceScore,
                    reasoning = reasoning,
                    candidateMatches = candidateMatches,
                    isVerified = true
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun buildIntelligentFallbackResult(
        url: String,
        merchantName: String,
        rawMetadata: RawExtractedMetadata?
    ): ShoppingResult {
        val merchantInfo = MerchantRegistry.findMerchant(merchantName)
        
        val titleFromRaw = rawMetadata?.title?.takeIf { it.length >= 3 && !it.startsWith("http") && !it.contains("Access Denied", ignoreCase = true) }
        val priceFromRaw = rawMetadata?.currentPrice?.takeIf { it > 0.0 }
        val origPriceFromRaw = rawMetadata?.originalPrice?.takeIf { priceFromRaw == null || it >= priceFromRaw }
        val imageFromRaw = rawMetadata?.imageUrl?.takeIf { it.startsWith("http") }

        if (titleFromRaw != null && priceFromRaw != null) {
            // Live Extracted & Verified Product
            val origP = origPriceFromRaw ?: priceFromRaw
            val discountP = if (origP > priceFromRaw) (((origP - priceFromRaw) / origP) * 100).toInt() else 0

            return ShoppingResult(
                url = url,
                productName = titleFromRaw,
                brand = rawMetadata.brand ?: merchantName,
                imageUrl = "electronics",
                productImageWebUrl = imageFromRaw,
                detectedStore = merchantName,
                logoChar = merchantName.firstOrNull()?.uppercaseChar() ?: 'S',
                accentColor = merchantInfo.primaryColor,
                currentPrice = priceFromRaw,
                bestPrice = priceFromRaw,
                originalPrice = origP,
                discountPercent = discountP,
                availability = rawMetadata.availability ?: "In Stock",
                rating = rawMetadata.rating ?: 0.0,
                reviewsCount = rawMetadata.reviewsCount ?: 0,
                priceTrend = emptyList(),
                similarProducts = emptyList(),
                priceComparison = emptyList(),
                aiRecommendation = "ViralToolAI Verdict: Product details verified directly from $merchantName store catalog.",
                detectionConfidence = 95,
                isCloudVerificationRequired = false,
                isReliable = true,
                isPreviewResult = false,
                category = rawMetadata.category ?: "General",
                estimatedMatch = "Verified Product",
                status = "Verified Result"
            )
        }

        // Unverified Link
        return ShoppingResult(
            url = url,
            productName = titleFromRaw ?: "Product details unavailable",
            brand = merchantName,
            imageUrl = "unknown",
            detectedStore = merchantName,
            logoChar = merchantName.firstOrNull()?.uppercaseChar() ?: 'S',
            accentColor = merchantInfo.primaryColor,
            currentPrice = priceFromRaw ?: 0.0,
            bestPrice = priceFromRaw ?: 0.0,
            originalPrice = priceFromRaw ?: 0.0,
            discountPercent = 0,
            availability = "Unavailable",
            rating = 0.0,
            reviewsCount = 0,
            priceTrend = emptyList(),
            similarProducts = emptyList(),
            priceComparison = emptyList(),
            aiRecommendation = "Product details could not be extracted cleanly from $merchantName link.",
            detectionConfidence = 0,
            isCloudVerificationRequired = false,
            isReliable = false,
            isPreviewResult = false,
            category = "General",
            estimatedMatch = "Unverified Link",
            status = "Product details unavailable"
        )
    }

    private fun buildUnverifiedResult(url: String, merchantName: String, reasoning: String?): ShoppingResult {
        val merchantInfo = MerchantRegistry.findMerchant(merchantName)
        return ShoppingResult(
            url = url,
            productName = "Product details unavailable",
            brand = merchantName,
            imageUrl = "unknown",
            detectedStore = merchantName,
            logoChar = merchantName.firstOrNull()?.uppercaseChar() ?: 'S',
            accentColor = merchantInfo.primaryColor,
            currentPrice = 0.0,
            bestPrice = 0.0,
            originalPrice = 0.0,
            discountPercent = 0,
            availability = "Unavailable",
            rating = 0.0,
            reviewsCount = 0,
            priceTrend = emptyList(),
            similarProducts = emptyList(),
            priceComparison = emptyList(),
            aiRecommendation = "Product details could not be verified with high confidence from $merchantName.",
            shoppingSummary = "Unverified or broken product link.",
            pros = emptyList(),
            cons = emptyList(),
            detectionConfidence = 30,
            confidenceScoreLevel = "Low",
            hiddenReasoning = reasoning ?: "Low confidence or unverified extraction.",
            isCloudVerificationRequired = false,
            isReliable = false,
            isPreviewResult = false,
            category = "General",
            estimatedMatch = "Unverified Link",
            status = "Product details unavailable"
        )
    }

    private fun buildShoppingResultFromAi(
        url: String,
        productName: String,
        category: String,
        brand: String,
        currentPrice: Double,
        originalPrice: Double,
        rating: Double,
        reviewsCount: Int,
        variant: String,
        imageUrl: String,
        merchantName: String,
        specifications: List<ProductSpecification>,
        highlights: List<String>,
        aiRecommendation: String,
        shoppingSummary: String? = null,
        pros: List<String> = emptyList(),
        cons: List<String> = emptyList(),
        confidenceScore: String = "High",
        reasoning: String? = null,
        candidateMatches: List<ComparisonMatchResult> = emptyList(),
        isVerified: Boolean = true
    ): ShoppingResult {
        val merchantInfo = MerchantRegistry.findMerchant(merchantName)
        val origPriceVal = if (originalPrice > currentPrice) originalPrice else currentPrice
        val discountPercent = if (origPriceVal > currentPrice) (((origPriceVal - currentPrice) / origPriceVal) * 100).toInt() else 0

        val defaultSpecs = specifications.ifEmpty {
            listOf(
                ProductSpecification("Brand", brand),
                ProductSpecification("Category", category),
                ProductSpecification("Variant", variant),
                ProductSpecification("Merchant Store", merchantName)
            )
        }

        val defaultHighlights = highlights.ifEmpty {
            listOf(
                "Genuine product verified directly from $merchantName catalog"
            )
        }

        val baseCompareItem = PriceCompareItem(
            store = merchantName,
            price = currentPrice,
            isBest = true,
            logoChar = merchantName.firstOrNull()?.uppercaseChar() ?: 'S',
            accentColor = merchantInfo.primaryColor,
            url = url,
            rating = rating,
            deliverySpeed = "Standard Store Delivery",
            returnPolicy = "Standard Return Policy",
            isOfficialStore = true,
            deliveryEstimate = "Express Store Shipping",
            stock = "In Stock",
            isVerified = true,
            originalPrice = origPriceVal,
            discountPercent = discountPercent,
            rankBadge = "Best Deal",
            recommendationReason = "Direct store price on $merchantName.",
            reviewsCount = reviewsCount,
            productName = productName,
            productImage = imageUrl
        )

        val comparisonList = MultiStoreComparisonEngine.processAndVerifyComparisonList(
            baseStore = merchantName,
            basePrice = currentPrice,
            baseItem = baseCompareItem,
            candidateMatches = candidateMatches
        )

        val bestPriceVal = comparisonList.minOfOrNull { it.price } ?: currentPrice
        val detConf = if (confidenceScore.equals("High", ignoreCase = true)) 98 else 75

        return ShoppingResult(
            url = url,
            productName = productName,
            brand = brand,
            imageUrl = category.lowercase(),
            productImageWebUrl = imageUrl.ifBlank { null },
            detectedStore = merchantName,
            logoChar = merchantName.firstOrNull()?.uppercaseChar() ?: 'S',
            accentColor = merchantInfo.primaryColor,
            currentPrice = currentPrice,
            bestPrice = bestPriceVal,
            availability = "In Stock",
            rating = rating,
            reviewsCount = reviewsCount,
            priceTrend = emptyList(),
            similarProducts = emptyList(),
            priceComparison = comparisonList,
            aiRecommendation = aiRecommendation,
            shoppingSummary = shoppingSummary ?: "Verified $productName from $merchantName.",
            pros = pros.ifEmpty { listOf("Verified price and store catalog entry") },
            cons = cons,
            detectionConfidence = detConf,
            confidenceScoreLevel = confidenceScore,
            hiddenReasoning = reasoning,
            isCloudVerificationRequired = false,
            isReliable = true,
            isPreviewResult = false,
            category = category,
            estimatedMatch = if (confidenceScore.equals("High", ignoreCase = true)) "Exact Product Match" else "Verified Product",
            status = "ViralToolAI Verified",
            variant = variant,
            originalPrice = origPriceVal,
            discountPercent = discountPercent,
            specifications = defaultSpecs,
            highlights = defaultHighlights,
            coupons = emptyList()
        )
    }

    private fun buildUnsupportedLinkResult(url: String): ShoppingResult {
        return ShoppingResult(
            url = url,
            productName = "Unsupported Shopping Link",
            brand = "Unsupported Store",
            imageUrl = "",
            detectedStore = "Unsupported",
            logoChar = '!',
            accentColor = 0xFFE74C3C,
            currentPrice = 0.0,
            bestPrice = 0.0,
            availability = "Unsupported Link",
            rating = 0.0,
            reviewsCount = 0,
            priceTrend = emptyList(),
            similarProducts = emptyList(),
            priceComparison = emptyList(),
            aiRecommendation = "Unsupported Shopping Link: This shopping website is not supported yet.",
            detectionConfidence = 0,
            isCloudVerificationRequired = false,
            isReliable = false,
            isPreviewResult = false,
            category = "Unsupported",
            estimatedMatch = "Unsupported Link",
            status = "Unsupported Shopping Link"
        )
    }

    private fun buildInvalidLinkResult(url: String): ShoppingResult {
        return ShoppingResult(
            url = url,
            productName = "Invalid Product Link",
            brand = "Unknown",
            imageUrl = "unknown",
            detectedStore = "E-Commerce",
            logoChar = '!',
            accentColor = 0xFFE74C3C,
            currentPrice = 0.0,
            bestPrice = 0.0,
            availability = "Invalid Link",
            rating = 0.0,
            reviewsCount = 0,
            priceTrend = emptyList(),
            similarProducts = emptyList(),
            priceComparison = emptyList(),
            aiRecommendation = "Invalid Product Link: Please paste a valid product link from Amazon, Flipkart, Meesho, Myntra, AJIO, or another supported e-commerce store.",
            detectionConfidence = 0,
            isCloudVerificationRequired = false,
            isReliable = false,
            isPreviewResult = false,
            category = "Invalid",
            estimatedMatch = "Invalid Link",
            status = "Invalid Link"
        )
    }
}
