package com.musicplayer.data

import androidx.room.*
import com.musicplayer.domain.Playlist
import kotlinx.coroutines.flow.Flow

/**
 * Room database for playlist management
 */
@Database(
    entities = [PlaylistEntity::class, PlaylistTrackEntity::class],
    version = 1,
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
    primaryKeys = ["playlistId", "trackId", "position"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTrackEntity)

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
        return playlistDao.getAllPlaylists()
    }

    suspend fun createPlaylist(name: String, description: String? = null): Long {
        val now = System.currentTimeMillis()
        val entity = PlaylistEntity(
            name = name,
            description = description,
            createdAt = now,
            updatedAt = now
        )
        return playlistDao.insertPlaylist(entity)
    }

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long) {
        val position = playlistDao.getPlaylistTrackCount(playlistId)
        val entity = PlaylistTrackEntity(
            playlistId = playlistId,
            trackId = trackId,
            position = position,
            addedAt = System.currentTimeMillis()
        )
        playlistDao.insertPlaylistTrack(entity)
        updatePlaylistTimestamp(playlistId)
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

    private suspend fun updatePlaylistTimestamp(playlistId: Long) {
        val playlist = playlistDao.getPlaylistById(playlistId)
        playlist?.let {
            playlistDao.updatePlaylist(it.copy(updatedAt = System.currentTimeMillis()))
        }
    }
}
