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
import com.maulana.weathermobile.domain.model.Coord
import com.maulana.weathermobile.util.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRemoteDataSourceImpl : LocationRemoteDataSource {

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
}