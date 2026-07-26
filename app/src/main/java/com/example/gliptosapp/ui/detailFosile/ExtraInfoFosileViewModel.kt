package com.example.gliptosapp.ui.detailFosile

import androidx.lifecycle.LiveData
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
): ViewModel(){
    private val _fosil = MutableLiveData<FosilConEstado?>()
    val fosil: LiveData<FosilConEstado?> = _fosil

    private val _game = MutableLiveData<ComparativeGame?>()
    val game: LiveData<ComparativeGame?> = _game
    fun cargarFosil(fosilId: Long) {
        viewModelScope.launch {
            _fosil.value = repository.getFosilConEstadoPorId(fosilId)
            _game.value = gameRepository.getInfoComparativeGame(fosilId)
        }
    }
}