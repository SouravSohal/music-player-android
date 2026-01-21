package com.musicplayer.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.musicplayer.databinding.ActivitySearchBinding;
import com.musicplayer.domain.AudioTrack;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010 \n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0012\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0014J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\nH\u0002J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u000eH\u0002J\b\u0010\u0017\u001a\u00020\u000eH\u0002J\b\u0010\u0018\u001a\u00020\u000eH\u0002J\b\u0010\u0019\u001a\u00020\u000eH\u0002J\u0010\u0010\u001a\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u001b\u001a\u00020\u000eH\u0002J\b\u0010\u001c\u001a\u00020\u000eH\u0002J\u0016\u0010\u001d\u001a\u00020\u000e2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00150\u001fH\u0002J\u0010\u0010 \u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010!\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\"\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/musicplayer/ui/SearchActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/musicplayer/databinding/ActivitySearchBinding;", "searchAdapter", "Lcom/musicplayer/ui/MusicAdapter;", "viewModel", "Lcom/musicplayer/ui/MusicPlayerViewModel;", "formatDuration", "", "durationMs", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "performSearch", "query", "playTrack", "track", "Lcom/musicplayer/domain/AudioTrack;", "setupObservers", "setupRecyclerView", "setupSearch", "setupToolbar", "shareTrack", "showCreatePlaylistDialog", "showRecentSearches", "showSearchResults", "results", "", "showSelectPlaylistDialog", "showTrackDetails", "showTrackOptions", "app_debug"})
public final class SearchActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.musicplayer.databinding.ActivitySearchBinding binding;
    private com.musicplayer.ui.MusicPlayerViewModel viewModel;
    private com.musicplayer.ui.MusicAdapter searchAdapter;
    
    public SearchActivity() {
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
    
    private final void setupSearch() {
    }
    
    private final void setupObservers() {
    }
    
    private final void performSearch(java.lang.String query) {
    }
    
    private final void showRecentSearches() {
    }
    
    private final void showSearchResults(java.util.List<com.musicplayer.domain.AudioTrack> results) {
    }
    
    private final void playTrack(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void showTrackOptions(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void showSelectPlaylistDialog(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void showCreatePlaylistDialog() {
    }
    
    private final void shareTrack(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final void showTrackDetails(com.musicplayer.domain.AudioTrack track) {
    }
    
    private final java.lang.String formatDuration(long durationMs) {
        return null;
    }
}