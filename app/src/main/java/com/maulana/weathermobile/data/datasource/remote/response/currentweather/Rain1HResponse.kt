package com.maulana.weathermobile.data.datasource.remote.response.currentweather


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Rain1HResponse(
    @SerializedName("1h")
    val h: Double?
)