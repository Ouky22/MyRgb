package com.myrgb.ledcontroller.persistence

import android.content.Context
import com.myrgb.ledcontroller.R

/**
 * This class handles the access to the shared preferences which store the ip addresses of
 * the rgb controller.
 */
class IpAddressStorage(context: Context) {

    private val ipAddressSharedPreferences = context.getSharedPreferences(
        context.getString(R.string.ip_addresses_preferences_file_key),
        Context.MODE_PRIVATE
    )

    private val ipAddressesKey = "IP_ADDRESSES"

    fun addIpAddress(ipAddress: String) {
        val currentIpAddresses = getIpAddresses().toMutableSet()
        val newIpAddress = currentIpAddresses.add(ipAddress)

        if (newIpAddress)
            with(ipAddressSharedPreferences.edit()) {
                putStringSet(ipAddressesKey, currentIpAddresses)
                apply()
            }
    }

    fun removeIpAddress(ipAddress: String) {
        val currentIpAddresses = getIpAddresses().toMutableSet()
        val containsIpAddress = currentIpAddresses.remove(ipAddress)

        if (containsIpAddress)
            with(ipAddressSharedPreferences.edit()) {
                putStringSet(ipAddressesKey, currentIpAddresses)
                apply()
            }

    }

    fun getIpAddresses() =
        // convert to read-only set because instance returned by getStringSet must not be modified (see docs)
        ipAddressSharedPreferences.getStringSet(ipAddressesKey, setOf())?.toSet() ?: setOf()

}