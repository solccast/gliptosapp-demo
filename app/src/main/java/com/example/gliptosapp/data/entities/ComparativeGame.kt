package com.example.gliptosapp.data.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "comparative_games")
data class ComparativeGame (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fosilId: Long, // Id del fosil
    val textoPregunta: String,
    val realizada: Boolean,
    val infoExtra: String, //Texto q se muestra al acertar
    @Ignore
    val opciones: List<OptionComparativeGame> = emptyList()
){
    constructor(
        id: Long,
        fosilId: Long,
        textoPregunta: String,
        realizada: Boolean,
        infoExtra: String
    ) : this(id, fosilId, textoPregunta, realizada, infoExtra, emptyList())
}