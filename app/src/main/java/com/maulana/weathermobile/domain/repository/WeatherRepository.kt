package com.maulana.weathermobile.domain.repository

import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import com.maulana.weathermobile.domain.model.WeatherLocal
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

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

    fun getAllSavedWeather(): Flow<List<WeatherLocal>>

    suspend fun insertWeather(weather: WeatherLocal)

    fun getDataCount(): Int

    suspend fun deleteSavedWeather(locationId: Int)

}