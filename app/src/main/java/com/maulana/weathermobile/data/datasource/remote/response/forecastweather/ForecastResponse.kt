package com.maulana.weathermobile.data.datasource.remote.response.forecastweather


import com.google.gson.annotations.SerializedName
import com.maulana.weathermobile.data.datasource.remote.response.CloudsResponse
import com.maulana.weathermobile.data.datasource.remote.response.MainResponse
import com.maulana.weathermobile.data.datasource.remote.response.WeatherResponse
import com.maulana.weathermobile.data.datasource.remote.response.WindResponse
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerializedName("clouds")
    val clouds: CloudsResponse?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("dt_txt")
    val dtTxt: String?,
    @SerializedName("main")
    val main: MainResponse?,
    @SerializedName("pop")
    val pop: Double?,
    @SerializedName("rain")
    val rain: RainResponse?,
    @SerializedName("sys")
    val sys: SysPodResponse?,
    @SerializedName("visibility")
    val visibility: Int?,
    @SerializedName("weather")
    val weather: List<WeatherResponse?>?,
    @SerializedName("wind")
    val wind: WindResponse?
)