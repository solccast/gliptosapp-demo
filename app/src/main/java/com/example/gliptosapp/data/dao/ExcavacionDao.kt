package com.example.gliptosapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gliptosapp.data.entities.EstadoExcavacion
import com.example.gliptosapp.data.entities.Excavacion
import kotlinx.coroutines.flow.Flow

@Dao
interface ExcavacionDao {

    // Observa todas las excavaciones en tiempo real para pintar el mapa
    @Query("SELECT * FROM excavaciones")
    fun observeTodasLasExcavaciones(): Flow<List<Excavacion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExcavacion(excavacion: Excavacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(excavaciones: List<Excavacion>)

    // Actualiza el estado cuando el niño lo descubre o termina de excavar
    @Query("UPDATE excavaciones SET estado = :nuevoEstado WHERE id = :excavacionId")
    suspend fun actualizarEstado(excavacionId: Int, nuevoEstado: EstadoExcavacion)
}