package com.example.gliptosapp.ui.excavation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.data.entities.EstadoExcavacion
import com.example.gliptosapp.repository.ExcavacionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcavacionViewModel @Inject constructor(
    private val repository: ExcavacionRepository
) : ViewModel() {

    var estadoActual: Int = 1
        private set

    var fosilId: Int = 1
        private set

    var nombreFosilBase: String = "gliptodonte"
        private set

    fun inicializarFosil(id: Int) {
        fosilId = id
        nombreFosilBase = when (id) {
            1 -> "gliptodonte"
            2 -> "euphractus"
            3 -> "smilodon"
            else -> "gliptodonte"
        }
    }

    fun avanzarEstado() {
        if (estadoActual < 5) {
            estadoActual++
            if (estadoActual == 5) {
                marcarComoCompletado()
            }
        }
    }

    private fun marcarComoCompletado() {
        viewModelScope.launch {
            // Nombre correcto del método en el repositorio
            repository.actualizarEstadoExcavacion(fosilId, EstadoExcavacion.COMPLETADO)
        }
    }
}