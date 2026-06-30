package com.example.gliptosapp.repository

import com.example.gliptosapp.R
import com.example.gliptosapp.data.ComparativeGame
import com.example.gliptosapp.data.OptionComparativeGame
import javax.inject.Inject


class ComparativeGameRepository @Inject constructor() {
    private val listaPreguntas = listOf(
        ComparativeGame(
            textoPregunta = "¿Cual es más grande?",
            realizada = false,
            opciones = listOf(
                OptionComparativeGame(
                    "Un camion",
                    false,
                    R.drawable.truck
                ),
                OptionComparativeGame(
                    "Un gliptodonte",
                    true,
                    R.drawable.gliptodonte
                )
            ),
            infoExtra = "El gliptodonte podía medir hasta 3 metros de largo y pesar hasta 2,000 kg. ¡Era enorme!"
        )
    )

    fun getComparativeGameFosile(nombre: String): ComparativeGame?{
        return listaPreguntas.firstOrNull();
    }
}