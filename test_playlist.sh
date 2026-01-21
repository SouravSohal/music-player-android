#!/bin/bash

echo "=========================================="
echo "Music Player - Playlist Debugging Script"
echo "=========================================="
echo ""

# Check if ADB is available
if ! command -v adb &> /dev/null; then
    echo "❌ ADB not found. Please install Android SDK Platform Tools."
    echo "   Download from: https://developer.android.com/tools/releases/platform-tools"
    exit 1
fi

# Check if device is connected
if ! adb devices | grep -q "device$"; then
    echo "❌ No Android device connected."
    echo "   Please connect your device and enable USB debugging."
    exit 1
fi

echo "✅ Device connected"
echo ""

# Get package name
PACKAGE="com.musicplayer"

# Check if app is installed
if ! adb shell pm list packages | grep -q "$PACKAGE"; then
    echo "❌ App not installed."
    echo "   Installing APK..."
    adb install -r app/build/outputs/apk/debug/app-debug.apk
    if [ $? -ne 0 ]; then
        echo "❌ Installation failed!"
        exit 1
    fi
    echo "✅ App installed"
fi

echo "📱 App is installed"
echo ""
echo "🧹 Clearing app data (to reset database)..."
adb shell pm clear $PACKAGE

echo ""
echo "🚀 Launching app..."
adb shell am start -n $PACKAGE/.ui.MainActivity

echo ""
echo "📋 Monitoring logs (Press Ctrl+C to stop)..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Monitor logs with color coding
adb logcat -c  # Clear old logs
adb logcat | grep --line-buffered -E "(MainActivity|MusicPlayerViewModel|PlaylistRepository|FullScreenPlayer|SearchActivity)" | while read line; do
    if echo "$line" | grep -q "ERROR"; then
        echo -e "\033[0;31m$line\033[0m"  # Red
    elif echo "$line" | grep -q "WARN"; then
        echo -e "\033[0;33m$line\033[0m"  # Yellow
    elif echo "$line" | grep -q "=== "; then
        echo -e "\033[0;36m$line\033[0m"  # Cyan
    elif echo "$line" | grep -q "Successfully"; then
        echo -e "\033[0;32m$line\033[0m"  # Green
    else
        echo "$line"
    fi
done
