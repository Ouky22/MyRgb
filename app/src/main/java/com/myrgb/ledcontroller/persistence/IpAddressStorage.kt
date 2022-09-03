package com.myrgb.ledcontroller.persistence

interface IpAddressStorage {
    fun addIpAddress(ipAddress: String)
    fun removeIpAddress(ipAddress: String)
    fun getIpAddresses(): Set<String>
}