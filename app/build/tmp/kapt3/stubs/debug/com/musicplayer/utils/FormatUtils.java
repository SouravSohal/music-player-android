package com.musicplayer.utils;

import java.util.concurrent.TimeUnit;

/**
 * Utility functions for formatting time and file sizes
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\n\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\tJ\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u0006J\u000e\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004J\u0016\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0006\u00a8\u0006\u0013"}, d2 = {"Lcom/musicplayer/utils/FormatUtils;", "", "()V", "formatBitrate", "", "bitrate", "", "formatDuration", "millis", "", "formatFileSize", "bytes", "formatSampleRate", "sampleRate", "getFileExtension", "path", "truncate", "text", "maxLength", "app_debug"})
public final class FormatUtils {
    @org.jetbrains.annotations.NotNull()
    public static final com.musicplayer.utils.FormatUtils INSTANCE = null;
    
    private FormatUtils() {
        super();
    }
    
    /**
     * Format milliseconds to MM:SS or HH:MM:SS
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatDuration(long millis) {
        return null;
    }
    
    /**
     * Format file size in bytes to human-readable format
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatFileSize(long bytes) {
        return null;
    }
    
    /**
     * Format bitrate from bps to kbps
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatBitrate(int bitrate) {
        return null;
    }
    
    /**
     * Format sample rate from Hz to kHz
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatSampleRate(int sampleRate) {
        return null;
    }
    
    /**
     * Get file extension from path
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFileExtension(@org.jetbrains.annotations.NotNull()
    java.lang.String path) {
        return null;
    }
    
    /**
     * Truncate string with ellipsis if too long
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String truncate(@org.jetbrains.annotations.NotNull()
    java.lang.String text, int maxLength) {
        return null;
    }
}