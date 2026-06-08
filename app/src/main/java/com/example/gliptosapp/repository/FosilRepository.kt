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
            descripcion = "El gliptodonte fue un mamífero acorazado que habitó Sudamérica durante el Pleistoceno. Emparentado con los armadillos actuales, poseía un caparazón rígido formado por placas óseas y una cola fuerte, a veces con maza. Era herbívoro y de gran tamaño, comparable a un automóvil pequeño. Se extinguió hace unos 10.000 años, probablemente por cambios climáticos y la acción humana."
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
            descripcion = "Euphactus es un género de armadillo extinto que vivió en Sudamérica durante el Pleistoceno. Formaba parte de los dasipódidos, parientes de los armadillos modernos, aunque de mayor tamaño. Poseía un caparazón flexible compuesto por bandas móviles que le permitían cierta agilidad. Era un animal terrestre, probablemente insectívoro u omnívoro, adaptado a ambientes abiertos y secos."
        )
    )

    fun getFosiles(): List<Fosil>{
        return listaFosiles
    }

    fun getFosilPorNombre(nombre: String): Fosil?{
        return listaFosiles.find {it.nombre == nombre}
    }
}