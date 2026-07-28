package com.example.gliptosapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gliptosapp.data.entities.EstadoExcavacion
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.data.relations.ExcavacionConFosil
import kotlinx.coroutines.flow.Flow

@Dao
interface ExcavacionDao {

    @Query("SELECT * FROM excavaciones")
    fun observeTodasLasExcavaciones(): Flow<List<Excavacion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExcavacion(excavacion: Excavacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(excavaciones: List<Excavacion>)

    @Query("UPDATE excavaciones SET estado = :nuevoEstado WHERE id = :excavacionId")
    suspend fun actualizarEstado(excavacionId: Int, nuevoEstado: EstadoExcavacion)

    @Query("SELECT * FROM excavaciones WHERE id = :excavacionId LIMIT 1")
    suspend fun obtenerPorId(excavacionId: Int): Excavacion?

    @Query("SELECT * FROM excavaciones")
    fun obtenerExcavacionesConFosil(): Flow<List<ExcavacionConFosil>>
}