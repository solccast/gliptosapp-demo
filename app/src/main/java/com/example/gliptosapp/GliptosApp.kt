package com.example.gliptosapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.gliptosapp.repository.ExcavacionRepository

@HiltAndroidApp
class GliptosApp: Application(){

    override fun onCreate() {
        super.onCreate()
    }
}
