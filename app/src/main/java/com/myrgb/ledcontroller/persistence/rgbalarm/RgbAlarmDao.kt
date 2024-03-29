package com.myrgb.ledcontroller.persistence.rgbalarm

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RgbAlarmDao {
    @Query("SELECT * FROM rgbAlarm ORDER BY time_minutes_of_day ASC")
    fun observeAllAlarmsSortedByTime(): Flow<List<RgbAlarmDatabaseEntity>>

    @Query("SELECT * FROM rgbAlarm WHERE is_active = 1")
    suspend fun getAllActiveAlarms(): List<RgbAlarmDatabaseEntity>

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

    @Delete
    suspend fun delete(rgbAlarms: List<RgbAlarmDatabaseEntity>)

    @Query("DELETE FROM rgbAlarm WHERE time_minutes_of_day = :timeMinutesOfDay")
    suspend fun deleteByTime(timeMinutesOfDay: Int)

    @Query(
        "UPDATE rgbAlarm SET is_active = 1, last_time_activated_seconds = :currentDateTimeSeconds " +
            "WHERE time_minutes_of_day = :timeMinutesOfDay")
    suspend fun activateRgbAlarmByTime(timeMinutesOfDay: Int, currentDateTimeSeconds: Long)

    @Query("UPDATE rgbAlarm SET is_active = 0 WHERE time_minutes_of_day = :timeMinutesOfDay AND is_active = 1")
    suspend fun deactivateRgbAlarmByTime(timeMinutesOfDay: Int)
}