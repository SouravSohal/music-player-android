package com.musicplayer.ui

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.musicplayer.R
import com.musicplayer.bluetooth.BluetoothEventListener
import com.musicplayer.bluetooth.BluetoothManager
import com.musicplayer.databinding.ActivityMainYtmusicBinding
import com.musicplayer.databinding.BottomSheetTrackOptionsBinding
import com.musicplayer.databinding.DialogCreatePlaylistBinding
import com.musicplayer.databinding.DialogSelectPlaylistBinding
import com.musicplayer.domain.AudioTrack
import com.musicplayer.domain.Playlist
import com.musicplayer.service.MusicPlaybackService
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), BluetoothEventListener {

    private lateinit var binding: ActivityMainYtmusicBinding
    private lateinit var viewModel: MusicPlayerViewModel
    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null
    private lateinit var musicAdapter: MusicAdapter
    private var currentTrackPosition = -1
    private var cachedPlaylists: List<Playlist> = emptyList()
    private var repeatMode = Player.REPEAT_MODE_OFF
    private var shuffleEnabled = false
    private var seekBarUpdateJob: kotlinx.coroutines.Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainYtmusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]

        setupBluetoothListener()
        setupRecyclerView()
        setupToolbar()
        checkPermissions()
        setupMediaController()
        setupObservers()
        setupUI()
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_playlists -> {
                    showPlaylistsDialog()
                    true
                }
                R.id.action_equalizer -> {
                    openEqualizer()
                    true
                }
                R.id.action_settings -> {
                    Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
    
    private fun openEqualizer() {
        // Get audio session ID from MediaController via custom command
        mediaController?.let { controller ->
            val command = androidx.media3.session.SessionCommand(
                com.musicplayer.service.MusicPlaybackService.COMMAND_GET_AUDIO_SESSION,
                Bundle.EMPTY
            )
            
            val future = controller.sendCustomCommand(command, Bundle.EMPTY)
            future.addListener({
                try {
                    val result = future.get()
                    val audioSessionId = result.extras.getInt("audioSessionId", 0)
                    
                    val intent = Intent(this, EqualizerActivity::class.java)
                    intent.putExtra("audioSessionId", audioSessionId)
                    startActivity(intent)
                } catch (e: Exception) {
                    android.util.Log.e("MainActivity", "Failed to get audio session ID", e)
                    // Fallback: open equalizer with session ID 0
                    val intent = Intent(this, EqualizerActivity::class.java)
                    intent.putExtra("audioSessionId", 0)
                    startActivity(intent)
                }
            }, MoreExecutors.directExecutor())
        } ?: run {
            // MediaController not ready
            Toast.makeText(this, "Player not ready. Please play a song first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBluetoothListener() {
        BluetoothManager.getInstance().setListener(this)
    }

    private fun setupRecyclerView() {
        musicAdapter = MusicAdapter(
            onTrackClick = { track, position -> playTrack(track, position) },
            onMoreClick = { track, position -> showTrackOptions(track, position) }
        )
        binding.rvMusicList.adapter = musicAdapter
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        
        // Storage/Media permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        
        // Bluetooth permission for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val granted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (granted) {
            onPermissionsGranted()
        } else {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onPermissionsGranted()
            } else {
                Toast.makeText(this, "Storage permission required to play music", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onPermissionsGranted() {
        lifecycleScope.launch {
            viewModel.loadMediaLibrary()
        }
    }

    private fun setupMediaController() {
        val sessionToken = SessionToken(this, ComponentName(this, MusicPlaybackService::class.java))
        mediaControllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        mediaControllerFuture?.addListener(
            {
                try {
                    mediaController = mediaControllerFuture?.get()
                    mediaController?.addListener(playerListener)
                } catch (e: Exception) {
                    android.util.Log.e("MainActivity", "MediaController connection failed", e)
                    Toast.makeText(this, "Playback service unavailable. Please restart the app.", Toast.LENGTH_LONG).show()
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlayPauseButton(isPlaying)
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaItem?.let { updateNowPlaying() }
        }
    }

    private fun setupObservers() {
        viewModel.tracks.observe(this) { tracks ->
            musicAdapter.submitList(tracks)
            if (tracks.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.rvMusicList.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.rvMusicList.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }

        viewModel.currentTrack.observe(this) { track ->
            track?.let { updateBottomPlayer(it) }
        }
        
        // Cache playlists to avoid re-observing in dialogs
        viewModel.playlists.observe(this) { playlists ->
            cachedPlaylists = playlists
            android.util.Log.d("MainActivity", "Playlists updated: ${playlists.size} playlists available")
            playlists.forEach {
                android.util.Log.d("MainActivity", "  - Playlist: ID=${it.id}, Name='${it.name}', Tracks=${it.trackCount}")
            }
        }
    }

    private fun setupUI() {
        // Setup toolbar with hamburger menu
        binding.toolbar.setNavigationOnClickListener {
            showNavigationMenu()
        }
        
        // Mini Player - expand to full screen
        binding.miniPlayer.setOnClickListener {
            val intent = android.content.Intent(this, FullScreenPlayerActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.fade_out)
        }
        
        // Mini Player controls
        binding.btnMiniPlayPause.setOnClickListener {
            mediaController?.let { controller ->
                if (controller.isPlaying) controller.pause() else controller.play()
            }
        }

        binding.btnMiniNext.setOnClickListener {
            mediaController?.seekToNext()
        }

        binding.miniSeekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaController?.seekTo((mediaController?.duration ?: 0) * progress / 100)
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        // Update seekbar periodically
        startSeekBarUpdates()
        
        // Chip filters
        binding.chipAll.setOnClickListener { viewModel.loadMediaLibrary() }
        binding.chipSongs.setOnClickListener { viewModel.loadMediaLibrary() }
        binding.chipPlaylists.setOnClickListener { showPlaylistsDialog() }
        
        // Bottom Navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already on home
                    true
                }
                R.id.nav_explore -> {
                    val intent = android.content.Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out)
                    true
                }
                R.id.nav_library -> {
                    showPlaylistsDialog()
                    true
                }
                R.id.nav_upgrade -> {
                    Toast.makeText(this, "Upgrade to Premium", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        
        // Search
        binding.searchCard.setOnClickListener {
            val intent = android.content.Intent(this, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out)
        }
    }
    
    private fun showCreatePlaylistDialog() {
        val dialogBinding = DialogCreatePlaylistBinding.inflate(layoutInflater)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setTitle("Create Playlist")
            .create()
        
        dialogBinding.btnCreate.setOnClickListener {
            val name = dialogBinding.etPlaylistName.text.toString().trim()
            val description = dialogBinding.etPlaylistDescription.text.toString().trim()
            
            if (name.isEmpty()) {
                dialogBinding.etPlaylistName.error = "Name required"
                return@setOnClickListener
            }
            
            lifecycleScope.launch {
                try {
                    viewModel.createPlaylist(name, description.ifEmpty { null })
                    Toast.makeText(this@MainActivity, "Playlist \"$name\" created", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error creating playlist: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    private fun showNavigationMenu() {
        val items = arrayOf(
            "\ud83c\udfb5 Songs",
            "\ud83d\udcbf Albums", 
            "\ud83c\udfa4 Artists",
            "\ud83d\udcc1 Folders",
            "\u2b50 Favorites",
            "\u2699\ufe0f Settings",
            "\u2139\ufe0f About"
        )
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Menu")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> { /* Already on Songs */ }
                    1 -> Toast.makeText(this, "Albums view - Coming soon", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this, "Artists view - Coming soon", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(this, "Folders view - Coming soon", Toast.LENGTH_SHORT).show()
                    4 -> Toast.makeText(this, "Favorites - Coming soon", Toast.LENGTH_SHORT).show()
                    5 -> Toast.makeText(this, "Settings - Coming soon", Toast.LENGTH_SHORT).show()
                    6 -> showAboutDialog()
                }
            }
            .show()
    }
    
    private fun showAboutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Music Player")
            .setMessage("A beautiful music player inspired by YouTube Music\\n\\nVersion: 1.0.0\\nBuilt with \u2764\ufe0f")
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showTrackOptions(track: AudioTrack, position: Int) {
        android.util.Log.d("MainActivity", "showTrackOptions called for track: ID=${track.id}, Title='${track.title}'")
        
        val bottomSheetDialog = BottomSheetDialog(this)
        val sheetBinding = BottomSheetTrackOptionsBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(sheetBinding.root)
        
        sheetBinding.tvSheetTitle.text = track.title
        
        sheetBinding.optionAddToPlaylist.setOnClickListener {
            android.util.Log.d("MainActivity", "Add to playlist clicked for track: ${track.title}")
            bottomSheetDialog.dismiss()
            showSelectPlaylistDialog(track)
        }
        
        sheetBinding.optionPlayNext.setOnClickListener {
            bottomSheetDialog.dismiss()
            mediaController?.let { controller ->
                val currentIndex = controller.currentMediaItemIndex
                val mediaItem = MediaItem.Builder()
                    .setUri(track.path)
                    .setMediaId(track.id.toString())
                    .build()
                controller.addMediaItem(currentIndex + 1, mediaItem)
                Toast.makeText(this, "${track.title} will play next", Toast.LENGTH_SHORT).show()
            } ?: Toast.makeText(this, "Player not ready", Toast.LENGTH_SHORT).show()
        }
        
        sheetBinding.optionShareTrack.setOnClickListener {
            bottomSheetDialog.dismiss()
            shareTrack(track)
        }
        
        sheetBinding.optionTrackDetails.setOnClickListener {
            bottomSheetDialog.dismiss()
            showTrackDetails(track)
        }
        
        bottomSheetDialog.show()
    }
    
    private fun showSelectPlaylistDialog(track: AudioTrack) {
        android.util.Log.d("MainActivity", "showSelectPlaylistDialog called for track: ${track.title}")
        android.util.Log.d("MainActivity", "Available playlists: ${cachedPlaylists.size}")
        
        if (cachedPlaylists.isEmpty()) {
            android.util.Log.w("MainActivity", "No playlists available, showing create dialog")
            MaterialAlertDialogBuilder(this)
                .setTitle("No Playlists")
                .setMessage("Create a playlist first to add songs.")
                .setPositiveButton("Create Playlist") { _, _ ->
                    showCreatePlaylistDialog()
                }
                .setNegativeButton("Cancel", null)
                .show()
            return
        }
        
        val dialogBinding = DialogSelectPlaylistBinding.inflate(layoutInflater)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setTitle("Add to Playlist")
            .create()
            
        val playlistAdapter = PlaylistAdapter { playlist ->
            lifecycleScope.launch {
                try {
                    android.util.Log.d("MainActivity", "=== User clicked playlist: ${playlist.name} (ID: ${playlist.id}) ===")
                    android.util.Log.d("MainActivity", "Track to add: ${track.title} (ID: ${track.id})")
                    
                    if (track.id == 0L) {
                        android.util.Log.e("MainActivity", "ERROR: Track ID is 0!")
                        Toast.makeText(this@MainActivity, "Error: Invalid track ID", Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    
                    if (playlist.id == 0L) {
                        android.util.Log.e("MainActivity", "ERROR: Playlist ID is 0!")
                        Toast.makeText(this@MainActivity, "Error: Invalid playlist ID", Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    
                    viewModel.addTrackToPlaylist(playlist.id, track.id)
                    Toast.makeText(this@MainActivity, "\u2713 Added to ${playlist.name}", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    android.util.Log.d("MainActivity", "Successfully added track to playlist")
                } catch (e: IllegalStateException) {
                    android.util.Log.w("MainActivity", "Track already in playlist", e)
                    Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: Exception) {
                    android.util.Log.e("MainActivity", "Error adding to playlist", e)
                    Toast.makeText(this@MainActivity, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        
        dialogBinding.rvPlaylistSelection.adapter = playlistAdapter
        playlistAdapter.submitList(cachedPlaylists)
        
        dialogBinding.btnCreateNewPlaylist.setOnClickListener {
            dialog.dismiss()
            showCreatePlaylistDialog()
        }
        
        android.util.Log.d("MainActivity", "Showing playlist selection dialog")
        dialog.show()
    }
    
    private fun shareTrack(track: AudioTrack) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, track.title)
            putExtra(Intent.EXTRA_TEXT, "${track.title} by ${track.artist}")
        }
        startActivity(Intent.createChooser(shareIntent, "Share track"))
    }
    
    private fun showTrackDetails(track: AudioTrack) {
        val details = buildString {
            appendLine("Title: ${track.title}")
            appendLine("Artist: ${track.artist}")
            appendLine("Album: ${track.album}")
            appendLine("Duration: ${formatDuration(track.duration)}")
            appendLine("Path: ${track.path}")
            if (track.bitrate != null) appendLine("Bitrate: ${track.bitrate} kbps")
            if (track.sampleRate != null) appendLine("Sample Rate: ${track.sampleRate} Hz")
            appendLine("Format: ${track.mimeType}")
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Track Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun formatDuration(durationMs: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
        return String.format("%d:%02d", minutes, seconds)
    }
    
    private fun showPlaylistsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_playlists, null)
        val recyclerView = dialogView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvPlaylists)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Playlists")
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .setNeutralButton("Create New") { _, _ -> showCreatePlaylistDialog() }
            .create()
        
        val playlistAdapter = PlaylistAdapter { playlist ->
            dialog.dismiss()
            val intent = android.content.Intent(this, PlaylistDetailActivity::class.java)
            intent.putExtra(PlaylistDetailActivity.EXTRA_PLAYLIST_ID, playlist.id)
            startActivity(intent)
        }
        recyclerView.adapter = playlistAdapter
        playlistAdapter.submitList(cachedPlaylists)
        
        dialog.show()
    }

    private fun playTrack(track: AudioTrack, position: Int) {
        currentTrackPosition = position
        viewModel.setCurrentTrack(track)
        
        mediaController?.let { controller ->
            val mediaItem = MediaItem.Builder().setUri(track.path).setMediaId(track.id.toString()).build()
            controller.setMediaItem(mediaItem)
            controller.prepare()
            controller.play()
        }
    }

    private fun updateBottomPlayer(track: AudioTrack) {
        binding.tvMiniTrackTitle.text = track.title
        binding.tvMiniArtist.text = track.artist
        
        Glide.with(this)
            .load(if (track.albumArtPath != null) Uri.parse(track.albumArtPath) else R.drawable.ic_music_note)
            .placeholder(R.drawable.ic_music_note)
            .error(R.drawable.ic_music_note)
            .centerCrop()
            .into(binding.ivMiniAlbumArt)
        
        binding.miniPlayer.visibility = View.VISIBLE
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        binding.btnMiniPlayPause.setImageResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
    }

    private fun updateSeekBar() {
        mediaController?.let { controller ->
            if (controller.duration > 0) {
                val progress = ((controller.currentPosition.toFloat() / controller.duration) * 100).toInt()
                binding.miniSeekBar.progress = progress
            }
        }
    }

    private fun updateNowPlaying() {
        mediaController?.currentMediaItem?.mediaId?.toLongOrNull()?.let { trackId ->
            val track = viewModel.tracks.value?.find { it.id == trackId }
            track?.let { viewModel.setCurrentTrack(it) }
        }
    }

    private fun toggleShuffle() {
        shuffleEnabled = !shuffleEnabled
        mediaController?.shuffleModeEnabled = shuffleEnabled
        val message = if (shuffleEnabled) "Shuffle enabled" else "Shuffle disabled"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun toggleRepeat() {
        repeatMode = when (repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }
        mediaController?.repeatMode = repeatMode
        val message = when (repeatMode) {
            Player.REPEAT_MODE_OFF -> "Repeat off"
            Player.REPEAT_MODE_ALL -> "Repeat all"
            Player.REPEAT_MODE_ONE -> "Repeat one"
            else -> "Repeat off"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBluetoothDeviceConnected(device: BluetoothDevice?) {
        runOnUiThread { Toast.makeText(this, "Bluetooth device connected", Toast.LENGTH_SHORT).show() }
    }

    override fun onBluetoothDeviceDisconnected(device: BluetoothDevice?) {
        runOnUiThread {
            Toast.makeText(this, "Bluetooth device disconnected", Toast.LENGTH_SHORT).show()
            mediaController?.pause()
        }
    }

    override fun onA2dpConnected() {}
    override fun onA2dpDisconnected() {}
    override fun onHeadsetPlugged() {}
    override fun onHeadsetUnplugged() { mediaController?.pause() }
    
    private fun startSeekBarUpdates() {
        seekBarUpdateJob?.cancel()
        seekBarUpdateJob = lifecycleScope.launch {
            while (true) {
                updateSeekBar()
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        seekBarUpdateJob?.cancel()
        mediaController?.removeListener(playerListener)
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
