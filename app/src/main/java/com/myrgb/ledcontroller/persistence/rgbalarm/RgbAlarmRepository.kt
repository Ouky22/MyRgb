package com.myrgb.ledcontroller.persistence.rgbalarm

import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.util.asEntityDatabaseModel
import com.myrgb.ledcontroller.persistence.util.asDomainModel
import com.myrgb.ledcontroller.persistence.util.asDomainModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RgbAlarmRepository @Inject constructor(
    private val alarmDao: RgbAlarmDao
) {

    @ExperimentalCoroutinesApi
    val alarms = alarmDao.observeAllAlarmsSortedByTime().mapLatest {
        val refreshedAlarms = disableExpiredOneTimeAlarms(it)
        alarmDao.insertOrIgnore(refreshedAlarms)
        refreshedAlarms.asDomainModels()
    }

    suspend fun getNextActivatedAlarm() = try {
        alarmDao.getNextActivatedAlarm().asDomainModel()
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("There is no active alarm")
    }

    suspend fun getByTime(timeMinutesOfDay: Int) = try {
        alarmDao.getByTime(timeMinutesOfDay).asDomainModel()
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("RgbAlarm with time $timeMinutesOfDay does not exist")
    }

    suspend fun insertOrReplace(rgbAlarm: RgbAlarm) {
        alarmDao.insertOrReplace(rgbAlarm.asEntityDatabaseModel())
    }

    suspend fun delete(rgbAlarm: RgbAlarm) {
        alarmDao.delete(rgbAlarm.asEntityDatabaseModel())
    }

    suspend fun deleteByTime(timeMinutesOfDay: Int) {
        alarmDao.deleteByTime(timeMinutesOfDay)
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



































