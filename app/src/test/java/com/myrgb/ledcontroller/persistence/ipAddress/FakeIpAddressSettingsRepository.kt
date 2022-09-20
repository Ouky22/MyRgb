package com.myrgb.ledcontroller.persistence.ipAddress

import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.IpAddressSettings
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeIpAddressSettingsRepository : IpAddressSettingsRepository {
    private var currentIpAddressSettings = IpAddressSettings.getDefaultInstance()

    private val flow = MutableSharedFlow<IpAddressSettings>()

    override val ipAddressSettings: Flow<IpAddressSettings>
        get() = flow

    suspend fun emit(settings: IpAddressSettings) {
        currentIpAddressSettings = settings
        flow.emit(settings)
    }

    override suspend fun getIpAddressNamePairByIpAddress(ipAddress: String): IpAddressNamePair? =
        currentIpAddressSettings.ipAddressNamePairsList.firstOrNull { it.ipAddress == ipAddress }


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

    override suspend fun updateIpAddressNamePair(
        oldIpAddress: String, newIpAddress: String, newIpName: String
    ) {
        removeIpAddressNamePair(oldIpAddress)
        addIpAddressNamePair(newIpAddress, newIpName)
    }

    override suspend fun existsIpAddressNamePairWithIpAddress(ipAddress: String): Boolean {
        return currentIpAddressSettings.ipAddressNamePairsList.any { it.ipAddress == ipAddress }
    }

    override suspend fun removeIpAddressNamePair(ipAddress: String) {
        val ipAddressNamePairIndex = currentIpAddressSettings.ipAddressNamePairsList.indexOfFirst {
            it.ipAddress == ipAddress
        }
        currentIpAddressSettings = currentIpAddressSettings.toBuilder()
            .removeIpAddressNamePairs(ipAddressNamePairIndex)
            .build()
        emit(currentIpAddressSettings)
    }
}