package com.maulana.weathermobile.data.datasource.remote.response.currentweather


import com.google.gson.annotations.SerializedName
import com.maulana.weathermobile.data.datasource.remote.response.CloudsResponse
import com.maulana.weathermobile.data.datasource.remote.response.CoordResponse
import com.maulana.weathermobile.data.datasource.remote.response.MainResponse
import com.maulana.weathermobile.data.datasource.remote.response.WeatherResponse
import com.maulana.weathermobile.data.datasource.remote.response.WindResponse
import com.maulana.weathermobile.data.datasource.remote.response.forecastweather.RainResponse
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerializedName("base")
    val base: String?,
    @SerializedName("clouds")
    val clouds: CloudsResponse?,
    @SerializedName("cod")
    val cod: Int?,
    @SerializedName("coord")
    val coord: CoordResponse?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: MainResponse?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("rain")
    val rain: Rain1HResponse?,
    @SerializedName("sys")
    val sys: SysResponse?,
    @SerializedName("timezone")
    val timezone: Int?,
    @SerializedName("visibility")
    val visibility: Int?,
    @SerializedName("weather")
    val weather: List<WeatherResponse?>?,
    @SerializedName("wind")
    val wind: WindResponse?
)