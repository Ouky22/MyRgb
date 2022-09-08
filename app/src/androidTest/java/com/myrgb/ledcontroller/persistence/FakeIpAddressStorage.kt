package com.myrgb.ledcontroller.persistence

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeIpAddressStorage @Inject constructor() : IpAddressStorage {
    private val ipAddresses = mutableListOf<String>()

    fun deleteAllIpAddresses() {
        ipAddresses.clear()
    }

    override fun addIpAddress(ipAddress: String) {
        ipAddresses.add(ipAddress)
    }

    override fun removeIpAddress(ipAddress: String) {
        ipAddresses.remove(ipAddress)
    }

    override fun getIpAddresses(): Set<String> {
        return ipAddresses.toSet()
    }

    override fun setOnIpAddressesChangedCallback(callback: () -> Unit) {}

    override fun removeOnIpAddressesChangedCallback() {}
}