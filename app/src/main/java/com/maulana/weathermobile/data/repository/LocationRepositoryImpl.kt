package com.maulana.weathermobile.data.repository

import android.content.Context
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.data.datasource.remote.LocationRemoteDataSource
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(private val locationRemoteDataSource: LocationRemoteDataSource) :
    LocationRepository {

    override fun getCurrentLocation(context: Context): Flow<UIState<Coord>> {
        return locationRemoteDataSource.getCurrentLocation(context)
    }
}