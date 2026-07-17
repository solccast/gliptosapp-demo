package com.example.gliptosapp.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.repository.ExcavacionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapaViewModel @Inject constructor(
    private val repository: ExcavacionRepository
) : ViewModel() {

    // El Activity observará esta lista. Si hay cambios en la BD, el mapa se actualiza solo.
    val excavaciones: StateFlow<List<Excavacion>> = repository.todasLasExcavaciones
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        preCargarFosilesDeLaPlata()
    }

    private fun preCargarFosilesDeLaPlata() {
        viewModelScope.launch {
            // Revisamos si la base de datos está vacía tomando el primer valor
            val listaActual = repository.todasLasExcavaciones.first()

            if (listaActual.isEmpty()) {
                val fosilesIniciales = listOf(
                    Excavacion(
                        nombre = "Gliptodonte",
                        latitud = -34.9205, // Plaza Moreno aprox
                        longitud = -57.9536,
                        icResName = "ic_gliptodonte"
                    ),
                    Excavacion(
                        nombre = "Neosclerocalyptus",
                        latitud = -34.9150, // Museo de La Plata aprox
                        longitud = -57.9480,
                        icResName = "ic_neosclerocalyptus"
                    ),
                    Excavacion(
                        nombre = "Panochthus",
                        latitud = -34.9250, // Zona Parque Saavedra
                        longitud = -57.9400,
                        icResName = "ic_panochthus"
                    ),
                    Excavacion(
                        nombre = "Doedicurus",
                        latitud = -34.9100, // Zona Plaza Italia
                        longitud = -57.9580,
                        icResName = "doedicurus_descubierto"
                    )
                )
                // Al insertar, se crea el archivo .db físico y el StateFlow avisa a la UI
                repository.insertVariasExcavaciones(fosilesIniciales)
            }
        }
    }
}