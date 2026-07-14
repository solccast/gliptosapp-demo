package com.example.gliptosapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.gliptosapp.data.db.DatabaseProvider

@HiltAndroidApp
class GliptosApp: Application(){
    val database by lazy { DatabaseProvider.getDatabase(this) }

    // Acá deberia inicializar el repositorio pasando el DAO
    // val excavacionRepository by lazy { ExcavacionRepository(database.excavacionDao()) }

    override fun onCreate() {
        super.onCreate()
    }
}