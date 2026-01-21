package com.musicplayer.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Interface for Bluetooth events
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\u0012\u0010\u0005\u001a\u00020\u00032\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H&J\u0012\u0010\b\u001a\u00020\u00032\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H&J\b\u0010\t\u001a\u00020\u0003H&J\b\u0010\n\u001a\u00020\u0003H&\u00a8\u0006\u000b"}, d2 = {"Lcom/musicplayer/bluetooth/BluetoothEventListener;", "", "onA2dpConnected", "", "onA2dpDisconnected", "onBluetoothDeviceConnected", "device", "Landroid/bluetooth/BluetoothDevice;", "onBluetoothDeviceDisconnected", "onHeadsetPlugged", "onHeadsetUnplugged", "app_debug"})
public abstract interface BluetoothEventListener {
    
    public abstract void onBluetoothDeviceConnected(@org.jetbrains.annotations.Nullable()
    android.bluetooth.BluetoothDevice device);
    
    public abstract void onBluetoothDeviceDisconnected(@org.jetbrains.annotations.Nullable()
    android.bluetooth.BluetoothDevice device);
    
    public abstract void onA2dpConnected();
    
    public abstract void onA2dpDisconnected();
    
    public abstract void onHeadsetPlugged();
    
    public abstract void onHeadsetUnplugged();
}