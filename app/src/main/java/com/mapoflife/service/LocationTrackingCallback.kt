package com.mapoflife.service

import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.mapoflife.data.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LocationTrackingCallback(
    private val locationRepository: LocationRepository,
    private val scope: CoroutineScope
) : LocationCallback() {

    override fun onLocationResult(result: LocationResult) {
        result.lastLocation?.let { location ->
            scope.launch {
                locationRepository.saveLocationIfNeeded(
                    location.latitude,
                    location.longitude
                )
            }
        }
    }
}
