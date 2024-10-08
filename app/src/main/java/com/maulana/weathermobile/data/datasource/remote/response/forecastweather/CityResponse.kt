package com.maulana.weathermobile.data.datasource.remote.response.forecastweather


import com.google.gson.annotations.SerializedName
import com.maulana.weathermobile.data.datasource.remote.response.CoordResponse
import kotlinx.serialization.Serializable

@Serializable
data class CityResponse(
    @SerializedName("coord")
    val coord: CoordResponse?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("population")
    val population: Int?,
    @SerializedName("sunrise")
    val sunrise: Int?,
    @SerializedName("sunset")
    val sunset: Int?,
    @SerializedName("timezone")
    val timezone: Int?
)