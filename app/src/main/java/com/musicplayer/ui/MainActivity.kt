package com.musicplayer.ui

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.musicplayer.R
import com.musicplayer.bluetooth.BluetoothEventListener
import com.musicplayer.bluetooth.BluetoothManager
import com.musicplayer.databinding.ActivityMainBinding
import com.musicplayer.service.MusicPlaybackService
import kotlinx.coroutines.launch

/**
 * Main activity with modern UI and gesture support
 */
class MainActivity : AppCompatActivity(), BluetoothEventListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MusicPlayerViewModel
    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]

        setupBluetoothListener()
        checkPermissions()
        setupMediaController()
        setupObservers()
        setupUI()
    }

    private fun setupBluetoothListener() {
        BluetoothManager.getInstance().setListener(this)
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            permissions.add(Manifest.permission.BLUETOOTH)
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            onPermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onPermissionsGranted()
            } else {
                Toast.makeText(
                    this,
                    "Permissions required for music playback",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun onPermissionsGranted() {
        lifecycleScope.launch {
            viewModel.loadMediaLibrary()
        }
    }

    private fun setupMediaController() {
        val sessionToken = SessionToken(
            this,
            ComponentName(this, MusicPlaybackService::class.java)
        )

        mediaControllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        mediaControllerFuture?.addListener(
            {
                mediaController = mediaControllerFuture?.get()
                // MediaController is ready
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun setupObservers() {
        viewModel.tracks.observe(this) { tracks ->
            // Update UI with tracks
        }

        viewModel.playbackState.observe(this) { state ->
            updatePlaybackUI(state)
        }

        viewModel.currentTrack.observe(this) { track ->
            // Update now playing UI
        }
    }

    private fun setupUI() {
        // Play/Pause button
        binding.btnPlayPause.setOnClickListener {
            mediaController?.let { controller ->
                if (controller.isPlaying) {
                    controller.pause()
                } else {
                    controller.play()
                }
            }
        }

        // Next button
        binding.btnNext.setOnClickListener {
            mediaController?.seekToNext()
        }

        // Previous button
        binding.btnPrevious.setOnClickListener {
            mediaController?.seekToPrevious()
        }

        // Seek bar
        binding.seekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaController?.seekTo(progress.toLong())
                }
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
    }

    private fun updatePlaybackUI(state: com.musicplayer.domain.PlaybackState) {
        // Update UI based on playback state
        binding.btnPlayPause.setImageResource(
            if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )
    }

    // Bluetooth Event Callbacks
    override fun onBluetoothDeviceConnected(device: BluetoothDevice?) {
        runOnUiThread {
            Toast.makeText(this, "Bluetooth device connected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBluetoothDeviceDisconnected(device: BluetoothDevice?) {
        runOnUiThread {
            Toast.makeText(this, "Bluetooth device disconnected", Toast.LENGTH_SHORT).show()
            mediaController?.pause()
        }
    }

    override fun onA2dpConnected() {
        // Handle A2DP connection
    }

    override fun onA2dpDisconnected() {
        // Handle A2DP disconnection
    }

    override fun onHeadsetPlugged() {
        // Optionally resume playback
    }

    override fun onHeadsetUnplugged() {
        // Pause playback
        mediaController?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaControllerFuture?.let {
            MediaController.releaseFuture(it)
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
