package com.musicplayer.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.musicplayer.audio.AudioEffectsManager
import com.musicplayer.ui.MainActivity

/**
 * Foreground service for music playback with MediaSession support
 * Handles background playback, lock-screen controls, and Bluetooth integration
 */
class MusicPlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer
    private lateinit var audioEffectsManager: AudioEffectsManager

    override fun onCreate() {
        super.onCreate()
        initializePlayer()
        initializeMediaSession()
        audioEffectsManager = AudioEffectsManager(player.audioSessionId)
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this)
            .build()
            .apply {
                // Enable gapless playback
                setWakeMode(android.os.PowerManager.PARTIAL_WAKE_LOCK)
                
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_ENDED -> {
                                // Handle track end for crossfade
                            }
                        }
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        // Update notification and UI
                    }
                })
            }
    }

    private fun initializeMediaSession() {
        val sessionActivityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(sessionActivityPendingIntent)
            .setCallback(MediaSessionCallback())
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        audioEffectsManager.release()
        super.onDestroy()
    }

    /**
     * MediaSession callback to handle custom commands and actions
     */
    private inner class MediaSessionCallback : MediaSession.Callback {
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                .add(SessionCommand(COMMAND_TOGGLE_SHUFFLE, Bundle.EMPTY))
                .add(SessionCommand(COMMAND_SET_REPEAT_MODE, Bundle.EMPTY))
                .add(SessionCommand(COMMAND_APPLY_AUDIO_EFFECTS, Bundle.EMPTY))
                .add(SessionCommand(COMMAND_GET_AUDIO_SESSION, Bundle.EMPTY))
                .build()

            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            when (customCommand.customAction) {
                COMMAND_TOGGLE_SHUFFLE -> {
                    player.shuffleModeEnabled = !player.shuffleModeEnabled
                }
                COMMAND_SET_REPEAT_MODE -> {
                    val mode = args.getInt("mode", Player.REPEAT_MODE_OFF)
                    player.repeatMode = mode
                }
                COMMAND_APPLY_AUDIO_EFFECTS -> {
                    applyAudioEffects(args)
                }
                COMMAND_GET_AUDIO_SESSION -> {
                    val resultBundle = Bundle().apply {
                        putInt("audioSessionId", player.audioSessionId)
                    }
                    return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS, resultBundle))
                }
            }
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }
    }

    /**
     * Apply audio effects and enhancements
     */
    private fun applyAudioEffects(args: Bundle) {
        val bassBoost = args.getInt("bassBoost", 0)
        val trebleEnhancement = args.getInt("trebleEnhancement", 0)
        val equalizerEnabled = args.getBoolean("equalizerEnabled", false)
        
        audioEffectsManager.setBassBoost(bassBoost)
        audioEffectsManager.setTrebleEnhancement(trebleEnhancement)
        audioEffectsManager.setEqualizerEnabled(equalizerEnabled)
    }
    
    /**
     * Get the audio session ID for effects
     */
    fun getAudioSessionId(): Int {
        return player.audioSessionId
    }

    companion object {
        private const val COMMAND_TOGGLE_SHUFFLE = "TOGGLE_SHUFFLE"
        private const val COMMAND_SET_REPEAT_MODE = "SET_REPEAT_MODE"
        private const val COMMAND_APPLY_AUDIO_EFFECTS = "APPLY_AUDIO_EFFECTS"
        const val COMMAND_GET_AUDIO_SESSION = "GET_AUDIO_SESSION"
    }
}
