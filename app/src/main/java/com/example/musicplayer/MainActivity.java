package com.example.musicplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MusicService.OnSongChangeListener {
    private ListView songListView;
    private TextView songTitleTextView;
    private TextView artistTextView;
    private SeekBar seekBar;
    private Button playPauseButton;
    private Button nextButton;
    private Button previousButton;

    private List<Song> songs = new ArrayList<>();
    private SongAdapter songAdapter;
    private MusicService musicService;
    private boolean serviceBound = false;
    private Handler handler = new Handler();
    private boolean isUserSeeking = false;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    loadSongs();
                } else {
                    Toast.makeText(this, R.string.permission_required, Toast.LENGTH_LONG).show();
                }
            });

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            serviceBound = true;
            musicService.setSongs(songs);
            musicService.setSongChangeListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
        checkPermissionsAndLoadSongs();

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initializeViews() {
        songListView = findViewById(R.id.songListView);
        songTitleTextView = findViewById(R.id.songTitleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        seekBar = findViewById(R.id.seekBar);
        playPauseButton = findViewById(R.id.playPauseButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);

        songAdapter = new SongAdapter(this, songs);
        songListView.setAdapter(songAdapter);
    }

    private void setupListeners() {
        songListView.setOnItemClickListener((parent, view, position, id) -> {
            if (serviceBound) {
                musicService.playSong(position);
            }
        });

        playPauseButton.setOnClickListener(v -> {
            if (serviceBound) {
                if (musicService.isPlaying()) {
                    musicService.pause();
                } else {
                    musicService.resume();
                }
            }
        });

        nextButton.setOnClickListener(v -> {
            if (serviceBound) {
                musicService.playNext();
            }
        });

        previousButton.setOnClickListener(v -> {
            if (serviceBound) {
                musicService.playPrevious();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && serviceBound) {
                    musicService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserSeeking = false;
            }
        });
    }

    private void checkPermissionsAndLoadSongs() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_AUDIO;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            loadSongs();
        } else {
            requestPermissionLauncher.launch(permission);
        }
    }

    private void loadSongs() {
        songs.clear();
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int pathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

                String title = cursor.getString(titleIndex);
                String artist = cursor.getString(artistIndex);
                String path = cursor.getString(pathIndex);
                long duration = cursor.getLong(durationIndex);

                Song song = new Song(title, artist, path, duration);
                songs.add(song);
            }
            cursor.close();
        }

        songAdapter.notifyDataSetChanged();

        if (songs.isEmpty()) {
            Toast.makeText(this, R.string.no_songs, Toast.LENGTH_SHORT).show();
        }

        if (serviceBound) {
            musicService.setSongs(songs);
        }
    }

    @Override
    public void onSongChanged(Song song) {
        runOnUiThread(() -> {
            songTitleTextView.setText(song.getTitle());
            artistTextView.setText(song.getArtist());
            if (serviceBound) {
                seekBar.setMax(musicService.getDuration());
            }
        });
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        runOnUiThread(() -> {
            playPauseButton.setText(isPlaying ? R.string.pause : R.string.play);
            if (isPlaying) {
                startSeekBarUpdate();
            }
        });
    }

    private void startSeekBarUpdate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (serviceBound && musicService.isPlaying() && !isUserSeeking) {
                    seekBar.setProgress(musicService.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
        handler.removeCallbacksAndMessages(null);
    }
}
