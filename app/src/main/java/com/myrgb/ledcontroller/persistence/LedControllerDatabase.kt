package com.myrgb.ledcontroller.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmDao

@Database(entities = [RgbAlarm::class], version = 1, exportSchema = false)
abstract class LedControllerDatabase : RoomDatabase() {

    abstract val rgbAlarmDao: RgbAlarmDao

    companion object {
        @Volatile
        private var INSTANCE: LedControllerDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context,
                LedControllerDatabase::class.java,
                "led_controller_database"
            ).build()

            INSTANCE = instance
            instance
        }
    }
}