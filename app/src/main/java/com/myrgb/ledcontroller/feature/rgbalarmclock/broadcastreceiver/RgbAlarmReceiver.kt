package com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.feature.rgbalarmclock.RGB_ALARM_EXTRA_NAME
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmScheduler
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.persistence.ipaddress.DefaultIpAddressSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class RgbAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var rgbAlarmScheduler: RgbAlarmScheduler

    @Inject
    lateinit var ipAddressSettingsRepository: DefaultIpAddressSettingsRepository

    @Inject
    lateinit var rgbRequestRepository: RgbRequestRepository


    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.rgbAlarmBroadcastComponent().create()
            .inject(this)

        val pendingResult = goAsync()
        CoroutineScope(IO).launch {
            try {
                val alarmColor =
                    intent.getParcelableExtra<RgbTriplet>(RGB_ALARM_EXTRA_NAME) ?: return@launch
                val ipAddressSettings =
                    ipAddressSettingsRepository.ipAddressSettings.firstOrNull() ?: return@launch

                ipAddressSettings.ipAddressNamePairsList.forEach { ipAddressNamePair ->
                    rgbRequestRepository.setColor(ipAddressNamePair.ipAddress, alarmColor)
                    rgbRequestRepository.triggerRgbAlarm(ipAddressNamePair.ipAddress)
                }

                rgbAlarmScheduler.scheduleNextAlarmIfExists()
            } finally {
                pendingResult.finish()
            }
        }
    }
}