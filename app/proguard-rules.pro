# Add project specific ProGuard rules here.
-keep class com.arthenica.mobileffmpeg.** { *; }
-keep class androidx.media3.** { *; }
-keepclassmembers class * {
    @androidx.media3.common.util.UnstableApi *;
}
