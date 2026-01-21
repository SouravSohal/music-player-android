package com.musicplayer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.musicplayer.data.MediaRepository
import com.musicplayer.data.PlaylistDatabase
import com.musicplayer.data.PlaylistRepository
import com.musicplayer.domain.AudioTrack
import com.musicplayer.domain.PlaybackState
import com.musicplayer.domain.Playlist
import kotlinx.coroutines.launch

/**
 * ViewModel for music player following MVVM architecture
 */
class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val mediaRepository = MediaRepository(application.contentResolver)
    
    private val playlistDatabase = Room.databaseBuilder(
        application,
        PlaylistDatabase::class.java,
        "music_player_db"
    )
    .fallbackToDestructiveMigration()
    .build()
    
    private val playlistRepository = PlaylistRepository(playlistDatabase.playlistDao())
    
    val playlists: LiveData<List<Playlist>> = playlistRepository.getAllPlaylists().asLiveData()

    private val _allTracks = MutableLiveData<List<AudioTrack>>()
    private val _tracks = MutableLiveData<List<AudioTrack>>()
    val tracks: LiveData<List<AudioTrack>> = _tracks

    private val _currentTrack = MutableLiveData<AudioTrack?>()
    val currentTrack: LiveData<AudioTrack?> = _currentTrack

    private val _playbackState = MutableLiveData<PlaybackState>()
    val playbackState: LiveData<PlaybackState> = _playbackState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    /**
     * Load media library from device
     */
    fun loadMediaLibrary() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                val scannedTracks = mediaRepository.scanAudioFiles()
                _allTracks.value = scannedTracks
                _tracks.value = scannedTracks
                
                if (scannedTracks.isEmpty()) {
                    _errorMessage.value = "No music files found on device"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading music: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Search tracks
     */
    fun searchTracks(query: String) {
        viewModelScope.launch {
            try {
                val allTracks = _allTracks.value ?: return@launch
                if (query.isBlank()) {
                    // Reset to all tracks
                    _tracks.value = allTracks
                    return@launch
                }
                
                val filtered = allTracks.filter { track ->
                    track.title.contains(query, ignoreCase = true) ||
                    track.artist.contains(query, ignoreCase = true) ||
                    track.album.contains(query, ignoreCase = true)
                }
                _tracks.value = filtered
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Update playback state
     */
    fun updatePlaybackState(state: PlaybackState) {
        _playbackState.value = state
    }

    /**
     * Set current track
     */
    fun setCurrentTrack(track: AudioTrack) {
        _currentTrack.value = track
    }

    /**
     * Get track by index
     */
    fun getTrackByIndex(index: Int): AudioTrack? {
        return _tracks.value?.getOrNull(index)
    }
    
    /**
     * Create new playlist
     */
    fun createPlaylist(name: String, description: String? = null) {
        viewModelScope.launch {
            try {
                android.util.Log.d("MusicPlayerViewModel", "Creating playlist: $name")
                val playlistId = playlistRepository.createPlaylist(name, description)
                android.util.Log.d("MusicPlayerViewModel", "Playlist created with ID: $playlistId")
            } catch (e: Exception) {
                android.util.Log.e("MusicPlayerViewModel", "Error creating playlist", e)
                _errorMessage.value = "Error creating playlist: ${e.message}"
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Add track to playlist
     */
    fun addTrackToPlaylist(playlistId: Long, trackId: Long) {
        viewModelScope.launch {
            try {
                android.util.Log.d("MusicPlayerViewModel", "Attempting to add track $trackId to playlist $playlistId")
                playlistRepository.addTrackToPlaylist(playlistId, trackId)
                android.util.Log.d("MusicPlayerViewModel", "Successfully added track to playlist")
            } catch (e: Exception) {
                android.util.Log.e("MusicPlayerViewModel", "Error adding track to playlist", e)
                _errorMessage.value = "Error adding track: ${e.message}"
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Get tracks for a specific playlist
     */
    fun getPlaylistTracks(playlistId: Long): LiveData<List<AudioTrack>> {
        val result = MutableLiveData<List<AudioTrack>>()
        viewModelScope.launch {
            try {
                playlistRepository.getPlaylistTracksWithDetails(playlistId).collect { trackIds ->
                    val allTracks = _allTracks.value ?: emptyList()
                    val playlistTracks = trackIds.mapNotNull { trackId ->
                        allTracks.find { it.id == trackId }
                    }
                    result.postValue(playlistTracks)
                }
            } catch (e: Exception) {
                android.util.Log.e("MusicPlayerViewModel", "Error loading playlist tracks", e)
                result.postValue(emptyList())
            }
        }
        return result
    }
}
