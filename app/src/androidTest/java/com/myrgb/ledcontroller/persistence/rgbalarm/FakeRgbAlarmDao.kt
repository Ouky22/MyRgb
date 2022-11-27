package com.myrgb.ledcontroller.persistence.rgbalarm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRgbAlarmDao @Inject constructor() : RgbAlarmDao {
    val alarmList = mutableListOf<RgbAlarmDatabaseEntity>()
    private val alarms = MutableSharedFlow<MutableList<RgbAlarmDatabaseEntity>>()

    suspend fun emitAlarms() {
        alarms.emit(alarmList)
    }

    override fun observeAllAlarmsSortedByTime(): Flow<List<RgbAlarmDatabaseEntity>> = alarms

    override suspend fun getAllActiveAlarms(): List<RgbAlarmDatabaseEntity> =
        alarmList.filter { it.activated }

    override suspend fun getByTime(timeMinutesOfDay: Int): RgbAlarmDatabaseEntity =
        alarmList.first { it.timeMinutesOfDay == timeMinutesOfDay }

    override suspend fun insertOrReplace(rgbAlarm: RgbAlarmDatabaseEntity) {
        alarmList.removeIf { it.timeMinutesOfDay == rgbAlarm.timeMinutesOfDay }
        alarmList.add(rgbAlarm)
    }

    override suspend fun insertOrReplace(rgbAlarms: List<RgbAlarmDatabaseEntity>) {
        rgbAlarms.forEach { insertOrReplace(it) }
    }

    override suspend fun insertOrIgnore(rgbAlarms: List<RgbAlarmDatabaseEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(rgbAlarm: RgbAlarmDatabaseEntity) {
        alarmList.removeIf { it.timeMinutesOfDay == rgbAlarm.timeMinutesOfDay }
    }

    override suspend fun delete(rgbAlarms: List<RgbAlarmDatabaseEntity>) {
        rgbAlarms.forEach { rgbAlarm ->
            alarmList.removeIf { it.timeMinutesOfDay == rgbAlarm.timeMinutesOfDay }
        }
    }

    override suspend fun deleteByTime(timeMinutesOfDay: Int) {
        alarmList.removeIf { it.timeMinutesOfDay == timeMinutesOfDay }
    }

    override suspend fun activateRgbAlarmByTime(timeMinutesOfDay: Int) {
        val indexOfAlarmToUpdate =
            alarmList.indexOfFirst { it.timeMinutesOfDay == timeMinutesOfDay }

        val alarmExists = indexOfAlarmToUpdate > -1
        if (alarmExists) {
            val updatedAlarm = alarmList.removeAt(indexOfAlarmToUpdate).copy(activated = true)
            alarmList.add(updatedAlarm)
        }
    }

    override suspend fun deactivateRgbAlarmByTime(timeMinutesOfDay: Int) {
        val indexOfAlarmToUpdate =
            alarmList.indexOfFirst { it.timeMinutesOfDay == timeMinutesOfDay }

        val alarmExists = indexOfAlarmToUpdate > -1
        if (alarmExists) {
            val updatedAlarm = alarmList.removeAt(indexOfAlarmToUpdate).copy(activated = false)
            alarmList.add(updatedAlarm)
        }
    }
}