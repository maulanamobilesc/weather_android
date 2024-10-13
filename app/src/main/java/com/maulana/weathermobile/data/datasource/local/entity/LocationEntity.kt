package com.maulana.weathermobile.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    val id: Int,
    @PrimaryKey
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @ColumnInfo("is_default")
    val isDefault: Boolean
)