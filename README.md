# Music Player Android Application

A full-featured, production-ready music player application for Android with advanced audio processing, background playback, Bluetooth integration, and media management capabilities.

## 🎵 Features

### Core Functionality

#### 1. Local Music & Media Management
- **Multi-format Support**: MP3, WAV, FLAC, AAC, OGG
- **Smart Scanning**: Automatic detection of audio files on device
- **Organization**: Browse by folders, artists, albums, or all tracks
- **Metadata Display**: ID3 tags, album art, duration, bitrate, sample rate
- **Search**: Fast search across titles, artists, and albums
- **Playlists**: Create, edit, and manage custom playlists

#### 2. Background & Lock-Screen Playback
- **Foreground Service**: Continues playback when app is minimized
- **Lock-Screen Controls**: Full media controls on lock screen
- **MediaSession Integration**: Native Android media framework
- **Notification Controls**: Play, pause, next, previous with album art
- **Auto-Resume**: Maintains playback state across interruptions

#### 3. Bluetooth & External Device Integration
- **Seamless Audio Output**: Automatic Bluetooth device detection
- **Headset Controls**: Full support for Bluetooth headset buttons
- **Smart Handling**: Pause on disconnect, optional resume on connect
- **A2DP Support**: High-quality Bluetooth audio streaming
- **Wired Headphones**: Detect and respond to headphone plug/unplug

#### 4. Audio Enhancement & Effects
- **Equalizer**: 5-band equalizer with 10 presets
  - Normal, Classical, Dance, Flat, Folk, Heavy Metal, Hip Hop, Jazz, Pop, Rock
- **Bass Boost**: Adjustable low-frequency enhancement (0-100%)
- **Treble Enhancement**: High-frequency clarity control
- **Vocal Clarity**: Loudness enhancement for voice clarity
- **Spatial Effects**: Virtual surround sound simulation
- **Noise Reduction**: Optional background noise filtering
- **Real-Time Processing**: Low-latency audio effects

#### 5. Advanced Playback Features
- **Gapless Playback**: Seamless transitions between tracks
- **Crossfade**: Smooth blending between songs
- **Speed Control**: Adjust playback speed without pitch change
- **Pitch Control**: Modify pitch independently
- **Repeat Modes**: Off, Repeat One, Repeat All
- **Shuffle**: Random playback order
- **ReplayGain**: Automatic loudness normalization

#### 6. Video to Audio Conversion
- **Local Conversion**: Convert video files to audio formats
- **Format Selection**: MP3, AAC, FLAC output options
- **Quality Control**: Bitrate and sample rate selection
- **Progress Tracking**: Real-time conversion progress
- **FFmpeg Integration**: Professional-grade conversion engine

#### 7. Online Media Download
- **URL Input**: Download from video URLs
- **Audio Extraction**: Extract audio-only from videos
- **Quality Selection**: Choose bitrate and codec
- **Error Handling**: Graceful failure recovery
- **Legal Compliance**: Built-in copyright notices

## 🏗️ Architecture

### High-Level System Design

