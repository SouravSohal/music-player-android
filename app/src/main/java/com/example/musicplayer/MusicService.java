package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private final IBinder binder = new MusicBinder();
    private MediaPlayer mediaPlayer;
    private List<Song> songs = new ArrayList<>();
    private int currentPosition = -1;
    private OnSongChangeListener songChangeListener;

    public interface OnSongChangeListener {
        void onSongChanged(Song song);
        void onPlaybackStateChanged(boolean isPlaying);
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> playNext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void setSongChangeListener(OnSongChangeListener listener) {
        this.songChangeListener = listener;
    }

    public void playSong(int position) {
        if (songs.isEmpty() || position < 0 || position >= songs.size()) {
            return;
        }

        currentPosition = position;
        Song song = songs.get(position);

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            if (songChangeListener != null) {
                songChangeListener.onSongChanged(song);
                songChangeListener.onPlaybackStateChanged(true);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error playing song: " + e.getMessage());
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (songChangeListener != null) {
                songChangeListener.onPlaybackStateChanged(false);
            }
        }
    }

    public void resume() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            if (songChangeListener != null) {
                songChangeListener.onPlaybackStateChanged(true);
            }
        }
    }

    public void playNext() {
        if (songs.isEmpty()) {
            return;
        }
        int nextPosition = (currentPosition + 1) % songs.size();
        playSong(nextPosition);
    }

    public void playPrevious() {
        if (songs.isEmpty()) {
            return;
        }
        int previousPosition = currentPosition - 1;
        if (previousPosition < 0) {
            previousPosition = songs.size() - 1;
        }
        playSong(previousPosition);
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    public int getDuration() {
        return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
    }

    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
