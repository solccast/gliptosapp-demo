package com.example.gliptosapp.di

import android.content.Context
import androidx.room.Room
import com.example.gliptosapp.data.dao.ComparativeGameDao
import com.example.gliptosapp.data.dao.ExcavacionDao
import com.example.gliptosapp.data.dao.FosilDao
import com.example.gliptosapp.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton // Asegura que solo exista una instancia en toda la app
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "gliptosapp_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideExcavacionDao(appDatabase: AppDatabase): ExcavacionDao {
        return appDatabase.excavacionDao()
    }

    @Provides
    fun provideComparativeGameDao(appDatabase: AppDatabase): ComparativeGameDao {
        return appDatabase.comparativeGameDao()
    }

    @Provides
    fun provideFosilDao(appDatabase: AppDatabase): FosilDao{
        return appDatabase.fosilDao()
    }
}