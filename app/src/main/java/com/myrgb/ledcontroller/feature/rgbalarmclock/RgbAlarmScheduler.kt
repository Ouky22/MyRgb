package com.myrgb.ledcontroller.feature.rgbalarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.feature.rgbalarmclock.alarmbrodacastreceiver.RgbAlarmReceiver
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RgbAlarmScheduler @Inject constructor(
    private val rgbAlarmRepository: RgbAlarmRepository,
    private val context: Context
) {
    suspend fun scheduleNextAlarm() {
        val nextAlarm = rgbAlarmRepository.getNextActiveAlarm()

        if (nextAlarm == null) {
            deactivateAlarmRestartReceiver()
            return
        }

        scheduleNextAlarmWithAlarmManager(nextAlarm)
        activateAlarmRestartReceiver()
    }

    private fun scheduleNextAlarmWithAlarmManager(nextAlarm: RgbAlarm) {
        val alarmIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            0,
            Intent(context.applicationContext, RgbAlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val triggerDateTimeMillis =
            nextAlarm.nextTriggerDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, triggerDateTimeMillis, alarmIntent
        )
    }

    private fun activateAlarmRestartReceiver() {
    }

    private fun deactivateAlarmRestartReceiver() {
    }
}