package com.myrgb.ledcontroller.persistence.rgbalarm

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDatabaseEntity
import kotlinx.coroutines.flow.Flow

interface RgbAlarmRepository {
    val alarms: Flow<List<RgbAlarm>>

    suspend fun getNextActivatedAlarm(): RgbAlarmDatabaseEntity

    suspend fun getById(id: Int): RgbAlarmDatabaseEntity

    suspend fun insertOrUpdate(rgbAlarm: RgbAlarm)

    suspend fun delete(rgbAlarm: RgbAlarm)
}