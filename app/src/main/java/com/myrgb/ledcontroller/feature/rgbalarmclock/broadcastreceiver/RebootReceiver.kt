package com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * When the device shuts down, all alarms scheduled by AlarmManager are canceled.
 * Therefore this BroadcastReceiver is needed to reschedule RgbAlarms.
 */
class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.intent.action.BOOT_COMPLETED")
            return

        Log.d("testing", "reboot receiver")
    }
}