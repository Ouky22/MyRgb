package com.myrgb.ledcontroller.feature.rgbalarmclock

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel

class RgbAlarmRepository(private val alarmDao: RgbAlarmDao) {

    suspend fun getNextActivatedAlarm() {
        val activeAlarms = alarmDao.getAllActiveAlarms()
        // TODO sort and get next alarm. Check if trigger date-time is not expired

    }

    fun getAllSortedByTriggerTime() = alarmDao.getAllSortedByTriggerTime()

    suspend fun getById(id: Int) = alarmDao.getById(id)

    suspend fun insert(rgbAlarm: RgbAlarm) {
        alarmDao.insert(rgbAlarm.asEntityDatabaseModel())
    }

    suspend fun update(rgbAlarm: RgbAlarm) {
        alarmDao.update(rgbAlarm.asEntityDatabaseModel())
    }

    suspend fun delete(rgbAlarm: RgbAlarm) {
        alarmDao.delete(rgbAlarm.asEntityDatabaseModel())
    }
}