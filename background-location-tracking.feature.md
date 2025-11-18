## Feature: Background Location Tracking

**Branch**: feature/background-location-tracking  
**Created**: November 18, 2025  
**Author**: GitHub Copilot  
**Jira**: N/A

## Overview
Implement continuous background location tracking that captures user GPS coordinates every 10 seconds and stores them efficiently in a Room database. The system will automatically deduplicate locations within 10 meters to optimize storage and query performance for future map visualization features.

## Problem Statement
The app needs to build a comprehensive location history database that will later be used to visualize all places where the user has been. This requires:
- Reliable background location tracking that works continuously
- High-performance database capable of handling thousands/millions of location points
- Efficient storage by avoiding redundant data (duplicate locations within small radius)
- Battery-efficient implementation that doesn't drain device resources
- Always-on functionality that survives app restarts and device reboots

## Solution
Implement a multi-layered architecture:
- **Database Layer**: Lightweight LocationPoint entity with spatial indexing for fast queries
- **Service Layer**: Android Foreground Service for continuous location tracking with proper lifecycle management
- **Persistence Layer**: WorkManager to ensure service restarts after boot/crashes
- **Optimization Layer**: Intelligent deduplication using Haversine distance calculation to skip locations within 10m
- **Configuration**: Centralized constants for easy adjustment of tracking interval and deduplication threshold

## Implementation Plan

### Phase 1: Database & Repository Layer
- [x] Create `LocationPoint` entity
  - Fields: id (Long, PK), latitude (Double), longitude (Double), timestamp (Long)
  - Add indices on latitude and longitude for spatial queries
  - Separate table from LifeItemEntity for performance isolation
  
- [x] Update AppDatabase
  - Add LocationPoint entity to database
  - Increment database version to 2
  - Add migration strategy from version 1 to 2
  
- [x] Create `LocationPointDao`
  - Insert operation (single point)
  - Batch insert operation for future optimization
  - Query for last location (for deduplication check)
  - Query for all locations (for future map visualization)
  - Count query (for debugging/statistics)
  
- [x] Create `LocationRepository`
  - Implement Haversine distance calculation
  - shouldSaveLocation() method (checks if >10m from last point)
  - saveLocationIfNeeded() method (combines check + save)
  - Expose Flow<List<LocationPoint>> for observers
  
- [x] Create `LocationConfig` object
  - TRACKING_INTERVAL_MS = 10_000L (easily editable)
  - DEDUPLICATION_DISTANCE_METERS = 10.0 (easily editable)
  - LOCATION_PRIORITY = balanced accuracy

### Phase 2: Location Service Implementation
- [x] Create `LocationTrackingService` (Foreground Service)
  - Extend Service class
  - Inject LocationRepository via Hilt
  - Create persistent notification channel
  - Implement location callback handler
  
- [x] Implement location client integration
  - Use Google Play Services FusedLocationProviderClient
  - Configure LocationRequest with 10s interval and balanced accuracy
  - Handle location updates in callback
  - Implement proper cleanup on service destroy
  
- [x] Add service lifecycle management
  - Start service in foreground with notification
  - Stop location updates on service destroy
  - Handle service restart scenarios
  
- [x] Create `LocationTrackingWorker` (WorkManager)
  - Periodic worker to ensure service stays alive
  - Check if service is running, restart if needed
  - Schedule on app startup and boot complete

### Phase 3: Permissions & Integration
- [ ] Update AndroidManifest.xml
  - Add ACCESS_FINE_LOCATION permission
  - Add ACCESS_COARSE_LOCATION permission
  - Add ACCESS_BACKGROUND_LOCATION permission (API 29+)
  - Add FOREGROUND_SERVICE permission
  - Add FOREGROUND_SERVICE_LOCATION permission (API 34+)
  - Add POST_NOTIFICATIONS permission (API 33+)
  - Add RECEIVE_BOOT_COMPLETED permission
  - Register LocationTrackingService
  - Register boot receiver
  
- [ ] Create `PermissionHandler` utility
  - Check if all location permissions granted
  - Request permissions using ActivityResultContracts
  - Handle permission denial scenarios
  
- [ ] Update MainActivity
  - Request permissions on app launch
  - Start LocationTrackingService after permissions granted
  - Schedule WorkManager periodic check
  
- [ ] Update MyLifeApplication
  - Initialize WorkManager on app startup
  - Schedule LocationTrackingWorker

### Phase 4: Dependency Management
- [x] Update build.gradle.kts
  - Add Google Play Services Location dependency
  - Add WorkManager dependency
  - Ensure correct versions and compatibility
  
- [x] Update libs.versions.toml
  - Add playServicesLocation version
  - Add workManager version
  - Create dependency bundles if needed

### Phase 5: Testing & Optimization
- [ ] Test location tracking
  - Verify 10-second interval tracking
  - Test deduplication logic (move <10m, no save; move >10m, save)
  - Test service survives app minimization
  - Test service restarts after reboot
  
- [ ] Performance optimization
  - Monitor database insert performance
  - Verify spatial index effectiveness
  - Check memory usage and battery drain
  - Add batch insert if needed for better performance
  
- [ ] Add logging and monitoring
  - Log location saves vs skipped
  - Log service lifecycle events
  - Add debug notification showing tracking status

## Acceptance Criteria
- [ ] Location tracking starts automatically when app launches
- [ ] Locations captured approximately every 10 seconds
- [ ] Locations within 10m of previous point are not saved
- [ ] Database performs efficiently with 1000+ location points
- [ ] Service continues running when app is minimized
- [ ] Service restarts after device reboot
- [ ] Foreground notification displayed while tracking active
- [ ] All permissions properly requested and handled
- [ ] Tracking interval and deduplication distance easily configurable via LocationConfig
- [ ] No app crashes or ANRs during extended tracking sessions

## Side Notes
- **Future Enhancement**: Add UI to manually start/stop tracking and view statistics
- **Future Enhancement**: Implement battery optimization exemption request for more reliable tracking
- **Future Enhancement**: Add geofencing support to detect significant location changes
- **Future Enhancement**: Export location data functionality
- **Database Performance**: Room supports up to millions of rows efficiently; spatial indices will help with future map queries
- **Battery Consideration**: 10-second interval is aggressive; monitor battery impact and may need to adjust
- **Play Services**: Requires Google Play Services on device; won't work on devices without it (consider fallback to LocationManager)
- **Migration Path**: When adding map visualization later, existing location data will be immediately usable
