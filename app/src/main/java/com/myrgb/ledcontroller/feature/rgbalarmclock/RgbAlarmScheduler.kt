package com.myrgb.ledcontroller.feature.rgbalarmclock

import android.content.Context
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository

class RgbAlarmScheduler(
    private val rgbAlarmRepository: RgbAlarmRepository // TODO USE DI
) {
    suspend fun scheduleNextAlarm(context: Context) {
        val nextAlarm = rgbAlarmRepository.getNextActiveAlarm()

        if (nextAlarm == null) {
            deactivateAlarmRestartReceiver()
            return
        }

        scheduleNextAlarmWithAlarmManager(nextAlarm, context)
        activateAlarmRestartReceiver()
    }

    private fun scheduleNextAlarmWithAlarmManager(nextAlarm: RgbAlarm, context: Context) {
        TODO("Not yet implemented")
    }

    private fun activateAlarmRestartReceiver() {
        TODO("Not yet implemented")
    }

    private fun deactivateAlarmRestartReceiver() {
        TODO("Not yet implemented")
    }
}