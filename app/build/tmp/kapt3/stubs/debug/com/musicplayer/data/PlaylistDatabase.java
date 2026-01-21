package com.musicplayer.data;

import androidx.room.*;
import com.musicplayer.domain.Playlist;
import kotlinx.coroutines.flow.Flow;

/**
 * Room database for playlist management
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0005"}, d2 = {"Lcom/musicplayer/data/PlaylistDatabase;", "Landroidx/room/RoomDatabase;", "()V", "playlistDao", "Lcom/musicplayer/data/PlaylistDao;", "app_debug"})
@androidx.room.Database(entities = {com.musicplayer.data.PlaylistEntity.class, com.musicplayer.data.PlaylistTrackEntity.class}, version = 2, exportSchema = false)
public abstract class PlaylistDatabase extends androidx.room.RoomDatabase {
    
    public PlaylistDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.musicplayer.data.PlaylistDao playlistDao();
}