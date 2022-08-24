package com.example.ledcontroller.repository

import android.app.Application
import androidx.compose.runtime.collectAsState
import com.example.ledcontroller.App
import com.example.ledcontroller.model.RgbAlarm
import kotlinx.coroutines.flow.collect

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