package com.myrgb.ledcontroller.persistence

import android.content.Context
import android.content.SharedPreferences
import com.myrgb.ledcontroller.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class handles the access to the shared preferences which store the ip addresses of
 * the rgb controller.
 */
@Singleton
class DefaultIpAddressStorage @Inject constructor(context: Context) : IpAddressStorage {
    private val ipAddressSharedPreferences = context.getSharedPreferences(
        context.getString(R.string.ip_addresses_preferences_file_key),
        Context.MODE_PRIVATE
    )

    private var onSharedPreferenceChangeListener:
            SharedPreferences.OnSharedPreferenceChangeListener? = null

    private val ipAddressesKey = "IP_ADDRESSES"

    override fun addIpAddress(ipAddress: String) {
        val currentIpAddresses = getIpAddresses().toMutableSet()
        val newIpAddress = currentIpAddresses.add(ipAddress)

        if (newIpAddress)
            with(ipAddressSharedPreferences.edit()) {
                putStringSet(ipAddressesKey, currentIpAddresses)
                apply()
            }
    }

    override fun removeIpAddress(ipAddress: String) {
        val currentIpAddresses = getIpAddresses().toMutableSet()
        val containsIpAddress = currentIpAddresses.remove(ipAddress)

        if (containsIpAddress)
            with(ipAddressSharedPreferences.edit()) {
                putStringSet(ipAddressesKey, currentIpAddresses)
                apply()
            }
    }

    override fun getIpAddresses() =
        // convert to read-only set because instance returned by getStringSet must not be modified (see docs)
        ipAddressSharedPreferences.getStringSet(ipAddressesKey, setOf())?.toSet() ?: setOf()

    override fun setOnIpAddressesChangedCallback(callback: () -> Unit) {
        onSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> callback.invoke() }

        ipAddressSharedPreferences.registerOnSharedPreferenceChangeListener(
            onSharedPreferenceChangeListener
        )
    }

    override fun removeOnIpAddressesChangedCallback() {
        ipAddressSharedPreferences.unregisterOnSharedPreferenceChangeListener(
            onSharedPreferenceChangeListener
        )
    }
}