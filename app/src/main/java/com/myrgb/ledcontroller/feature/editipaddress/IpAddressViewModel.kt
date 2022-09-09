package com.myrgb.ledcontroller.feature.editipaddress

import android.net.InetAddresses
import android.os.Build
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrgb.ledcontroller.persistence.IpAddressStorage
import javax.inject.Inject

class IpAddressViewModel @Inject constructor(private val ipAddressStorage: IpAddressStorage) : ViewModel() {
    private val _ipAddresses = MutableLiveData(ipAddressStorage.getIpAddresses().toList())
    val ipAddresses: LiveData<List<String>>
        get() = _ipAddresses

    fun removeIpAddress(ipAddress: String) {
        ipAddressStorage.removeIpAddress(ipAddress)
        _ipAddresses.value = ipAddressStorage.getIpAddresses().toList()
    }

    fun addIpAddress(ipAddress: String) {
        ipAddressStorage.addIpAddress(ipAddress)
        _ipAddresses.value = ipAddressStorage.getIpAddresses().toList()
    }

    fun isValidIpAddress(text: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            InetAddresses.isNumericAddress(text)
        else
            Patterns.IP_ADDRESS.matcher(text).matches()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val ipAddressStorage: IpAddressStorage) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (IpAddressViewModel(ipAddressStorage)) as T
    }
}