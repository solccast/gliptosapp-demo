package com.example.gliptosapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.gliptosapp.data.entities.ComparativeGame
import com.example.gliptosapp.data.entities.OptionComparativeGame
import com.example.gliptosapp.data.relations.ComparativeGameWithOptions
import kotlinx.coroutines.flow.Flow

@Dao
interface ComparativeGameDao {

    @Insert
    suspend fun insertGame(game: ComparativeGame): Long

    @Insert
    suspend fun insertOptions(options: List<OptionComparativeGame>)

    @Transaction
    suspend fun insertGameConOpciones(game: ComparativeGame): Long {
        val gameId = insertGame(game)
        insertOptions(game.opciones.map { it.copy(gameId = gameId) })
        return gameId
    }

    @Transaction
    @Query("SELECT * FROM comparative_games")
    fun getAllGamesWithOptions(): Flow<List<ComparativeGameWithOptions>>

    @Transaction
    @Query("SELECT * FROM comparative_games WHERE id = :gameId LIMIT 1")
    suspend fun getGameWithOptions(gameId: Long): ComparativeGameWithOptions?

    @Query("UPDATE comparative_games SET realizada = 1 WHERE fosilId = :fosilId")
    suspend fun marcarComoRealizado(fosilId: Long)

    @Update
    suspend fun updateGame(game: ComparativeGame)

    @Query ("SELECT COUNT(*) FROM comparative_games ")
    suspend fun contarJuegos(): Int

    @Query("SELECT * FROM comparative_games WHERE fosilId = :fosilId LIMIT 1")
    suspend fun getGameByFosilId(fosilId: Long): ComparativeGameWithOptions?

    @Query("SELECT * FROM comparative_games WHERE fosilId = :fosilId LIMIT 1")
    suspend fun getInfoGame(fosilId: Long): ComparativeGame?
}