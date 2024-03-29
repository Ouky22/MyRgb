package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.ipsettings.addedit.di.IpAddressAddEditComponent
import com.myrgb.ledcontroller.feature.rgbcontroller.di.ControllerComponent
import com.myrgb.ledcontroller.feature.ipsettings.di.IpAddressComponent
import com.myrgb.ledcontroller.feature.rgbalarmclock.addedit.di.RgbAlarmAddEditComponent
import com.myrgb.ledcontroller.feature.rgbalarmclock.broadcastreceiver.di.RgbAlarmBroadcastComponent
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.di.RgbAlarmListComponent
import com.myrgb.ledcontroller.feature.rgbshow.di.RgbShowComponent
import dagger.Module

@Module(
    subcomponents = [
        ControllerComponent::class,
        IpAddressComponent::class,
        IpAddressAddEditComponent::class,
        RgbShowComponent::class,
        RgbAlarmListComponent::class,
        RgbAlarmAddEditComponent::class,
        RgbAlarmBroadcastComponent::class
    ]
)
class AppSubcomponentsModule