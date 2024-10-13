package com.maulana.weathermobile.data.datasource.local

import com.maulana.weathermobile.domain.model.WeatherLocal
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    fun getAllSavedWeather(): Flow<List<WeatherLocal>>

    suspend fun insertWeather(weather: WeatherLocal)

    fun getDataCount(): Int

}