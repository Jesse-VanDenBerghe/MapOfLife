package com.mapoflife.worker

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mapoflife.service.LocationTrackingService
import java.util.concurrent.TimeUnit

class LocationTrackingWorker(
    context: Context,
    params: androidx.work.WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            ensureLocationTrackingServiceRunning()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun ensureLocationTrackingServiceRunning() {
        val context = applicationContext
        val serviceIntent = Intent(context, LocationTrackingService::class.java)
        
        // Check if service is running
        if (!isServiceRunning(context)) {
            context.startService(serviceIntent)
        }
    }

    private fun isServiceRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        return activityManager.getRunningServices(Int.MAX_VALUE).any {
            it.service.className == LocationTrackingService::class.java.name
        }
    }

    companion object {
        private const val WORK_NAME = "location_tracking_work"
        private const val INTERVAL_MINUTES = 15L

        fun schedulePeriodicWork(context: Context) {
            val locationTrackingWork = PeriodicWorkRequestBuilder<LocationTrackingWorker>(
                INTERVAL_MINUTES,
                TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                locationTrackingWork
            )
        }

        fun cancelWork(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
