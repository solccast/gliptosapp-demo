package com.example.gliptosapp.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.data.relations.ExcavacionConFosil
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
    val excavaciones: StateFlow<List<ExcavacionConFosil>> = repository.todasLasExcavaciones
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

        }
    }
}