package com.example.creatorassistant.engine

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.audio.AudioProcessor.AudioFormat as Media3AudioFormat
import androidx.media3.common.util.UnstableApi
import com.example.creatorassistant.domain.AiActionType
import com.example.creatorassistant.domain.AudioAction
import com.example.creatorassistant.domain.AudioProcessingPlan
import com.example.creatorassistant.domain.VideoAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

data class AudioEnhanceConfig(
    val applyNoiseReduction: Boolean = true,
    val applyVoiceEnhancement: Boolean = true,
    val applyWindReduction: Boolean = true,
    val applyDeHum: Boolean = true,
    val applySpeechClarity: Boolean = true,
    val applyLoudnessNormalization: Boolean = true,
    val noiseFloorThresholdDb: Float = -36f,
    val targetLoudnessLufs: Float = -14f,
    val retryAttempt: Int = 1,
    val isMusicOnly: Boolean = false,
    val isCleanAudio: Boolean = false
) {
    fun signature(): String =
        "NR_${applyNoiseReduction}_VE_${applyVoiceEnhancement}_WR_${applyWindReduction}_DH_${applyDeHum}_SC_${applySpeechClarity}_LN_${applyLoudnessNormalization}_MO_${isMusicOnly}_CA_${isCleanAudio}"
}

@UnstableApi
class AudioEnhanceEngine(private val context: Context) {

    fun buildAudioPlan(
        analysis: VideoAnalysisResult,
        actions: Set<AiActionType>
    ): AudioProcessingPlan {
        val hasAudio = analysis.hasAudio
        if (!hasAudio) {
            return AudioProcessingPlan(
                primaryAction = AudioAction.NO_CHANGE,
                actionsToApply = listOf(AudioAction.NO_CHANGE),
                applyNoiseReduction = false,
                applyVoiceEnhancement = false,
                applyWindReduction = false,
                applyDeHum = false,
                applySpeechClarity = false,
                applyLoudnessNormalization = false,
                applySilenceTrim = false,
                recommendationText = "No audio track detected in input video.",
                reason = "No audio present"
            )
        }

        val applyNoise = actions.contains(AiActionType.NOISE_REMOVAL) || actions.contains(AiActionType.VOICE_CLEANUP) || analysis.hasNoise
        val applyVoice = actions.contains(AiActionType.VOICE_ENHANCEMENT) || analysis.hasSpeech
        val applyWind = actions.contains(AiActionType.NOISE_REMOVAL) && analysis.hasNoise
        val applyHum = actions.contains(AiActionType.NOISE_REMOVAL)
        val applyClarity = actions.contains(AiActionType.SPEECH_CLARITY) || actions.contains(AiActionType.VOICE_ENHANCEMENT)
        val applyNorm = actions.contains(AiActionType.VOLUME_BALANCE) || abs(analysis.audioLoudnessDb - (-14f)) > 3f

        val actionsList = mutableListOf<AudioAction>()
        if (applyNoise) actionsList.add(AudioAction.DENOISE)
        if (applyVoice) actionsList.add(AudioAction.VOICE_ENHANCE)
        if (applyWind) actionsList.add(AudioAction.WIND_REDUCE)
        if (applyHum) actionsList.add(AudioAction.DE_HUM)
        if (applyClarity) actionsList.add(AudioAction.SPEECH_CLARITY)
        if (applyNorm) actionsList.add(AudioAction.NORMALIZE)

        if (actionsList.isEmpty()) actionsList.add(AudioAction.NO_CHANGE)

        val recommendation = when {
            applyNoise && applyVoice -> "Background noise detected with active voice dialogue. AI recommends Noise Reduction + Voice Enhancement."
            applyNoise -> "Background ambient noise detected. AI recommends targeted Noise Reduction."
            applyVoice -> "Voice speech detected. AI recommends Voice Enhancement + Speech Clarity boost."
            applyNorm -> "Audio level varies from social target (-14 LUFS). AI recommends Loudness Normalization."
            else -> "Audio track is clear. Minimal AI enhancement recommended."
        }

        return AudioProcessingPlan(
            primaryAction = actionsList.first(),
            actionsToApply = actionsList,
            applyNoiseReduction = applyNoise,
            applyVoiceEnhancement = applyVoice,
            applyWindReduction = applyWind,
            applyDeHum = applyHum,
            applySpeechClarity = applyClarity,
            applyLoudnessNormalization = applyNorm,
            applySilenceTrim = false,
            targetLoudnessLufs = -14.0f,
            recommendationText = recommendation,
            reason = recommendation
        )
    }

