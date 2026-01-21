package com.musicplayer.ui;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.musicplayer.R;
import com.musicplayer.bluetooth.BluetoothEventListener;
import com.musicplayer.bluetooth.BluetoothManager;
import com.musicplayer.databinding.ActivityMainYtmusicBinding;
import com.musicplayer.databinding.BottomSheetTrackOptionsBinding;
import com.musicplayer.databinding.DialogCreatePlaylistBinding;
import com.musicplayer.databinding.DialogSelectPlaylistBinding;
import com.musicplayer.domain.AudioTrack;
import com.musicplayer.domain.Playlist;
import com.musicplayer.service.MusicPlaybackService;
import java.util.concurrent.TimeUnit;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0019\u0018\u0000 O2\u00020\u00012\u00020\u0002:\u0001OB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u001a\u001a\u00020\u001bH\u0002J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020\u001bH\u0016J\b\u0010!\u001a\u00020\u001bH\u0016J\u0012\u0010\"\u001a\u00020\u001b2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\u0012\u0010%\u001a\u00020\u001b2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\u0012\u0010&\u001a\u00020\u001b2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0014J\b\u0010)\u001a\u00020\u001bH\u0014J\b\u0010*\u001a\u00020\u001bH\u0016J\b\u0010+\u001a\u00020\u001bH\u0016J\b\u0010,\u001a\u00020\u001bH\u0002J-\u0010-\u001a\u00020\u001b2\u0006\u0010.\u001a\u00020\n2\u000e\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001d002\u0006\u00101\u001a\u000202H\u0016\u00a2\u0006\u0002\u00103J\b\u00104\u001a\u00020\u001bH\u0002J\u0018\u00105\u001a\u00020\u001b2\u0006\u00106\u001a\u0002072\u0006\u00108\u001a\u00020\nH\u0002J\b\u00109\u001a\u00020\u001bH\u0002J\b\u0010:\u001a\u00020\u001bH\u0002J\b\u0010;\u001a\u00020\u001bH\u0002J\b\u0010<\u001a\u00020\u001bH\u0002J\b\u0010=\u001a\u00020\u001bH\u0002J\b\u0010>\u001a\u00020\u001bH\u0002J\u0010\u0010?\u001a\u00020\u001b2\u0006\u00106\u001a\u000207H\u0002J\b\u0010@\u001a\u00020\u001bH\u0002J\b\u0010A\u001a\u00020\u001bH\u0002J\b\u0010B\u001a\u00020\u001bH\u0002J\b\u0010C\u001a\u00020\u001bH\u0002J\u0010\u0010D\u001a\u00020\u001b2\u0006\u00106\u001a\u000207H\u0002J\u0010\u0010E\u001a\u00020\u001b2\u0006\u00106\u001a\u000207H\u0002J\u0018\u0010F\u001a\u00020\u001b2\u0006\u00106\u001a\u0002072\u0006\u00108\u001a\u00020\nH\u0002J\b\u0010G\u001a\u00020\u001bH\u0002J\b\u0010H\u001a\u00020\u001bH\u0002J\b\u0010I\u001a\u00020\u001bH\u0002J\u0010\u0010J\u001a\u00020\u001b2\u0006\u00106\u001a\u000207H\u0002J\b\u0010K\u001a\u00020\u001bH\u0002J\u0010\u0010L\u001a\u00020\u001b2\u0006\u0010M\u001a\u00020\u0017H\u0002J\b\u0010N\u001a\u00020\u001bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006P"}, d2 = {"Lcom/musicplayer/ui/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/musicplayer/bluetooth/BluetoothEventListener;", "()V", "binding", "Lcom/musicplayer/databinding/ActivityMainYtmusicBinding;", "cachedPlaylists", "", "Lcom/musicplayer/domain/Playlist;", "currentTrackPosition", "", "mediaController", "Landroidx/media3/session/MediaController;", "mediaControllerFuture", "Lcom/google/common/util/concurrent/ListenableFuture;", "musicAdapter", "Lcom/musicplayer/ui/MusicAdapter;", "playerListener", "Landroidx/media3/common/Player$Listener;", "repeatMode", "seekBarUpdateJob", "Lkotlinx/coroutines/Job;", "shuffleEnabled", "", "viewModel", "Lcom/musicplayer/ui/MusicPlayerViewModel;", "checkPermissions", "", "formatDuration", "", "durationMs", "", "onA2dpConnected", "onA2dpDisconnected", "onBluetoothDeviceConnected", "device", "Landroid/bluetooth/BluetoothDevice;", "onBluetoothDeviceDisconnected", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onHeadsetPlugged", "onHeadsetUnplugged", "onPermissionsGranted", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "openEqualizer", "playTrack", "track", "Lcom/musicplayer/domain/AudioTrack;", "position", "setupBluetoothListener", "setupMediaController", "setupObservers", "setupRecyclerView", "setupToolbar", "setupUI", "shareTrack", "showAboutDialog", "showCreatePlaylistDialog", "showNavigationMenu", "showPlaylistsDialog", "showSelectPlaylistDialog", "showTrackDetails", "showTrackOptions", "startSeekBarUpdates", "toggleRepeat", "toggleShuffle", "updateBottomPlayer", "updateNowPlaying", "updatePlayPauseButton", "isPlaying", "updateSeekBar", "Companion", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity implements com.musicplayer.bluetooth.BluetoothEventListener {
    private com.musicplayer.databinding.ActivityMainYtmusicBinding binding;
    private com.musicplayer.ui.MusicPlayerViewModel viewModel;
    @org.jetbrains.annotations.Nullable()
    private com.google.common.util.concurrent.ListenableFuture<androidx.media3.session.MediaController> mediaControllerFuture;
    @org.jetbrains.annotations.Nullable()
    private androidx.media3.session.MediaController mediaController;
    private com.musicplayer.ui.MusicAdapter musicAdapter;
    private int currentTrackPosition = -1;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.musicplayer.domain.Playlist> cachedPlaylists;
    private int repeatMode = androidx.media3.common.Player.REPEAT_MODE_OFF;
    private boolean shuffleEnabled = false;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job seekBarUpdateJob;
    @org.jetbrains.annotations.NotNull()
    private final androidx.media3.common.Player.Listener playerListener = null;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    @org.jetbrains.annotations.NotNull()
    public static final com.musicplayer.ui.MainActivity.Companion Companion = null;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupToolbar() {
    }
    
    private final void openEqualizer() {
    }
    
    private final void setupBluetoothListener() {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void checkPermissions() {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final void onPermissionsGranted() {
    }
    
    private final void setupMediaController() {
    }
    
    private final void setupObservers() {
    }
    
    private final void setupUI() {
    }
    
    private final void showCreatePlaylistDialog() {
    }
    
    private final void showNavigationMenu() {
    }
    
    private final void showAboutDialog() {
    }
    
    private final void showTrackOptions(com.musicplayer.domain.AudioTrack track, int position) {
    }
    
    private final void showSelectPlaylistDialog(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void shareTrack(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void showTrackDetails(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final java.lang.String formatDuration(long durationMs) {
        return null;
    }
    
    private final void showPlaylistsDialog() {
    }
    
    private final void playTrack(com.musicplayer.domain.AudioTrack track, int position) {
    }
    
    private final void updateBottomPlayer(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void updatePlayPauseButton(boolean isPlaying) {
    }
    
    private final void updateSeekBar() {
    }
    
    private final void updateNowPlaying() {
    }
    
    private final void toggleShuffle() {
    }
    
    private final void toggleRepeat() {
    }
    
    @java.lang.Override()
    public void onBluetoothDeviceConnected(@org.jetbrains.annotations.Nullable()
    android.bluetooth.BluetoothDevice device) {
    }
    
    @java.lang.Override()
    public void onBluetoothDeviceDisconnected(@org.jetbrains.annotations.Nullable()
    android.bluetooth.BluetoothDevice device) {
    }
    
    @java.lang.Override()
    public void onA2dpConnected() {
    }
    
    @java.lang.Override()
    public void onA2dpDisconnected() {
    }
    
    @java.lang.Override()
    public void onHeadsetPlugged() {
    }
    
    @java.lang.Override()
    public void onHeadsetUnplugged() {
    }
    
    private final void startSeekBarUpdates() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/musicplayer/ui/MainActivity$Companion;", "", "()V", "PERMISSION_REQUEST_CODE", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}