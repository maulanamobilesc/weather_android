package com.maulana.weathermobile.domain

import android.content.Context
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.LocationLocal
import com.maulana.weathermobile.domain.model.WeatherLocal

sealed class UserIntent {
    data object FetchItems : UserIntent()
    data object Logout : UserIntent()
}

sealed class LoginUserIntent : UserIntent() {
    data object Login : LoginUserIntent()
    data object ShowForgotPassword : LoginUserIntent()
    data object ShowSignUp : LoginUserIntent()
}

sealed class LocationIntent : UserIntent() {
    data object LoadSavedWeather : LocationIntent()
    data object SearchLocation : LocationIntent()
    data class GetWeatherFromSelectedCity(val locationLocal: LocationLocal) : LocationIntent()
    data class InsertCurrentWeather(val weather: WeatherLocal) : LocationIntent()
    data class DeleteLocation(val locationId: Int) : LocationIntent()
}

sealed class WeatherIntent : UserIntent() {
    data class FetchWeatherAndForecast(val coordinate: Coord, val withInsert: Boolean = false) :
        WeatherIntent()

    data class FetchCurrentWeather(val coordinate: Coord, val withInsert: Boolean = false) :
        WeatherIntent()

    data class FetchForecast(val coordinate: Coord) : WeatherIntent()
    data class GetSavedWeather(val context: Context) : WeatherIntent()
    data class GetCurrentLocation(val context: Context) : WeatherIntent()
    data class InsertCurrentWeather(val weather: WeatherLocal) : WeatherIntent()
    data class SetActiveLocation(val locationIndex: Int) : WeatherIntent()
}