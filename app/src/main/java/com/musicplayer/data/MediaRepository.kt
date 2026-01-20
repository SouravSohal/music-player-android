package com.musicplayer.data

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.musicplayer.domain.AudioTrack
import com.musicplayer.domain.Album
import com.musicplayer.domain.Artist
import com.musicplayer.domain.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for scanning and managing local media files
 */
class MediaRepository(private val contentResolver: ContentResolver) {

    /**
     * Scan all audio files from device storage
     */
    suspend fun scanAudioFiles(): List<AudioTrack> = withContext(Dispatchers.IO) {
        val tracks = mutableListOf<AudioTrack>()
        
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.BITRATE
        )
        
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val bitrateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val albumId = cursor.getLong(albumIdColumn)
                
                tracks.add(
                    AudioTrack(
                        id = id,
                        title = cursor.getString(titleColumn) ?: "Unknown",
                        artist = cursor.getString(artistColumn) ?: "Unknown Artist",
                        album = cursor.getString(albumColumn) ?: "Unknown Album",
                        duration = cursor.getLong(durationColumn),
                        path = cursor.getString(dataColumn),
                        albumArtPath = getAlbumArtUri(albumId).toString(),
                        bitrate = cursor.getIntOrNull(bitrateColumn),
                        mimeType = cursor.getString(mimeTypeColumn) ?: "audio/*",
                        dateAdded = cursor.getLong(dateAddedColumn),
                        size = cursor.getLong(sizeColumn),
                        isLocal = true
                    )
                )
            }
        }
        
        tracks
    }

    /**
     * Get all albums from device
     */
    suspend fun getAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val albums = mutableListOf<Album>()
        
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.FIRST_YEAR
        )
        
        contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Audio.Albums.ALBUM} ASC"
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(0)
                albums.add(
                    Album(
                        id = id,
                        name = cursor.getString(1) ?: "Unknown Album",
                        artist = cursor.getString(2) ?: "Unknown Artist",
                        albumArtPath = getAlbumArtUri(id).toString(),
                        trackCount = cursor.getInt(3),
                        year = cursor.getIntOrNull(4)
                    )
                )
            }
        }
        
        albums
    }

    /**
     * Get all artists from device
     */
    suspend fun getArtists(): List<Artist> = withContext(Dispatchers.IO) {
        val artists = mutableListOf<Artist>()
        
        val projection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )
        
        contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Audio.Artists.ARTIST} ASC"
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                artists.add(
                    Artist(
                        id = cursor.getLong(0),
                        name = cursor.getString(1) ?: "Unknown Artist",
                        albumCount = cursor.getInt(2),
                        trackCount = cursor.getInt(3)
                    )
                )
            }
        }
        
        artists
    }

    /**
     * Search tracks by query
     */
    suspend fun searchTracks(query: String): List<AudioTrack> = withContext(Dispatchers.IO) {
        scanAudioFiles().filter {
            it.title.contains(query, ignoreCase = true) ||
            it.artist.contains(query, ignoreCase = true) ||
            it.album.contains(query, ignoreCase = true)
        }
    }

    /**
     * Get tracks by folder path
     */
    suspend fun getTracksByFolder(folderPath: String): List<AudioTrack> = withContext(Dispatchers.IO) {
        scanAudioFiles().filter { it.path.startsWith(folderPath) }
    }

    private fun getAlbumArtUri(albumId: Long): Uri {
        return ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
        )
    }

    private fun Cursor.getIntOrNull(columnIndex: Int): Int? {
        return if (isNull(columnIndex)) null else getInt(columnIndex)
    }
}
