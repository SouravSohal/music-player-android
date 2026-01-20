package com.musicplayer.domain

/**
 * Domain model representing an audio track
 */
data class AudioTrack(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long, // in milliseconds
    val path: String,
    val albumArtPath: String? = null,
    val bitrate: Int? = null,
    val sampleRate: Int? = null,
    val mimeType: String,
    val dateAdded: Long,
    val size: Long,
    val isLocal: Boolean = true,
    val folderId: Long? = null
)

/**
 * Domain model representing an album
 */
data class Album(
    val id: Long,
    val name: String,
    val artist: String,
    val albumArtPath: String? = null,
    val trackCount: Int,
    val year: Int? = null
)

/**
 * Domain model representing an artist
 */
data class Artist(
    val id: Long,
    val name: String,
    val albumCount: Int,
    val trackCount: Int
)

/**
 * Domain model representing a folder
 */
data class Folder(
    val id: Long,
    val path: String,
    val name: String,
    val trackCount: Int
)

/**
 * Domain model representing a playlist
 */
data class Playlist(
    val id: Long,
    val name: String,
    val description: String? = null,
    val trackCount: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val isSmart: Boolean = false
)

/**
 * Domain model for playback state
 */
data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentTrack: AudioTrack? = null,
    val position: Long = 0,
    val duration: Long = 0,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleEnabled: Boolean = false
)

enum class RepeatMode {
    OFF, ONE, ALL
}

/**
 * Audio quality settings for conversion
 */
data class AudioQuality(
    val format: AudioFormat,
    val bitrate: Int, // in kbps
    val sampleRate: Int = 44100
)

enum class AudioFormat {
    MP3, AAC, FLAC, WAV, OGG
}

/**
 * Download state for online media
 */
sealed class DownloadState {
    object Idle : DownloadState()
    data class Downloading(val progress: Int, val eta: Long) : DownloadState()
    data class Success(val filePath: String) : DownloadState()
    data class Error(val message: String) : DownloadState()
}

/**
 * Audio enhancement settings
 */
data class AudioEnhancement(
    val equalizerEnabled: Boolean = false,
    val equalizerPreset: EqualizerPreset? = null,
    val bassBoost: Int = 0, // 0-100
    val trebleEnhancement: Int = 0, // 0-100
    val noiseReduction: Boolean = false,
    val vocalClarity: Boolean = false,
    val spatialEffect: Boolean = false,
    val customEqBands: List<Int>? = null // EQ band values
)

enum class EqualizerPreset {
    NORMAL, CLASSICAL, DANCE, FLAT, FOLK, HEAVY_METAL, 
    HIP_HOP, JAZZ, POP, ROCK
}
