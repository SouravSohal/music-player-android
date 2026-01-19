# Architecture Documentation

## Overview

The Music Player Android application follows Clean Architecture principles with MVVM (Model-View-ViewModel) pattern for the UI layer. This ensures separation of concerns, testability, and maintainability.

## Architecture Layers

### 1. Presentation Layer (UI)

**Purpose**: Handle user interactions and display data

**Components**:
- `MainActivity`: Main UI entry point
- `MusicPlayerViewModel`: Business logic and state management
- XML Layouts: View definitions using Material Design 3
- ViewBinding: Type-safe view access

**Responsibilities**:
- Display data to user
- Capture user input
- Navigate between screens
- Observe ViewModel state changes

**Key Patterns**:
- MVVM for separation of UI and logic
- LiveData for reactive UI updates
- ViewBinding for type-safe views
- Single Activity architecture with Navigation Component

### 2. Domain Layer

**Purpose**: Business logic and data models

**Components**:
- `Models.kt`: Core domain models (AudioTrack, Album, Artist, etc.)
- Use cases (future): Business operations
- Repository interfaces (future): Data source abstractions

**Responsibilities**:
- Define business entities
- Encapsulate business rules
- Provide clean API for UI layer

**Key Patterns**:
- Immutable data classes
- Sealed classes for state representation
- Enums for type safety

### 3. Data Layer

**Purpose**: Data management and persistence

**Components**:
- `MediaRepository`: Local media scanning and queries
- `PlaylistRepository`: Playlist CRUD operations
- `PlaylistDatabase`: Room database for playlists
- `MediaStore`: Android system media provider

**Responsibilities**:
- Fetch data from various sources
- Cache and persist data
- Provide data to domain layer

**Key Patterns**:
- Repository pattern for data abstraction
- Room for local persistence
- Coroutines for async operations

### 4. Service Layer

**Purpose**: Background operations and system integration

**Components**:
- `MusicPlaybackService`: Foreground service for playback
- `MediaSession`: System media integration
- `ExoPlayer`: Media playback engine
- `AudioEffectsManager`: Audio processing

**Responsibilities**:
- Maintain playback state
- Handle system media controls
- Manage audio focus
- Provide lock-screen controls

**Key Patterns**:
- Foreground Service for reliable playback
- MediaSessionService for system integration
- Observer pattern for state updates

### 5. Integration Layer

**Purpose**: External system and device integration

**Components**:
- `BluetoothReceiver`: Bluetooth event handling
- `BluetoothManager`: Bluetooth state coordination
- `VideoToAudioConverter`: FFmpeg integration
- `VideoDownloader`: Network download handling

**Responsibilities**:
- Handle device connections
- Process media conversions
- Download external content
- Manage hardware integrations

**Key Patterns**:
- BroadcastReceiver for system events
- Singleton for global state
- Flow for async progress updates

## Data Flow

### Playback Flow
```
User Tap Track → MainActivity → ViewModel → MediaController 
→ MusicPlaybackService → ExoPlayer → Audio Output
                ↓
         AudioEffectsManager
                ↓
    Equalizer, BassBoost, etc.
```

### Media Scanning Flow
```
App Launch → Permission Check → MediaRepository → MediaStore Query 
→ AudioTrack List → ViewModel → MainActivity → Display
```

### Download Flow
```
User Input URL → VideoDownloader → HTTP Download → Local Storage 
→ MediaRepository Rescan → Library Update
```

### Bluetooth Flow
```
BT Device Connect → BluetoothReceiver → BluetoothManager 
→ MainActivity Callback → Resume Playback
```

## Key Design Decisions

### 1. ExoPlayer vs MediaPlayer
**Decision**: Use ExoPlayer
**Rationale**:
- Better codec support (FLAC, OGG, AAC)
- Gapless playback support
- Superior buffer management
- Active development and maintenance
- MediaSession integration

### 2. Room vs SQLite
**Decision**: Use Room
**Rationale**:
- Compile-time query verification
- Kotlin Coroutines support
- LiveData/Flow integration
- Less boilerplate code
- Type-safe queries

### 3. MediaStore vs File Scanner
**Decision**: Use MediaStore
**Rationale**:
- Android recommended approach
- Respects user privacy (scoped storage)
- System-indexed for performance
- Automatic metadata extraction
- No manual file permissions needed (Android 10+)

