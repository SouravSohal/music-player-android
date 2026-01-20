# Contributing to Music Player Android

Thank you for your interest in contributing to the Music Player Android project! This document provides guidelines and instructions for contributing.

## Code of Conduct

- Be respectful and inclusive
- Welcome newcomers and help them learn
- Focus on constructive feedback
- Assume good intentions

## How to Contribute

### Reporting Bugs

**Before submitting a bug report:**
1. Check existing issues to avoid duplicates
2. Test on the latest version
3. Gather relevant information

**Bug Report Should Include:**
- Clear, descriptive title
- Steps to reproduce
- Expected vs actual behavior
- Android version and device model
- Screenshots if applicable
- Logs if available

**Template:**
```markdown
**Description:**
Clear description of the bug

**Steps to Reproduce:**
1. Go to '...'
2. Click on '...'
3. Scroll to '...'
4. See error

**Expected Behavior:**
What should happen

**Actual Behavior:**
What actually happens

**Environment:**
- Device: [e.g., Pixel 6]
- Android Version: [e.g., Android 13]
- App Version: [e.g., 1.0.0]

**Screenshots:**
If applicable

**Additional Context:**
Any other relevant information
```

### Suggesting Features

**Before suggesting a feature:**
1. Check if it's already planned or exists
2. Consider if it fits the app's scope
3. Think about implementation complexity

**Feature Request Should Include:**
- Clear description
- Use cases and benefits
- Possible implementation approach
- Mockups or examples if applicable

### Pull Requests

**Before Creating a PR:**
1. Fork the repository
2. Create a feature branch (`feature/your-feature-name`)
3. Make your changes
4. Test thoroughly
5. Update documentation
6. Follow code style guidelines

**PR Guidelines:**
- One feature/fix per PR
- Clear, descriptive title
- Detailed description of changes
- Reference related issues
- Include tests if applicable
- Update documentation
- Follow commit message conventions

**PR Template:**
```markdown
**Description:**
What does this PR do?

**Related Issues:**
Fixes #123

**Type of Change:**
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

**Testing:**
How was this tested?

**Screenshots:**
If applicable

**Checklist:**
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] No new warnings generated
- [ ] Tests added/updated
- [ ] All tests pass
```

## Development Setup

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Git

### Setup Steps

1. **Fork and Clone**
```bash
git clone https://github.com/YOUR_USERNAME/music-player-android.git
cd music-player-android
```

2. **Open in Android Studio**
- File → Open → Select project directory
- Wait for Gradle sync

3. **Create Feature Branch**
```bash
git checkout -b feature/your-feature-name
```

4. **Make Changes**
- Write code
- Add tests
- Update docs

5. **Test**
```bash
./gradlew test
./gradlew connectedAndroidTest
```

6. **Commit**
```bash
git add .
git commit -m "feat: add your feature"
git push origin feature/your-feature-name
```

7. **Create PR**
- Go to GitHub
- Click "New Pull Request"
- Fill out PR template
- Submit

## Code Style Guidelines

### Kotlin Style

**Follow Official Kotlin Style Guide:**
- Use 4 spaces for indentation
- Use camelCase for variables and functions
- Use PascalCase for classes
- Use UPPER_SNAKE_CASE for constants

**Example:**
```kotlin
class MusicPlayer {
    private val currentTrack: AudioTrack? = null
    
    fun playTrack(track: AudioTrack) {
        // Implementation
    }
    
    companion object {
        private const val TAG = "MusicPlayer"
        const val DEFAULT_VOLUME = 0.5f
    }
}
```

### XML Style

**Layout Files:**
- Use ConstraintLayout for complex layouts
- Group related attributes
- Use meaningful IDs
- Follow Material Design guidelines

**Example:**
```xml
<TextView
    android:id="@+id/tvTitle"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:text="@string/title"
    android:textSize="24sp"
    android:textStyle="bold"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```

### Comments

**When to Comment:**
- Complex algorithms
- Non-obvious business logic
- Workarounds for bugs
- Public API documentation

**When NOT to Comment:**
- Self-explanatory code
- Restating the code
- Commented-out code (delete it)

**Example:**
```kotlin
/**
 * Converts video file to audio format with specified quality
 * 
 * @param inputPath Path to input video file
 * @param quality Desired audio quality settings
 * @return Flow emitting conversion progress and result
 */
fun convertVideoToAudio(
    inputPath: String,
    quality: AudioQuality
): Flow<ConversionResult>
```

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/musicplayer/
│   │   │   ├── audio/          # Audio processing
│   │   │   ├── bluetooth/      # Bluetooth integration
│   │   │   ├── data/           # Data layer
│   │   │   ├── domain/         # Domain models
│   │   │   ├── download/       # Download functionality
│   │   │   ├── service/        # Background services
│   │   │   ├── ui/             # UI layer
│   │   │   └── utils/          # Utility classes
│   │   ├── res/                # Resources
│   │   └── AndroidManifest.xml
│   └── test/                   # Unit tests
└── build.gradle
```

## Testing Guidelines

### Unit Tests
- Test business logic
- Mock Android dependencies
- Use descriptive test names
- Aim for high coverage

**Example:**
```kotlin
@Test
fun `formatDuration should format milliseconds correctly`() {
    val result = FormatUtils.formatDuration(125000)
    assertEquals("2:05", result)
}
```

### Integration Tests
- Test component interactions
- Use AndroidX Test
- Test on real Android environment

### UI Tests
- Test user flows
- Use Espresso
- Test on multiple devices

## Documentation

### Code Documentation
- KDoc for public APIs
- Clear parameter descriptions
- Return value documentation
- Exception documentation

### Architecture Documentation
- Update ARCHITECTURE.md for major changes
- Document design decisions
- Explain trade-offs

### User Documentation
- Update README.md
- Add usage examples
- Include screenshots

## Commit Message Convention

Use conventional commits format:

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance tasks

**Examples:**
```
feat(audio): add equalizer presets
fix(bluetooth): handle disconnect gracefully
docs(readme): update installation instructions
refactor(player): simplify playback logic
```

## Review Process

### For Contributors
1. Create PR following template
2. Wait for review feedback
3. Address comments
4. Request re-review
5. Wait for approval and merge

### For Reviewers
1. Check code quality
2. Verify tests
3. Test functionality
4. Review documentation
5. Provide constructive feedback
6. Approve or request changes

## Community

### Communication Channels
- GitHub Issues: Bug reports and features
- GitHub Discussions: Questions and ideas
- Pull Requests: Code review and discussion

### Getting Help
- Read documentation
- Search existing issues
- Ask in discussions
- Be patient and respectful

## Recognition

Contributors will be:
- Listed in CONTRIBUTORS.md
- Mentioned in release notes
- Credited in the app (future)

## License

By contributing, you agree that your contributions will be licensed under the same license as the project.

## Questions?

If you have questions about contributing, please:
1. Check this guide
2. Read the documentation
3. Search existing issues
4. Open a new discussion

Thank you for contributing! 🎵
