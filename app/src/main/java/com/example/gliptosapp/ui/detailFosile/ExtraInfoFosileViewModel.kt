package com.example.gliptosapp.ui.detailFosile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.data.entities.Fosil
import com.example.gliptosapp.repository.FosilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtraInfoFosileViewModel @Inject constructor(
    private val repository: FosilRepository
): ViewModel(){
    private val _fosil = MutableLiveData<Fosil?>()
    val fosil: LiveData<Fosil?> = _fosil

    fun cargarFosil(nombre: String) {
        viewModelScope.launch {
            _fosil.value = repository.getFosilPorNombre(nombre)
        }
    }
}