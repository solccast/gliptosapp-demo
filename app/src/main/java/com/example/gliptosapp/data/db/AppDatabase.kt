package com.example.gliptosapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gliptosapp.data.dao.ComparativeGameDao
import com.example.gliptosapp.data.dao.ExcavacionDao
import com.example.gliptosapp.data.dao.FosilDao
import com.example.gliptosapp.data.entities.ComparativeGame
import com.example.gliptosapp.data.entities.Converters
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.data.entities.OptionComparativeGame
import com.example.gliptosapp.data.entities.Fosil

@Database(
    entities = [Excavacion::class, ComparativeGame::class, OptionComparativeGame::class, Fosil::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun excavacionDao(): ExcavacionDao
    abstract fun comparativeGameDao(): ComparativeGameDao

    abstract fun fosilDao(): FosilDao
}