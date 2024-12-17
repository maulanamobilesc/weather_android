package com.maulana.weathermobile.data.datasource.local

import com.maulana.weathermobile.data.datasource.local.dao.WeatherDao
import com.maulana.weathermobile.data.datasource.local.entity.WeatherEntity
import com.maulana.weathermobile.domain.model.WeatherLocal
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class WeatherLocalDataSourceImplTest {

    private lateinit var weatherDao: WeatherDao
    private lateinit var appDatabase: AppDatabase
    private lateinit var dataSource: WeatherLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        weatherDao = mock(WeatherDao::class.java)
        appDatabase = mock(AppDatabase::class.java)
        `when`(appDatabase.weatherDao()).thenReturn(weatherDao)
        dataSource = WeatherLocalDataSourceImpl(appDatabase)
    }

    @Test
    fun `getAllSavedWeather returns list of WeatherLocal`() = runTest {
        // Mock data
        val weatherEntityList = listOf(
            WeatherEntity(
                locationId = 1,
                locationName = "City A",
                currentWeather = "Sunny",
                weatherCode = "01",
                temperature = 30.0,
                maxTemperature = 35.0,
                minTemperature = 25.0,
                date = "2024-06-17",
                isDefault = true,
                latitude = 1.23,
                longitude = 4.56
            ),
            WeatherEntity(
                locationId = 2,
                locationName = "City B",
                currentWeather = "Rainy",
                weatherCode = "02",
                temperature = 25.0,
                maxTemperature = 28.0,
                minTemperature = 20.0,
                date = "2024-06-18",
                isDefault = false,
                latitude = 7.89,
                longitude = 0.12
            )
        )

        val expectedWeatherLocalList = weatherEntityList.map {
            WeatherLocal(
                locationId = it.locationId,
                locationName = it.locationName,
                currentWeather = it.currentWeather,
                weatherCode = it.weatherCode,
                temperature = it.temperature,
                maxTemperature = it.maxTemperature,
                minTemperature = it.minTemperature,
                date = it.date,
                isDefault = it.isDefault,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }

        // Mock DAO response
        `when`(weatherDao.getAllSavedWeather()).thenReturn(flowOf(weatherEntityList))

        // Call method
        val result = dataSource.getAllSavedWeather()

        result.collect { weatherLocals ->
            assertEquals(expectedWeatherLocalList, weatherLocals)
        }

        verify(weatherDao, times(1)).getAllSavedWeather()
    }

    @Test
    fun `insertWeather calls insertWeather in DAO`() = runTest {
        // Mock data
        val weatherLocal = WeatherLocal(
            locationId = 1,
            locationName = "City C",
            currentWeather = "Cloudy",
            weatherCode = "03",
            temperature = 28.0,
            maxTemperature = 32.0,
            minTemperature = 24.0,
            date = "2024-06-19",
            isDefault = false,
            latitude = 3.45,
            longitude = 6.78
        )

        val weatherEntity = WeatherEntity(
            locationId = weatherLocal.locationId,
            locationName = weatherLocal.locationName,
            currentWeather = weatherLocal.currentWeather,
            weatherCode = weatherLocal.weatherCode,
            temperature = weatherLocal.temperature,
            maxTemperature = weatherLocal.maxTemperature,
            minTemperature = weatherLocal.minTemperature,
            date = weatherLocal.date,
            isDefault = weatherLocal.isDefault,
            latitude = weatherLocal.latitude,
            longitude = weatherLocal.longitude
        )

        // Mock DAO behavior
        `when`(weatherDao.insertWeather(weatherEntity)).then { }

        // Call method
        dataSource.insertWeather(weatherLocal)

        verify(weatherDao, times(1)).insertWeather(weatherEntity)
    }

    @Test
    fun `getDataCount returns correct count`() {
        // Mock count
        `when`(weatherDao.getDataCount()).thenReturn(3)

        // Call method
        val result = dataSource.getDataCount()

        assertEquals(3, result)
        verify(weatherDao, times(1)).getDataCount()
    }

    @Test
    fun `deleteSavedWeather calls deleteSavedWeather in DAO`() = runTest {
        // Mock location ID
        val locationId = 1

        // Mock DAO behavior
        `when`(weatherDao.deleteSavedWeather(locationId)).then { }

        // Call method
        dataSource.deleteSavedWeather(locationId)

        verify(weatherDao, times(1)).deleteSavedWeather(locationId)
    }
}
