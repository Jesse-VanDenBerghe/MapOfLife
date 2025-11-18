package com.mapoflife.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationPointDao {
    @Insert
    suspend fun insert(location: LocationPointEntity): Long

    @Insert
    suspend fun insertBatch(locations: List<LocationPointEntity>): List<Long>

    @Query("SELECT * FROM location_points ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastLocation(): LocationPointEntity?

    @Query("SELECT * FROM location_points ORDER BY timestamp ASC")
    fun getAllLocations(): Flow<List<LocationPointEntity>>

    @Query("SELECT COUNT(*) FROM location_points")
    suspend fun count(): Long
}
