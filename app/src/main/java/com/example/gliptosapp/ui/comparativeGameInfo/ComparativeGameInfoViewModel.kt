package com.example.gliptosapp.ui.comparativeGameInfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.data.entities.ComparativeGame
import com.example.gliptosapp.repository.ComparativeGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComparativeGameInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ComparativeGameRepository
) : ViewModel() {

    private val nombreFosil: String = checkNotNull(savedStateHandle["nombreFosil"])

    private val _uiState = MutableStateFlow(ComparativeGameUiState(juego = null))

    val uiState: StateFlow<ComparativeGameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val juego = repository.getComparativeGameFosile("Gliptodonte")
            _uiState.value = _uiState.value.copy(juego = juego)
        }
    }

    fun seleccionarOpcion(index: Int) {
        val estado = _uiState.value
        Log.d("VM", "seleccionarOpcion llamado con index=$index")
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