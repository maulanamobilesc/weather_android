package com.maulana.weathermobile.data.datasource.remote.response.currentweather


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SysResponse(
    @SerializedName("country")
    val country: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("sunrise")
    val sunrise: Int?,
    @SerializedName("sunset")
    val sunset: Int?,
    @SerializedName("type")
    val type: Int?
)