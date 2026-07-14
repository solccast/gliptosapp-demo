package com.example.gliptosapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gliptosapp.data.dao.ExcavacionDao
import com.example.gliptosapp.data.entities.Converters
import com.example.gliptosapp.data.entities.Excavacion

@Database(
    entities = [Excavacion::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun excavacionDao(): ExcavacionDao
}