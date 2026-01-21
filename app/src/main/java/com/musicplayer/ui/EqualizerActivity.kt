package com.musicplayer.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.musicplayer.R
import com.musicplayer.audio.AudioEffectsManager
import com.musicplayer.databinding.ActivityEqualizerBinding

class EqualizerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEqualizerBinding
    private var audioEffectsManager: AudioEffectsManager? = null
    private var audioSessionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEqualizerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioSessionId = intent.getIntExtra("audioSessionId", 0)
        
        if (audioSessionId != 0) {
            try {
                audioEffectsManager = AudioEffectsManager(audioSessionId)
            } catch (e: Exception) {
                android.util.Log.e("EqualizerActivity", "Failed to initialize audio effects", e)
                Toast.makeText(this, "Audio effects not available on this device", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Please play a song first to use equalizer", Toast.LENGTH_LONG).show()
        }

        setupUI()
    }

    private fun setupUI() {
        // Equalizer enable/disable
        binding.switchEqualizer.setOnCheckedChangeListener { _, isChecked ->
            audioEffectsManager?.setEqualizerEnabled(isChecked)
        }

        // Preset selection
        binding.chipNormal.setOnClickListener { applyPreset(0) }
        binding.chipClassical.setOnClickListener { applyPreset(1) }
        binding.chipDance.setOnClickListener { applyPreset(2) }
        binding.chipRock.setOnClickListener { applyPreset(8) }
        binding.chipPop.setOnClickListener { applyPreset(7) }
        binding.chipJazz.setOnClickListener { applyPreset(5) }

        // Bass boost
        binding.sliderBass.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                val strength = (value * 10).toInt() // 0-1000 range
                audioEffectsManager?.setBassBoost(strength)
            }
        }

        // Treble enhancement
        binding.sliderTreble.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                audioEffectsManager?.setTrebleEnhancement(value.toInt())
            }
        }

        // Spatial effect
        binding.switchSpatial.setOnCheckedChangeListener { _, isChecked ->
            audioEffectsManager?.setSpatialEffect(isChecked, if (isChecked) 700 else 0)
        }

        // Vocal clarity
        binding.switchVocalClarity.setOnCheckedChangeListener { _, isChecked ->
            audioEffectsManager?.setVocalClarity(isChecked)
        }

        // Reset button
        binding.btnResetEffects.setOnClickListener {
            resetAllEffects()
            Toast.makeText(this, "Effects reset to default", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyPreset(presetIndex: Int) {
        audioEffectsManager?.setEqualizerEnabled(true)
        audioEffectsManager?.setEqualizerPreset(presetIndex.toShort())
        binding.switchEqualizer.isChecked = true
    }

    private fun resetAllEffects() {
        audioEffectsManager?.resetAllEffects()
        binding.switchEqualizer.isChecked = false
        binding.sliderBass.value = 0f
        binding.sliderTreble.value = 0f
        binding.switchSpatial.isChecked = false
        binding.switchVocalClarity.isChecked = false
        binding.chipGroupPresets.clearCheck()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioEffectsManager?.release()
    }
}
