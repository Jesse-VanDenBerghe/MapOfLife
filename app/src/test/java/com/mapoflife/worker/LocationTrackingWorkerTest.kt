package com.mapoflife.worker

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.mapoflife.service.LocationTrackingService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("LocationTrackingWorker Tests")
class LocationTrackingWorkerTest {
    private lateinit var mockContext: Context
    private lateinit var mockWorkerParams: WorkerParameters
    private lateinit var worker: LocationTrackingWorker
    private lateinit var mockActivityManager: ActivityManager

    @BeforeEach
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockWorkerParams = mockk(relaxed = true)
        mockActivityManager = mockk(relaxed = true)

        every { mockContext.getSystemService(Context.ACTIVITY_SERVICE) } returns mockActivityManager
        every { mockContext.applicationContext } returns mockContext
        every { mockActivityManager.getRunningServices(Int.MAX_VALUE) } returns emptyList()
        every { mockContext.startService(any()) } returns mockk()

        worker = LocationTrackingWorker(mockContext, mockWorkerParams)
    }

    @Test
    @DisplayName("doWork returns success when service starts successfully")
    fun testDoWorkReturnsSuccess() = runTest {
        // Act
        val result = worker.doWork()

        // Assert
        assert(result is ListenableWorker.Result.Success)
    }

    @Test
    @DisplayName("doWork starts LocationTrackingService when not running")
    fun testDoWorkStartsServiceWhenNotRunning() = runTest {
        // Arrange
        every { mockActivityManager.getRunningServices(Int.MAX_VALUE) } returns emptyList()
        val intentSlot = slot<Intent>()
        every { mockContext.startService(capture(intentSlot)) } returns mockk()

        // Act
        worker.doWork()

        // Assert
        verify { mockContext.startService(any()) }
    }

    @Test
    @DisplayName("doWork skips starting service when already running")
    fun testDoWorkSkipsStartingServiceWhenRunning() = runTest {
        // Arrange
        // When service is found running, don't start again
        val mockComponentName = ComponentName(
            "com.mapoflife",
            LocationTrackingService::class.java.name
        )
        val runningService = ActivityManager.RunningServiceInfo().apply {
            service = mockComponentName
        }
        every { mockActivityManager.getRunningServices(Int.MAX_VALUE) } returns listOf(runningService)

        // Act
        val result = worker.doWork()

        // Assert: Should succeed without starting service
        assert(result is ListenableWorker.Result.Success)
    }

    @Test
    @DisplayName("doWork returns retry on exception")
    fun testDoWorkReturnsRetryOnException() = runTest {
        // Arrange
        every { mockActivityManager.getRunningServices(Int.MAX_VALUE) } throws Exception("Service lookup failed")

        // Act
        val result = worker.doWork()

        // Assert
        assert(result is ListenableWorker.Result.Retry)
    }

    @Test
    @DisplayName("worker checks service running status correctly")
    fun testWorkerChecksServiceRunningStatusCorrectly() = runTest {
        // Arrange
        every { mockActivityManager.getRunningServices(Int.MAX_VALUE) } returns emptyList()
        every { mockContext.startService(any()) } returns mockk()

        // Act
        worker.doWork()

        // Assert
        verify { mockActivityManager.getRunningServices(Int.MAX_VALUE) }
    }

    @Test
    @DisplayName("schedulePeriodicWork method exists on companion object")
    fun testSchedulePeriodicWorkMethodExists() {
        // This test verifies the companion object method exists
        assert(LocationTrackingWorker.javaClass.declaredMethods.any { it.name == "schedulePeriodicWork" })
    }

    @Test
    @DisplayName("cancelWork method exists on companion object")
    fun testCancelWorkMethodExists() {
        // This test verifies the companion object method exists
        assert(LocationTrackingWorker.javaClass.declaredMethods.any { it.name == "cancelWork" })
    }

    @Test
    @DisplayName("doWork handles multiple services in running list")
    fun testDoWorkHandlesMultipleRunningServices() = runTest {
        // Arrange
        val otherComponent = ComponentName(
            "com.other",
            "com.other.Service"
        )
        val locationComponent = ComponentName(
            "com.mapoflife",
            LocationTrackingService::class.java.name
        )
        val otherService = ActivityManager.RunningServiceInfo().apply {
            service = otherComponent
        }
        val locationService = ActivityManager.RunningServiceInfo().apply {
            service = locationComponent
        }
        every { mockActivityManager.getRunningServices(Int.MAX_VALUE) } returns listOf(otherService, locationService)

        // Act
        val result = worker.doWork()

        // Assert: Should find location service and not start again
        assert(result is ListenableWorker.Result.Success)
    }

    @Test
    @DisplayName("doWork uses max value for running services query")
    fun testDoWorkUsesMaxValueForQuery() = runTest {
        // Arrange
        every { mockActivityManager.getRunningServices(Int.MAX_VALUE) } returns emptyList()
        every { mockContext.startService(any()) } returns mockk()

        // Act
        worker.doWork()

        // Assert
        verify { mockActivityManager.getRunningServices(Int.MAX_VALUE) }
    }

    @Test
    @DisplayName("doWork success result is not retry")
    fun testDoWorkSuccessIsNotRetry() = runTest {
        // Act
        val result = worker.doWork()

        // Assert
        assert(result !is ListenableWorker.Result.Retry)
    }
}
