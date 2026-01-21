package com.musicplayer.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import com.musicplayer.data.MediaRepository;
import com.musicplayer.data.PlaylistDatabase;
import com.musicplayer.data.PlaylistRepository;
import com.musicplayer.domain.AudioTrack;
import com.musicplayer.domain.PlaybackState;
import com.musicplayer.domain.Playlist;

/**
 * ViewModel for music player following MVVM architecture
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020(J\u001a\u0010*\u001a\u00020&2\u0006\u0010+\u001a\u00020\u000b2\n\b\u0002\u0010,\u001a\u0004\u0018\u00010\u000bJ\u001a\u0010-\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00122\u0006\u0010\'\u001a\u00020(J\u0010\u0010.\u001a\u0004\u0018\u00010\b2\u0006\u0010/\u001a\u000200J\u0006\u00101\u001a\u00020&J\u000e\u00102\u001a\u00020&2\u0006\u00103\u001a\u00020\u000bJ\u000e\u00104\u001a\u00020&2\u0006\u00105\u001a\u00020\bJ\u000e\u00106\u001a\u00020&2\u0006\u00107\u001a\u00020\u000fR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0019\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0014R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0014R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0014R\u001d\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0014\u00a8\u00068"}, d2 = {"Lcom/musicplayer/ui/MusicPlayerViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_allTracks", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/musicplayer/domain/AudioTrack;", "_currentTrack", "_errorMessage", "", "_isLoading", "", "_playbackState", "Lcom/musicplayer/domain/PlaybackState;", "_tracks", "currentTrack", "Landroidx/lifecycle/LiveData;", "getCurrentTrack", "()Landroidx/lifecycle/LiveData;", "errorMessage", "getErrorMessage", "isLoading", "mediaRepository", "Lcom/musicplayer/data/MediaRepository;", "playbackState", "getPlaybackState", "playlistDatabase", "Lcom/musicplayer/data/PlaylistDatabase;", "playlistRepository", "Lcom/musicplayer/data/PlaylistRepository;", "playlists", "Lcom/musicplayer/domain/Playlist;", "getPlaylists", "tracks", "getTracks", "addTrackToPlaylist", "", "playlistId", "", "trackId", "createPlaylist", "name", "description", "getPlaylistTracks", "getTrackByIndex", "index", "", "loadMediaLibrary", "searchTracks", "query", "setCurrentTrack", "track", "updatePlaybackState", "state", "app_debug"})
public final class MusicPlayerViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.musicplayer.data.MediaRepository mediaRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.musicplayer.data.PlaylistDatabase playlistDatabase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.musicplayer.data.PlaylistRepository playlistRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.musicplayer.domain.Playlist>> playlists = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.musicplayer.domain.AudioTrack>> _allTracks = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.musicplayer.domain.AudioTrack>> _tracks = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.musicplayer.domain.AudioTrack>> tracks = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.musicplayer.domain.AudioTrack> _currentTrack = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.musicplayer.domain.AudioTrack> currentTrack = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.musicplayer.domain.PlaybackState> _playbackState = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.musicplayer.domain.PlaybackState> playbackState = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Boolean> isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> _errorMessage = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.String> errorMessage = null;
    
    public MusicPlayerViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.musicplayer.domain.Playlist>> getPlaylists() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.musicplayer.domain.AudioTrack>> getTracks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.musicplayer.domain.AudioTrack> getCurrentTrack() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.musicplayer.domain.PlaybackState> getPlaybackState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Boolean> isLoading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.String> getErrorMessage() {
        return null;
    }
    
    /**
     * Load media library from device
     */
    public final void loadMediaLibrary() {
    }
    
    /**
     * Search tracks
     */
    public final void searchTracks(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    /**
     * Update playback state
     */
    public final void updatePlaybackState(@org.jetbrains.annotations.NotNull()
    com.musicplayer.domain.PlaybackState state) {
    }
    
    /**
     * Set current track
     */
    public final void setCurrentTrack(@org.jetbrains.annotations.NotNull()
    com.musicplayer.domain.AudioTrack track) {
    }
    
    /**
     * Get track by index
     */
    @org.jetbrains.annotations.Nullable()
    public final com.musicplayer.domain.AudioTrack getTrackByIndex(int index) {
        return null;
    }
    
    /**
     * Create new playlist
     */
    public final void createPlaylist(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.Nullable()
    java.lang.String description) {
    }
    
    /**
     * Add track to playlist
     */
    public final void addTrackToPlaylist(long playlistId, long trackId) {
    }
    
    /**
     * Get tracks for a specific playlist
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.musicplayer.domain.AudioTrack>> getPlaylistTracks(long playlistId) {
        return null;
    }
}