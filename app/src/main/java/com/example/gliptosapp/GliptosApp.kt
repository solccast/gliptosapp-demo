package com.example.gliptosapp

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.gliptosapp.ui.settings.sound.SoundManager
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.di.ApplicationScope
import dagger.hilt.android.HiltAndroidApp
import com.example.gliptosapp.repository.ExcavacionRepository
import com.example.gliptosapp.repository.FosilRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class GliptosApp : Application() {
    @Inject
    lateinit var fosilRepository: FosilRepository
    @Inject
    lateinit var excavacionRepository: ExcavacionRepository

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()

        SoundManager.initialize(this)

        ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(SoundManager)

        applicationScope.launch {
            fosilRepository.sembrarSiEsNecesario()
            preCargarExcavacionesDeLaPlata()
        }
    }

    private suspend fun preCargarExcavacionesDeLaPlata() {
        val listaActual = excavacionRepository.todasLasExcavaciones.first()
        if (listaActual.isEmpty()) {
            val excavacionesIniciales = listOf(
                Excavacion(
                    //nombre = "Gliptodonte",
                    fosilId = 1,
                    latitud = -34.9205, // Plaza Moreno aprox
                    longitud = -57.9536,
                    icResName = "ic_gliptodonte"
                ),
                Excavacion(
                    //nombre = "Euphractus",
                    fosilId = 2,
                    latitud = -34.9250, // Zona Parque Saavedra
                    longitud = -57.9400,
                    icResName = "ic_euphractus"
                ),
                Excavacion(
                    //nombre = "Smilodon",
                    fosilId = 3,
                    latitud = -34.9100, // Zona Plaza Italia
                    longitud = -57.9580,
                    icResName = "ic_smilodon"
                ),
            )

            excavacionRepository.insertVariasExcavaciones(excavacionesIniciales)
        }
    }
}