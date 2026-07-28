package com.example.gliptosapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gliptosapp.data.entities.EstadoExcavacion
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.data.entities.Fosil

data class FosilConEstado(
    @Embedded val fosil: Fosil,
    @Relation(
        parentColumn = "id",
        entityColumn = "fosilId"
    )
    val excavaciones: List<Excavacion> = emptyList()
) {
    val nombre: String get() = fosil.nombre
    val descripcion: String? get() = fosil.descripcion
    val descubierto: Boolean
        get() = excavaciones.any { it.estado == EstadoExcavacion.COMPLETADO }

    fun obtenerImagen(): String =
        if (descubierto) fosil.imgDescubierto else fosil.imgSinDescubrir
}