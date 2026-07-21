package com.example.gliptosapp.ui.colection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.data.entities.Fosil
import com.example.gliptosapp.repository.FosilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ColectionViewModel @Inject constructor(
    private val repository: FosilRepository
) : ViewModel(){
    private val _fosiles = MutableLiveData<List<Fosil>>()

    val fosiles: LiveData<List<Fosil>> = repository.getFosiles().asLiveData()

    init {
        viewModelScope.launch {
            repository.sembrarSiEsNecesario()
        }
    }

}