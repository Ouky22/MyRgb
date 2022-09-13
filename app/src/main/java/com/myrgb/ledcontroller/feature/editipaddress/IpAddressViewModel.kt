package com.myrgb.ledcontroller.feature.editipaddress

import android.net.InetAddresses
import android.os.Build
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class IpAddressViewModel @Inject constructor(private val ipAddressSettingsRepository: IpAddressSettingsRepository) :
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

    fun addIpAddressNamePair(ipAddress: String, name: String) {
        if (validateIpAddressTextInput(ipAddress) !is Success || validateIpAddressNameTextInput(name) !is Success)
            return

        viewModelScope.launch {
            ipAddressSettingsRepository.addIpAddressNamePair(ipAddress, name)
        }
    }

    fun removeIpAddressNamePair(ipAddressNamePair: IpAddressNamePair) {
        viewModelScope.launch {
            ipAddressSettingsRepository.removeIpAddressNamePair(ipAddressNamePair)
        }
    }

    fun validateIpAddressTextInput(text: String?): TextValidationResult {
        return if (text == null || text.isBlank())
            Failure(R.string.no_ip_entered)
        else if (ipAddressAlreadyExists(text))
            Failure(R.string.ip_already_exists)
        else if (ipAddressIsValid(text))
            Success
        else
            Failure(R.string.invalid_ip)
    }

    fun validateIpAddressNameTextInput(text: String?): TextValidationResult {
        return if (ipAddressNameIsValid(text))
            Success
        else
            Failure(R.string.no_name_entered)
    }

    private fun ipAddressAlreadyExists(ipAddress: String) =
        _ipAddressNamePairs.value.any { it.ipAddress == ipAddress }

    private fun ipAddressIsValid(text: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            InetAddresses.isNumericAddress(text)
        else
            PatternsCompat.IP_ADDRESS.matcher(text).matches()

    private fun ipAddressNameIsValid(text: String?) = text != null && text.isNotBlank()

    @Suppress("UNCHECKED_CAST")
    class Factory(private val ipAddressSettingsRepository: IpAddressSettingsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (IpAddressViewModel(ipAddressSettingsRepository)) as T
    }
}

interface TextValidationResult
object Success : TextValidationResult
data class Failure(@StringRes val messageResourceId: Int) : TextValidationResult