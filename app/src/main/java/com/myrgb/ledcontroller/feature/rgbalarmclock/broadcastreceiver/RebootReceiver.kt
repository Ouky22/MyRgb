package com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * When the device shuts down, all alarms scheduled by AlarmManager are canceled.
 * Therefore this BroadcastReceiver is needed to reschedule RgbAlarms.
 */
class RebootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var rgbAlarmScheduler: RgbAlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.intent.action.BOOT_COMPLETED")
            return

        (context.applicationContext as App).appComponent.rgbAlarmBroadcastComponent().create()
            .inject(this)

        val pendingResult = goAsync()
        CoroutineScope(IO).launch {
            try {
                rgbAlarmScheduler.scheduleNextAlarmIfExists()
            } finally {
                pendingResult.finish()
            }
        }
    }
}