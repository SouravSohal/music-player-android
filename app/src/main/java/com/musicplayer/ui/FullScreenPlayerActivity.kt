package com.musicplayer.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.musicplayer.R
import com.musicplayer.databinding.ActivityPlayerFullscreenBinding
import com.musicplayer.domain.AudioTrack
import com.musicplayer.service.MusicPlaybackService
import android.content.ComponentName
import android.net.Uri
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class FullScreenPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerFullscreenBinding
    private lateinit var viewModel: MusicPlayerViewModel
    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null
    private var currentTrack: AudioTrack? = null
    private var repeatMode = Player.REPEAT_MODE_OFF
    private var shuffleEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]

        setupMediaController()
        setupUI()
        setupObservers()
        startSeekBarUpdates()
    }

    private fun setupMediaController() {
        val sessionToken = SessionToken(this, ComponentName(this, MusicPlaybackService::class.java))
        mediaControllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        mediaControllerFuture?.addListener(
            {
                mediaController = mediaControllerFuture?.get()
                mediaController?.addListener(playerListener)
                updateUI()
            },
            MoreExecutors.directExecutor()
        )
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlayPauseButton(isPlaying)
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateUI()
        }
    }

    private fun setupUI() {
        binding.btnCollapse.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down)
        }

        binding.btnPlayerPlayPause.setOnClickListener {
            mediaController?.let { controller ->
                if (controller.isPlaying) controller.pause() else controller.play()
            }
        }

        binding.btnPlayerNext.setOnClickListener {
            mediaController?.seekToNext()
        }

        binding.btnPlayerPrevious.setOnClickListener {
            mediaController?.seekToPrevious()
        }

        binding.btnPlayerShuffle.setOnClickListener {
            toggleShuffle()
        }

        binding.btnPlayerRepeat.setOnClickListener {
            toggleRepeat()
        }

        binding.btnPlayerEqualizer.setOnClickListener {
            startActivity(android.content.Intent(this, EqualizerActivity::class.java))
        }

        binding.btnPlayerShare.setOnClickListener {
            currentTrack?.let { shareTrack(it) }
        }

        binding.btnPlayerAddToPlaylist.setOnClickListener {
            currentTrack?.let { track ->
                showSelectPlaylistDialog(track)
            }
        }

        binding.btnPlayerFavorite.setOnClickListener {
            Toast.makeText(this, "Add to favorites", Toast.LENGTH_SHORT).show()
        }

        binding.playerSeekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaController?.seekTo((mediaController?.duration ?: 0) * progress / 100)
                }
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
    }

    private fun setupObservers() {
        viewModel.currentTrack.observe(this) { track ->
            track?.let {
                currentTrack = it
                updateTrackInfo(it)
            }
        }
    }

    private fun updateUI() {
        mediaController?.currentMediaItem?.mediaId?.toLongOrNull()?.let { trackId ->
            val track = viewModel.tracks.value?.find { it.id == trackId }
            track?.let {
                currentTrack = it
                viewModel.setCurrentTrack(it)
            }
        }
        mediaController?.let { controller ->
            updatePlayPauseButton(controller.isPlaying)
        }
    }

    private fun updateTrackInfo(track: AudioTrack) {
        binding.tvPlayerTrackTitle.text = track.title
        binding.tvPlayerArtist.text = track.artist

        Glide.with(this)
            .load(if (track.albumArtPath != null) Uri.parse(track.albumArtPath) else R.drawable.ic_music_note)
            .placeholder(R.drawable.ic_music_note)
            .error(R.drawable.ic_music_note)
            .centerCrop()
            .into(binding.ivPlayerAlbumArt)

        // Also set background blur
        Glide.with(this)
            .load(if (track.albumArtPath != null) Uri.parse(track.albumArtPath) else R.drawable.ic_music_note)
            .centerCrop()
            .into(binding.ivBackgroundBlur)
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        binding.btnPlayerPlayPause.setImageResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
    }

    private fun startSeekBarUpdates() {
        lifecycleScope.launch {
            while (true) {
                updateSeekBar()
                delay(1000)
            }
        }
    }

    private fun updateSeekBar() {
        mediaController?.let { controller ->
            if (controller.duration > 0) {
                val progress = ((controller.currentPosition.toFloat() / controller.duration) * 100).toInt()
                binding.playerSeekBar.progress = progress
                
                binding.tvCurrentTime.text = formatDuration(controller.currentPosition)
                binding.tvTotalTime.text = formatDuration(controller.duration)
            }
        }
    }

    private fun formatDuration(durationMs: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    private fun toggleShuffle() {
        shuffleEnabled = !shuffleEnabled
        mediaController?.shuffleModeEnabled = shuffleEnabled
        binding.btnPlayerShuffle.alpha = if (shuffleEnabled) 1.0f else 0.5f
        Toast.makeText(this, if (shuffleEnabled) "Shuffle on" else "Shuffle off", Toast.LENGTH_SHORT).show()
    }

    private fun toggleRepeat() {
        repeatMode = when (repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }
        mediaController?.repeatMode = repeatMode
        binding.btnPlayerRepeat.alpha = if (repeatMode != Player.REPEAT_MODE_OFF) 1.0f else 0.5f
        val message = when (repeatMode) {
            Player.REPEAT_MODE_OFF -> "Repeat off"
            Player.REPEAT_MODE_ALL -> "Repeat all"
            Player.REPEAT_MODE_ONE -> "Repeat one"
            else -> "Repeat off"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun shareTrack(track: AudioTrack) {
        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_SUBJECT, track.title)
            putExtra(android.content.Intent.EXTRA_TEXT, "${track.title} by ${track.artist}")
        }
        startActivity(android.content.Intent.createChooser(shareIntent, "Share track"))
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaController?.removeListener(playerListener)
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
    }
    
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down)
    }
    
    private fun showSelectPlaylistDialog(track: AudioTrack) {
        android.util.Log.d("FullScreenPlayer", "showSelectPlaylistDialog called for track: ${track.title}")
        val playlists = viewModel.playlists.value ?: emptyList()
        android.util.Log.d("FullScreenPlayer", "Available playlists: ${playlists.size}")
        
        if (playlists.isEmpty()) {
            com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("No Playlists")
                .setMessage("Create a playlist first to add songs.")
                .setPositiveButton("Create Playlist") { _, _ ->
                    showCreatePlaylistDialog()
                }
                .setNegativeButton("Cancel", null)
                .show()
            return
        }
        
        val dialogBinding = com.musicplayer.databinding.DialogSelectPlaylistBinding.inflate(layoutInflater)
        
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setTitle("Add to Playlist")
            .create()
            
        val playlistAdapter = PlaylistAdapter { playlist ->
            lifecycleScope.launch {
                try {
                    android.util.Log.d("FullScreenPlayer", "=== User clicked playlist: ${playlist.name} (ID: ${playlist.id}) ===")
                    android.util.Log.d("FullScreenPlayer", "Track to add: ${track.title} (ID: ${track.id})")
                    
                    if (track.id == 0L) {
                        android.util.Log.e("FullScreenPlayer", "ERROR: Track ID is 0!")
                        Toast.makeText(this@FullScreenPlayerActivity, "Error: Invalid track ID", Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    
                    if (playlist.id == 0L) {
                        android.util.Log.e("FullScreenPlayer", "ERROR: Playlist ID is 0!")
                        Toast.makeText(this@FullScreenPlayerActivity, "Error: Invalid playlist ID", Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    
                    viewModel.addTrackToPlaylist(playlist.id, track.id)
                    Toast.makeText(this@FullScreenPlayerActivity, "\u2713 Added to ${playlist.name}", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    android.util.Log.d("FullScreenPlayer", "Successfully added track to playlist")
                } catch (e: IllegalStateException) {
                    android.util.Log.w("FullScreenPlayer", "Track already in playlist", e)
                    Toast.makeText(this@FullScreenPlayerActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: Exception) {
                    android.util.Log.e("FullScreenPlayer", "Error adding to playlist", e)
                    Toast.makeText(this@FullScreenPlayerActivity, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        
        dialogBinding.rvPlaylistSelection.adapter = playlistAdapter
        playlistAdapter.submitList(playlists)
        
        dialogBinding.btnCreateNewPlaylist.setOnClickListener {
            dialog.dismiss()
            showCreatePlaylistDialog()
        }
        
        android.util.Log.d("FullScreenPlayer", "Showing playlist selection dialog")
        dialog.show()
    }
    
    private fun showCreatePlaylistDialog() {
        val dialogBinding = com.musicplayer.databinding.DialogCreatePlaylistBinding.inflate(layoutInflater)
        
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
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
                    Toast.makeText(this@FullScreenPlayerActivity, "Playlist \"$name\" created", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(this@FullScreenPlayerActivity, "Error creating playlist: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
}
