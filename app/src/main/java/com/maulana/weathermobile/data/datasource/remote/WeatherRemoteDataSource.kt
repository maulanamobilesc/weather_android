package com.maulana.weathermobile.data.datasource.remote

import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {

    fun getCurrentWeather(
        apiKey: String,
        latitude: Double,
        longitude: Double
    ): Flow<UIState<CurrentWeather>>

    fun getForecast(
        apiKey: String,
        latitude: Double,
        longitude: Double
    ): Flow<UIState<List<Forecast>>>

}