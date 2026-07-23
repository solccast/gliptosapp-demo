package com.example.gliptosapp.ui.settings.sound

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.gliptosapp.R
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
object SoundManager : DefaultLifecycleObserver {
    private var soundEnabled = true
    private var soundPool: SoundPool? = null
    private var backgroundPlayer: MediaPlayer? = null

    private var clickId = 0 //SoundPool nunca devuelve un ID válido igual a 0 | (0 representa que no se cargó)
    private var errorId = 0

    private var picoId = 0
    private var palaId = 0
    private var pincelId = 0
    private var appContext: Context? = null
    fun initialize(
        context: Context
    ) {
        appContext = context.applicationContext
        if (soundPool == null) {

            soundPool =
                SoundPool.Builder()
                    .setMaxStreams(5)
                    .build()

            clickId =
                soundPool!!.load(
                    context,
                    R.raw.click_button,
                    1
                )

            errorId =
                soundPool!!.load(
                    context,
                    R.raw.error_sound,
                    1
                )

            picoId =
                soundPool!!.load(
                    context,
                    R.raw.pico,
                    1
                )

            palaId =
                soundPool!!.load(
                    context,
                    R.raw.pala,
                    1
                )

            pincelId =
                soundPool!!.load(
                    context,
                    R.raw.pincel,
                    1
                )
        }
        refreshAudioState(context)
    }

    private fun soundsEnabled(
        context: Context
    ): Boolean {

        return context
            .getSharedPreferences(
                "settings",
                Context.MODE_PRIVATE
            )
            .getBoolean(
                "sound_enabled",
                true
            )
    }

    private fun play(
        soundId: Int
    ) {

        if (!soundEnabled)
            return

        soundPool?.play(
            soundId,
            1f,
            1f,
            1,
            0,
            1f
        )
    }
    fun playPico() = play(picoId)
    fun playPala() = play(palaId)
    fun playPincel() = play(pincelId)
    fun playError() = play(errorId)

    fun startBackgroundMusic(
        context: Context
    ) {

        if (!soundsEnabled(context))
            return

        if (backgroundPlayer == null) {

            backgroundPlayer =
                MediaPlayer.create(
                    context.applicationContext,
                    R.raw.background_music
                )

            backgroundPlayer?.isLooping = true
            backgroundPlayer?.setVolume(
                0.2f,
                0.2f
            )
        }

        if (backgroundPlayer?.isPlaying == false) {
            backgroundPlayer?.start()
        }
    }

    fun pauseBackgroundMusic() {
        backgroundPlayer?.pause()
    }

    fun refreshAudioState(
        context: Context
    ) {

        if (!soundsEnabled(context)) {

            pauseBackgroundMusic()

        } else {

            startBackgroundMusic(context)
        }
    }
    override fun onStart(owner: LifecycleOwner) {
        appContext?.let {
            refreshAudioState(it)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        pauseBackgroundMusic()
    }
}