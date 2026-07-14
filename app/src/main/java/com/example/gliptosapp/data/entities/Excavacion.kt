package com.example.gliptosapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "excavaciones")
data class Excavacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val estado: EstadoExcavacion = EstadoExcavacion.OCULTO,
    val icResName: String // Ejemplo: "ic_gliptodonte"
)