package com.myrgb.ledcontroller.persistence

import android.content.SharedPreferences

interface IpAddressStorage {
    fun addIpAddress(ipAddress: String)
    fun removeIpAddress(ipAddress: String)
    fun getIpAddresses(): Set<String>
    fun registerOnSharedPreferencesChangedListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun unregisterOnSharedPreferencesChangedListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
}