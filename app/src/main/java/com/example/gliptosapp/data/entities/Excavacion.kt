package com.example.gliptosapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "excavaciones")
data class Excavacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fosilId: Long,
    val latitud: Double,
    val longitud: Double,
    val estado: EstadoExcavacion = EstadoExcavacion.OCULTO,
    val icResName: String // Ejemplo: "ic_gliptodonte"
)