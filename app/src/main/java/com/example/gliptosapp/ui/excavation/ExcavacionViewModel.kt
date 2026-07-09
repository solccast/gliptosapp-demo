package com.example.gliptosapp.ui.excavation

import androidx.lifecycle.ViewModel

class ExcavacionViewModel : ViewModel() {
    var estadoActual: Int = 1
        private set

    fun avanzarEstado() {
        if (estadoActual < 5) {
            estadoActual++
        }
    }
}