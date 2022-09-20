package com.myrgb.ledcontroller.feature.ipsettings.addedit

import android.net.InetAddresses
import android.os.Build
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.myrgb.ledcontroller.IpAddressNamePair
import com.myrgb.ledcontroller.R
import com.myrgb.ledcontroller.persistence.ipaddress.DefaultIpAddressSettingsRepository
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class IpAddressAddEditViewModel @Inject constructor(
    private val ipAddressSettingsRepository: IpAddressSettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(IpAddressAddEditState())
    val state: StateFlow<IpAddressAddEditState>
        get() = _state

    private var _selectedIpAddressNamePair: IpAddressNamePair? = null

    private val inEditingMode: Boolean
        get() = _selectedIpAddressNamePair != null


    fun updateIpAddressTextInput(input: String) {
        _state.value = _state.value.copy(
            ipAddress = input,
            ipAddressErrorMessageId = -1
        )
    }

    fun updateIpAddressNameTextInput(input: String) {
        _state.value = _state.value.copy(
            ipAddressName = input,
            ipAddressNameErrorMessageId = -1
        )
    }

    fun setSelectedIpAddressNamePair(ipAddress: String) {
        viewModelScope.launch {
            ipAddressSettingsRepository.getIpAddressNamePairByIpAddress(ipAddress)?.let {
                _selectedIpAddressNamePair = it
                _state.value = _state.value.copy(
                    ipAddress = it.ipAddress,
                    ipAddressName = it.name
                )
            }
        }
    }

    fun submitIpAddressSettingsData() {
        val ipAddressValidationResult = validateIpAddressTextInput(state.value.ipAddress)
        val ipAddressNameValidationResult =
            validateIpAddressNameTextInput(state.value.ipAddressName)

        _state.value = _state.value.copy(
            ipAddressErrorMessageId = ipAddressValidationResult.errorMessageId,
            ipAddressNameErrorMessageId = ipAddressNameValidationResult.errorMessageId
        )

        if (ipAddressValidationResult.isSuccessful && ipAddressNameValidationResult.isSuccessful)
            saveIpAddressNamePair()
    }

    private fun saveIpAddressNamePair() {
        if (inEditingMode)
            updateIpAddressNamePair()
        else
            addIpAddressNamePair()
    }

    private fun addIpAddressNamePair() {
        viewModelScope.launch {
            ipAddressSettingsRepository.addIpAddressNamePair(
                state.value.ipAddress,
                state.value.ipAddressName
            )
            _state.value = _state.value.copy(ipAddressNamePairSuccessfullySaved = true)
        }
    }

    private fun updateIpAddressNamePair() {
        if (selectedIpAddressNamePairNotEdited()) {
            _state.value = _state.value.copy(ipAddressNamePairSuccessfullySaved = true)
            return
        }

        viewModelScope.launch {
            ipAddressSettingsRepository.updateIpAddressNamePair(
                oldIpAddress = _selectedIpAddressNamePair!!.ipAddress,
                newIpAddress = state.value.ipAddress,
                newIpName = state.value.ipAddressName
            )
            _state.value = _state.value.copy(ipAddressNamePairSuccessfullySaved = true)
        }
    }

    private fun selectedIpAddressNamePairNotEdited(): Boolean =
        state.value.ipAddress == _selectedIpAddressNamePair?.ipAddress
                && state.value.ipAddressName == _selectedIpAddressNamePair?.name

    private fun validateIpAddressTextInput(text: String?): ValidationResult {
        return if (text == null || text.isBlank())
            ValidationResult(false, R.string.no_ip_entered)
        else if (ipAddressAlreadyExists(text))
            ValidationResult(false, R.string.ip_already_exists)
        else if (ipAddressIsValid(text))
            ValidationResult(true)
        else
            ValidationResult(false, R.string.invalid_ip)
    }

    private fun validateIpAddressNameTextInput(text: String?): ValidationResult {
        return if (ipAddressNameIsValid(text))
            ValidationResult(true)
        else
            ValidationResult(false, R.string.no_name_entered)
    }

    private fun ipAddressAlreadyExists(ipAddress: String): Boolean {
        if (inEditingMode && ipAddress == _selectedIpAddressNamePair?.ipAddress)
            return false

        var containsIp: Boolean
        runBlocking {
            val deferred = viewModelScope.async {
                ipAddressSettingsRepository.existsIpAddressNamePairWithIpAddress(ipAddress)
            }
            containsIp = deferred.await()
        }
        return containsIp
    }

    private fun ipAddressIsValid(text: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            InetAddresses.isNumericAddress(text)
        else
            PatternsCompat.IP_ADDRESS.matcher(text).matches()

    private fun ipAddressNameIsValid(text: String?) = text != null && text.isNotBlank()

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val ipAddressSettingsRepository: DefaultIpAddressSettingsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (IpAddressAddEditViewModel(ipAddressSettingsRepository)) as T
    }
}
