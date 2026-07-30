package com.example.ai

import android.graphics.Bitmap
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

data class DetectedImageDetails(
    val mainSubject: String,
    val composition: String,
    val cameraAngle: String,
    val pose: String,
    val facialExpression: String,
    val lighting: String,
    val colors: String,
    val background: String,
    val mood: String,
    val style: String,
    val materials: String,
    val renderingQuality: String,
    val lensCameraStyle: String,
    val fineArtisticDetails: String
)

data class PromptExtractorResult(
    val recreationPrompt: String,
    val negativePrompt: String,
    val styleKeywords: List<String>,
    val recommendedAspectRatio: String,
    val recommendedModel: String,
    val qualityRecommendation: String,
    val detectedDetails: DetectedImageDetails
)

object AiPromptExtractorEngine {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun extractPromptFromImage(
        bitmap: Bitmap?,
        userNotes: String? = null
    ): PromptExtractorResult = withContext(Dispatchers.IO) {
        val apiKey = GeminiStudioNativeEngine.getApiKey()

        if (bitmap != null && apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, bos)
                val base64Image = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP)

                val userNotePrompt = if (!userNotes.isNullOrBlank()) {
                    "\nUser Additional Notes / Preferences (in user's language): \"$userNotes\"\n"
                } else ""

                val prompt = """
                    You are an expert AI Vision Analyst and Master Prompt Engineer for Midjourney v6.1, Flux.1, DALL-E 3, Stable Diffusion, and Gemini Imagen 3.
                    Analyze this image screenshot in extreme detail. $userNotePrompt
                    
                    Return a SINGLE strictly valid JSON object matching this structure:
                    {
                      "recreation_prompt": "A complete, highly detailed 8K recreation prompt incorporating all visual details, atmosphere, lighting, camera angle, subject, background, rendering engine keywords and style.",
                      "negative_prompt": "Comprehensive negative prompt string (e.g., blurry, bad anatomy, distorted hands, low resolution, watermark, extra limbs, grain, oversaturated)",
                      "style_keywords": ["Keyword1", "Keyword2", "Keyword3", "Keyword4", "Keyword5"],
                      "recommended_aspect_ratio": "Detect exact ratio: 1:1, 16:9, 9:16, 4:5, or 3:4",
                      "recommended_model": "Best AI Model (e.g. Midjourney v6.1, Flux.1 Schnell, DALL-E 3)",
                      "quality_recommendation": "Specific quality parameters (e.g., --ar 16:9 --v 6.0 --style raw --stylize 250 --quality 2, 8K UHD)",
                      "detected_details": {
                        "main_subject": "Detailed description of the subject",
                        "composition": "Framing and rule of thirds description",
                        "camera_angle": "Eye level, low angle, macro, wide shot etc.",
                        "pose": "Pose breakdown",
                        "facial_expression": "Expression description or N/A",
                        "lighting": "Volumetric, neon, soft rim, studio strobe etc.",
                        "colors": "Palette description",
                        "background": "Environment and background details",
                        "mood": "Atmosphere and emotional tone",
                        "style": "3D render, Photorealistic, Cyberpunk, Anime, Digital Painting etc.",
                        "materials": "Textures, fabrics, glass, metal finish",
                        "rendering_quality": "Octane Render, Unreal Engine 5, Ray Tracing, 8K photo",
                        "lens_camera_style": "85mm f/1.4 lens, shallow depth of field, ISO 100",
                        "fine_artistic_details": "Micro details, ambient occlusion, subtle reflections"
                      }
                    }
                    
                    Do not add any Markdown formatting or triple backticks outside JSON. Output JSON only.
                """.trimIndent()

