package com.maulana.weathermobile.domain.model

data class LocationLocal(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isDefault: Boolean,
    val state: String,
    val country: String
)