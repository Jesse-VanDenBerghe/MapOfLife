package com.mapoflife.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LifeItemDao {
    @Insert
    suspend fun insert(item: LifeItemEntity): Long

    @Update
    suspend fun update(item: LifeItemEntity)

    @Delete
    suspend fun delete(item: LifeItemEntity)

    @Query("SELECT * FROM life_items WHERE id = :id")
    suspend fun getById(id: Long): LifeItemEntity?

    @Query("SELECT * FROM life_items ORDER BY createdAt DESC")
    fun getAllItems(): Flow<List<LifeItemEntity>>

    @Query("DELETE FROM life_items")
    suspend fun deleteAll()
}
