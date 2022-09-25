package com.myrgb.ledcontroller.persistence.rgbalarm

import android.content.res.Resources
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import com.myrgb.ledcontroller.persistence.util.asDomainModel
import com.myrgb.ledcontroller.persistence.util.asDomainModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import java.time.LocalDateTime
import java.time.ZoneOffset

class DefaultRgbAlarmRepository(private val alarmDao: RgbAlarmDao) : RgbAlarmRepository {

    @ExperimentalCoroutinesApi
    override val alarms = alarmDao.observeAllAlarmsSortedByTime().mapLatest {
        val refreshedAlarms = disableExpiredOneTimeAlarms(it)
        alarmDao.insertOrUpdate(refreshedAlarms)
        refreshedAlarms.asDomainModels()
    }

    override suspend fun getNextActivatedAlarm() = alarmDao.getNextActivatedAlarm() ?: throw Resources.NotFoundException("There is no active alarm")

    override suspend fun getById(id: Int) =
        alarmDao.getById(id) ?: throw Resources.NotFoundException("Alarm with id $id not found")

    override suspend fun insertOrUpdate(rgbAlarm: RgbAlarm) {
        alarmDao.insertOrUpdate(rgbAlarm.asEntityDatabaseModel())
    }

    override suspend fun delete(rgbAlarm: RgbAlarm) {
        alarmDao.delete(rgbAlarm.asEntityDatabaseModel())
    }

    private fun disableExpiredOneTimeAlarms(alarms: List<RgbAlarmDatabaseEntity>): List<RgbAlarmDatabaseEntity> {
        return alarms.map { alarmDatabaseEntity ->
            val alarmDomainModel = alarmDatabaseEntity.asDomainModel()

            if (alarmDomainModel.isOneTimeAlarm) {
                val dateTimeTheAlarmWasEnabledFor = LocalDateTime.ofEpochSecond(
                    alarmDatabaseEntity.dateTimeMillisTheAlarmWasActivatedFor,
                    0, ZoneOffset.UTC
                )
                val alarmIsExpired =
                    dateTimeTheAlarmWasEnabledFor.isBefore(alarmDomainModel.nextTriggerDateTime)

                if (alarmIsExpired)
                    return@map alarmDatabaseEntity.copy(activated = false)
            }
            alarmDatabaseEntity
        }
    }
}



































