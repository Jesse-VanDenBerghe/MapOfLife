package com.mapoflife

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mapoflife.worker.LocationTrackingWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyLifeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeLocationTracking()
    }

    private fun initializeLocationTracking() {
        val locationTrackingRequest = PeriodicWorkRequestBuilder<LocationTrackingWorker>(
            15,
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "location_tracking_work",
            ExistingPeriodicWorkPolicy.KEEP,
            locationTrackingRequest
        )
    }
}
