package com.maulana.weathermobile.domain.repository

import android.content.Context
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.LocationLocal
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getCurrentLocation(context: Context): Flow<UIState<Coord>>

    fun getAllSavedLocation(): Flow<List<LocationLocal>>

    suspend fun insertLocation(location: LocationLocal)

    fun searchLocation(query: String, apiKey: String): Flow<UIState<List<LocationLocal>>>

}