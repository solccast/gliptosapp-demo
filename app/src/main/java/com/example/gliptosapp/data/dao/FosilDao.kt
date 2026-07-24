package com.example.gliptosapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gliptosapp.data.entities.Fosil
import com.example.gliptosapp.data.relations.FosilConEstado
import kotlinx.coroutines.flow.Flow

@Dao
interface FosilDao{

    @Insert
    suspend fun insertFosil(fosil: Fosil): Long

    @Insert
    suspend fun insertFosiles(fosiles: List<Fosil>)

    @Query("SELECT * FROM fosiles")
    fun getAllFosiles(): Flow<List<Fosil>>

    @Query("SELECT * FROM fosiles WHERE nombre = :nombre LIMIT 1")
    suspend fun getFosilPorNombre(nombre: String): Fosil?

    @Query("SELECT COUNT(*) FROM fosiles")
    suspend fun contarFosiles(): Int

    @Query("SELECT * FROM fosiles WHERE id = :id LIMIT 1")
    suspend fun getFosilPorId(id: Long): Fosil?

    @Query("SELECT * FROM fosiles")
    fun obtenerFosilesConEstado(): Flow<List<FosilConEstado>> //


    @Query("SELECT * FROM fosiles WHERE nombre = :nombre LIMIT 1")
    suspend fun getFosilConEstadoPorNombre(nombre: String): FosilConEstado?
}