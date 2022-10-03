package com.myrgb.ledcontroller.persistence.rgbalarm

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RgbAlarmDao {
    @Query("SELECT * FROM rgbAlarm ORDER BY time_minutes_of_day ASC")
    fun observeAllAlarmsSortedByTime(): Flow<List<RgbAlarmDatabaseEntity>>

    @Query("SELECT * FROM rgbAlarm WHERE is_active = 1")
    suspend fun getAllActiveAlarms(): List<RgbAlarmDatabaseEntity>

    @Query(
        "SELECT * FROM rgbAlarm " +
                "WHERE is_active = 1 " +
                "ORDER BY date_time_millis_the_alarm_was_activated_for " +
                "LIMIT 1"
    )
    suspend fun getNextActivatedAlarm(): RgbAlarmDatabaseEntity

    @Query("SELECT * FROM rgbAlarm WHERE time_minutes_of_day = :timeMinutesOfDay LIMIT 1")
    suspend fun getByTime(timeMinutesOfDay: Int): RgbAlarmDatabaseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(rgbAlarm: RgbAlarmDatabaseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(rgbAlarms: List<RgbAlarmDatabaseEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(rgbAlarms: List<RgbAlarmDatabaseEntity>)

    @Delete
    suspend fun delete(rgbAlarm: RgbAlarmDatabaseEntity)

    @Query("DELETE FROM rgbAlarm WHERE time_minutes_of_day = :timeMinutesOfDay")
    suspend fun deleteByTime(timeMinutesOfDay: Int)
}