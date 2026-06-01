package com.example.gliptosapp.ui.colection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gliptosapp.R
import com.example.gliptosapp.data.Fosil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ColectionViewModel @Inject constructor() : ViewModel(){
    private val _fosiles = MutableLiveData<List<Fosil>>()
    val fosiles: LiveData<List<Fosil>> = _fosiles

    init {
        cargarMock()
    }

    private fun cargarMock() {
        _fosiles.value = listOf(
            Fosil("Gliptodonte", true, R.drawable.gliptodonte, null),
            Fosil("Tiranosaurio", false, null, null),
            Fosil("Trilobite", true, null, null)
        )
    }
}