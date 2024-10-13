package com.maulana.weathermobile.data.datasource.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maulana.weathermobile.data.datasource.local.dao.LocationDao
import com.maulana.weathermobile.data.datasource.local.dao.WeatherDao
import com.maulana.weathermobile.data.datasource.local.entity.LocationEntity
import com.maulana.weathermobile.data.datasource.local.entity.WeatherEntity

@Database(entities = [WeatherEntity::class, LocationEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun weatherDao(): WeatherDao

    companion object {
        private const val DATABASE_NAME = "weather.db"

        private lateinit var instance: AppDatabase

        @Synchronized
        fun getInstance(application: Application): AppDatabase {
            if (!Companion::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return instance
        }
    }
}