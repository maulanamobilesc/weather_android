package com.maulana.weathermobile.util

import com.maulana.weathermobile.R
import com.maulana.weathermobile.WeatherCode

fun getDrawableIcon(weatherCode: WeatherCode): Int {
    return when(weatherCode){
        WeatherCode.ClearSkyDay -> R.drawable.ic_fluent_weather_sunny_24_regular
        WeatherCode.Unknown -> TODO()
        WeatherCode.ClearSkyNight -> R.drawable.ic_fluent_weather_sunny_24_regular
        WeatherCode.FewCloudsDay -> TODO()
        WeatherCode.FewCloudsNight -> TODO()
        WeatherCode.ScatteredCloudsDay -> TODO()
        WeatherCode.ScatteredCloudsNight -> TODO()
        WeatherCode.BrokenCloudsDay -> TODO()
        WeatherCode.BrokenCloudsNight -> TODO()
        WeatherCode.ShowerRainDay -> TODO()
        WeatherCode.ShowerRainNight -> TODO()
        WeatherCode.RainDay -> TODO()
        WeatherCode.RainNight -> TODO()
        WeatherCode.ThunderstormDay -> TODO()
        WeatherCode.ThunderstormNight -> TODO()
        WeatherCode.SnowDay -> TODO()
        WeatherCode.SnowNight -> TODO()
        WeatherCode.MistDay -> TODO()
        WeatherCode.MistNight -> TODO()
    }
}