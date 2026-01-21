package com.musicplayer.data

import androidx.room.*
import com.musicplayer.domain.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room database for playlist management
 */
@Database(
    entities = [PlaylistEntity::class, PlaylistTrackEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val isSmart: Boolean = false,
    val smartCriteria: String? = null // JSON string for smart playlist rules
)

@Entity(
    tableName = "playlist_tracks",
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["playlistId", "trackId"], unique = true)]
)
data class PlaylistTrackEntity(
    val playlistId: Long,
    val trackId: Long,
    val position: Int,
    val addedAt: Long
)

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists ORDER BY updatedAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY position")
    fun getPlaylistTracks(playlistId: Long): Flow<List<PlaylistTrackEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTrackEntity): Long
    
    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun isTrackInPlaylist(playlistId: Long, trackId: Long): Int

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getPlaylistTrackCount(playlistId: Long): Int
}

/**
 * Repository for playlist operations
 */
class PlaylistRepository(private val playlistDao: PlaylistDao) {

    fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { entity ->
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    trackCount = 0, // Will be populated separately if needed
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt,
                    isSmart = entity.isSmart
                )
            }
        }
    }

    suspend fun createPlaylist(name: String, description: String? = null): Long {
        android.util.Log.d("PlaylistRepository", "Creating playlist: name='$name', description='$description'")
        val now = System.currentTimeMillis()
        val entity = PlaylistEntity(
            name = name,
            description = description,
            createdAt = now,
            updatedAt = now
        )
        val playlistId = playlistDao.insertPlaylist(entity)
        android.util.Log.d("PlaylistRepository", "Playlist created successfully with ID: $playlistId")
        return playlistId
    }

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long) {
        android.util.Log.d("PlaylistRepository", "=== Starting addTrackToPlaylist ===")
        android.util.Log.d("PlaylistRepository", "PlaylistId: $playlistId, TrackId: $trackId")
        
        // Check if track already exists
        val existsCount = playlistDao.isTrackInPlaylist(playlistId, trackId)
        if (existsCount > 0) {
            android.util.Log.w("PlaylistRepository", "Track $trackId already exists in playlist $playlistId")
            throw IllegalStateException("Song already in this playlist")
        }
        
        val position = playlistDao.getPlaylistTrackCount(playlistId)
        android.util.Log.d("PlaylistRepository", "Current track count: $position, adding at position $position")
        
        val entity = PlaylistTrackEntity(
            playlistId = playlistId,
            trackId = trackId,
            position = position,
            addedAt = System.currentTimeMillis()
        )
        
        android.util.Log.d("PlaylistRepository", "Inserting track entity: $entity")
        try {
            val rowId = playlistDao.insertPlaylistTrack(entity)
            android.util.Log.d("PlaylistRepository", "Track inserted successfully! Row ID: $rowId")
        } catch (e: Exception) {
            android.util.Log.e("PlaylistRepository", "Failed to insert track", e)
            throw Exception("Database insert failed: ${e.message}")
        }
        
        android.util.Log.d("PlaylistRepository", "Updating playlist timestamp")
        updatePlaylistTimestamp(playlistId)
        android.util.Log.d("PlaylistRepository", "=== addTrackToPlaylist completed successfully ===")
    }

    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        playlistDao.removeTrackFromPlaylist(playlistId, trackId)
        updatePlaylistTimestamp(playlistId)
    }

    suspend fun deletePlaylist(playlistId: Long) {
        val playlist = playlistDao.getPlaylistById(playlistId)
        playlist?.let {
            playlistDao.deletePlaylist(it)
        }
    }
    
    /**
     * Get track IDs for a playlist
     */
    fun getPlaylistTracksWithDetails(playlistId: Long): Flow<List<Long>> {
        return playlistDao.getPlaylistTracks(playlistId).map { tracks ->
            tracks.sortedBy { it.position }.map { it.trackId }
        }
    }

    private suspend fun updatePlaylistTimestamp(playlistId: Long) {
        val playlist = playlistDao.getPlaylistById(playlistId)
        playlist?.let {
            playlistDao.updatePlaylist(it.copy(updatedAt = System.currentTimeMillis()))
        }
    }
}
