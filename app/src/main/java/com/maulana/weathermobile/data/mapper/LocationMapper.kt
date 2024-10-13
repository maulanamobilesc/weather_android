package com.maulana.weathermobile.data.mapper

import com.maulana.weathermobile.data.datasource.local.entity.LocationEntity
import com.maulana.weathermobile.data.datasource.remote.response.searchlocation.SearchLocationItemResponse
import com.maulana.weathermobile.domain.model.LocationLocal

fun LocationEntity.toLocationLocal() =
    LocationLocal(id, name, latitude, longitude, isDefault, "","")

fun LocationLocal.toLocationEntity() =
    LocationEntity(id, name, latitude, longitude, isDefault)

fun SearchLocationItemResponse.toLocationLocal() =
    LocationLocal(0, name ?: "", lat ?: 0.0, lon ?: 0.0, false, state ?: "", country ?: "")
