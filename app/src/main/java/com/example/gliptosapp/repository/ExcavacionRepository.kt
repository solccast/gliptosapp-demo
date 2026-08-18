package com.example.gliptosapp.repository

import com.example.gliptosapp.data.dao.ExcavacionDao
import com.example.gliptosapp.data.entities.EstadoExcavacion
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.data.relations.ExcavacionConFosil
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExcavacionRepository @Inject constructor(private val excavacionDao: ExcavacionDao) {
    // Exponemos la lista como un Flow. El ViewModel la "recolectará" (collect)
    // y la UI se actualizará automáticamente si hay algún cambio en la base de datos.
    val todasLasExcavaciones: Flow<List<ExcavacionConFosil>> = excavacionDao.obtenerExcavacionesConFosil()

    // Usamos 'suspend' porque escribir en la base de datos es una operación
    // que debe correr en un hilo secundario (Corrutina) para no congelar la pantalla.
    suspend fun insertExcavacion(excavacion: Excavacion) {
        excavacionDao.insertExcavacion(excavacion)
    }

    suspend fun insertVariasExcavaciones(excavaciones: List<Excavacion>) {
        excavacionDao.insertAll(excavaciones)
    }

    suspend fun actualizarEstadoExcavacion(excavacionId: Int, nuevoEstado: EstadoExcavacion) {
        excavacionDao.actualizarEstado(excavacionId, nuevoEstado)
    }

    suspend fun obtenerEstadoExcavacion(fosilId: Int): EstadoExcavacion? {
        return excavacionDao.obtenerPorId(fosilId)?.estado // ajustar nombre del DAO según tengas
    }
}