```
┌─────────────────────────────────────────────────────────────┐
│                        UI Layer (MVVM)                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  MainActivity │  │  ViewModels  │  │   Adapters   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │    Models    │  │  Use Cases   │  │ Repositories │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ MediaStore   │  │ Room Database│  │  Network API │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    Service Layer                             │
│  ┌──────────────────────────────────────────────────┐       │
│  │         MusicPlaybackService (Foreground)        │       │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐ │       │
│  │  │ ExoPlayer  │  │MediaSession│  │   Audio    │ │       │
│  │  │   Engine   │  │  Manager   │  │  Effects   │ │       │
│  │  └────────────┘  └────────────┘  └────────────┘ │       │
│  └──────────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                 External Integrations                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Bluetooth   │  │    FFmpeg    │  │   Network    │      │
│  │   Manager    │  │  Converter   │  │  Downloader  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

### Component Breakdown

#### UI Layer
- **MainActivity**: Main entry point with playback controls
- **MusicPlayerViewModel**: State management and business logic
- **Material Design 3**: Modern, gesture-enabled UI
- **ViewBinding**: Type-safe view access

#### Service Layer
- **MusicPlaybackService**: Foreground service with MediaSessionService
- **ExoPlayer**: High-performance media playback engine
- **MediaSession**: Lock-screen and external control integration
- **Notification Manager**: Persistent media notification

#### Audio Processing
- **AudioEffectsManager**: Centralizes all audio effects
- **Equalizer**: Android's built-in audio equalizer
- **BassBoost, Virtualizer**: Native audio effects
- **VideoToAudioConverter**: FFmpeg-based conversion

#### Data Management
- **MediaRepository**: Scans and queries local media
- **PlaylistRepository**: Manages playlist CRUD operations
- **Room Database**: Persistent storage for playlists

#### Bluetooth Integration
- **BluetoothReceiver**: Broadcast receiver for BT events
- **BluetoothManager**: Singleton event coordinator
- **Event Callbacks**: Clean interface for BT state changes

#### Download System
- **VideoDownloader**: HTTP-based download with progress
- **OkHttp**: Robust networking library
- **Error Handling**: Comprehensive failure recovery

## 📋 Technology Stack

### Core Technologies
- **Language**: Kotlin 1.9.20
- **Build System**: Gradle 8.2.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Major Libraries
- **Media**: AndroidX Media3 (ExoPlayer) 1.2.0
- **UI**: Material Components 1.11.0
- **Architecture**: AndroidX Lifecycle 2.7.0
- **Navigation**: Navigation Component 2.7.6
- **Database**: Room 2.6.1
- **Networking**: Retrofit 2.9.0, OkHttp 4.12.0
- **Audio Processing**: Mobile FFmpeg 4.4.LTS
- **Coroutines**: Kotlinx Coroutines 1.7.3
- **Image Loading**: Glide 4.16.0

## 🚀 Setup & Installation

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 34
- Gradle 8.2+
- Java 17

### Build Instructions

1. **Clone the repository**
```bash
git clone https://github.com/SouravSohal/music-player-android.git
cd music-player-android
```

2. **Open in Android Studio**
   - File → Open → Select project directory
   - Wait for Gradle sync to complete

3. **Build the project**
```bash
./gradlew build
```

4. **Run on device/emulator**
```bash
./gradlew installDebug
```

Or use Android Studio's Run button (▶️)

### Permissions Required
The app requests the following permissions:
- Storage (READ_MEDIA_AUDIO, READ_MEDIA_VIDEO on API 33+)
- Bluetooth (BLUETOOTH_CONNECT on API 31+)
- Internet (for downloads)
- Foreground Service
- Post Notifications (API 33+)

## 📱 User Flows

### Primary User Flows

#### 1. Play Local Music
```
Launch App → Grant Permissions → Library Scans Automatically 
→ Browse Tracks/Albums/Artists → Tap to Play → Lock Screen Shows Controls
```

#### 2. Create Playlist
```
Library View → Long Press Track → Add to Playlist → Create New Playlist 
→ Name Playlist → Add More Tracks → Save
```

#### 3. Download & Convert
```
Menu → Download → Enter URL → Select Audio Only → Choose Quality 
→ Download → Audio Appears in Library
```

#### 4. Convert Video to Audio
```
Menu → Convert → Select Video File → Choose Output Format (MP3/AAC/FLAC) 
→ Set Bitrate → Convert → Track Added to Library
```

#### 5. Apply Audio Effects
```
Now Playing → Equalizer Button → Choose Preset or Custom 
→ Adjust Bass/Treble → Enable Spatial Effects → Apply
```

#### 6. Bluetooth Playback
```
Connect Bluetooth Device → App Auto-Detects → Play Music 
→ Use Headset Controls → Disconnect → Playback Pauses
```

## 🔒 Security & Legal Considerations

### Security Measures
1. **Permission Scoping**: Request only necessary permissions
2. **Runtime Permissions**: Proper Android 6.0+ permission handling
3. **Input Validation**: URL and file path sanitization
4. **Secure Storage**: No sensitive data in SharedPreferences
5. **HTTPS Only**: Network calls use secure connections

### Legal Compliance
⚠️ **Copyright Notice**: Users are responsible for ensuring they have legal rights to download and convert content. The app includes:
- Prominent legal notices
- Terms of service acceptance
- Educational warnings about copyright

**Recommendations for Production**:
- Implement content verification
- Partner with legal streaming services
- Add DRM support if needed
- Include comprehensive ToS and Privacy Policy
- Consider geo-restrictions

## 🎯 Performance Optimization

### Battery Efficiency
- **Foreground Service**: Only active during playback
- **Wake Locks**: Minimal, only for playback
- **Background Limits**: Respects Android Doze mode
- **Efficient Scanning**: MediaStore queries with projections

### Memory Management
- **Glide**: Efficient image loading and caching
- **Paging**: Large lists use pagination
- **Lifecycle Awareness**: Proper cleanup in onDestroy

### Audio Quality
- **Hardware Acceleration**: Uses device audio hardware
- **Low Latency**: ExoPlayer's optimized pipeline
- **Buffer Management**: Smart audio buffering

## 🧪 Testing Strategy

### Unit Tests
- Repository logic
- ViewModel state management
- Audio effects calculations

### Integration Tests
- MediaStore queries
- Database operations
- Service lifecycle

### UI Tests
- Navigation flows
- Playback controls
- Permission handling

## 🔮 Future Enhancements

### Planned Features
- **Cloud Sync**: Backup playlists to cloud storage
- **Streaming Integration**: Spotify, YouTube Music APIs
- **Lyrics Display**: Synchronized lyrics
- **Sleep Timer**: Auto-stop playback
- **Car Mode**: Android Auto integration
- **Voice Control**: Google Assistant integration
- **Social Features**: Share playlists with friends
- **Advanced Analytics**: Listening statistics
- **Podcast Support**: Podcast player with chapters
- **Online Radio**: Internet radio streaming

### Scalability Considerations
- **Modular Architecture**: Easy to add new features
- **Clean Separation**: UI, domain, and data layers
- **Repository Pattern**: Abstracted data sources
- **Dependency Injection**: Ready for Hilt/Koin
- **Multi-module**: Can split into feature modules

## 📄 License

This project is provided as-is for educational and reference purposes. Please ensure compliance with all applicable laws and licenses when using this code.

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Follow Kotlin coding conventions
4. Add tests for new features
5. Submit a pull request

## 📞 Support

For issues, questions, or feature requests, please open an issue on GitHub.

---

**Built with ❤️ for music lovers**