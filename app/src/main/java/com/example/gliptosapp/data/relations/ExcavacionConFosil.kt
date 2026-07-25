package com.example.gliptosapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gliptosapp.data.entities.EstadoExcavacion
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.data.entities.Fosil

data class ExcavacionConFosil(
    @Embedded val excavacion: Excavacion,
    @Relation(
        parentColumn = "fosilId", // FK en Excavacion
        entityColumn = "id"       // PK en Fosil
    )
    val fosil: Fosil
) {
    val id: Int get() = excavacion.id
    val fosilId: Long get() = excavacion.fosilId
    val latitud: Double get() = excavacion.latitud
    val longitud: Double get() = excavacion.longitud
    val estado: EstadoExcavacion get() = excavacion.estado
    val icResName: String get() = excavacion.icResName
    val nombre: String get() = fosil.nombre
}