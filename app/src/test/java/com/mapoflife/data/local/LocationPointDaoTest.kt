package com.mapoflife.data.local

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("LocationPointDao tests")
class LocationPointDaoTest {

    private lateinit var mockDao: LocationPointDao

    @BeforeEach
    fun setUp() {
        mockDao = mockk()
    }

    @Test
    @DisplayName("Should insert single location")
    fun testInsert() = runTest {
        val location = LocationPointEntity(
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = 1234567890L
        )
        val insertedId = 1L

        coEvery { mockDao.insert(location) } returns insertedId

        val result = mockDao.insert(location)

        assertEquals(insertedId, result)
        coVerify { mockDao.insert(location) }
    }

    @Test
    @DisplayName("Should insert batch of locations")
    fun testInsertBatch() = runTest {
        val locations = listOf(
            LocationPointEntity(latitude = 40.7128, longitude = -74.0060, timestamp = 1234567890L),
            LocationPointEntity(latitude = 51.5074, longitude = -0.1278, timestamp = 1234567891L),
            LocationPointEntity(latitude = 48.8566, longitude = 2.3522, timestamp = 1234567892L)
        )
        val insertedIds = listOf(1L, 2L, 3L)

        coEvery { mockDao.insertBatch(locations) } returns insertedIds

        val result = mockDao.insertBatch(locations)

        assertEquals(insertedIds, result)
        coVerify { mockDao.insertBatch(locations) }
    }

    @Test
    @DisplayName("Should get last location")
    fun testGetLastLocation() = runTest {
        val lastLocation = LocationPointEntity(
            id = 1L,
            latitude = 40.7128,
            longitude = -74.0060,
            timestamp = 1234567890L
        )

        coEvery { mockDao.getLastLocation() } returns lastLocation

        val result = mockDao.getLastLocation()

        assertEquals(lastLocation, result)
        coVerify { mockDao.getLastLocation() }
    }

    @Test
    @DisplayName("Should return null when no last location")
    fun testGetLastLocationNull() = runTest {
        coEvery { mockDao.getLastLocation() } returns null

        val result = mockDao.getLastLocation()

        assertEquals(null, result)
    }

    @Test
    @DisplayName("Should get all locations")
    fun testGetAllLocations() = runTest {
        val locations = listOf(
            LocationPointEntity(id = 1L, latitude = 40.7128, longitude = -74.0060, timestamp = 1234567890L),
            LocationPointEntity(id = 2L, latitude = 51.5074, longitude = -0.1278, timestamp = 1234567891L)
        )
        val flow = flowOf(locations)

        coEvery { mockDao.getAllLocations() } returns flow

        val result = mockDao.getAllLocations()

        assertEquals(flow, result)
        coVerify { mockDao.getAllLocations() }
    }

    @Test
    @DisplayName("Should count locations")
    fun testCount() = runTest {
        val count = 42L

        coEvery { mockDao.count() } returns count

        val result = mockDao.count()

        assertEquals(count, result)
        coVerify { mockDao.count() }
    }

    @Test
    @DisplayName("Should return 0 when no locations")
    fun testCountZero() = runTest {
        coEvery { mockDao.count() } returns 0L

        val result = mockDao.count()

        assertEquals(0L, result)
    }
}
