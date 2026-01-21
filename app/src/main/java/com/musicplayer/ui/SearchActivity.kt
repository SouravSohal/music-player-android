package com.musicplayer.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.musicplayer.databinding.ActivitySearchBinding
import com.musicplayer.domain.AudioTrack
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: MusicPlayerViewModel
    private lateinit var searchAdapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        searchAdapter = MusicAdapter(
            onTrackClick = { track, position -> playTrack(track) },
            onMoreClick = { track, position -> showTrackOptions(track) }
        )
        binding.rvSearchResults.adapter = searchAdapter
    }

    private fun setupSearch() {
        binding.etSearch.requestFocus()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    showRecentSearches()
                    binding.btnClearSearch.visibility = View.GONE
                } else {
                    performSearch(query)
                    binding.btnClearSearch.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnClearSearch.setOnClickListener {
            binding.etSearch.text.clear()
        }

        // Filter chips
        binding.chipSearchAll.setOnClickListener { performSearch(binding.etSearch.text.toString()) }
        binding.chipSearchSongs.setOnClickListener { performSearch(binding.etSearch.text.toString()) }
        binding.chipSearchAlbums.setOnClickListener { performSearch(binding.etSearch.text.toString()) }
        binding.chipSearchArtists.setOnClickListener { performSearch(binding.etSearch.text.toString()) }
    }

    private fun setupObservers() {
        viewModel.tracks.observe(this) { tracks ->
            // Show all tracks initially
            if (binding.etSearch.text.isEmpty()) {
                showSearchResults(tracks)
            }
        }
    }

    private fun performSearch(query: String) {
        val allTracks = viewModel.tracks.value ?: emptyList()
        
        if (query.isEmpty()) {
            // Show all tracks when search is empty
            showSearchResults(allTracks)
            return
        }

        val results = allTracks.filter { track ->
            track.title.contains(query, ignoreCase = true) ||
            track.artist.contains(query, ignoreCase = true) ||
            track.album.contains(query, ignoreCase = true)
        }

        showSearchResults(results)
    }

    private fun showRecentSearches() {
        // Hide this for now and just show all tracks
        val allTracks = viewModel.tracks.value ?: emptyList()
        showSearchResults(allTracks)
    }

    private fun showSearchResults(results: List<AudioTrack>) {
        binding.layoutRecentSearches.visibility = View.GONE
        binding.layoutSearchResults.visibility = View.VISIBLE

        if (results.isEmpty()) {
            binding.tvNoResults.visibility = View.VISIBLE
            binding.rvSearchResults.visibility = View.GONE
        } else {
            binding.tvNoResults.visibility = View.GONE
            binding.rvSearchResults.visibility = View.VISIBLE
            searchAdapter.submitList(results)
        }
    }

    private fun playTrack(track: AudioTrack) {
        // Play track and close search
        viewModel.setCurrentTrack(track)
        finish()
    }

    private fun showTrackOptions(track: AudioTrack) {
        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val sheetBinding = com.musicplayer.databinding.BottomSheetTrackOptionsBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(sheetBinding.root)
        
        sheetBinding.tvSheetTitle.text = track.title
        
        sheetBinding.optionAddToPlaylist.setOnClickListener {
            bottomSheetDialog.dismiss()
            showSelectPlaylistDialog(track)
        }
        
        sheetBinding.optionPlayNext.setOnClickListener {
            bottomSheetDialog.dismiss()
            android.widget.Toast.makeText(this, "Play Next: ${track.title}", android.widget.Toast.LENGTH_SHORT).show()
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
        android.util.Log.d("SearchActivity", "showSelectPlaylistDialog called for track: ${track.title}")
        val playlists = viewModel.playlists.value ?: emptyList()
        android.util.Log.d("SearchActivity", "Available playlists: ${playlists.size}")
        
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
                    android.util.Log.d("SearchActivity", "=== User clicked playlist: ${playlist.name} (ID: ${playlist.id}) ===")
                    android.util.Log.d("SearchActivity", "Track to add: ${track.title} (ID: ${track.id})")
                    
                    if (track.id == 0L) {
                        android.util.Log.e("SearchActivity", "ERROR: Track ID is 0!")
                        android.widget.Toast.makeText(this@SearchActivity, "Error: Invalid track ID", android.widget.Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    
                    if (playlist.id == 0L) {
                        android.util.Log.e("SearchActivity", "ERROR: Playlist ID is 0!")
                        android.widget.Toast.makeText(this@SearchActivity, "Error: Invalid playlist ID", android.widget.Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    
                    viewModel.addTrackToPlaylist(playlist.id, track.id)
                    android.widget.Toast.makeText(this@SearchActivity, "\u2713 Added to ${playlist.name}", android.widget.Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    android.util.Log.d("SearchActivity", "Successfully added track to playlist")
                } catch (e: IllegalStateException) {
                    android.util.Log.w("SearchActivity", "Track already in playlist", e)
                    android.widget.Toast.makeText(this@SearchActivity, "${e.message}", android.widget.Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: Exception) {
                    android.util.Log.e("SearchActivity", "Error adding to playlist", e)
                    android.widget.Toast.makeText(this@SearchActivity, "Failed: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
        
        dialogBinding.rvPlaylistSelection.adapter = playlistAdapter
        playlistAdapter.submitList(playlists)
        
        dialogBinding.btnCreateNewPlaylist.setOnClickListener {
            dialog.dismiss()
            showCreatePlaylistDialog()
        }
        
        android.util.Log.d("SearchActivity", "Showing playlist selection dialog")
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
                    android.widget.Toast.makeText(this@SearchActivity, "Playlist \"$name\" created", android.widget.Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: Exception) {
                    android.widget.Toast.makeText(this@SearchActivity, "Error creating playlist: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    private fun shareTrack(track: AudioTrack) {
        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_SUBJECT, track.title)
            putExtra(android.content.Intent.EXTRA_TEXT, "${track.title} by ${track.artist}")
        }
        startActivity(android.content.Intent.createChooser(shareIntent, "Share track"))
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
        }
        
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle("Track Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun formatDuration(durationMs: Long): String {
        val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(durationMs)
        val seconds = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}
