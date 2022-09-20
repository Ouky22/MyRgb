package com.myrgb.ledcontroller.persistence.ipaddress

import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.IpAddressSettings
import kotlinx.coroutines.flow.Flow

interface IpAddressSettingsRepository {
    val ipAddressSettings: Flow<IpAddressSettings>
    suspend fun getIpAddressNamePairByIpAddress(ipAddress: String): IpAddressNamePair?
    suspend fun addIpAddressNamePair(ipAddressNamePair: IpAddressNamePair)
    suspend fun addIpAddressNamePair(ipAddress: String, ipName: String)
    suspend fun updateIpAddressNamePair(
        oldIpAddress: String, newIpAddress: String, newIpName: String
    )
    suspend fun removeIpAddressNamePair(ipAddress: String)
    suspend fun existsIpAddressNamePairWithIpAddress(ipAddress: String): Boolean
}