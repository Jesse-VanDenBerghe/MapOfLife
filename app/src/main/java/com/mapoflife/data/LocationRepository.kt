package com.mapoflife.data

import com.mapoflife.data.local.LocationPointDao
import com.mapoflife.data.local.LocationPointEntity
import kotlinx.coroutines.flow.Flow
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.PI

class LocationRepository(private val locationPointDao: LocationPointDao) {
    companion object {
        private const val EARTH_RADIUS_METERS = 6_371_000.0
    }

    fun getAllLocations(): Flow<List<LocationPointEntity>> = locationPointDao.getAllLocations()

    suspend fun shouldSaveLocation(newLat: Double, newLon: Double): Boolean {
        val lastLocation = locationPointDao.getLastLocation() ?: return true
        val distanceMeters = calculateHaversineDistance(
            lastLocation.latitude,
            lastLocation.longitude,
            newLat,
            newLon
        )
        return distanceMeters > LocationConfig.DEDUPLICATION_DISTANCE_METERS
    }

    suspend fun saveLocationIfNeeded(latitude: Double, longitude: Double): Boolean {
        if (shouldSaveLocation(latitude, longitude)) {
            locationPointDao.insert(
                LocationPointEntity(
                    latitude = latitude,
                    longitude = longitude,
                    timestamp = System.currentTimeMillis()
                )
            )
            return true
        }
        return false
    }

    private fun calculateHaversineDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val lat1Rad = lat1 * PI / 180
        val lat2Rad = lat2 * PI / 180
        val dLat = (lat2 - lat1) * PI / 180
        val dLon = (lon2 - lon1) * PI / 180

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * asin(sqrt(a))
        return EARTH_RADIUS_METERS * c
    }
}
