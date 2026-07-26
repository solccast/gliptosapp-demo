package com.example.gliptosapp.ui.excavation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.data.entities.EstadoExcavacion
import com.example.gliptosapp.repository.ExcavacionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstadoJuego(
    val estadoActual: Int = 1,
    val yaCompletado: Boolean = false,
    val cargado: Boolean = false // evita pintar algo antes de saber el estado real guardado
)

@HiltViewModel
class ExcavacionViewModel @Inject constructor(
    private val repository: ExcavacionRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoJuego())
    val estado: StateFlow<EstadoJuego> = _estado.asStateFlow()

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

        viewModelScope.launch {
            // Ver nota más abajo: hay que agregar este método al repositorio
            val estadoGuardado = repository.obtenerEstadoExcavacion(fosilId)
            val completado = estadoGuardado == EstadoExcavacion.COMPLETADO
            _estado.value = EstadoJuego(
                estadoActual = if (completado) 5 else 1,
                yaCompletado = completado,
                cargado = true
            )
        }
    }

    fun avanzarEstado() {
        val actual = _estado.value
        if (actual.estadoActual < 5) {
            val nuevo = actual.estadoActual + 1
            _estado.value = actual.copy(estadoActual = nuevo, yaCompletado = nuevo == 5)
            if (nuevo == 5) marcarComoCompletado()
        }
    }

    fun reiniciarJuego() {
        // Solo reinicia la mecánica visual; NO toca el estado en BD,
        // porque el fósil ya está descubierto y no queremos que el mapa
        // vuelva a mostrarlo como "pendiente".
        _estado.value = _estado.value.copy(estadoActual = 1, yaCompletado = false)
    }

    private fun marcarComoCompletado() {
        viewModelScope.launch {
            repository.actualizarEstadoExcavacion(fosilId, EstadoExcavacion.COMPLETADO)
        }
    }
}