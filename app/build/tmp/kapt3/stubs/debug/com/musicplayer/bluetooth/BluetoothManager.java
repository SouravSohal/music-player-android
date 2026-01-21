package com.musicplayer.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Singleton manager for Bluetooth state and callbacks
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\u0006J\u0010\u0010\b\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\nJ\u0010\u0010\u000b\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\nJ\u0006\u0010\f\u001a\u00020\u0006J\u0006\u0010\r\u001a\u00020\u0006J\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/musicplayer/bluetooth/BluetoothManager;", "", "()V", "listener", "Lcom/musicplayer/bluetooth/BluetoothEventListener;", "onA2dpConnected", "", "onA2dpDisconnected", "onDeviceConnected", "device", "Landroid/bluetooth/BluetoothDevice;", "onDeviceDisconnected", "onHeadsetPlugged", "onHeadsetUnplugged", "setListener", "Companion", "app_debug"})
public final class BluetoothManager {
    @org.jetbrains.annotations.Nullable()
    private com.musicplayer.bluetooth.BluetoothEventListener listener;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.musicplayer.bluetooth.BluetoothManager instance;
    @org.jetbrains.annotations.NotNull()
    public static final com.musicplayer.bluetooth.BluetoothManager.Companion Companion = null;
    
    private BluetoothManager() {
        super();
    }
    
    public final void setListener(@org.jetbrains.annotations.NotNull()
    com.musicplayer.bluetooth.BluetoothEventListener listener) {
    }
    
    public final void onDeviceConnected(@org.jetbrains.annotations.Nullable()
    android.bluetooth.BluetoothDevice device) {
    }
    
    public final void onDeviceDisconnected(@org.jetbrains.annotations.Nullable()
    android.bluetooth.BluetoothDevice device) {
    }
    
    public final void onA2dpConnected() {
    }
    
    public final void onA2dpDisconnected() {
    }
    
    public final void onHeadsetPlugged() {
    }
    
    public final void onHeadsetUnplugged() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/musicplayer/bluetooth/BluetoothManager$Companion;", "", "()V", "instance", "Lcom/musicplayer/bluetooth/BluetoothManager;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.musicplayer.bluetooth.BluetoothManager getInstance() {
            return null;
        }
    }
}