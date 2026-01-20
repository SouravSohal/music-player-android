package com.musicplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicplayer.data.MediaRepository
import com.musicplayer.domain.AudioTrack
import com.musicplayer.domain.PlaybackState
import kotlinx.coroutines.launch

/**
 * ViewModel for music player following MVVM architecture
 */
class MusicPlayerViewModel : ViewModel() {

    private val _tracks = MutableLiveData<List<AudioTrack>>()
    val tracks: LiveData<List<AudioTrack>> = _tracks

    private val _currentTrack = MutableLiveData<AudioTrack?>()
    val currentTrack: LiveData<AudioTrack?> = _currentTrack

    private val _playbackState = MutableLiveData<PlaybackState>()
    val playbackState: LiveData<PlaybackState> = _playbackState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Load media library from device
     */
    fun loadMediaLibrary() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // In real implementation, inject MediaRepository
                // val tracks = mediaRepository.scanAudioFiles()
                // _tracks.value = tracks
            } catch (e: Exception) {
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
            // Implement search logic
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
}
