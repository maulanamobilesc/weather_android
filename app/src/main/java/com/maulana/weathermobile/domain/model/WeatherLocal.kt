package com.maulana.weathermobile.domain.model

data class WeatherLocal(
    val locationId: Int,
    val locationName: String,
    val currentWeather: String,
    val weatherCode: String,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val date: String,
    val isDefault: Boolean,
    val latitude: Double,
    val longitude: Double,
)