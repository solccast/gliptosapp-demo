package com.example.gliptosapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gliptosapp.data.entities.ComparativeGame
import com.example.gliptosapp.data.entities.OptionComparativeGame

data class ComparativeGameWithOptions(
    @Embedded
    val game: ComparativeGame,

    @Relation(
        parentColumn = "id",
        entityColumn = "gameId"
    )
    val opciones: List<OptionComparativeGame>
)