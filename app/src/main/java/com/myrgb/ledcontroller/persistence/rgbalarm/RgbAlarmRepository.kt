package com.myrgb.ledcontroller.persistence.rgbalarm

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModels
import com.myrgb.ledcontroller.persistence.util.asDomainModel
import com.myrgb.ledcontroller.persistence.util.asDomainModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RgbAlarmRepository @Inject constructor(
    private val alarmDao: RgbAlarmDao
) {
    @ExperimentalCoroutinesApi
    val alarms: Flow<List<RgbAlarm>> = alarmDao.observeAllAlarmsSortedByTime()
        .onEach { alarms -> alarms.forEach { deactivateIfExpiredOneTimeAlarm(it) } }
        .mapLatest { alarms -> alarms.asDomainModels() }


    suspend fun getByTime(timeMinutesOfDay: Int) = try {
        alarmDao.getByTime(timeMinutesOfDay).asDomainModel()
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("RgbAlarm with time $timeMinutesOfDay does not exist")
    }

    suspend fun getNextActiveAlarm(): RgbAlarm? {
        deactivateAllExpiredOneTimeAlarms()

        val allActiveAlarms = alarmDao.getAllActiveAlarms()
        if (allActiveAlarms.isEmpty())
            return null

        return allActiveAlarms.asDomainModels().minByOrNull { it.nextTriggerDateTime }
    }

    suspend fun insertOrReplace(rgbAlarm: RgbAlarm) {
        alarmDao.insertOrReplace(rgbAlarm.asEntityDatabaseModel())
    }

    suspend fun delete(rgbAlarm: RgbAlarm) {
        alarmDao.delete(rgbAlarm.asEntityDatabaseModel())
    }

    suspend fun delete(rgbAlarms: List<RgbAlarm>) {
        alarmDao.delete(rgbAlarms.asEntityDatabaseModels())
    }

    suspend fun deleteByTime(timeMinutesOfDay: Int) {
        alarmDao.deleteByTime(timeMinutesOfDay)
    }

    suspend fun activateRgbAlarm(rgbAlarm: RgbAlarm) {
        alarmDao.activateRgbAlarmByTime(
            rgbAlarm.timeMinutesOfDay, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )
    }

    suspend fun deactivateRgbAlarm(rgbAlarm: RgbAlarm) {
        alarmDao.deactivateRgbAlarmByTime(rgbAlarm.timeMinutesOfDay)
    }

    private suspend fun deactivateAllExpiredOneTimeAlarms() {
        alarmDao.getAllActiveAlarms().forEach { deactivateIfExpiredOneTimeAlarm(it) }
    }

    private suspend fun deactivateIfExpiredOneTimeAlarm(alarmDatabaseEntity: RgbAlarmDatabaseEntity) {
        val alarmDomainModel = alarmDatabaseEntity.asDomainModel()

        if (alarmDomainModel.isOneTimeAlarm && alarmDomainModel.activated) {
            val dateTimeAlarmWasActivatedFor = alarmDomainModel.lastTimeActivated?.let {
                alarmDomainModel.getNextTriggerDateTimeFrom(it)
            } ?: return

            val currentDateTime = LocalDateTime.now(RgbAlarm.clock)
            val alarmExpired = dateTimeAlarmWasActivatedFor.isBefore(currentDateTime)
                    || dateTimeAlarmWasActivatedFor.isEqual(currentDateTime)
            if (alarmExpired)
                alarmDao.deactivateRgbAlarmByTime(alarmDatabaseEntity.timeMinutesOfDay)
        }
    }
}




































