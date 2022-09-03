package com.myrgb.ledcontroller.persistence

class FakeIpAddressStorage : IpAddressStorage {
    private val ipAddresses = mutableListOf<String>()

    override fun addIpAddress(ipAddress: String) {
        ipAddresses.add(ipAddress)
    }

    override fun removeIpAddress(ipAddress: String) {
        ipAddresses.remove(ipAddress)
    }

    override fun getIpAddresses(): Set<String> {
        return ipAddresses.toSet()
    }
}