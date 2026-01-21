package com.musicplayer.ui;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.musicplayer.R;
import com.musicplayer.databinding.ItemTrackYtmusicBinding;
import com.musicplayer.domain.AudioTrack;
import java.util.concurrent.TimeUnit;

/**
 * Adapter for music track list (YouTube Music style)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0011\u0012B9\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0006H\u0016J\u0018\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0006H\u0016R \u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/musicplayer/ui/MusicAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/musicplayer/domain/AudioTrack;", "Lcom/musicplayer/ui/MusicAdapter$TrackViewHolder;", "onTrackClick", "Lkotlin/Function2;", "", "", "onMoreClick", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;)V", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "TrackDiffCallback", "TrackViewHolder", "app_debug"})
public final class MusicAdapter extends androidx.recyclerview.widget.ListAdapter<com.musicplayer.domain.AudioTrack, com.musicplayer.ui.MusicAdapter.TrackViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function2<com.musicplayer.domain.AudioTrack, java.lang.Integer, kotlin.Unit> onTrackClick = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function2<com.musicplayer.domain.AudioTrack, java.lang.Integer, kotlin.Unit> onMoreClick = null;
    
    public MusicAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.musicplayer.domain.AudioTrack, ? super java.lang.Integer, kotlin.Unit> onTrackClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.musicplayer.domain.AudioTrack, ? super java.lang.Integer, kotlin.Unit> onMoreClick) {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.musicplayer.ui.MusicAdapter.TrackViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.musicplayer.ui.MusicAdapter.TrackViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/musicplayer/ui/MusicAdapter$TrackDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/musicplayer/domain/AudioTrack;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    public static final class TrackDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.musicplayer.domain.AudioTrack> {
        
        public TrackDiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.musicplayer.domain.AudioTrack oldItem, @org.jetbrains.annotations.NotNull()
        com.musicplayer.domain.AudioTrack newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.musicplayer.domain.AudioTrack oldItem, @org.jetbrains.annotations.NotNull()
        com.musicplayer.domain.AudioTrack newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\u0018\u00002\u00020\u0001BA\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0005\u0012\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0007J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/musicplayer/ui/MusicAdapter$TrackViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/musicplayer/databinding/ItemTrackYtmusicBinding;", "onTrackClick", "Lkotlin/Function2;", "Lcom/musicplayer/domain/AudioTrack;", "", "", "onMoreClick", "(Lcom/musicplayer/databinding/ItemTrackYtmusicBinding;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;)V", "bind", "track", "position", "formatDuration", "", "durationMs", "", "app_debug"})
    public static final class TrackViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.musicplayer.databinding.ItemTrackYtmusicBinding binding = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function2<com.musicplayer.domain.AudioTrack, java.lang.Integer, kotlin.Unit> onTrackClick = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function2<com.musicplayer.domain.AudioTrack, java.lang.Integer, kotlin.Unit> onMoreClick = null;
        
        public TrackViewHolder(@org.jetbrains.annotations.NotNull()
        com.musicplayer.databinding.ItemTrackYtmusicBinding binding, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function2<? super com.musicplayer.domain.AudioTrack, ? super java.lang.Integer, kotlin.Unit> onTrackClick, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function2<? super com.musicplayer.domain.AudioTrack, ? super java.lang.Integer, kotlin.Unit> onMoreClick) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.musicplayer.domain.AudioTrack track, int position) {
        }
        
        private final java.lang.String formatDuration(long durationMs) {
            return null;
        }
    }
}