package com.maulana.weathermobile.data.datasource.remote.response.forecastweather


import com.google.gson.annotations.SerializedName

import kotlinx.serialization.Serializable

@Serializable
data class ForecastWeatherResponse(
    @SerializedName("city")
    val city: CityResponse?,
    @SerializedName("cnt")
    val cnt: Int?,
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("list")
    val list: List<ForecastResponse?>?,
    @SerializedName("message")
    val message: Int?
)