package com.mapoflife.data

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("LocationConfig tests")
class LocationConfigTest {

    @Test
    @DisplayName("Should have tracking interval of 10 seconds")
    fun testTrackingInterval() {
        assertEquals(10_000L, LocationConfig.TRACKING_INTERVAL_MS)
    }

    @Test
    @DisplayName("Should have deduplication distance of 10 meters")
    fun testDeduplicationDistance() {
        assertEquals(10.0, LocationConfig.DEDUPLICATION_DISTANCE_METERS)
    }

    @Test
    @DisplayName("Should have balanced power accuracy location priority")
    fun testLocationPriority() {
        assertEquals("PRIORITY_BALANCED_POWER_ACCURACY", LocationConfig.LOCATION_PRIORITY)
    }
}
