package com.maulana.weathermobile.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maulana.weathermobile.data.datasource.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather order by location_id asc")
    fun getAllSavedWeather(): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT COUNT(*) FROM weather")
    fun getDataCount(): Int

    @Query("DELETE FROM weather WHERE location_id = :locationId")
    suspend fun deleteSavedWeather(locationId: Int)

}