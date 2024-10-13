package com.maulana.weathermobile.data.datasource.remote

import android.content.Context
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.LocationLocal
import kotlinx.coroutines.flow.Flow

interface LocationRemoteDataSource {

    fun getCurrentLocation(context: Context): Flow<UIState<Coord>>

    fun searchLocation(query: String, apiKey: String): Flow<UIState<List<LocationLocal>>>

}