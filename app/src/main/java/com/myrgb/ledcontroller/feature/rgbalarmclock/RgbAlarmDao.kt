package com.myrgb.ledcontroller.feature.rgbalarmclock

import androidx.room.*
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDatabaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RgbAlarmDao {

    @Query("SELECT * FROM rgbAlarm ORDER BY trigger_time_minutes_of_day ASC")
    fun getAllSortedByTriggerTime(): Flow<List<RgbAlarmDatabaseEntity>>

    @Query("SELECT * FROM rgbAlarm WHERE is_active = 1")
    suspend fun getAllActiveAlarms(): List<RgbAlarmDatabaseEntity>

    @Query("SELECT * FROM rgbAlarm WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): RgbAlarmDatabaseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rgbAlarm: RgbAlarmDatabaseEntity)

    @Update
    suspend fun update(rgbAlarm: RgbAlarmDatabaseEntity)

    @Delete
    suspend fun delete(rgbAlarm: RgbAlarmDatabaseEntity)
}