package com.maulana.weathermobile.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maulana.weathermobile.data.datasource.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM location order by id asc")
    fun getAllSavedLocation(): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

}