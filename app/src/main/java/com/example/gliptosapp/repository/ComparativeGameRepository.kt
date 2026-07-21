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
            fosilId =  "Gliptodonte", // TODO: cambiarlo por el id de la entidad fósil
            textoPregunta = "¿Cual es más grande?",
            realizada = false,
            infoExtra = "El gliptodonte podía medir hasta 3 metros de largo y pesar hasta 2,000 kg. ¡Era enorme!",
            opciones = listOf(
                OptionComparativeGame(texto = "Un camion", esCorrecta = false, imgOption = R.drawable.truck),
                OptionComparativeGame(texto = "Un gliptodonte", esCorrecta = true, imgOption = R.drawable.gliptodonte)
            )
        ),
        ComparativeGame(
            fosilId = "Euphactus", // 👈 debe coincidir con Fosil.nombre para que el DAO lo encuentre
            textoPregunta = "¿Cual es más chico?",
            realizada = false,
            infoExtra = "Euphractus era mucho más pequeño que un armadillo gigante actual: apenas alcanzaba el tamaño de un armadillo común, adaptado a ambientes abiertos y secos.",
            opciones = listOf(
                OptionComparativeGame(texto = "Un armadillo gigante", esCorrecta = false, imgOption = R.drawable.truck),
                OptionComparativeGame(texto = "Un Euphractus", esCorrecta = true, imgOption = R.drawable.euphractus_descubierto)
            ))
    )

    suspend fun getComparativeGameFosile(nombre: String): ComparativeGame?{
        sembrarSiEsNecesario()
        val resultado = comparativeGameDao.getGameByFosilName(nombre) ?: return null
        return resultado.game.copy(opciones = resultado.opciones)
    }


    private suspend fun sembrarSiEsNecesario() {
        if (comparativeGameDao.contarJuegos() == 0) {
            listaPreguntas.forEach { comparativeGameDao.insertGameConOpciones(it) }
        }
    }

}