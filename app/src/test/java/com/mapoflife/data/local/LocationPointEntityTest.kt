package com.mapoflife.data.local

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("LocationPointEntity tests")
class LocationPointEntityTest {

    @Test
    @DisplayName("Should create LocationPointEntity with default id of 0")
    fun testDefaultId() {
        val location = LocationPointEntity(
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = 1234567890L
        )
        assertEquals(0L, location.id)
    }

    @Test
    @DisplayName("Should create LocationPointEntity with provided id")
    fun testProvidedId() {
        val location = LocationPointEntity(
            id = 123L,
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = 1234567890L
        )
        assertEquals(123L, location.id)
    }

    @Test
    @DisplayName("Should store latitude correctly")
    fun testLatitude() {
        val lat = 40.7128
        val location = LocationPointEntity(
            latitude = lat,
            longitude = -74.0060,
            timestamp = 1234567890L
        )
        assertEquals(lat, location.latitude)
    }

    @Test
    @DisplayName("Should store longitude correctly")
    fun testLongitude() {
        val lon = -74.0060
        val location = LocationPointEntity(
            latitude = 40.7128,
            longitude = lon,
            timestamp = 1234567890L
        )
        assertEquals(lon, location.longitude)
    }

    @Test
    @DisplayName("Should store timestamp correctly")
    fun testTimestamp() {
        val timestamp = 1234567890L
        val location = LocationPointEntity(
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = timestamp
        )
        assertEquals(timestamp, location.timestamp)
    }

    @Test
    @DisplayName("Should handle negative coordinates")
    fun testNegativeCoordinates() {
        val location = LocationPointEntity(
            latitude = -33.8688,
            longitude = 151.2093,
            timestamp = 1234567890L
        )
        assertEquals(-33.8688, location.latitude)
        assertEquals(151.2093, location.longitude)
    }
}
