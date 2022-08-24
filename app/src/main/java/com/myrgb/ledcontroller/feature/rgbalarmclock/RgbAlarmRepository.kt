package com.myrgb.ledcontroller.feature.rgbalarmclock

import android.app.Application
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.domain.RgbAlarm

class RgbAlarmRepository(application: Application) {

    private val alarmDao = (application as App).ledControllerDatabase.rgbAlarmDao

    suspend fun getNextActivatedAlarm() {
        val activeAlarms = alarmDao.getAllActiveAlarms()
        // TODO sort and get next alarm. Check if trigger date-time is not expired

    }

    fun getAllSortedByTriggerTime() = alarmDao.getAllSortedByTriggerTime()

    suspend fun getById(id: Int) = alarmDao.getById(id)

    suspend fun insert(rgbAlarm: RgbAlarm) {
        alarmDao.insert(rgbAlarm)
    }

    suspend fun update(rgbAlarm: RgbAlarm) {
        alarmDao.update(rgbAlarm)
    }

    suspend fun delete(rgbAlarm: RgbAlarm) {
        alarmDao.delete(rgbAlarm)
    }
}