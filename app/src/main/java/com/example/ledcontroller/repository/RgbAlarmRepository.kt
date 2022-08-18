package com.example.ledcontroller.repository

import android.app.Application
import com.example.ledcontroller.App
import com.example.ledcontroller.model.RgbAlarm

class RgbAlarmRepository(application: Application) {

    private val alarmDao = (application as App).ledControllerDatabase.rgbAlarmDao

    suspend fun getNextActivatedAlarm() {
        // TODO
    }

    suspend fun getAllSortedByTriggerTime() = alarmDao.getAllSortedByTriggerTime()

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