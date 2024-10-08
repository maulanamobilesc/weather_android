package com.maulana.weathermobile.data.datasource.remote.response.forecastweather


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SysPodResponse(
    @SerializedName("pod")
    val pod: String?
)