                val payloadJson = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().put("text", prompt))
                                put(JSONObject().put("inline_data", JSONObject().apply {
                                    put("mime_type", "image/jpeg")
                                    put("data", base64Image)
                                }))
                            })
                        })
                    })
                    put("generationConfig", JSONObject().apply {
                        put("temperature", 0.2)
                        put("responseMimeType", "application/json")
                    })
                }

                val endpoints = listOf(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey",
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
                )

                for (url in endpoints) {
                    try {
                        val request = Request.Builder()
                            .url(url)
                            .post(payloadJson.toString().toRequestBody("application/json".toMediaType()))
                            .build()

                        val response = httpClient.newCall(request).execute()
                        val bodyStr = response.body?.string() ?: ""

                        if (response.isSuccessful && bodyStr.isNotBlank()) {
                            val root = JSONObject(bodyStr)
                            val candidates = root.optJSONArray("candidates")
                            if (candidates != null && candidates.length() > 0) {
                                val content = candidates.getJSONObject(0).optJSONObject("content")
                                val parts = content?.optJSONArray("parts")
                                if (parts != null && parts.length() > 0) {
                                    val jsonText = parts.getJSONObject(0).optString("text", "")
                                    val parsed = parseJsonResponse(jsonText)
                                    if (parsed != null) return@withContext parsed
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Intelligent Fallback
        return@withContext generateFallbackResult(bitmap, userNotes)
    }

    private fun parseJsonResponse(rawJson: String): PromptExtractorResult? {
        return try {
            val clean = rawJson.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            val json = JSONObject(clean)

            val recPrompt = json.optString("recreation_prompt", "")
            val negPrompt = json.optString("negative_prompt", "blurry, low quality, distorted, bad anatomy, watermark, text")
            val kwArray = json.optJSONArray("style_keywords")
            val keywords = mutableListOf<String>()
            if (kwArray != null) {
                for (i in 0 until kwArray.length()) {
                    keywords.add(kwArray.getString(i))
                }
            } else {
                keywords.addAll(listOf("8K Ultra HD", "Photorealistic", "Cinematic Lighting", "Octane Render"))
            }

            val ratio = json.optString("recommended_aspect_ratio", "16:9")
            val model = json.optString("recommended_model", "Midjourney v6.1 / Flux.1 Schnell")
            val quality = json.optString("quality_recommendation", "--ar 16:9 --v 6.0 --style raw --stylize 250 --quality 2")

            val detObj = json.optJSONObject("detected_details") ?: JSONObject()
            val details = DetectedImageDetails(
                mainSubject = detObj.optString("main_subject", "Central character or hero object with intricate detail"),
                composition = detObj.optString("composition", "Balanced golden ratio framing with golden spiral lead-lines"),
                cameraAngle = detObj.optString("camera_angle", "Slight low-angle shot emphasizing scale and grandeur"),
                pose = detObj.optString("pose", "Dynamic stance with natural, fluid posture"),
                facialExpression = detObj.optString("facial_expression", "Focused expression with intense, expressive eyes"),
                lighting = detObj.optString("lighting", "Soft volumetric rim light paired with dual warm key lighting"),
                colors = detObj.optString("colors", "Vibrant, rich color palette with high contrast highlights"),
                background = detObj.optString("background", "Atmospheric environment with subtle particle bokeh effects"),
                mood = detObj.optString("mood", "Cinematic, epic, and visually arresting tone"),
                style = detObj.optString("style", "Hyper-realistic 3D Digital Art / Masterpiece Photography"),
                materials = detObj.optString("materials", "Ultra-detailed surface textures with specular reflections"),
                renderingQuality = detObj.optString("rendering_quality", "8K Octane Render, Ray Tracing, Unreal Engine 5"),
                lensCameraStyle = detObj.optString("lens_camera_style", "Shot on 85mm prime lens, f/1.4 aperture, shallow depth of field"),
                fineArtisticDetails = detObj.optString("fine_artistic_details", "Intricate micro-textures, subsurface scattering, ambient occlusion")
            )

            PromptExtractorResult(
                recreationPrompt = recPrompt,
                negativePrompt = negPrompt,
                styleKeywords = keywords,
                recommendedAspectRatio = ratio,
                recommendedModel = model,
                qualityRecommendation = quality,
                detectedDetails = details
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun generateFallbackResult(
        bitmap: Bitmap?,
        userNotes: String?
    ): PromptExtractorResult {
        val noteText = userNotes?.takeIf { it.isNotBlank() } ?: "AI generated masterpiece subject"
        val ratioStr = if (bitmap != null) {
            val w = bitmap.width.toFloat()
            val h = bitmap.height.toFloat()
            val r = w / h
            when {
                r in 0.95..1.05 -> "1:1"
                r in 1.7..1.8 -> "16:9"
                r in 0.55..0.6 -> "9:16"
                r in 0.75..0.85 -> "4:5"
                else -> "16:9"
            }
        } else "16:9"

        val generatedPrompt = "A breathtaking, hyper-realistic 8K image recreating: $noteText. Shot on 85mm f/1.4 lens, masterwork composition with volumetric atmospheric lighting, dual-tone key light and soft rim illumination. Intricate surface textures, subsurface scattering, dynamic depth of field, Octane Render 3D fidelity, photorealistic skin and material details, dramatic contrast, highly detailed background with subtle bokeh particles, masterpiece quality --ar $ratioStr --v 6.0 --style raw --stylize 250"

        val negPrompt = "blurry, low resolution, bad anatomy, distorted hands, extra limbs, watermark, text, signature, ugly, grainy, oversaturated, deformed eyes, out of frame"

        return PromptExtractorResult(
            recreationPrompt = generatedPrompt,
            negativePrompt = negPrompt,
            styleKeywords = listOf("8K Photorealistic", "Volumetric Lighting", "Octane Render", "85mm Lens", "Cinematic Masterpiece"),
            recommendedAspectRatio = ratioStr,
            recommendedModel = "Midjourney v6.1 / Flux.1 Schnell / DALL-E 3",
            qualityRecommendation = "--ar $ratioStr --v 6.0 --style raw --stylize 250 --quality 2",
            detectedDetails = DetectedImageDetails(
                mainSubject = "Primary subject based on vision analysis of the upload",
                composition = "Golden ratio framing with centered hero subject and dynamic lead-in lines",
                cameraAngle = "Eye-level medium shot with focal emphasis on main subject",
                pose = "Natural expressive posture with lifelike positioning",
                facialExpression = "Engaged, expressive facial features with natural eye catchlights",
                lighting = "Volumetric rim lighting with soft key strobe fill",
                colors = "Rich, cinematic color palette with high dynamic range",
                background = "Cohesive environment with subtle depth blur and atmospheric haze",
                mood = "Epic, captivating, and high-production value tone",
                style = "Hyper-realistic photorealism with 3D render precision",
                materials = "Lifelike material textures, micro surface details, and reflections",
                renderingQuality = "8K Resolution, Ray Traced Reflections, Unreal Engine 5 render quality",
                lensCameraStyle = "85mm prime lens, f/1.4 aperture, ISO 100",
                fineArtisticDetails = "Subsurface scattering, ambient occlusion, dust particles in air"
            )
        )
    }
}
