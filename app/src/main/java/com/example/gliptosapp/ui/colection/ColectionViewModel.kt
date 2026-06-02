package com.example.gliptosapp.ui.colection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gliptosapp.R
import com.example.gliptosapp.data.Fosil
import com.example.gliptosapp.repository.FosilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ColectionViewModel @Inject constructor() : ViewModel(){
    private val _fosiles = MutableLiveData<List<Fosil>>()
    val fosiles: LiveData<List<Fosil>> = _fosiles

    private val repository = FosilRepository()

    init {
        cargarMock()
    }

    private fun cargarMock() {
        _fosiles.value = repository.getFosiles()
    }
}