package com.musicplayer.bluetooth

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Handles Bluetooth device connections and controls
 */
class BluetoothReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                handleBluetoothConnected(context, intent)
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                handleBluetoothDisconnected(context, intent)
            }
            BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> {
                handleA2dpConnectionStateChanged(context, intent)
            }
            Intent.ACTION_HEADSET_PLUG -> {
                handleHeadsetPlugged(context, intent)
            }
        }
    }

    private fun handleBluetoothConnected(context: Context, intent: Intent) {
        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        Log.d(TAG, "Bluetooth device connected: ${device?.name}")
        
        // Optionally resume playback
        BluetoothManager.getInstance().onDeviceConnected(device)
    }

    private fun handleBluetoothDisconnected(context: Context, intent: Intent) {
        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        Log.d(TAG, "Bluetooth device disconnected: ${device?.name}")
        
        // Optionally pause playback
        BluetoothManager.getInstance().onDeviceDisconnected(device)
    }

    private fun handleA2dpConnectionStateChanged(context: Context, intent: Intent) {
        val state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_DISCONNECTED)
        when (state) {
            BluetoothA2dp.STATE_CONNECTED -> {
                Log.d(TAG, "A2DP connected")
                BluetoothManager.getInstance().onA2dpConnected()
            }
            BluetoothA2dp.STATE_DISCONNECTED -> {
                Log.d(TAG, "A2DP disconnected")
                BluetoothManager.getInstance().onA2dpDisconnected()
            }
        }
    }

    private fun handleHeadsetPlugged(context: Context, intent: Intent) {
        val state = intent.getIntExtra("state", -1)
        when (state) {
            0 -> {
                Log.d(TAG, "Headset unplugged")
                BluetoothManager.getInstance().onHeadsetUnplugged()
            }
            1 -> {
                Log.d(TAG, "Headset plugged")
                BluetoothManager.getInstance().onHeadsetPlugged()
            }
        }
    }

    companion object {
        private const val TAG = "BluetoothReceiver"
    }
}

/**
 * Singleton manager for Bluetooth state and callbacks
 */
class BluetoothManager private constructor() {

    private var listener: BluetoothEventListener? = null

    fun setListener(listener: BluetoothEventListener) {
        this.listener = listener
    }

    fun onDeviceConnected(device: BluetoothDevice?) {
        listener?.onBluetoothDeviceConnected(device)
    }

    fun onDeviceDisconnected(device: BluetoothDevice?) {
        listener?.onBluetoothDeviceDisconnected(device)
    }

    fun onA2dpConnected() {
        listener?.onA2dpConnected()
    }

    fun onA2dpDisconnected() {
        listener?.onA2dpDisconnected()
    }

    fun onHeadsetPlugged() {
        listener?.onHeadsetPlugged()
    }

    fun onHeadsetUnplugged() {
        listener?.onHeadsetUnplugged()
    }

    companion object {
        @Volatile
        private var instance: BluetoothManager? = null

        fun getInstance(): BluetoothManager {
            return instance ?: synchronized(this) {
                instance ?: BluetoothManager().also { instance = it }
            }
        }
    }
}

/**
 * Interface for Bluetooth events
 */
interface BluetoothEventListener {
    fun onBluetoothDeviceConnected(device: BluetoothDevice?)
    fun onBluetoothDeviceDisconnected(device: BluetoothDevice?)
    fun onA2dpConnected()
    fun onA2dpDisconnected()
    fun onHeadsetPlugged()
    fun onHeadsetUnplugged()
}
