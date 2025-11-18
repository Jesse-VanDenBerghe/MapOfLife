package com.mapoflife.data

import com.mapoflife.data.local.LocationPointDao
import com.mapoflife.data.local.LocationPointEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("LocationRepository tests")
class LocationRepositoryTest {

    private lateinit var mockDao: LocationPointDao
    private lateinit var repository: LocationRepository

    @BeforeEach
    fun setUp() {
        mockDao = mockk()
        repository = LocationRepository(mockDao)
    }

    @Test
    @DisplayName("Should get all locations from DAO")
    fun testGetAllLocations() = runTest {
        val locations = listOf(
            LocationPointEntity(id = 1L, latitude = 40.7128, longitude = -74.0060, timestamp = 1000L),
            LocationPointEntity(id = 2L, latitude = 51.5074, longitude = -0.1278, timestamp = 2000L)
        )
        val flow = flowOf(locations)

        coEvery { mockDao.getAllLocations() } returns flow

        val result = repository.getAllLocations()

        assertEquals(flow, result)
        coVerify { mockDao.getAllLocations() }
    }

    @Test
    @DisplayName("Should save location when no previous location exists")
    fun testSaveLocationIfNeeded_NoPreviousLocation() = runTest {
        coEvery { mockDao.getLastLocation() } returns null
        coEvery { mockDao.insert(any()) } returns 1L

        val result = repository.saveLocationIfNeeded(40.7128, -74.0060)

        assertTrue(result)
        coVerify { mockDao.getLastLocation() }
        coVerify { mockDao.insert(any()) }
    }

    @Test
    @DisplayName("Should not save location when within deduplication distance")
    fun testSaveLocationIfNeeded_WithinDistance() = runTest {
        val lastLocation = LocationPointEntity(
            id = 1L,
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = 1000L
        )
        coEvery { mockDao.getLastLocation() } returns lastLocation

        val result = repository.saveLocationIfNeeded(40.7128001, -74.0060001)

        assertFalse(result)
        coVerify { mockDao.getLastLocation() }
        coVerify(exactly = 0) { mockDao.insert(any()) }
    }

    @Test
    @DisplayName("Should save location when outside deduplication distance")
    fun testSaveLocationIfNeeded_OutsideDistance() = runTest {
        val lastLocation = LocationPointEntity(
            id = 1L,
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = 1000L
        )
        coEvery { mockDao.getLastLocation() } returns lastLocation
        coEvery { mockDao.insert(any()) } returns 2L

        val result = repository.saveLocationIfNeeded(40.7240, -74.0090)

        assertTrue(result)
        coVerify { mockDao.getLastLocation() }
        coVerify { mockDao.insert(any()) }
    }

    @Test
    @DisplayName("Should return false for shouldSaveLocation when no previous location")
    fun testShouldSaveLocation_NoPreviousLocation() = runTest {
        coEvery { mockDao.getLastLocation() } returns null

        val result = repository.shouldSaveLocation(40.7128, -74.0060)

        assertTrue(result)
    }

    @Test
    @DisplayName("Should return false for shouldSaveLocation when within distance")
    fun testShouldSaveLocation_WithinDistance() = runTest {
        val lastLocation = LocationPointEntity(
            id = 1L,
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = 1000L
        )
        coEvery { mockDao.getLastLocation() } returns lastLocation

        val result = repository.shouldSaveLocation(40.7128001, -74.0060001)

        assertFalse(result)
    }

    @Test
    @DisplayName("Should return true for shouldSaveLocation when outside distance")
    fun testShouldSaveLocation_OutsideDistance() = runTest {
        val lastLocation = LocationPointEntity(
            id = 1L,
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = 1000L
        )
        coEvery { mockDao.getLastLocation() } returns lastLocation

        val result = repository.shouldSaveLocation(40.7240, -74.0090)

        assertTrue(result)
    }

    @ParameterizedTest
    @DisplayName("Should calculate Haversine distance correctly")
    @CsvSource(
        "40.7128,-74.0060,40.7128,-74.0060,0.0",        // Same location
        "0.0,0.0,0.0,0.00001,0.0011",                   // ~1.1m at equator
        "51.5074,-0.1278,48.8566,2.3522,340000.0",      // London to Paris ~340km
        "34.0522,-118.2437,37.7749,-122.4194,560000.0"  // LA to SF ~560km
    )
    fun testHaversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double, expectedKm: Double) = runTest {
        coEvery { mockDao.getLastLocation() } returns LocationPointEntity(
            latitude = lat1,
            longitude = lon1,
            timestamp = 1000L
        )

        val result = repository.shouldSaveLocation(lat2, lon2)

        val expectedMeters = expectedKm * 1000
        val deduplicationDistance = LocationConfig.DEDUPLICATION_DISTANCE_METERS

        if (expectedMeters <= deduplicationDistance) {
            assertFalse(result, "Distance $expectedKm km should be within dedup distance")
        } else {
            assertTrue(result, "Distance $expectedKm km should be outside dedup distance")
        }
    }

    @Test
    @DisplayName("Should insert location with current timestamp")
    fun testSaveLocationTimestamp() = runTest {
        coEvery { mockDao.getLastLocation() } returns null
        coEvery { mockDao.insert(any()) } returns 1L

        val beforeTime = System.currentTimeMillis()
        repository.saveLocationIfNeeded(40.7128, -74.0060)
        val afterTime = System.currentTimeMillis()

        coVerify {
            mockDao.insert(match {
                it.latitude == 40.7128 &&
                it.longitude == -74.0060 &&
                it.timestamp in beforeTime..afterTime
            })
        }
    }

    @Test
    @DisplayName("Should handle multiple sequential saves")
    fun testMultipleSequentialSaves() = runTest {
        var location: LocationPointEntity? = null

        coEvery { mockDao.getLastLocation() } answers { location }
        coEvery { mockDao.insert(any()) } answers {
            location = it.invocation.args[0] as LocationPointEntity
            location!!.id + 1
        }

        val result1 = repository.saveLocationIfNeeded(40.7128, -74.0060)
        val result2 = repository.saveLocationIfNeeded(40.7240, -74.0090)

        assertTrue(result1)
        assertTrue(result2)
    }
}
