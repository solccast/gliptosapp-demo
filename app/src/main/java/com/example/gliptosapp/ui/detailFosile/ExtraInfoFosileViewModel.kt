package com.example.gliptosapp.ui.detailFosile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gliptosapp.R
import com.example.gliptosapp.data.Fosil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExtraInfoFosileViewModel @Inject constructor(): ViewModel(){
    private val _fosil = MutableLiveData<Fosil>()
    val fosil: LiveData<Fosil> = _fosil

    fun cargarFosil(nombre: String){
        val listaMock = listOf(
            Fosil("Gliptodonte", true, R.drawable.gliptodonte, null),
            Fosil("Tiranosaurio", false, null, null),
            Fosil("Trilobite", true, null, null)
        )
        _fosil.value = listaMock.find { it.nombre == nombre }
    }
}