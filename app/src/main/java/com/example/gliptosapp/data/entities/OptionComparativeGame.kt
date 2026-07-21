package com.example.gliptosapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "option_comparative_games",
    foreignKeys = [
        ForeignKey(
            entity = ComparativeGame::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("gameId")]
)
data class OptionComparativeGame(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameId: Long = 0, //Clave a comparativeGame
    val texto: String,
    val esCorrecta: Boolean,
    val imgOption: Int
)