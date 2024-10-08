package com.maulana.weathermobile.domain.repository

import android.content.Context
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.domain.model.Coord
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getCurrentLocation(context: Context): Flow<UIState<Coord>>

}