package com.mapoflife.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [LifeItemEntity::class, LocationPointEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lifeItemDao(): LifeItemDao
    abstract fun locationPointDao(): LocationPointDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS location_points (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        latitude REAL NOT NULL,
                        longitude REAL NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS index_location_points_latitude ON location_points(latitude)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_location_points_longitude ON location_points(longitude)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_location_points_timestamp ON location_points(timestamp)")
            }
        }
    }
}