### 4. FFmpeg for Conversion
**Decision**: Use Mobile FFmpeg
**Rationale**:
- Industry-standard tool
- Supports all major formats
- High-quality output
- Customizable encoding options
- Well-documented

### 5. Foreground Service
**Decision**: Use MediaSessionService with foreground notification
**Rationale**:
- Required for background playback
- Prevents system from killing service
- User-visible notification (transparency)
- Android recommended practice
- Supports media controls

## Threading Model

### Main Thread
- UI updates
- View interactions
- ViewModel state changes

### IO Dispatcher
- MediaStore queries
- Database operations
- File I/O
- Network requests

### Default Dispatcher
- Data processing
- List filtering
- Computation-heavy tasks

### Player Thread
- ExoPlayer maintains internal threads
- Audio effects on audio thread
- Automatic thread management

## State Management

### Application State
- Managed by ViewModels
- Persisted across configuration changes
- LiveData for reactive updates

### Playback State
- Maintained in MusicPlaybackService
- Synchronized with MediaSession
- Restored on service restart

### User Preferences
- SharedPreferences (future implementation)
- Room database for playlists
- MediaStore for media metadata

## Error Handling

### Strategy
1. **Catch at Source**: Handle errors where they occur
2. **Propagate Meaningful Messages**: Convert exceptions to user-friendly messages
3. **Log for Debugging**: Use Android Logcat
4. **User Notification**: Toast/Snackbar for errors
5. **Graceful Degradation**: Continue operation if possible

### Common Error Scenarios
- **Permission Denied**: Request permissions, show rationale
- **Network Failure**: Retry logic, offline mode
- **File Not Found**: Remove from library, notify user
- **Conversion Error**: Show error message, keep original
- **Bluetooth Disconnect**: Pause playback, notify user

## Security Considerations

### Permission Model
- Request permissions at runtime
- Explain permission usage to user
- Gracefully handle denial
- Use scoped storage (Android 10+)

### Data Protection
- No sensitive data stored
- HTTPS for network calls
- Input validation for URLs
- File path sanitization

### User Privacy
- No analytics/tracking
- No data sharing
- Local processing only
- User control over data

## Testing Strategy

### Unit Tests
- ViewModel logic
- Repository operations
- Data transformations
- State management

### Integration Tests
- Database operations
- MediaStore queries
- Service lifecycle
- Bluetooth integration

### UI Tests
- Navigation flows
- User interactions
- Permission handling
- Playback controls

### Manual Tests
- Audio quality
- Battery usage
- Bluetooth devices
- Various Android versions

## Performance Considerations

### Memory
- Glide for image caching
- Pagination for large lists
- Proper lifecycle management
- Avoid memory leaks

### CPU
- Background processing
- Efficient queries
- Coroutines for concurrency
- Hardware-accelerated audio

### Battery
- Minimal wake locks
- Efficient service usage
- Respect Doze mode
- Background limits

### Storage
- Efficient database schema
- Image caching strategy
- Cleanup temporary files
- User control over cache

## Scalability

### Feature Addition
- Modular package structure
- Clear layer boundaries
- Dependency injection ready
- Repository abstraction

### Multi-Module (Future)
```
:app
:feature-player
:feature-library
:feature-download
:feature-effects
:core-audio
:core-data
:core-ui
```

### Dependency Injection (Future)
- Hilt recommended
- Easy to add later
- Current manual injection
- Testability maintained

## Accessibility

### Current Features
- Content descriptions
- Touch target sizes
- Color contrast
- Text scaling support

### Future Enhancements
- TalkBack optimization
- Switch access support
- Voice commands
- Haptic feedback

## Internationalization

### Current State
- English strings in resources
- Hardcoded text avoided
- Ready for translation

### Future Enhancements
- Multiple language support
- RTL layout support
- Locale-specific formats
- Cultural considerations

## Monitoring & Analytics (Future)

### Crash Reporting
- Firebase Crashlytics
- Stack trace collection
- User impact analysis

### Performance Monitoring
- App startup time
- Frame rendering
- Network performance
- Battery usage

### User Analytics
- Feature usage
- Retention metrics
- User flows
- Privacy-compliant

## Conclusion

This architecture provides a solid foundation for a production-ready music player app. It balances simplicity with extensibility, ensuring the codebase can evolve with new requirements while maintaining code quality and performance.
