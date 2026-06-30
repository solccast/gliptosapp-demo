package com.example.gliptosapp.ui.comparativeGameInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gliptosapp.data.ComparativeGame
import com.example.gliptosapp.repository.ComparativeGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ComparativeGameInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ComparativeGameRepository
) : ViewModel() {

    private val nombreFosil: String = checkNotNull(savedStateHandle["nombreFosil"])

    private val _uiState = MutableStateFlow(
        ComparativeGameUiState(juego = repository.getComparativeGameFosile(nombreFosil))
    )
    val uiState: StateFlow<ComparativeGameUiState> = _uiState.asStateFlow()

    fun seleccionarOpcion(index: Int) {
        val estado = _uiState.value
        val juego = estado.juego ?: return
        if (estado.completado) return // ya ganó, no permitir más interacción

        val opcion = juego.opciones[index]

        _uiState.value = estado.copy(
            indiceSeleccionado = index,
            esCorrecta = opcion.esCorrecta,
            completado = opcion.esCorrecta // solo se "completa" si acertó
        )

        if (opcion.esCorrecta) {
            // TODO: cuando el repository tenga persistencia,
            // acá llamamos a repository.marcarComoRealizado(nombreFosil)
        }
    }
}

data class ComparativeGameUiState(
    val juego: ComparativeGame?,
    val completado: Boolean = false,
    val indiceSeleccionado: Int? = null,
    val esCorrecta: Boolean? = null
)