package com.myrgb.ledcontroller.feature.ipsettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class IpAddressListViewModel @Inject constructor(private val ipAddressSettingsRepository: IpAddressSettingsRepository) :
    ViewModel() {
    private val _ipAddressNamePairs = MutableStateFlow<List<IpAddressNamePair>>(emptyList())
    val ipAddressNamePairs: StateFlow<List<IpAddressNamePair>>
        get() = _ipAddressNamePairs

    init {
        viewModelScope.launch {
            ipAddressSettingsRepository.ipAddressSettings.collect { ipAddressSettings ->
                _ipAddressNamePairs.value = ipAddressSettings.ipAddressNamePairsList
            }
        }
    }

    fun removeIpAddressNamePair(ipAddressNamePair: IpAddressNamePair) {
        viewModelScope.launch {
            ipAddressSettingsRepository.removeIpAddressNamePair(ipAddressNamePair.ipAddress)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val ipAddressSettingsRepository: IpAddressSettingsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (IpAddressListViewModel(ipAddressSettingsRepository)) as T
    }
}