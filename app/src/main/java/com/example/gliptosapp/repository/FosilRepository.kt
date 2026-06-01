package com.example.gliptosapp.repository

import com.example.gliptosapp.R
import com.example.gliptosapp.data.Fosil
import javax.inject.Inject


class FosilRepository @Inject constructor(){
    private val listaFosiles = listOf(
        Fosil(
            descubierto = true,
            nombre = "Gliptodonte",
            imgDescubierto = R.drawable.gliptodonte,
            imgSinDescubrir = R.drawable.gliptodonte_sin_descubrir,
            descripcion = "Mamífero acorazado del Pleistoceno"
        ),
        Fosil(
            descubierto = false,
            nombre = "Doedicurus",
            imgDescubierto = R.drawable.doedicurus_descubierto,
            imgSinDescubrir = R.drawable.doedicurus_sin_descubrir,
            descripcion = "Depredador del período Cretácico"
        ),
        Fosil(
            descubierto = true,
            nombre = "Euphactus",
            imgDescubierto = R.drawable.euphractus_descubierto,
            imgSinDescubrir = R.drawable.euphractus_sin_descubrir,
            descripcion = "Artrópodo marino del Paleozoico"
        )
    )

    fun getFosiles(): List<Fosil>{
        return listaFosiles
    }

    fun getFosilPorNombre(nombre: String): Fosil?{
        return listaFosiles.find {it.nombre == nombre}
    }
}