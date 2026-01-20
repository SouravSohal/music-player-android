package com.musicplayer.utils

import java.util.concurrent.TimeUnit

/**
 * Utility functions for formatting time and file sizes
 */
object FormatUtils {

    /**
     * Format milliseconds to MM:SS or HH:MM:SS
     */
    fun formatDuration(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%d:%02d", minutes, seconds)
        }
    }

    /**
     * Format file size in bytes to human-readable format
     */
    fun formatFileSize(bytes: Long): String {
        val units = arrayOf("B", "KB", "MB", "GB")
        var size = bytes.toDouble()
        var unitIndex = 0

        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }

        return String.format("%.2f %s", size, units[unitIndex])
    }

    /**
     * Format bitrate from bps to kbps
     */
    fun formatBitrate(bitrate: Int): String {
        val kbps = bitrate / 1000
        return "$kbps kbps"
    }

    /**
     * Format sample rate from Hz to kHz
     */
    fun formatSampleRate(sampleRate: Int): String {
        val khz = sampleRate / 1000.0
        return String.format("%.1f kHz", khz)
    }

    /**
     * Get file extension from path
     */
    fun getFileExtension(path: String): String {
        val lastDot = path.lastIndexOf('.')
        return if (lastDot != -1 && lastDot < path.length - 1) {
            path.substring(lastDot + 1).lowercase()
        } else {
            ""
        }
    }

    /**
     * Truncate string with ellipsis if too long
     */
    fun truncate(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            text.substring(0, maxLength - 3) + "..."
        } else {
            text
        }
    }
}
