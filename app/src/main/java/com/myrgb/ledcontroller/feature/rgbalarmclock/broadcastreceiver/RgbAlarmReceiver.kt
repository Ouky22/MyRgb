package com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.feature.rgbalarmclock.RGB_ALARM_COLOR_EXTRA_NAME
import com.myrgb.ledcontroller.feature.rgbalarmclock.RGB_ALARM_ID_EXTRA_NAME
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmScheduler
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class RgbAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var rgbAlarmScheduler: RgbAlarmScheduler

    @Inject
    lateinit var ipAddressSettingsRepository: IpAddressSettingsRepository

    @Inject
    lateinit var rgbRequestRepository: RgbRequestRepository

    @Inject
    lateinit var rgbAlarmRepository: RgbAlarmRepository


    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.rgbAlarmBroadcastComponent().create()
            .inject(this)

        val pendingResult = goAsync()
        CoroutineScope(IO).launch {
            try {
                val alarmId = intent.getIntExtra(RGB_ALARM_ID_EXTRA_NAME, -1)
                if (alarmId == -1)
                    return@launch

                val alarmColor = intent.getParcelableExtra<RgbTriplet>(RGB_ALARM_COLOR_EXTRA_NAME)
                    ?: return@launch

                val ipAddressSettings = ipAddressSettingsRepository.ipAddressSettings.firstOrNull()
                    ?: return@launch

                ipAddressSettings.ipAddressNamePairsList.forEach { ipAddressNamePair ->
                    rgbRequestRepository.setColor(ipAddressNamePair.ipAddress, alarmColor)
                    rgbRequestRepository.triggerRgbAlarm(ipAddressNamePair.ipAddress)
                }

                val alarm = rgbAlarmRepository.getByTime(alarmId)
                if (alarm.isOneTimeAlarm)
                    rgbAlarmRepository.deactivateRgbAlarmById(alarmId)
                
                rgbAlarmScheduler.scheduleNextAlarmIfExists()
            } finally {
                pendingResult.finish()
            }
        }
    }
}