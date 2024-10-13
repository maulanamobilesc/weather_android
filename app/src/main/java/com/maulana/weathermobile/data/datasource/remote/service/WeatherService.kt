package com.maulana.weathermobile.data.datasource.remote.service

import com.maulana.weathermobile.data.datasource.remote.response.currentweather.CurrentWeatherResponse
import com.maulana.weathermobile.data.datasource.remote.response.forecastweather.ForecastWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/weather?units=metric")
    suspend fun getCurrentWeather(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): CurrentWeatherResponse

    @GET("data/2.5/forecast?units=metric")
    suspend fun getForecast(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): ForecastWeatherResponse
}