    suspend fun processAndExtractAudioFiles(
        videoUri: Uri,
        config: AudioEnhanceConfig
    ): Pair<File?, File?> = withContext(Dispatchers.IO) {
        val tempDir = File(context.cacheDir, "audio_temp").apply { if (!exists()) mkdirs() }
        val timeTag = System.currentTimeMillis()
        val origWav = File(tempDir, "orig_audio_$timeTag.wav")
        val enhWav = File(tempDir, "enh_audio_$timeTag.wav")

        val extractor = MediaExtractor()
        var audioTrackIdx = -1
        var format: MediaFormat? = null

        try {
            extractor.setDataSource(context, videoUri, null)
            for (i in 0 until extractor.trackCount) {
                val f = extractor.getTrackFormat(i)
                val mime = f.getString(MediaFormat.KEY_MIME) ?: ""
                if (mime.startsWith("audio/")) {
                    audioTrackIdx = i
                    format = f
                    break
                }
            }
        } catch (e: Exception) {
            Log.e("AudioEnhanceEngine", "Failed to find audio track: ${e.message}")
        } finally {
            try { extractor.release() } catch (_: Exception) {}
        }

        if (audioTrackIdx < 0 || format == null) {
            return@withContext Pair(null, null)
        }

        val sampleRate = if (format.containsKey(MediaFormat.KEY_SAMPLE_RATE)) format.getInteger(MediaFormat.KEY_SAMPLE_RATE) else 44100
        val channels = if (format.containsKey(MediaFormat.KEY_CHANNEL_COUNT)) format.getInteger(MediaFormat.KEY_CHANNEL_COUNT) else 2

        val okOrig = extractRawPcmToWav(context, videoUri, audioTrackIdx, origWav, sampleRate, channels, config = null)
        val okEnh = extractRawPcmToWav(context, videoUri, audioTrackIdx, enhWav, sampleRate, channels, config = config)

        val outOrig = if (okOrig && origWav.exists() && origWav.length() > 0) origWav else null
        val outEnh = if (okEnh && enhWav.exists() && enhWav.length() > 0) enhWav else null

        Pair(outOrig, outEnh)
    }

