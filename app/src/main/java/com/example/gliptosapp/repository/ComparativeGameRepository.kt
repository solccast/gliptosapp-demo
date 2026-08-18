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
            fosilId = 1, // Gliptodonte
            textoPregunta = "¿Cuál es más grande?",
            realizada = false,
            infoExtra = "El gliptodonte podía medir hasta 3 metros de largo y pesar hasta 2,000 kg. ¡Era casi del tamaño de un auto pequeño, pero mucho más pesado que uno!",
            opciones = listOf(
                OptionComparativeGame(texto = "Un auto pequeño", esCorrecta = false, imgOption = R.drawable.auto_pequeno),
                OptionComparativeGame(texto = "Un gliptodonte", esCorrecta = true, imgOption = R.drawable.gliptodonte)
            )
        ),
        ComparativeGame(
            fosilId = 2, // Euphractus
            textoPregunta = "¿Cuál es más chico?",
            realizada = false,
            infoExtra = "Euphractus era pequeño: apenas del tamaño de un perro mediano, adaptado a ambientes abiertos y secos. ¡Nada que ver con los gigantes de su familia!",
            opciones = listOf(
                OptionComparativeGame(texto = "Un perro grande", esCorrecta = false, imgOption = R.drawable.perro_grande),
                OptionComparativeGame(texto = "Un Euphractus", esCorrecta = true, imgOption = R.drawable.euphractus_descubierto)
            )
        ),
        ComparativeGame(
            fosilId = 3, // Smilodon
            textoPregunta = "¿Quién tiene los colmillos más largos?",
            realizada = false,
            infoExtra = "El Smilodon tenía enormes colmillos curvos de hasta 28 cm. ¡Eran mucho más largos que los de cualquier felino grande que exista hoy!",
            opciones = listOf(
                OptionComparativeGame(texto = "Un león", esCorrecta = false, imgOption = R.drawable.leon),
                OptionComparativeGame(texto = "Un Smilodon", esCorrecta = true, imgOption = R.drawable.smilodon_descubierto)
            )
        )
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

    suspend fun marcarComoRealizado(fosilId: Long) = comparativeGameDao.marcarComoRealizado(fosilId)
    suspend fun getInfoComparativeGame(fosilId: Long): ComparativeGame?{
        return comparativeGameDao.getInfoGame(fosilId)
    }
}