package com.musicplayer.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.musicplayer.databinding.ActivityPlaylistDetailBinding
import com.musicplayer.domain.AudioTrack
import com.musicplayer.domain.Playlist
import kotlinx.coroutines.launch

class PlaylistDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistDetailBinding
    private lateinit var viewModel: MusicPlayerViewModel
    private lateinit var trackAdapter: MusicAdapter
    private var playlistId: Long = -1
    private var playlist: Playlist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]
        
        playlistId = intent.getLongExtra(EXTRA_PLAYLIST_ID, -1)
        if (playlistId == -1L) {
            Toast.makeText(this, "Invalid playlist", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupToolbar()
        setupRecyclerView()
        setupUI()
        loadPlaylistDetails()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        trackAdapter = MusicAdapter(
            onTrackClick = { track, position -> playTrack(track, position) },
            onMoreClick = { track, position -> showTrackOptions(track) }
        )
        binding.rvPlaylistTracks.adapter = trackAdapter
    }

    private fun setupUI() {
        binding.btnShufflePlay.setOnClickListener {
            shuffleAndPlay()
        }

        binding.btnPlayAll.setOnClickListener {
            playAll()
        }

        binding.fabAddSongs.setOnClickListener {
            showAddSongsDialog()
        }
    }
    
    private fun showAddSongsDialog() {
        android.util.Log.d("PlaylistDetailActivity", "Opening add songs dialog for playlist $playlistId")
        
        val allTracks = viewModel.tracks.value ?: emptyList()
        
        if (allTracks.isEmpty()) {
            Toast.makeText(this, "No songs available. Please grant storage permission.", Toast.LENGTH_LONG).show()
            return
        }
        
        val dialogBinding = com.musicplayer.databinding.DialogSelectPlaylistBinding.inflate(layoutInflater)
        
        // Reuse the dialog but change title and adapter
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setTitle("Add Songs to Playlist")
            .create()
        
        // Hide the create playlist button since we're selecting songs
        dialogBinding.btnCreateNewPlaylist.visibility = android.view.View.GONE
        
        // Use MusicAdapter to show all songs
        val songAdapter = MusicAdapter(
            onTrackClick = { track, _ ->
                // Add this song to the playlist
                lifecycleScope.launch {
                    try {
                        android.util.Log.d("PlaylistDetailActivity", "Adding track ${track.id} to playlist $playlistId")
                        
                        if (track.id == 0L) {
                            Toast.makeText(this@PlaylistDetailActivity, "Error: Invalid track ID", Toast.LENGTH_LONG).show()
                            return@launch
                        }
                        
                        viewModel.addTrackToPlaylist(playlistId, track.id)
                        Toast.makeText(this@PlaylistDetailActivity, "✓ Added ${track.title}", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } catch (e: IllegalStateException) {
                        Toast.makeText(this@PlaylistDetailActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        android.util.Log.e("PlaylistDetailActivity", "Error adding track", e)
                        Toast.makeText(this@PlaylistDetailActivity, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            onMoreClick = { _, _ -> /* Ignore more button in this context */ }
        )
        
        dialogBinding.rvPlaylistSelection.adapter = songAdapter
        songAdapter.submitList(allTracks)
        
        dialog.show()
    }

    private fun loadPlaylistDetails() {
        lifecycleScope.launch {
            try {
                // Find playlist from cached playlists
                viewModel.playlists.observe(this@PlaylistDetailActivity) { playlists ->
                    playlist = playlists.find { it.id == playlistId }
                    playlist?.let { updateUI(it) }
                }
                
                // Load playlist tracks from database
                viewModel.getPlaylistTracks(playlistId).observe(this@PlaylistDetailActivity) { tracks ->
                    android.util.Log.d("PlaylistDetailActivity", "Loaded ${tracks.size} tracks for playlist $playlistId")
                    if (tracks.isEmpty()) {
                        showEmptyState()
                    } else {
                        showTracks(tracks)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("PlaylistDetailActivity", "Error loading playlist", e)
                Toast.makeText(this@PlaylistDetailActivity, "Error loading playlist", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun updateUI(playlist: Playlist) {
        binding.tvPlaylistName.text = playlist.name
        binding.tvPlaylistInfo.text = "${playlist.trackCount} songs"
        binding.collapsingToolbar.title = playlist.name
        
        if (playlist.description != null && playlist.description.isNotEmpty()) {
            binding.tvPlaylistDescription.text = playlist.description
            binding.tvPlaylistDescription.visibility = View.VISIBLE
        } else {
            binding.tvPlaylistDescription.visibility = View.GONE
        }
    }

    private fun showEmptyState() {
        binding.layoutEmptyPlaylist.visibility = View.VISIBLE
        binding.layoutPlaylistTracks.visibility = View.GONE
    }

    private fun showTracks(tracks: List<AudioTrack>) {
        binding.layoutEmptyPlaylist.visibility = View.GONE
        binding.layoutPlaylistTracks.visibility = View.VISIBLE
        trackAdapter.submitList(tracks)
    }

    private fun playTrack(track: AudioTrack, position: Int) {
        viewModel.setCurrentTrack(track)
        // Set up playback queue with all playlist tracks
        val allTracks = trackAdapter.currentList
        if (allTracks.isNotEmpty()) {
            android.util.Log.d("PlaylistDetailActivity", "Playing track $position of ${allTracks.size}")
        }
        Toast.makeText(this, "Playing: ${track.title}", Toast.LENGTH_SHORT).show()
    }

    private fun showTrackOptions(track: AudioTrack) {
        // Show bottom sheet with options to remove from playlist, etc.
        Toast.makeText(this, "Track options: ${track.title}", Toast.LENGTH_SHORT).show()
    }

    private fun shuffleAndPlay() {
        val tracks = trackAdapter.currentList
        if (tracks.isEmpty()) {
            Toast.makeText(this, "No tracks to play", Toast.LENGTH_SHORT).show()
            return
        }
        
        val shuffled = tracks.shuffled()
        playTrack(shuffled.first(), 0)
        Toast.makeText(this, "Shuffle play started", Toast.LENGTH_SHORT).show()
    }

    private fun playAll() {
        val tracks = trackAdapter.currentList
        if (tracks.isEmpty()) {
            Toast.makeText(this, "No tracks to play", Toast.LENGTH_SHORT).show()
            return
        }
        
        playTrack(tracks.first(), 0)
        Toast.makeText(this, "Playing all tracks", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_PLAYLIST_ID = "playlist_id"
    }
}
