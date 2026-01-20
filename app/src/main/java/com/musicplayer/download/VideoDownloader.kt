package com.musicplayer.download

import android.content.Context
import android.util.Log
import com.musicplayer.domain.AudioQuality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * Downloads videos from URLs and extracts audio
 * Note: This is a simplified implementation. For production, consider using
 * libraries like yt-dlp or youtube-dl with proper legal compliance.
 */
class VideoDownloader(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Download video/audio from URL
     * WARNING: Ensure compliance with copyright laws and terms of service
     */
    fun downloadFromUrl(
        url: String,
        outputDir: String,
        audioOnly: Boolean = true,
        quality: AudioQuality? = null
    ): Flow<DownloadResult> = flow {
        emit(DownloadResult.Started)

        try {
            // Validate URL
            if (!isValidUrl(url)) {
                emit(DownloadResult.Error("Invalid URL format"))
                return@flow
            }

            // Create output directory
            val dir = File(outputDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            // Build download request
            val request = Request.Builder()
                .url(url)
                .build()

            withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        emit(DownloadResult.Error("Download failed: ${response.code}"))
                        return@withContext
                    }

                    val contentLength = response.body?.contentLength() ?: -1
                    val inputStream = response.body?.byteStream()
                        ?: throw Exception("Response body is null")

                    val fileName = extractFileName(url, audioOnly)
                    val outputFile = File(dir, fileName)

                    downloadToFile(inputStream, outputFile, contentLength) { progress, eta ->
                        emit(DownloadResult.Downloading(progress, eta))
                    }

                    emit(DownloadResult.Success(outputFile.absolutePath, outputFile.length()))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Download error", e)
            emit(DownloadResult.Error(e.message ?: "Unknown error occurred"))
        }
    }

    /**
     * Download file with progress tracking
     */
    private suspend fun downloadToFile(
        inputStream: InputStream,
        outputFile: File,
        contentLength: Long,
        onProgress: suspend (Int, Long) -> Unit
    ) {
        FileOutputStream(outputFile).use { outputStream ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            var totalBytesRead = 0L
            val startTime = System.currentTimeMillis()

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead

                if (contentLength > 0) {
                    val progress = ((totalBytesRead * 100) / contentLength).toInt()
                    val eta = calculateEta(startTime, totalBytesRead, contentLength)
                    onProgress(progress, eta)
                }
            }
        }
    }

    /**
     * Calculate estimated time of arrival
     */
    private fun calculateEta(startTime: Long, downloaded: Long, total: Long): Long {
        val elapsed = System.currentTimeMillis() - startTime
        if (downloaded == 0L) return -1

        val rate = downloaded.toFloat() / elapsed
        val remaining = total - downloaded
        return (remaining / rate).toLong()
    }

    /**
     * Validate URL format
     */
    private fun isValidUrl(url: String): Boolean {
        return url.matches(Regex("^(https?://).*"))
    }

    /**
     * Extract file name from URL
     */
    private fun extractFileName(url: String, audioOnly: Boolean): String {
        val timestamp = System.currentTimeMillis()
        val extension = if (audioOnly) "mp3" else "mp4"
        return "download_$timestamp.$extension"
    }

    /**
     * Cancel ongoing download
     */
    fun cancelDownload() {
        // OkHttp calls can be cancelled via Call.cancel()
        // This would require maintaining a reference to the current call
    }

    companion object {
        private const val TAG = "VideoDownloader"
    }
}

/**
 * Sealed class representing download states
 */
sealed class DownloadResult {
    object Started : DownloadResult()
    data class Downloading(val progress: Int, val eta: Long) : DownloadResult()
    data class Success(val filePath: String, val fileSize: Long) : DownloadResult()
    data class Error(val message: String) : DownloadResult()
}
