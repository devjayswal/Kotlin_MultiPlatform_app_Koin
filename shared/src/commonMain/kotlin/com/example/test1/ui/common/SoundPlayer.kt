package com.example.test1.ui.common

interface SoundPlayer {
    fun playTick()
    fun playFinished()
}

object NoOpSoundPlayer : SoundPlayer {
    override fun playTick() = Unit

    override fun playFinished() = Unit
}

