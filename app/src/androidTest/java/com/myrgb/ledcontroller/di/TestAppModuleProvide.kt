package com.myrgb.ledcontroller.di

import android.content.Context
import androidx.room.Room
import com.myrgb.ledcontroller.persistence.LedControllerDatabase
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestAppModuleProvide {
    @Singleton
    @Provides
    fun provideTestDatabase(context: Context): LedControllerDatabase =
        Room.inMemoryDatabaseBuilder(context, LedControllerDatabase::class.java).build()

    @Singleton
    @Provides
    fun provideRgbAlarmDao(database: LedControllerDatabase): RgbAlarmDao = database.rgbAlarmDao
}