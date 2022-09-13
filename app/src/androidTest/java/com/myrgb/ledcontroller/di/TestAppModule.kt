package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.network.DefaultRgbRequestRepository
import com.myrgb.ledcontroller.network.FakeRgbRequestService
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.network.RgbRequestService
import com.myrgb.ledcontroller.persistence.ipaddress.FakeIpAddressSettingsRepository
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TestAppModule {
    @Singleton
    @Binds
    abstract fun bindRgbRequestRepository(rgbRequestRepository: DefaultRgbRequestRepository): RgbRequestRepository

    @Singleton
    @Binds
    abstract fun bindIpAddressSettingsRepository(repository: FakeIpAddressSettingsRepository): IpAddressSettingsRepository

    @Singleton
    @Binds
    abstract fun bindRgbRequestService(service: FakeRgbRequestService): RgbRequestService
}