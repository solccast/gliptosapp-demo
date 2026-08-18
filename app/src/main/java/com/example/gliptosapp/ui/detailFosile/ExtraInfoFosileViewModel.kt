package com.example.gliptosapp.ui.detailFosile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.data.entities.ComparativeGame
import com.example.gliptosapp.data.relations.FosilConEstado
import com.example.gliptosapp.repository.ComparativeGameRepository
import com.example.gliptosapp.repository.FosilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtraInfoFosileViewModel @Inject constructor(
    private val repository: FosilRepository,
    private val gameRepository: ComparativeGameRepository
) : ViewModel() {

    private val _fosil = MutableLiveData<FosilConEstado?>()
    private val _game = MutableLiveData<ComparativeGame?>()

    private val _uiState = MediatorLiveData<ExtraInfoUiState>()
    val uiState: LiveData<ExtraInfoUiState> = _uiState

    init {
        _uiState.addSource(_fosil) { actualizarUiState() }
        _uiState.addSource(_game) { actualizarUiState() }
    }

    private fun actualizarUiState() {
        val fosil = _fosil.value ?: return
        val game = _game.value

        _uiState.value = ExtraInfoUiState(
            fosil = fosil,
            desbloqueada = game?.realizada == true,
            infoExtra = game?.infoExtra
        )
    }

    fun cargarFosil(fosilId: Long) {
        viewModelScope.launch {
            _fosil.value = repository.getFosilConEstadoPorId(fosilId)
            _game.value = gameRepository.getInfoComparativeGame(fosilId)
        }
    }
}

data class ExtraInfoUiState(
    val fosil: FosilConEstado,
    val desbloqueada: Boolean,
    val infoExtra: String?
)