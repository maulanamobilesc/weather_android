package com.maulana.weathermobile.data.datasource.remote.response.forecastweather


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RainResponse(
    @SerializedName("3h")
    val h: Double?
)