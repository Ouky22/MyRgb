package com.example.ledcontroller.persistence

import androidx.room.*
import com.example.ledcontroller.model.RgbAlarm
import kotlinx.coroutines.flow.Flow

@Dao
interface RgbAlarmDao {

    @Query("SELECT * FROM rgbAlarm ORDER BY trigger_time_minutes_of_day ASC")
    fun getAllSortedByTriggerTime(): Flow<List<RgbAlarm>>

    @Query("SELECT * FROM rgbAlarm WHERE is_active = 1")
    suspend fun getAllActiveAlarms(): List<RgbAlarm>

    @Query("SELECT * FROM rgbAlarm WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): RgbAlarm

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rgbAlarm: RgbAlarm)

    @Update
    suspend fun update(rgbAlarm: RgbAlarm)

    @Delete
    suspend fun delete(rgbAlarm: RgbAlarm)
}