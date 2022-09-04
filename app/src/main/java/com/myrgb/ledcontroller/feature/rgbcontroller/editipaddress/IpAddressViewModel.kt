package com.myrgb.ledcontroller.feature.rgbcontroller.editipaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myrgb.ledcontroller.persistence.IpAddressStorage

class IpAddressViewModel(private val ipAddressStorage: IpAddressStorage) : ViewModel() {
    private val _ipAddresses = MutableLiveData(ipAddressStorage.getIpAddresses().toList())
    val ipAddresses: LiveData<List<String>>
        get() = _ipAddresses

    fun removeIpAddress(ipAddress: String) {
        ipAddressStorage.removeIpAddress(ipAddress)
    }

    fun addIpAddress(ipAddress: String) {
        ipAddressStorage.addIpAddress(ipAddress)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val ipAddressStorage: IpAddressStorage) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (IpAddressViewModel(ipAddressStorage)) as T
    }
}