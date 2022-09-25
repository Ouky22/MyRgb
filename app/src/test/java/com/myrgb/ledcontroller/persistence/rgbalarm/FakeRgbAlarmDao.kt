package com.myrgb.ledcontroller.persistence.rgbalarm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeRgbAlarmDao : RgbAlarmDao {
    private val alarmList = mutableListOf<RgbAlarmDatabaseEntity>()
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

    override suspend fun getById(id: Int): RgbAlarmDatabaseEntity =
        alarmList.first { it.id == id }

    override suspend fun insertOrUpdate(rgbAlarm: RgbAlarmDatabaseEntity) {
        if (alarmList.any { it.id == rgbAlarm.id }) {
            alarmList.removeIf { it.id == rgbAlarm.id }
            alarmList.add(rgbAlarm)
        } else
            alarmList.add(rgbAlarm)
    }

    override suspend fun insertOrUpdate(rgbAlarms: List<RgbAlarmDatabaseEntity>) {
        rgbAlarms.forEach { insertOrUpdate(it) }
    }

    override suspend fun delete(rgbAlarm: RgbAlarmDatabaseEntity) {
        alarmList.removeIf { it.id == rgbAlarm.id }
    }
}