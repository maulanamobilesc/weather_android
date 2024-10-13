package com.maulana.weathermobile.data.repository

import android.content.Context
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.data.datasource.local.LocationLocalDataSource
import com.maulana.weathermobile.data.datasource.remote.LocationRemoteDataSource
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.LocationLocal
import com.maulana.weathermobile.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val remoteDataSource: LocationRemoteDataSource,
    private val localDataSource: LocationLocalDataSource
) :
    LocationRepository {

    override fun getCurrentLocation(context: Context): Flow<UIState<Coord>> {
        return remoteDataSource.getCurrentLocation(context)
    }

    override fun getAllSavedLocation(): Flow<List<LocationLocal>> {
        return localDataSource.getAllSavedLocation()
    }

    override suspend fun insertLocation(location: LocationLocal) {
        localDataSource.insertLocation(location)
    }

    override fun searchLocation(query: String, apiKey: String): Flow<UIState<List<LocationLocal>>> {
        return remoteDataSource.searchLocation(query, apiKey)
    }
}