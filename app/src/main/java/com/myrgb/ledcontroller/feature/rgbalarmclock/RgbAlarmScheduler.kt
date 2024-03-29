package com.myrgb.ledcontroller.feature.rgbalarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver.RebootReceiver
import com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver.RgbAlarmReceiver
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

const val RGB_ALARM_COLOR_EXTRA_NAME = "rgb_alarm_color"
const val RGB_ALARM_ID_EXTRA_NAME = "rgb_alarm_id"

@Singleton
class RgbAlarmScheduler @Inject constructor(
    private val rgbAlarmRepository: RgbAlarmRepository,
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    suspend fun scheduleNextAlarmIfExists() {
        val nextAlarm = rgbAlarmRepository.getNextActiveAlarm()

        if (nextAlarm == null) {
            cancelActiveRgbAlarm()
            deactivateAlarmRestartReceiver()
            return
        }

        scheduleNextAlarmWithAlarmManager(nextAlarm)
        activateAlarmRestartReceiver()
    }

    private fun scheduleNextAlarmWithAlarmManager(nextAlarm: RgbAlarm) {
        val triggerDateTimeMillis =
            nextAlarm.nextTriggerDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(triggerDateTimeMillis, null),
            getRgbAlarmIntent(nextAlarm)
        )
    }

    private fun activateAlarmRestartReceiver() {
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context.applicationContext, RebootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun deactivateAlarmRestartReceiver() {
        context.packageManager.setComponentEnabledSetting(
            ComponentName(context.applicationContext, RebootReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun cancelActiveRgbAlarm() {
        alarmManager.cancel(getRgbAlarmIntent())
    }

    private fun getRgbAlarmIntent(rgbAlarm: RgbAlarm? = null): PendingIntent =
        PendingIntent.getBroadcast(
            context.applicationContext,
            0,
            Intent(context.applicationContext, RgbAlarmReceiver::class.java).apply {
                rgbAlarm?.let {
                    putExtra(RGB_ALARM_COLOR_EXTRA_NAME, it.color)
                    putExtra(RGB_ALARM_ID_EXTRA_NAME, it.timeMinutesOfDay)
                }
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}