package com.musicplayer.audio

import android.media.audiofx.*

/**
 * Manages all audio effects and enhancements
 */
class AudioEffectsManager(private val audioSessionId: Int) {

    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null
    private var loudnessEnhancer: LoudnessEnhancer? = null
    private var presetReverb: PresetReverb? = null

    init {
        initializeEffects()
    }

    private fun initializeEffects() {
        try {
            equalizer = Equalizer(0, audioSessionId).apply {
                enabled = false
            }

            bassBoost = BassBoost(0, audioSessionId).apply {
                enabled = false
            }

            virtualizer = Virtualizer(0, audioSessionId).apply {
                enabled = false
            }

            loudnessEnhancer = LoudnessEnhancer(audioSessionId).apply {
                enabled = false
            }

            presetReverb = PresetReverb(0, audioSessionId).apply {
                enabled = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Enable or disable equalizer
     */
    fun setEqualizerEnabled(enabled: Boolean) {
        equalizer?.enabled = enabled
    }

    /**
     * Set equalizer preset
     */
    fun setEqualizerPreset(preset: Short) {
        equalizer?.usePreset(preset)
    }

    /**
     * Set custom equalizer band level
     */
    fun setEqualizerBandLevel(band: Short, level: Short) {
        equalizer?.setBandLevel(band, level)
    }

    /**
     * Get number of equalizer bands
     */
    fun getNumberOfBands(): Short {
        return equalizer?.numberOfBands ?: 0
    }

    /**
     * Get band frequency range
     */
    fun getBandFreqRange(band: Short): IntArray? {
        return equalizer?.getBandFreqRange(band)
    }

    /**
     * Set bass boost strength (0-1000)
     */
    fun setBassBoost(strength: Int) {
        bassBoost?.apply {
            enabled = strength > 0
            setStrength(strength.toShort())
        }
    }

    /**
     * Set treble enhancement (using equalizer high bands)
     */
    fun setTrebleEnhancement(level: Int) {
        equalizer?.let { eq ->
            if (eq.numberOfBands >= 5) {
                val highBand = (eq.numberOfBands - 1).toShort()
                val levelValue = ((level / 100f) * 1000).toInt().toShort()
                eq.setBandLevel(highBand, levelValue)
            }
        }
    }

    /**
     * Enable spatial/surround effect
     */
    fun setSpatialEffect(enabled: Boolean, strength: Int = 500) {
        virtualizer?.apply {
            this.enabled = enabled
            if (enabled) {
                setStrength(strength.toShort())
            }
        }
    }

    /**
     * Set vocal clarity (using loudness enhancer)
     */
    fun setVocalClarity(enabled: Boolean) {
        loudnessEnhancer?.apply {
            this.enabled = enabled
            if (enabled) {
                setTargetGain(300) // 3dB boost
            }
        }
    }

    /**
     * Apply reverb preset
     */
    fun setReverbPreset(preset: Short) {
        presetReverb?.apply {
            enabled = preset >= 0
            if (enabled) {
                this.preset = preset
            }
        }
    }

    /**
     * Reset all effects to default
     */
    fun resetAllEffects() {
        equalizer?.enabled = false
        bassBoost?.enabled = false
        virtualizer?.enabled = false
        loudnessEnhancer?.enabled = false
        presetReverb?.enabled = false
    }

    /**
     * Release all audio effects
     */
    fun release() {
        equalizer?.release()
        bassBoost?.release()
        virtualizer?.release()
        loudnessEnhancer?.release()
        presetReverb?.release()
    }
}
