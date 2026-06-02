package com.example.gliptosapp.ui.detailFosile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gliptosapp.R
import com.example.gliptosapp.data.Fosil
import com.example.gliptosapp.repository.FosilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExtraInfoFosileViewModel @Inject constructor(): ViewModel(){
    private val _fosil = MutableLiveData<Fosil>()
    val fosil: LiveData<Fosil> = _fosil
    private val repository = FosilRepository()

    fun cargarFosil(nombre: String){
        _fosil.value = repository.getFosilPorNombre(nombre)
    }
}