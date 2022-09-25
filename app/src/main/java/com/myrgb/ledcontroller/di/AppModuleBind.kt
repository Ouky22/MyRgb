package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.persistence.ipaddress.DefaultIpAddressSettingsRepository
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import com.myrgb.ledcontroller.persistence.rgbalarm.DefaultRgbAlarmRepository
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModuleBind {
    @Singleton
    @Binds
    abstract fun bindIpAddressSettingsRepository(
        ipAddressSettingsRepository: DefaultIpAddressSettingsRepository
    ): IpAddressSettingsRepository

    @Singleton
    @Binds
    abstract fun bindRgbAlarmRepository(
        rgbAlarmRepository: DefaultRgbAlarmRepository
    ): RgbAlarmRepository
}