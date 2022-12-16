package com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver.di

import com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver.RebootReceiver
import com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver.RgbAlarmReceiver
import dagger.Subcomponent

@Subcomponent
interface RgbAlarmBroadcastComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): RgbAlarmBroadcastComponent
    }

    fun inject(rgbAlarmReceiver: RgbAlarmReceiver)
    fun inject(rebootReceiver: RebootReceiver)
}