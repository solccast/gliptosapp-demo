package com.example.gliptosapp.repository

import com.example.gliptosapp.R
import com.example.gliptosapp.data.dao.FosilDao
import com.example.gliptosapp.data.entities.Fosil
import com.example.gliptosapp.data.relations.FosilConEstado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class FosilRepository @Inject constructor(private val fosilDao: FosilDao){

    private val listaFosiles = listOf(
        Fosil(
            nombre = "Gliptodonte",
            imgDescubierto = "gliptodonte",
            imgSinDescubrir = "gliptodonte_sin_descubrir",
            descripcion = "El gliptodonte fue un mamífero acorazado que habitó Sudamérica durante el Pleistoceno. Emparentado con los armadillos actuales, poseía un caparazón rígido formado por placas óseas y una cola fuerte, a veces con maza. Era herbívoro y de gran tamaño, comparable a un automóvil pequeño. Se extinguió hace unos 10.000 años, probablemente por cambios climáticos y la acción humana."
        ),
        Fosil(
            nombre = "Doedicurus",
            imgDescubierto = "doedicurus_descubierto",
            imgSinDescubrir = "doedicurus_sin_descubrir",
            descripcion = "Depredador del período Cretácico"
        ),
        Fosil(
            nombre = "Euphactus",
            imgDescubierto = "euphractus_descubierto",
            imgSinDescubrir = "euphractus_sin_descubrir",
            descripcion = "Euphactus es un género de armadillo extinto que vivió en Sudamérica durante el Pleistoceno. Formaba parte de los dasipódidos, parientes de los armadillos modernos, aunque de mayor tamaño. Poseía un caparazón flexible compuesto por bandas móviles que le permitían cierta agilidad. Era un animal terrestre, probablemente insectívoro u omnívoro, adaptado a ambientes abiertos y secos."
        )
    )

    fun getFosiles(): Flow<List<FosilConEstado>> {
        return fosilDao.obtenerFosilesConEstado()
    }

    suspend fun sembrarSiEsNecesario() {
        if (fosilDao.contarFosiles() == 0) {
            fosilDao.insertFosiles(listaFosiles)
        }
    }

    suspend fun getFosilPorNombre(nombre: String): Fosil?{
        return fosilDao.getFosilPorNombre(nombre)
    }

    suspend fun getFosilConEstadoPorNombre(nombre: String): FosilConEstado?{
        return fosilDao.getFosilConEstadoPorNombre(nombre)
    }

    suspend fun getFosilConEstadoPorId(fosilid: Long): FosilConEstado?{
        return fosilDao.getFosilConEstadoPorId(fosilid)
    }
}