package com.myrgb.ledcontroller.persistence.rgbalarm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeRgbAlarmDao : RgbAlarmDao {
    val alarmList = mutableListOf<RgbAlarmDatabaseEntity>()
    private val alarms = MutableSharedFlow<MutableList<RgbAlarmDatabaseEntity>>()

    suspend fun emitAlarms() {
        alarms.emit(alarmList)
    }

    override fun observeAllAlarmsSortedByTime(): Flow<List<RgbAlarmDatabaseEntity>> = alarms

    override suspend fun getAllActiveAlarms(): List<RgbAlarmDatabaseEntity> =
        alarmList.filter { it.activated }

    override suspend fun getNextActivatedAlarm(): RgbAlarmDatabaseEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getByTime(timeMinutesOfDay: Int): RgbAlarmDatabaseEntity =
        alarmList.first { it.timeMinutesOfDay == timeMinutesOfDay }

    override suspend fun insertOrReplace(rgbAlarm: RgbAlarmDatabaseEntity) {
        if (alarmList.any { it.timeMinutesOfDay == rgbAlarm.timeMinutesOfDay }) {
            alarmList.removeIf { it.timeMinutesOfDay == rgbAlarm.timeMinutesOfDay }
            alarmList.add(rgbAlarm)
        } else
            alarmList.add(rgbAlarm)
    }

    override suspend fun insertOrReplace(rgbAlarms: List<RgbAlarmDatabaseEntity>) {
        rgbAlarms.forEach { insertOrReplace(it) }
    }

    override suspend fun insertOrIgnore(rgbAlarms: List<RgbAlarmDatabaseEntity>) {
        rgbAlarms.forEach { alarm ->
            if (!alarmList.any { it == alarm })
                alarmList.add(alarm)
        }
    }

    override suspend fun delete(rgbAlarm: RgbAlarmDatabaseEntity) {
        alarmList.removeIf { it.timeMinutesOfDay == rgbAlarm.timeMinutesOfDay }
    }

    override suspend fun deleteByTime(timeMinutesOfDay: Int) {
        alarmList.removeIf { it.timeMinutesOfDay == timeMinutesOfDay }
    }
}