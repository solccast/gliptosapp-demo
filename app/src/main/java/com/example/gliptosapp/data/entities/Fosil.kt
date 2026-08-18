package com.example.gliptosapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fosiles")
data class Fosil(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val imgDescubierto: String, // Éste es el que se muestra en la colección de fósiles
    val imgSinDescubrir: String, // éste es el que se muestra como una sombra sin descubrir
    val descripcion: String?,
    val epoca: String,
    val habitat: String,
    val tamano: String,
    val peso: String,
    val dieta: String
)