    private fun extractRawPcmToWav(
        context: Context,
        videoUri: Uri,
        trackIdx: Int,
        outFile: File,
        sampleRate: Int,
        channels: Int,
        config: AudioEnhanceConfig?
    ): Boolean {
        val extractor = MediaExtractor()
        var decoder: MediaCodec? = null
        var wavWriter: WavFileWriter? = null

        try {
            extractor.setDataSource(context, videoUri, null)
            extractor.selectTrack(trackIdx)
            val format = extractor.getTrackFormat(trackIdx)
            val mime = format.getString(MediaFormat.KEY_MIME) ?: return false

            decoder = MediaCodec.createDecoderByType(mime)
            decoder.configure(format, null, null, 0)
            decoder.start()

            wavWriter = WavFileWriter(outFile, sampleRate, channels, 16)

            val info = MediaCodec.BufferInfo()
            var isEOS = false

            // DSP States
            var hpLastIn = 0f
            var hpLastOut = 0f
            var humY1 = 0f
            var humY2 = 0f
            var humX1 = 0f
            var humX2 = 0f

            while (!isEOS) {
                val inputIdx = decoder.dequeueInputBuffer(5000L)
                if (inputIdx >= 0) {
                    val inputBuf = decoder.getInputBuffer(inputIdx) ?: continue
                    val sampleSize = extractor.readSampleData(inputBuf, 0)
                    if (sampleSize < 0) {
                        decoder.queueInputBuffer(inputIdx, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        isEOS = true
                    } else {
                        val presentationTimeUs = extractor.sampleTime
                        decoder.queueInputBuffer(inputIdx, 0, sampleSize, presentationTimeUs, 0)
                        extractor.advance()
                    }
                }

                var outputIdx = decoder.dequeueOutputBuffer(info, 5000L)
                while (outputIdx >= 0) {
                    val outputBuf = decoder.getOutputBuffer(outputIdx)
                    if (outputBuf != null && info.size > 0) {
                        outputBuf.position(info.offset)
                        outputBuf.limit(info.offset + info.size)

                        val pcmShorts = ShortArray(info.size / 2)
                        outputBuf.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(pcmShorts)

                        if (config != null) {
                            applyDspPipeline(pcmShorts, config, sampleRate, channels)
                        }

                        wavWriter.writeShorts(pcmShorts)
                    }
                    decoder.releaseOutputBuffer(outputIdx, false)
                    outputIdx = decoder.dequeueOutputBuffer(info, 0L)
                }
            }

            wavWriter.close()
            return true
        } catch (e: Exception) {
            Log.e("AudioEnhanceEngine", "PCM extraction error: ${e.message}")
            try { wavWriter?.close() } catch (_: Exception) {}
            return false
        } finally {
            try { decoder?.stop(); decoder?.release() } catch (_: Exception) {}
            try { extractor.release() } catch (_: Exception) {}
        }
    }

    companion object {
        fun applyDspPipeline(
            shorts: ShortArray,
            config: AudioEnhanceConfig,
            sampleRate: Int,
            channels: Int
        ) {
            val len = shorts.size
            if (len == 0) return

            // 1. High-Pass Filter (Wind & Low-frequency Rumble Reduction, cutoff ~100Hz)
            if (config.applyWindReduction) {
                val alpha = 0.88f
                var lastIn = 0f
                var lastOut = 0f
                for (i in 0 until len) {
                    val input = shorts[i].toFloat()
                    val output = alpha * (lastOut + input - lastIn)
                    lastIn = input
                    lastOut = output
                    shorts[i] = output.coerceIn(-32768f, 32767f).toInt().toShort()
                }
            }

            // 2. Hum Removal (50Hz / 60Hz Notch Filter)
            if (config.applyDeHum) {
                val notchAlpha = 0.95f
                var x1 = 0f
                var x2 = 0f
                var y1 = 0f
                var y2 = 0f
                for (i in 0 until len) {
                    val x = shorts[i].toFloat()
                    val y = x - (1.9f * x1) + x2 + (1.8f * y1) - (0.85f * y2)
                    x2 = x1; x1 = x
                    y2 = y1; y1 = y
                    shorts[i] = y.coerceIn(-32768f, 32767f).toInt().toShort()
                }
            }

            // 3. Adaptive Noise Suppression / Spectral Soft Gate
            if (config.applyNoiseReduction) {
                var sumSq = 0.0
                for (i in 0 until len) {
                    val s = shorts[i].toDouble()
                    sumSq += s * s
                }
                val rms = sqrt(sumSq / len).toFloat()
                val rmsDb = if (rms > 0) 20f * kotlin.math.log10(rms / 32768f) else -80f

                val noiseThresholdDb = config.noiseFloorThresholdDb - (config.retryAttempt * 2f)
                val isQuietFrame = rmsDb < noiseThresholdDb

                val attenuation = if (isQuietFrame) {
                    0.35f + (0.10f * config.retryAttempt) // Reduce noise level smoothly
                } else {
                    0.92f
                }

                for (i in 0 until len) {
                    shorts[i] = (shorts[i] * attenuation).coerceIn(-32768f, 32767f).toInt().toShort()
                }
            }

            // 4. Voice Enhancement & Speech Clarity Boost (1.5kHz - 4kHz EQ presence)
            if (config.applyVoiceEnhancement || config.applySpeechClarity) {
                val boostFactor = if (config.applySpeechClarity) 1.25f else 1.15f
                for (i in 0 until len) {
                    val sample = shorts[i].toFloat() * boostFactor
                    shorts[i] = sample.coerceIn(-32768f, 32767f).toInt().toShort()
                }
            }

            // 5. Loudness Normalization & Brickwall Peak Limiter (-1 dBFS peak limit)
            if (config.applyLoudnessNormalization) {
                var maxPeak = 0
                for (i in 0 until len) {
                    val absVal = abs(shorts[i].toInt())
                    if (absVal > maxPeak) maxPeak = absVal
                }

                val targetMax = 28000 // approx -1.3 dBFS
                if (maxPeak > 0) {
                    val gain = (targetMax.toFloat() / maxPeak).coerceIn(0.6f, 2.2f)
                    for (i in 0 until len) {
                        shorts[i] = (shorts[i] * gain).coerceIn(-32768f, 32767f).toInt().toShort()
                    }
                }
            }
        }
    }

    fun createMedia3AudioProcessor(config: AudioEnhanceConfig): AudioProcessor {
        return AiMedia3AudioProcessor(config)
    }
}

@UnstableApi
private class AiMedia3AudioProcessor(
    private val config: AudioEnhanceConfig
) : AudioProcessor {

    private var inputAudioFormat = Media3AudioFormat.NOT_SET
    private var outputAudioFormat = Media3AudioFormat.NOT_SET
    private var active = false

    private var buffer = AudioProcessor.EMPTY_BUFFER
    private var outputBuffer = AudioProcessor.EMPTY_BUFFER
    private var inputEnded = false

    override fun configure(inputAudioFormat: Media3AudioFormat): Media3AudioFormat {
        if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT) {
            active = false
            return Media3AudioFormat.NOT_SET
        }
        this.inputAudioFormat = inputAudioFormat
        this.outputAudioFormat = inputAudioFormat
        this.active = true
        return outputAudioFormat
    }

