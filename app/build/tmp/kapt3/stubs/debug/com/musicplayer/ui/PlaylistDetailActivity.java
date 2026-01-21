package com.musicplayer.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.musicplayer.databinding.ActivityPlaylistDetailBinding;
import com.musicplayer.domain.AudioTrack;
import com.musicplayer.domain.Playlist;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0004\u0018\u0000 #2\u00020\u0001:\u0001#B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\u0012\u0010\u000f\u001a\u00020\u000e2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014J\b\u0010\u0012\u001a\u00020\u000eH\u0002J\u0018\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u000eH\u0002J\b\u0010\u0019\u001a\u00020\u000eH\u0002J\b\u0010\u001a\u001a\u00020\u000eH\u0002J\b\u0010\u001b\u001a\u00020\u000eH\u0002J\b\u0010\u001c\u001a\u00020\u000eH\u0002J\u0010\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0016\u0010\u001e\u001a\u00020\u000e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00150 H\u0002J\b\u0010!\u001a\u00020\u000eH\u0002J\u0010\u0010\"\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/musicplayer/ui/PlaylistDetailActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/musicplayer/databinding/ActivityPlaylistDetailBinding;", "playlist", "Lcom/musicplayer/domain/Playlist;", "playlistId", "", "trackAdapter", "Lcom/musicplayer/ui/MusicAdapter;", "viewModel", "Lcom/musicplayer/ui/MusicPlayerViewModel;", "loadPlaylistDetails", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "playAll", "playTrack", "track", "Lcom/musicplayer/domain/AudioTrack;", "position", "", "setupRecyclerView", "setupToolbar", "setupUI", "showAddSongsDialog", "showEmptyState", "showTrackOptions", "showTracks", "tracks", "", "shuffleAndPlay", "updateUI", "Companion", "app_debug"})
public final class PlaylistDetailActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.musicplayer.databinding.ActivityPlaylistDetailBinding binding;
    private com.musicplayer.ui.MusicPlayerViewModel viewModel;
    private com.musicplayer.ui.MusicAdapter trackAdapter;
    private long playlistId = -1L;
    @org.jetbrains.annotations.Nullable()
    private com.musicplayer.domain.Playlist playlist;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PLAYLIST_ID = "playlist_id";
    @org.jetbrains.annotations.NotNull()
    public static final com.musicplayer.ui.PlaylistDetailActivity.Companion Companion = null;
    
    public PlaylistDetailActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupToolbar() {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupUI() {
    }
    
    private final void showAddSongsDialog() {
    }
    
    private final void loadPlaylistDetails() {
    }
    
    private final void updateUI(com.musicplayer.domain.Playlist playlist) {
    }
    
    private final void showEmptyState() {
    }
    
    private final void showTracks(java.util.List<com.musicplayer.domain.AudioTrack> tracks) {
    }
    
    private final void playTrack(com.musicplayer.domain.AudioTrack track, int position) {
    }
    
    private final void showTrackOptions(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void shuffleAndPlay() {
    }
    
    private final void playAll() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/musicplayer/ui/PlaylistDetailActivity$Companion;", "", "()V", "EXTRA_PLAYLIST_ID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}