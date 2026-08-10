package com.example.gliptosapp.repository

import com.example.gliptosapp.data.dao.FosilDao
import com.example.gliptosapp.data.entities.Fosil
import com.example.gliptosapp.data.relations.FosilConEstado
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FosilRepository @Inject constructor(private val fosilDao: FosilDao){

    private val listaFosiles = listOf(
        Fosil(
            nombre = "Gliptodonte",
            imgDescubierto = "gliptodonte",
            imgSinDescubrir = "gliptodonte_sin_descubrir",
            descripcion = "El gliptodonte fue un mamífero acorazado que habitó Sudamérica durante el Pleistoceno. Emparentado con los armadillos actuales, poseía un caparazón rígido formado por placas óseas y una cola fuerte, a veces con maza. Era herbívoro y de gran tamaño, comparable a un automóvil pequeño. Se extinguió hace unos 10.000 años, probablemente por cambios climáticos y la acción humana.",
            epoca = "Pleistoceno",
            habitat = "Llanuras y pastizales abiertos de Sudamérica",
            tamano = "Hasta 3,3 metros de largo",
            peso = "Hasta 2.000 kg",
            dieta = "Herbívoro"
        ),
        Fosil(
            nombre = "Euphractus",
            imgDescubierto = "euphractus_descubierto",
            imgSinDescubrir = "euphractus_sin_descubrir",
            descripcion = "Euphractus es un género de armadillo que vivió (y cuyo linaje sobrevive hoy) en Sudamérica desde el Pleistoceno. Formaba parte de los dasipódidos, parientes de los armadillos modernos. Poseía un caparazón flexible compuesto por bandas móviles que le permitían cierta agilidad. Era un animal terrestre, omnívoro, adaptado a ambientes abiertos y secos.",
            epoca = "Pleistoceno",
            habitat = "Sabanas, pastizales y bosques secos de Sudamérica",
            tamano = "Entre 40 y 50 cm de largo",
            peso = "Entre 3 y 6,5 kg",
            dieta = "Omnívoro"
        ),
        Fosil(
            nombre = "Smilodon",
            imgDescubierto = "smilodon_descubierto",
            imgSinDescubrir = "smilodon_sin_descubrir",
            descripcion = "El Smilodon fue un gran felino que vivió en América hace miles de años. Tenía unos enormes colmillos curvos que usaba para cazar. Era muy fuerte y se alimentaba de grandes animales. Se extinguió hace unos 10.000 años.",
            epoca = "Pleistoceno",
            habitat = "Praderas y bosques abiertos de América",
            tamano = "Alrededor de 1,2 metros de altura al hombro",
            peso = "Entre 160 y 280 kg",
            dieta = "Carnívoro"
        ),
        /*
        //Este fosil no existe todavia
        Fosil(
            nombre = "Doedicurus",
            imgDescubierto = "doedicurus_descubierto",
            imgSinDescubrir = "doedicurus_sin_descubrir",
            descripcion = "Pariente gigante de los armadillos, con una cola en forma de maza usada para defensa.",
            epoca = "Pleistoceno",
            habitat = "Llanuras y pastizales de Sudamérica",
            tamano = "Hasta 3,6 metros de largo (incluyendo la cola)",
            peso = "Hasta 1.400 kg",
            dieta = "Herbívoro"
        ),
        */
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