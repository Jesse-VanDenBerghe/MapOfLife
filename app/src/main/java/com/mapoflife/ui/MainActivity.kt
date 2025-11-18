package com.mapoflife.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mapoflife.R
import com.mapoflife.permissions.PermissionHandler
import com.mapoflife.service.LocationTrackingService
import com.mapoflife.ui.screens.HomeScreen
import com.mapoflife.ui.theme.MapOfLifeTheme
import com.mapoflife.worker.LocationTrackingWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Log.d(TAG, "Permission result: $permissions")
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            Log.d(TAG, "All permissions granted")
            startLocationTracking()
        } else {
            Log.d(TAG, "Some permissions denied")
        }
    }

    private var permissionsRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MapOfLifeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!permissionsRequested) {
            permissionsRequested = true
            requestPermissionsAndStartTracking()
        }
    }

    private fun requestPermissionsAndStartTracking() {
        Log.d(TAG, "Checking permissions...")
        if (PermissionHandler.areAllPermissionsGranted(this)) {
            Log.d(TAG, "Permissions already granted")
            startLocationTracking()
        } else {
            val permissionsToRequest = PermissionHandler.getPermissionsToRequest(this)
            Log.d(TAG, "Requesting permissions: ${permissionsToRequest.toList()}")
            if (permissionsToRequest.isNotEmpty()) {
                permissionLauncher.launch(permissionsToRequest)
            }
        }
    }

    private fun startLocationTracking() {
        Log.d(TAG, "Starting location tracking...")
        // Start foreground location tracking service
        val serviceIntent = Intent(this, LocationTrackingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        Log.d(TAG, "Service intent started")

        // Schedule WorkManager periodic check
        scheduleLocationTrackingWorker()
    }

    private fun scheduleLocationTrackingWorker() {
        val locationTrackingRequest = PeriodicWorkRequestBuilder<LocationTrackingWorker>(
            15,
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "location_tracking_work",
            ExistingPeriodicWorkPolicy.KEEP,
            locationTrackingRequest
        )
        Log.d(TAG, "WorkManager scheduled")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

