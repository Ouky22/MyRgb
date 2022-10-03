package com.myrgb.ledcontroller.persistence.rgbalarm

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDatabaseEntity
import kotlinx.coroutines.flow.Flow

interface RgbAlarmRepository {
    val alarms: Flow<List<RgbAlarm>>

    suspend fun getNextActivatedAlarm(): RgbAlarmDatabaseEntity

    suspend fun getByTime(timeMinutesOfDay: Int): RgbAlarmDatabaseEntity

    suspend fun insertOrReplace(rgbAlarm: RgbAlarm)

    suspend fun delete(rgbAlarm: RgbAlarm)

    suspend fun deleteByTime(timeMinutesOfDay: Int)
}