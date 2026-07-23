package com.example.gliptosapp

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.gliptosapp.ui.settings.sound.SoundManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GliptosApp : Application() {

    override fun onCreate() {
        super.onCreate()

        SoundManager.initialize(this)

        ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(SoundManager)
    }
}