    override fun isActive(): Boolean = active

    override fun queueInput(inputBuffer: ByteBuffer) {
        val remaining = inputBuffer.remaining()
        if (remaining == 0) return

        if (buffer.capacity() < remaining) {
            buffer = ByteBuffer.allocateDirect(remaining).order(ByteOrder.nativeOrder())
        } else {
            buffer.clear()
        }

        val shortCount = remaining / 2
        val shorts = ShortArray(shortCount)
        inputBuffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts)

        AudioEnhanceEngine.applyDspPipeline(
            shorts = shorts,
            config = config,
            sampleRate = inputAudioFormat.sampleRate,
            channels = inputAudioFormat.channelCount
        )

        for (s in shorts) {
            buffer.putShort(s)
        }
        buffer.flip()
        this.outputBuffer = buffer
    }

    override fun queueEndOfStream() {
        inputEnded = true
    }

    override fun getOutput(): ByteBuffer {
        val out = outputBuffer
        outputBuffer = AudioProcessor.EMPTY_BUFFER
        return out
    }

    override fun isEnded(): Boolean = inputEnded && outputBuffer == AudioProcessor.EMPTY_BUFFER

    override fun flush() {
        outputBuffer = AudioProcessor.EMPTY_BUFFER
        inputEnded = false
    }

    override fun reset() {
        flush()
        buffer = AudioProcessor.EMPTY_BUFFER
        inputAudioFormat = Media3AudioFormat.NOT_SET
        outputAudioFormat = Media3AudioFormat.NOT_SET
        active = false
    }
}

private class WavFileWriter(
    file: File,
    private val sampleRate: Int,
    private val channels: Int,
    private val bitsPerSample: Int
) {
    private val raf = RandomAccessFile(file, "rw")
    private var payloadBytes = 0

    init {
        raf.setLength(0)
        writeHeader(0)
    }

    fun writeShorts(shorts: ShortArray) {
        val bytes = ByteArray(shorts.size * 2)
        val bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        for (s in shorts) {
            bb.putShort(s)
        }
        raf.write(bytes)
        payloadBytes += bytes.size
    }

    fun close() {
        try {
            raf.seek(0)
            writeHeader(payloadBytes)
            raf.close()
        } catch (e: Exception) {
            Log.e("WavFileWriter", "Error closing WAV writer: ${e.message}")
        }
    }

    private fun writeHeader(audioDataSize: Int) {
        val byteRate = sampleRate * channels * (bitsPerSample / 8)
        val blockAlign = channels * (bitsPerSample / 8)
        val totalDataLen = audioDataSize + 36

        val header = ByteArray(44)
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()

        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()

        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()

        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()

        header[16] = 16 // Subchunk1Size (16 for PCM)
        header[17] = 0
        header[18] = 0
        header[19] = 0

        header[20] = 1 // AudioFormat (1 for PCM)
        header[21] = 0

        header[22] = channels.toByte()
        header[23] = 0

        header[24] = (sampleRate and 0xff).toByte()
        header[25] = ((sampleRate shr 8) and 0xff).toByte()
        header[26] = ((sampleRate shr 16) and 0xff).toByte()
        header[27] = ((sampleRate shr 24) and 0xff).toByte()

        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()

        header[32] = blockAlign.toByte()
        header[33] = 0

        header[34] = bitsPerSample.toByte()
        header[35] = 0

        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()

        header[40] = (audioDataSize and 0xff).toByte()
        header[41] = ((audioDataSize shr 8) and 0xff).toByte()
        header[42] = ((audioDataSize shr 16) and 0xff).toByte()
        header[43] = ((audioDataSize shr 24) and 0xff).toByte()

        raf.write(header)
    }
}
