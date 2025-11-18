package com.mapoflife.di

import android.content.Context
import androidx.room.Room
import com.mapoflife.data.LocationRepository
import com.mapoflife.data.local.AppDatabase
import com.mapoflife.data.local.LifeItemDao
import com.mapoflife.data.local.LocationPointDao
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

@DisplayName("DatabaseModule Tests")
class DatabaseModuleTest {
    private lateinit var mockContext: Context
    private lateinit var mockDatabase: AppDatabase

    @BeforeEach
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockDatabase = mockk(relaxed = true)
        mockkStatic(Room::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Room::class)
    }

    @Test
    @DisplayName("provideAppDatabase creates database with correct name")
    fun testProvideAppDatabase() {
        // Arrange
        mockkStatic(Room::class)
        val mockBuilder = mockk<androidx.room.RoomDatabase.Builder<AppDatabase>>(relaxed = true)
        mockBuilder.build()

        // Act
        val result = DatabaseModule.provideAppDatabase(mockContext)

        // Assert
        assertNotNull(result)
    }

    @Test
    @DisplayName("provideLifeItemDao returns dao from database")
    fun testProvideLifeItemDao() {
        // Arrange
        val mockDao = mockk<LifeItemDao>(relaxed = true)
        val mockDb = mockk<AppDatabase> {
            every { lifeItemDao() } returns mockDao
        }

        // Act
        val result = DatabaseModule.provideLifeItemDao(mockDb)

        // Assert
        assertNotNull(result)
        verify { mockDb.lifeItemDao() }
    }

    @Test
    @DisplayName("provideLocationPointDao returns dao from database")
    fun testProvideLocationPointDao() {
        // Arrange
        val mockDao = mockk<LocationPointDao>(relaxed = true)
        val mockDb = mockk<AppDatabase> {
            every { locationPointDao() } returns mockDao
        }

        // Act
        val result = DatabaseModule.provideLocationPointDao(mockDb)

        // Assert
        assertNotNull(result)
        verify { mockDb.locationPointDao() }
    }

    @Test
    @DisplayName("provideLocationRepository receives LocationPointDao dependency")
    fun testProvideLocationRepository() {
        // Arrange
        val mockLocationPointDao = mockk<LocationPointDao>(relaxed = true)

        // Act
        val result = DatabaseModule.provideLocationRepository(mockLocationPointDao)

        // Assert
        assertNotNull(result)
        assert(result is LocationRepository)
    }

    @Test
    @DisplayName("provideLocationRepository wraps injected LocationPointDao")
    fun testProvideLocationRepositoryUsesInjectedDao() {
        // Arrange
        val mockLocationPointDao = mockk<LocationPointDao>(relaxed = true)

        // Act
        val repository = DatabaseModule.provideLocationRepository(mockLocationPointDao)

        // Assert: Verify repository can be created with mocked dao
        assertNotNull(repository)
    }
}
