package com.mapoflife.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "life_items")
data class LifeItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
