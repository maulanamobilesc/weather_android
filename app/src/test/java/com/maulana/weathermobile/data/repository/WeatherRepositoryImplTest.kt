package com.maulana.weathermobile.data.repository

import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.data.datasource.local.WeatherLocalDataSource
import com.maulana.weathermobile.data.datasource.remote.WeatherRemoteDataSource
import com.maulana.weathermobile.domain.model.Clouds
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.CurrentWeather
import com.maulana.weathermobile.domain.model.Forecast
import com.maulana.weathermobile.domain.model.Main
import com.maulana.weathermobile.domain.model.Rain
import com.maulana.weathermobile.domain.model.Sys
import com.maulana.weathermobile.domain.model.SysPod
import com.maulana.weathermobile.domain.model.Weather
import com.maulana.weathermobile.domain.model.WeatherLocal
import com.maulana.weathermobile.domain.model.Wind
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class WeatherRepositoryImplTest {

    private lateinit var remoteDataSource: WeatherRemoteDataSource
    private lateinit var localDataSource: WeatherLocalDataSource
    private lateinit var weatherRepository: WeatherRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteDataSource = mock()
        localDataSource = mock()
        weatherRepository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getCurrentWeather should call remoteDataSource and return current weather`() = runTest {
        val apiKey = "testApiKey"
        val latitude = 37.7749
        val longitude = -122.4194

        val currentWeather = CurrentWeather(
            base = "stations",
            clouds = Clouds(all = 20),
            cod = 200,
            coord = Coord(lon = longitude, lat = latitude),
            dt = 1633036800,
            id = 12345,
            main = Main(
                temp = 25.0,
                feelsLike = 24.5,
                tempMin = 20.0,
                tempMax = 30.0,
                pressure = 1012,
                humidity = 60,
                grndLevel = 1,
                seaLevel = 1
            ),
            name = "San Francisco",
            rain = Rain(0.0),
            sys = Sys(
                type = 1,
                id = 1234,
                country = "US",
                sunrise = 1632993600,
                sunset = 1633036800
            ),
            timezone = -25200,
            visibility = 10000,
            weather = listOf(
                Weather(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            ),
            wind = Wind(speed = 5.0, deg = 180, gust = 0.0)
        )

        val expectedFlow = flowOf(UIState.Success(currentWeather))

        `when`(remoteDataSource.getCurrentWeather(apiKey, latitude, longitude)).thenReturn(
            expectedFlow
        )

        val result = weatherRepository.getCurrentWeather(apiKey, latitude, longitude)

        assertEquals(expectedFlow, result)
        verify(remoteDataSource).getCurrentWeather(apiKey, latitude, longitude)
    }

    @Test
    fun `getForecast should call remoteDataSource and return forecast data`() = runTest {
        val apiKey = "testApiKey"
        val latitude = 37.7749
        val longitude = -122.4194

        val forecastList = listOf(
            Forecast(
                clouds = Clouds(all = 30),
                dt = 1633036800,
                dtTxt = "2024-10-10 12:00:00",
                main = Main(
                    temp = 28.0,
                    feelsLike = 27.5,
                    tempMin = 26.0,
                    tempMax = 30.0,
                    pressure = 1010,
                    humidity = 70,
                    grndLevel = 1,
                    seaLevel = 1
                ),
                pop = 0.2,
                rain = Rain(0.0),
                sys = SysPod(pod = "d"),
                visibility = 10000,
                weather = listOf(
                    Weather(
                        id = 801,
                        main = "Clouds",
                        description = "few clouds",
                        icon = "02d"
                    )
                ),
                wind = Wind(speed = 4.5, deg = 190, gust = 0.0)
            )
        )

        val expectedFlow = flowOf(UIState.Success(forecastList))

        `when`(remoteDataSource.getForecast(apiKey, latitude, longitude)).thenReturn(expectedFlow)

        val result = weatherRepository.getForecast(apiKey, latitude, longitude)

        assertEquals(expectedFlow, result)
        verify(remoteDataSource).getForecast(apiKey, latitude, longitude)
    }

    @Test
    fun `getAllSavedWeather should call localDataSource and return saved weather`() = runTest {
        val savedWeatherList = listOf(
            WeatherLocal(
                locationId = 1,
                locationName = "Jakarta",
                currentWeather = "Clear",
                weatherCode = "01d",
                temperature = 30.0,
                maxTemperature = 32.0,
                minTemperature = 28.0,
                date = "2024-10-10",
                isDefault = true,
                latitude = -6.2088,
                longitude = 106.8456
            )
        )

        val expectedFlow = flowOf(savedWeatherList)

        `when`(localDataSource.getAllSavedWeather()).thenReturn(expectedFlow)

        val result = weatherRepository.getAllSavedWeather()

        assertEquals(expectedFlow, result)
        verify(localDataSource).getAllSavedWeather()
    }

    @Test
    fun `insertWeather should call localDataSource to insert weather`() = runTest {
        val weather = WeatherLocal(
            locationId = 2,
            locationName = "Bandung",
            currentWeather = "Rainy",
            weatherCode = "09d",
            temperature = 24.0,
            maxTemperature = 26.0,
            minTemperature = 22.0,
            date = "2024-10-11",
            isDefault = false,
            latitude = -6.9175,
            longitude = 107.6191
        )

        weatherRepository.insertWeather(weather)

        verify(localDataSource).insertWeather(weather)
    }

    @Test
    fun `getDataCount should return the data count from localDataSource`() {
        val count = 5
        `when`(localDataSource.getDataCount()).thenReturn(count)

        val result = weatherRepository.getDataCount()

        assertEquals(count, result)
        verify(localDataSource).getDataCount()
    }

    @Test
    fun `deleteSavedWeather should call localDataSource to delete saved weather by locationId`() =
        runTest {
            val locationId = 1

            weatherRepository.deleteSavedWeather(locationId)

            verify(localDataSource).deleteSavedWeather(locationId)
        }
}
