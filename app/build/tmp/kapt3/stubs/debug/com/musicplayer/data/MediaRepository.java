package com.musicplayer.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.musicplayer.domain.AudioTrack;
import com.musicplayer.domain.Album;
import com.musicplayer.domain.Artist;
import com.musicplayer.domain.Folder;
import kotlinx.coroutines.Dispatchers;

/**
 * Repository for scanning and managing local media files
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0086@\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\nH\u0086@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\n2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00100\nH\u0086@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00100\n2\u0006\u0010\u0016\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u001b\u0010\u0017\u001a\u0004\u0018\u00010\u0018*\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0018H\u0002\u00a2\u0006\u0002\u0010\u001bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/musicplayer/data/MediaRepository;", "", "contentResolver", "Landroid/content/ContentResolver;", "(Landroid/content/ContentResolver;)V", "getAlbumArtUri", "Landroid/net/Uri;", "albumId", "", "getAlbums", "", "Lcom/musicplayer/domain/Album;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getArtists", "Lcom/musicplayer/domain/Artist;", "getTracksByFolder", "Lcom/musicplayer/domain/AudioTrack;", "folderPath", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scanAudioFiles", "searchTracks", "query", "getIntOrNull", "", "Landroid/database/Cursor;", "columnIndex", "(Landroid/database/Cursor;I)Ljava/lang/Integer;", "app_debug"})
public final class MediaRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.ContentResolver contentResolver = null;
    
    public MediaRepository(@org.jetbrains.annotations.NotNull()
    android.content.ContentResolver contentResolver) {
        super();
    }
    
    /**
     * Scan all audio files from device storage
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object scanAudioFiles(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.musicplayer.domain.AudioTrack>> $completion) {
        return null;
    }
    
    /**
     * Get all albums from device
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAlbums(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.musicplayer.domain.Album>> $completion) {
        return null;
    }
    
    /**
     * Get all artists from device
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getArtists(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.musicplayer.domain.Artist>> $completion) {
        return null;
    }
    
    /**
     * Search tracks by query
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchTracks(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.musicplayer.domain.AudioTrack>> $completion) {
        return null;
    }
    
    /**
     * Get tracks by folder path
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTracksByFolder(@org.jetbrains.annotations.NotNull()
    java.lang.String folderPath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.musicplayer.domain.AudioTrack>> $completion) {
        return null;
    }
    
    private final android.net.Uri getAlbumArtUri(long albumId) {
        return null;
    }
    
    private final java.lang.Integer getIntOrNull(android.database.Cursor $this$getIntOrNull, int columnIndex) {
        return null;
    }
}