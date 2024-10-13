package com.maulana.weathermobile.data.datasource.remote

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.data.datasource.remote.service.GeoCodingService
import com.maulana.weathermobile.data.mapper.toLocationLocal
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.domain.model.LocationLocal
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class LocationRemoteDataSourceImpl(private val geoCodingService: GeoCodingService) :
    LocationRemoteDataSource {

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(context: Context): Flow<UIState<Coord>> = callbackFlow {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)

        val request = LocationRequest.Builder(10000)
            .setIntervalMillis(5000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let {
                    trySend(UIState.Success(Coord(it.latitude, it.longitude)))
                    locationClient.removeLocationUpdates(this)
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun searchLocation(query: String, apiKey: String): Flow<UIState<List<LocationLocal>>> {
        return flow {
            try {
                emit(UIState.Loading)

                emit(
                    UIState.Success(
                        geoCodingService.searchLocation(
                            query = query,
                            apiKey = apiKey,
                            limit = 10
                        ).map { it.toLocationLocal() })
                )
            } catch (e: Exception) {
                emit(UIState.Error(e.localizedMessage.orEmpty()))
            }
        }
    }
}