package com.musicplayer.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.musicplayer.R;
import com.musicplayer.databinding.ActivityPlayerFullscreenBinding;
import com.musicplayer.domain.AudioTrack;
import com.musicplayer.service.MusicPlaybackService;
import android.content.ComponentName;
import android.net.Uri;
import java.util.concurrent.TimeUnit;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0012\u0010\u0019\u001a\u00020\u00142\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\b\u0010\u001c\u001a\u00020\u0014H\u0014J\b\u0010\u001d\u001a\u00020\u0014H\u0002J\b\u0010\u001e\u001a\u00020\u0014H\u0002J\b\u0010\u001f\u001a\u00020\u0014H\u0002J\u0010\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006H\u0002J\b\u0010\"\u001a\u00020\u0014H\u0002J\u0010\u0010#\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006H\u0002J\b\u0010$\u001a\u00020\u0014H\u0002J\b\u0010%\u001a\u00020\u0014H\u0002J\b\u0010&\u001a\u00020\u0014H\u0002J\u0010\u0010\'\u001a\u00020\u00142\u0006\u0010(\u001a\u00020\u0010H\u0002J\b\u0010)\u001a\u00020\u0014H\u0002J\u0010\u0010*\u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006H\u0002J\b\u0010+\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/musicplayer/ui/FullScreenPlayerActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/musicplayer/databinding/ActivityPlayerFullscreenBinding;", "currentTrack", "Lcom/musicplayer/domain/AudioTrack;", "mediaController", "Landroidx/media3/session/MediaController;", "mediaControllerFuture", "Lcom/google/common/util/concurrent/ListenableFuture;", "playerListener", "Landroidx/media3/common/Player$Listener;", "repeatMode", "", "shuffleEnabled", "", "viewModel", "Lcom/musicplayer/ui/MusicPlayerViewModel;", "finish", "", "formatDuration", "", "durationMs", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "setupMediaController", "setupObservers", "setupUI", "shareTrack", "track", "showCreatePlaylistDialog", "showSelectPlaylistDialog", "startSeekBarUpdates", "toggleRepeat", "toggleShuffle", "updatePlayPauseButton", "isPlaying", "updateSeekBar", "updateTrackInfo", "updateUI", "app_debug"})
public final class FullScreenPlayerActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.musicplayer.databinding.ActivityPlayerFullscreenBinding binding;
    private com.musicplayer.ui.MusicPlayerViewModel viewModel;
    @org.jetbrains.annotations.Nullable()
    private com.google.common.util.concurrent.ListenableFuture<androidx.media3.session.MediaController> mediaControllerFuture;
    @org.jetbrains.annotations.Nullable()
    private androidx.media3.session.MediaController mediaController;
    @org.jetbrains.annotations.Nullable()
    private com.musicplayer.domain.AudioTrack currentTrack;
    private int repeatMode = androidx.media3.common.Player.REPEAT_MODE_OFF;
    private boolean shuffleEnabled = false;
    @org.jetbrains.annotations.NotNull()
    private final androidx.media3.common.Player.Listener playerListener = null;
    
    public FullScreenPlayerActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupMediaController() {
    }
    
    private final void setupUI() {
    }
    
    private final void setupObservers() {
    }
    
    private final void updateUI() {
    }
    
    private final void updateTrackInfo(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void updatePlayPauseButton(boolean isPlaying) {
    }
    
    private final void startSeekBarUpdates() {
    }
    
    private final void updateSeekBar() {
    }
    
    private final java.lang.String formatDuration(long durationMs) {
        return null;
    }
    
    private final void toggleShuffle() {
    }
    
    private final void toggleRepeat() {
    }
    
    private final void shareTrack(com.musicplayer.domain.AudioTrack track) {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @java.lang.Override()
    public void finish() {
    }
    
    private final void showSelectPlaylistDialog(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void showCreatePlaylistDialog() {
    }
}