# Music Player Android

A simple and functional music player application for Android that allows users to play audio files stored on their device.

## Features

- **Browse Music Library**: Automatically scans and displays all music files on the device
- **Playback Controls**: Play, pause, next, and previous track controls
- **Seek Bar**: Visual progress indicator and ability to seek within tracks
- **Now Playing Display**: Shows current song title and artist information
- **Background Playback**: Music continues playing in the background using a service

## Requirements

- Android Studio Arctic Fox or later
- Android SDK 24 (Android 7.0) or higher
- Gradle 8.1.0

## Permissions

The app requires the following permissions:
- `READ_MEDIA_AUDIO` (Android 13+)
- `READ_EXTERNAL_STORAGE` (Android 12 and below)
- `FOREGROUND_SERVICE` (for background playback)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/SouravSohal/music-player-android.git
   ```

2. Open the project in Android Studio

3. Build and run the application on an emulator or physical device

## Usage

1. Launch the app and grant storage permission when prompted
2. The app will automatically scan and display all music files on your device
3. Tap on any song in the list to start playing
4. Use the playback controls at the bottom:
   - **Previous**: Go to the previous track
   - **Play/Pause**: Toggle playback
   - **Next**: Skip to the next track
5. Use the seek bar to navigate within the current track

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/musicplayer/
│   │   ├── MainActivity.java        # Main activity with UI and controls
│   │   ├── MusicService.java        # Background service for music playback
│   │   ├── Song.java                # Data model for songs
│   │   └── SongAdapter.java         # Adapter for displaying songs in list
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml    # Main activity layout
│   │   │   └── song_item.xml        # Song list item layout
│   │   └── values/
│   │       ├── strings.xml          # String resources
│   │       ├── colors.xml           # Color resources
│   │       └── themes.xml           # App themes
│   └── AndroidManifest.xml          # App manifest with permissions
└── build.gradle                     # App-level build configuration
```

## Technical Details

- **Language**: Java
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Audio Playback**: MediaPlayer API
- **Service**: Bound service for background playback
- **UI Components**: Material Design Components

## License

This project is open source and available under the MIT License.