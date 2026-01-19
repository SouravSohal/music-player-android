package com.musicplayer.audio

import android.content.Context
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.musicplayer.domain.AudioFormat
import com.musicplayer.domain.AudioQuality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Converts video files to audio using FFmpeg
 */
class VideoToAudioConverter(private val context: Context) {

    /**
     * Convert video file to audio with progress updates
     */
    fun convertVideoToAudio(
        inputPath: String,
        outputDir: String,
        quality: AudioQuality,
        onProgress: (Float) -> Unit
    ): Flow<ConversionResult> = flow {
        emit(ConversionResult.Started)

        try {
            val outputFileName = getOutputFileName(inputPath, quality.format)
            val outputPath = File(outputDir, outputFileName).absolutePath

            // Create output directory if it doesn't exist
            File(outputDir).mkdirs()

            val command = buildConversionCommand(inputPath, outputPath, quality)

            // Set up statistics callback for progress
            Config.enableStatisticsCallback { statistics ->
                val progress = calculateProgress(statistics.time, statistics.size)
                onProgress(progress)
            }

            val result = withContext(Dispatchers.IO) {
                FFmpeg.execute(command)
            }

            when (result) {
                Config.RETURN_CODE_SUCCESS -> {
                    emit(ConversionResult.Success(outputPath, File(outputPath).length()))
                }
                Config.RETURN_CODE_CANCEL -> {
                    emit(ConversionResult.Cancelled)
                }
                else -> {
                    val errorMessage = Config.getLastCommandOutput()
                    emit(ConversionResult.Error("Conversion failed: $errorMessage"))
                }
            }
        } catch (e: Exception) {
            emit(ConversionResult.Error(e.message ?: "Unknown error"))
        } finally {
            Config.enableStatisticsCallback(null)
        }
    }

    /**
     * Build FFmpeg command based on audio quality settings
     */
    private fun buildConversionCommand(
        inputPath: String,
        outputPath: String,
        quality: AudioQuality
    ): String {
        return when (quality.format) {
            AudioFormat.MP3 -> {
                "-i \"$inputPath\" -vn -ar ${quality.sampleRate} -ac 2 -b:a ${quality.bitrate}k \"$outputPath\""
            }
            AudioFormat.AAC -> {
                "-i \"$inputPath\" -vn -ar ${quality.sampleRate} -ac 2 -c:a aac -b:a ${quality.bitrate}k \"$outputPath\""
            }
            AudioFormat.FLAC -> {
                "-i \"$inputPath\" -vn -ar ${quality.sampleRate} -ac 2 -c:a flac \"$outputPath\""
            }
            AudioFormat.WAV -> {
                "-i \"$inputPath\" -vn -ar ${quality.sampleRate} -ac 2 -c:a pcm_s16le \"$outputPath\""
            }
            AudioFormat.OGG -> {
                "-i \"$inputPath\" -vn -ar ${quality.sampleRate} -ac 2 -c:a libvorbis -b:a ${quality.bitrate}k \"$outputPath\""
            }
        }
    }

    /**
     * Cancel ongoing conversion
     */
    fun cancelConversion() {
        FFmpeg.cancel()
    }

    /**
     * Get estimated file size
     */
    fun estimateOutputSize(durationMs: Long, quality: AudioQuality): Long {
        // Estimate: bitrate (kbps) * duration (seconds) / 8 = size in KB
        val durationSeconds = durationMs / 1000
        return (quality.bitrate * durationSeconds / 8) * 1024 // Convert to bytes
    }

    private fun getOutputFileName(inputPath: String, format: AudioFormat): String {
        val baseName = File(inputPath).nameWithoutExtension
        val extension = when (format) {
            AudioFormat.MP3 -> "mp3"
            AudioFormat.AAC -> "aac"
            AudioFormat.FLAC -> "flac"
            AudioFormat.WAV -> "wav"
            AudioFormat.OGG -> "ogg"
        }
        return "${baseName}_converted.$extension"
    }

    private fun calculateProgress(timeMs: Int, size: Long): Float {
        // This is a simplified progress calculation
        // In a real implementation, you would need the total duration
        return (timeMs / 1000f) / 100f // Placeholder
    }
}

/**
 * Sealed class representing conversion states
 */
sealed class ConversionResult {
    object Started : ConversionResult()
    data class Success(val outputPath: String, val fileSize: Long) : ConversionResult()
    data class Error(val message: String) : ConversionResult()
    object Cancelled : ConversionResult()
}
