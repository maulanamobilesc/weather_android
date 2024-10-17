package com.maulana.weathermobile.data.datasource.local

import com.maulana.weathermobile.data.mapper.toWeatherEntity
import com.maulana.weathermobile.data.mapper.toWeatherLocal
import com.maulana.weathermobile.domain.model.WeatherLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherLocalDataSourceImpl(private val appDatabase: AppDatabase) : WeatherLocalDataSource {

    override fun getAllSavedWeather(): Flow<List<WeatherLocal>> {
        return appDatabase.weatherDao().getAllSavedWeather()
            .map { it.map { entity -> entity.toWeatherLocal() } }
    }

    override suspend fun insertWeather(weather: WeatherLocal) {
        appDatabase.weatherDao().insertWeather(weather.toWeatherEntity())
    }

    override fun getDataCount(): Int {
        return appDatabase.weatherDao().getDataCount()
    }

    override suspend fun deleteSavedWeather(locationId: Int) {
        return appDatabase.weatherDao().deleteSavedWeather(locationId)
    }
}