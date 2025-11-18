package com.mapoflife.di

import android.content.Context
import androidx.room.Room
import com.mapoflife.data.local.AppDatabase
import com.mapoflife.data.local.LifeItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "mapoflife.db"
    ).build()

    @Provides
    @Singleton
    fun provideLifeItemDao(database: AppDatabase): LifeItemDao =
        database.lifeItemDao()
}
