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

class RgbAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var rgbAlarmScheduler: RgbAlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.rgbAlarmBroadcastComponent().create()
            .inject(this)

        val pendingResult = goAsync()
        CoroutineScope(IO).launch {
            try {
                Log.d("testing", "rgb alarm received")

                rgbAlarmScheduler.scheduleNextAlarmIfExists()
            } finally {
                pendingResult.finish()
            }
        }
    }
}