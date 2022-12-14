package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.persistence.ipaddress.DefaultIpAddressSettingsRepository
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
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
}