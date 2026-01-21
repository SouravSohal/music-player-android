package com.musicplayer.domain;

/**
 * Domain model for playback state
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\nH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003JG\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u001d\u001a\u00020\u00032\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001J\t\u0010!\u001a\u00020\"H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011\u00a8\u0006#"}, d2 = {"Lcom/musicplayer/domain/PlaybackState;", "", "isPlaying", "", "currentTrack", "Lcom/musicplayer/domain/AudioTrack;", "position", "", "duration", "repeatMode", "Lcom/musicplayer/domain/RepeatMode;", "shuffleEnabled", "(ZLcom/musicplayer/domain/AudioTrack;JJLcom/musicplayer/domain/RepeatMode;Z)V", "getCurrentTrack", "()Lcom/musicplayer/domain/AudioTrack;", "getDuration", "()J", "()Z", "getPosition", "getRepeatMode", "()Lcom/musicplayer/domain/RepeatMode;", "getShuffleEnabled", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class PlaybackState {
    private final boolean isPlaying = false;
    @org.jetbrains.annotations.Nullable()
    private final com.musicplayer.domain.AudioTrack currentTrack = null;
    private final long position = 0L;
    private final long duration = 0L;
    @org.jetbrains.annotations.NotNull()
    private final com.musicplayer.domain.RepeatMode repeatMode = null;
    private final boolean shuffleEnabled = false;
    
    public PlaybackState(boolean isPlaying, @org.jetbrains.annotations.Nullable()
    com.musicplayer.domain.AudioTrack currentTrack, long position, long duration, @org.jetbrains.annotations.NotNull()
    com.musicplayer.domain.RepeatMode repeatMode, boolean shuffleEnabled) {
        super();
    }
    
    public final boolean isPlaying() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.musicplayer.domain.AudioTrack getCurrentTrack() {
        return null;
    }
    
    public final long getPosition() {
        return 0L;
    }
    
    public final long getDuration() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.musicplayer.domain.RepeatMode getRepeatMode() {
        return null;
    }
    
    public final boolean getShuffleEnabled() {
        return false;
    }
    
    public PlaybackState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.musicplayer.domain.AudioTrack component2() {
        return null;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.musicplayer.domain.RepeatMode component5() {
        return null;
    }
    
    public final boolean component6() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.musicplayer.domain.PlaybackState copy(boolean isPlaying, @org.jetbrains.annotations.Nullable()
    com.musicplayer.domain.AudioTrack currentTrack, long position, long duration, @org.jetbrains.annotations.NotNull()
    com.musicplayer.domain.RepeatMode repeatMode, boolean shuffleEnabled) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}