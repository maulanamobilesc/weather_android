package com.maulana.weathermobile.data.repository

import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.data.datasource.remote.WeatherRemoteDataSource
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import com.maulana.weathermobile.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(private val remoteDataSource: WeatherRemoteDataSource) :
    WeatherRepository {

    override fun getCurrentWeather(
        apiKey: String,
        latitude: Double,
        longitude: Double
    ): Flow<UIState<CurrentWeather>> {
        return remoteDataSource.getCurrentWeather(apiKey, latitude, longitude)
    }

    override fun getForecast(
        apiKey: String,
        latitude: Double,
        longitude: Double
    ): Flow<UIState<List<Forecast>>> {
        return remoteDataSource.getForecast(apiKey, latitude, longitude)
    }

}