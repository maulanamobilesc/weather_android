package com.maulana.weathermobile.data.datasource.remote

import android.content.Context
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.domain.model.Coord
import kotlinx.coroutines.flow.Flow

interface LocationRemoteDataSource {

    fun getCurrentLocation(context: Context): Flow<UIState<Coord>>

}