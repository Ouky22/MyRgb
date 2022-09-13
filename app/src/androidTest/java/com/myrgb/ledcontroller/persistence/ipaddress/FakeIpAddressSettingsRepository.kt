package com.myrgb.ledcontroller.persistence.ipaddress

import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.IpAddressSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeIpAddressSettingsRepository @Inject constructor() : IpAddressSettingsRepository {
    private var currentIpAddressSettings = IpAddressSettings.getDefaultInstance()

    private val flow = MutableSharedFlow<IpAddressSettings>()

    suspend fun emit(settings: IpAddressSettings) {
        currentIpAddressSettings = settings
        flow.emit(settings)
    }

    override val ipAddressSettings: Flow<IpAddressSettings>
        get() = flow

    override suspend fun addIpAddressNamePair(ipAddressNamePair: IpAddressNamePair) {
        currentIpAddressSettings =
            currentIpAddressSettings.toBuilder().addIpAddressNamePairs(ipAddressNamePair).build()
        emit(currentIpAddressSettings)
    }

    override suspend fun addIpAddressNamePair(ipAddress: String, ipName: String) {
        addIpAddressNamePair(
            IpAddressNamePair.newBuilder().setName(ipName).setIpAddress(ipAddress).build()
        )
    }

    override suspend fun removeIpAddressNamePair(ipAddressNamePair: IpAddressNamePair) {
        val ipAddressNamePairIndex = currentIpAddressSettings.ipAddressNamePairsList.indexOfFirst {
            it == ipAddressNamePair
        }
        currentIpAddressSettings = currentIpAddressSettings.toBuilder()
            .removeIpAddressNamePairs(ipAddressNamePairIndex)
            .build()
        emit(currentIpAddressSettings)
    }
}