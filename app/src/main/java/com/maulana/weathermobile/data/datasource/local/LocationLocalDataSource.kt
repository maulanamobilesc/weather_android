package com.maulana.weathermobile.data.datasource.local

import com.maulana.weathermobile.domain.model.LocationLocal
import kotlinx.coroutines.flow.Flow

interface LocationLocalDataSource {

    fun getAllSavedLocation(): Flow<List<LocationLocal>>

    suspend fun insertLocation(location: LocationLocal)

}