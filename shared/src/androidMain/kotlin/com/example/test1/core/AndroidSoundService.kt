package com.example.test1.core

import android.media.AudioManager
import android.media.ToneGenerator

class AndroidSoundService : SoundService {
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    override fun playTick() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
    }
}
