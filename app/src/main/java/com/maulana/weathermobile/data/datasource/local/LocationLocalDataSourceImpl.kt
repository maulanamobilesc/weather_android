package com.maulana.weathermobile.data.datasource.local

import com.maulana.weathermobile.data.mapper.toLocationEntity
import com.maulana.weathermobile.data.mapper.toLocationLocal
import com.maulana.weathermobile.domain.model.LocationLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationLocalDataSourceImpl(private val appDatabase: AppDatabase) : LocationLocalDataSource {

    override fun getAllSavedLocation(): Flow<List<LocationLocal>> {
        return appDatabase.locationDao().getAllSavedLocation()
            .map { it.map { entity -> entity.toLocationLocal() } }
    }

    override suspend fun insertLocation(location: LocationLocal) {
        appDatabase.locationDao().insertLocation(location.toLocationEntity())
    }
}