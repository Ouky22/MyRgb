package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.network.FakeRgbRequestRepository
import com.myrgb.ledcontroller.network.FakeRgbRequestService
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.network.RgbRequestService
import com.myrgb.ledcontroller.persistence.FakeIpAddressStorage
import com.myrgb.ledcontroller.persistence.IpAddressStorage
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TestAppModule {
    @Singleton
    @Binds
    abstract fun bindRgbRequestRepository(rgbRequestRepository: FakeRgbRequestRepository): RgbRequestRepository

    @Singleton
    @Binds
    abstract fun bindIpAddressStorage(storage: FakeIpAddressStorage): IpAddressStorage

    @Singleton
    @Binds
    abstract fun bindRgbRequestService(service: FakeRgbRequestService): RgbRequestService
}