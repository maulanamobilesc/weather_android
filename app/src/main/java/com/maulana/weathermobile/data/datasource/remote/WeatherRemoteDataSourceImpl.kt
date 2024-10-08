package com.maulana.weathermobile.data.datasource.remote

import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.data.datasource.remote.service.WeatherService
import com.maulana.weathermobile.data.mapper.toCurrentWeather
import com.maulana.weathermobile.data.mapper.toForecast
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImpl(private val service: WeatherService) : WeatherRemoteDataSource {

    override fun getCurrentWeather(
        apiKey: String,
        latitude: Double,
        longitude: Double
    ): Flow<UIState<CurrentWeather>> {
        return flow {
            try {
                emit(UIState.Loading)

                emit(
                    UIState.Success(
                        service.getCurrentWeather(apiKey, latitude, longitude).toCurrentWeather()
                    )
                )
            } catch (e: Exception) {
                emit(UIState.Error(e.localizedMessage.orEmpty()))
            }
        }
    }

    override fun getForecast(
        apiKey: String,
        latitude: Double,
        longitude: Double
    ): Flow<UIState<List<Forecast>>> {
        return flow {
            try {
                emit(UIState.Loading)

                emit(
                    UIState.Success(
                        service.getForecast(apiKey, latitude, longitude).list?.mapNotNull { it?.toForecast() }.orEmpty()
                    )
                )
            } catch (e: Exception) {
                emit(UIState.Error(e.localizedMessage.orEmpty()))
            }
        }
    }
}