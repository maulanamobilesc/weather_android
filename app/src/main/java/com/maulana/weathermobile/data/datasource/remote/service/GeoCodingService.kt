package com.maulana.weathermobile.data.datasource.remote.service

import com.maulana.weathermobile.data.datasource.remote.response.searchlocation.SearchLocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingService {

    @GET("geo/1.0/direct?")
    suspend fun searchLocation(
        @Query("appid") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 10
    ): SearchLocationResponse

}