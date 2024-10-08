package com.maulana.weathermobile.data.datasource.remote.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CloudsResponse(
    @SerializedName("all")
    val all: Int?
)