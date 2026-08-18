package com.example.gliptosapp.data.entities

import androidx.room.TypeConverter

enum class EstadoExcavacion {
    OCULTO,
    COMPLETADO
}

class Converters {
    @TypeConverter
    fun fromEstadoExcavacion(value: EstadoExcavacion): String {
        return value.name
    }

    @TypeConverter
    fun toEstadoExcavacion(value: String): EstadoExcavacion {
        return enumValueOf<EstadoExcavacion>(value)
    }
}