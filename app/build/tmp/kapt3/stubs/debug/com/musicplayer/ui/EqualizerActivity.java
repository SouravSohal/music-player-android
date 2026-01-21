package com.musicplayer.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.slider.Slider;
import com.musicplayer.R;
import com.musicplayer.audio.AudioEffectsManager;
import com.musicplayer.databinding.ActivityEqualizerBinding;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0002J\u0012\u0010\f\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0014J\b\u0010\u000f\u001a\u00020\nH\u0014J\b\u0010\u0010\u001a\u00020\nH\u0002J\b\u0010\u0011\u001a\u00020\nH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/musicplayer/ui/EqualizerActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "audioEffectsManager", "Lcom/musicplayer/audio/AudioEffectsManager;", "audioSessionId", "", "binding", "Lcom/musicplayer/databinding/ActivityEqualizerBinding;", "applyPreset", "", "presetIndex", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "resetAllEffects", "setupUI", "app_debug"})
public final class EqualizerActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.musicplayer.databinding.ActivityEqualizerBinding binding;
    @org.jetbrains.annotations.Nullable()
    private com.musicplayer.audio.AudioEffectsManager audioEffectsManager;
    private int audioSessionId = 0;
    
    public EqualizerActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupUI() {
    }
    
    private final void applyPreset(int presetIndex) {
    }
    
    private final void resetAllEffects() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
}