package com.mapoflife.service

import android.location.Location
import com.google.android.gms.location.LocationResult
import com.mapoflife.data.LocationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("LocationTrackingCallback Tests")
class LocationTrackingCallbackTest {
    private lateinit var mockLocationRepository: LocationRepository
    private lateinit var mockScope: CoroutineScope
    private lateinit var callback: LocationTrackingCallback

    @BeforeEach
    fun setUp() {
        mockLocationRepository = mockk(relaxed = true)
        mockScope = CoroutineScope(UnconfinedTestDispatcher())
        callback = LocationTrackingCallback(mockLocationRepository, mockScope)
    }

    @Test
    @DisplayName("onLocationResult calls saveLocationIfNeeded with location coordinates")
    fun testOnLocationResultCallsSaveLocationIfNeeded() = runTest {
        // Arrange
        val mockLocation = mockk<Location> {
            every { latitude } returns 40.7128
            every { longitude } returns -74.0060
        }
        val locationResult = mockk<LocationResult> {
            every { lastLocation } returns mockLocation
        }
        coEvery { mockLocationRepository.saveLocationIfNeeded(any(), any()) } returns true

        // Act
        callback.onLocationResult(locationResult)

        // Assert
        coVerify(atLeast = 1) {
            mockLocationRepository.saveLocationIfNeeded(40.7128, -74.0060)
        }
    }

    @Test
    @DisplayName("onLocationResult handles null location gracefully")
    fun testOnLocationResultHandlesNullLocation() = runTest {
        // Arrange
        val locationResult = mockk<LocationResult> {
            every { lastLocation } returns null
        }

        // Act & Assert: Should not throw exception
        callback.onLocationResult(locationResult)

        // Verify saveLocationIfNeeded was never called
        coVerify(exactly = 0) {
            mockLocationRepository.saveLocationIfNeeded(any(), any())
        }
    }

    @Test
    @DisplayName("onLocationResult extracts correct latitude and longitude")
    fun testOnLocationResultExtractsCoordinates() = runTest {
        // Arrange
        val testLat = 51.5074
        val testLon = -0.1278
        val mockLocation = mockk<Location> {
            every { latitude } returns testLat
            every { longitude } returns testLon
        }
        val locationResult = mockk<LocationResult> {
            every { lastLocation } returns mockLocation
        }
        coEvery { mockLocationRepository.saveLocationIfNeeded(any(), any()) } returns true

        // Act
        callback.onLocationResult(locationResult)

        // Assert
        coVerify {
            mockLocationRepository.saveLocationIfNeeded(testLat, testLon)
        }
    }

    @Test
    @DisplayName("onLocationResult executes in provided coroutine scope")
    fun testOnLocationResultUsesProvidedScope() = runTest {
        // Arrange
        val mockLocation = mockk<Location> {
            every { latitude } returns 35.6762
            every { longitude } returns 139.6503
        }
        val locationResult = mockk<LocationResult> {
            every { lastLocation } returns mockLocation
        }
        var executedInScope = false
        coEvery { mockLocationRepository.saveLocationIfNeeded(any(), any()) } coAnswers {
            executedInScope = true
            true
        }

        // Act
        callback.onLocationResult(locationResult)

        // Assert
        assert(executedInScope)
    }
}
