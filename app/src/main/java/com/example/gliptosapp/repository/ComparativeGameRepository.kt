package com.example.gliptosapp.repository

import com.example.gliptosapp.R
import com.example.gliptosapp.data.dao.ComparativeGameDao
import com.example.gliptosapp.data.entities.OptionComparativeGame
import com.example.gliptosapp.data.entities.ComparativeGame
import javax.inject.Inject


class ComparativeGameRepository @Inject constructor(
    private val comparativeGameDao: ComparativeGameDao
){
    private val listaPreguntas = listOf(
        ComparativeGame(
            fosilId =  1,
            textoPregunta = "¿Cual es más grande?",
            realizada = false,
            infoExtra = "El gliptodonte podía medir hasta 3 metros de largo y pesar hasta 2,000 kg. ¡Era enorme!",
            opciones = listOf(
                OptionComparativeGame(texto = "Un camion", esCorrecta = false, imgOption = R.drawable.truck),
                OptionComparativeGame(texto = "Un gliptodonte", esCorrecta = true, imgOption = R.drawable.gliptodonte)
            )
        ),
        ComparativeGame(
            fosilId = 3, // El euphactus
            textoPregunta = "¿Cual es más chico?",
            realizada = false,
            infoExtra = "Euphractus era mucho más pequeño que un armadillo gigante actual: apenas alcanzaba el tamaño de un armadillo común, adaptado a ambientes abiertos y secos.",
            opciones = listOf(
                OptionComparativeGame(texto = "Un armadillo gigante", esCorrecta = false, imgOption = R.drawable.truck),
                OptionComparativeGame(texto = "Un Euphractus", esCorrecta = true, imgOption = R.drawable.euphractus_descubierto)
            ))
    )

    suspend fun getComparativeGameFosile(fosilId: Long): ComparativeGame?{
        sembrarSiEsNecesario()
        val resultado = comparativeGameDao.getGameByFosilId(fosilId) ?: return null
        return resultado.game.copy(opciones = resultado.opciones)
    }

    private suspend fun sembrarSiEsNecesario() {
        if (comparativeGameDao.contarJuegos() == 0) {
            listaPreguntas.forEach { comparativeGameDao.insertGameConOpciones(it) }
        }
    }
}