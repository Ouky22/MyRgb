package com.myrgb.ledcontroller.feature.rgbshow

import androidx.lifecycle.*
import com.myrgb.ledcontroller.domain.Esp32Microcontroller
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.persistence.IpAddressStorage
import kotlinx.coroutines.launch

class RgbShowViewModel(
    private val rgbRequestRepository: RgbRequestRepository,
    private val ipAddressStorage: IpAddressStorage
) : ViewModel() {
    private val _rgbShowActive = MutableLiveData<Boolean>()
    val rgbShowActive: LiveData<Boolean>
        get() = _rgbShowActive

    private val _currentRgbShowSpeed = MutableLiveData<Int>()
    val currentRgbShowSpeed: LiveData<Int>
        get() = _currentRgbShowSpeed

    private val esp32Microcontroller = mutableListOf<Esp32Microcontroller>()

    val MAX_RGB_SHOW_SPEED = 10


    init {
        viewModelScope.launch { loadCurrentSettings() }
    }

    fun onStartStopRgbShowImageViewClick() {
        _rgbShowActive.value?.let { active ->

            if (active) {
                // viewModelScope.launch { rgbRequestRepository.stopRgbShow() } // TODO command not available yet
            } else {
                viewModelScope.launch {
                    val defaultSpeed = MAX_RGB_SHOW_SPEED / 2
                    esp32Microcontroller.forEach {
                        rgbRequestRepository.startRgbShow(
                            it,
                            currentRgbShowSpeed.value ?: defaultSpeed
                        )
                    }
                }
            }

            _rgbShowActive.value = !active
        }
    }

    fun onSpeedSeekBarProgressChanged(progress: Int, fromUser: Boolean) {
        if (!fromUser)
            return

        viewModelScope.launch {
            esp32Microcontroller.forEach {
                rgbRequestRepository.setRgbShowSpeed(it, progress)
            }
        }
        _currentRgbShowSpeed.value = progress
    }

    private suspend fun loadCurrentSettings() {
        ipAddressStorage.getIpAddresses().forEach { ipAddress ->
            esp32Microcontroller.add(Esp32Microcontroller(ipAddress, emptyList()))
        }

        for (esp32 in esp32Microcontroller) {
            val response = rgbRequestRepository.loadCurrentRgbSettings(esp32.ipAddress)
            if (response != null) {
                _rgbShowActive.value = response.isRgbShowActive
                break
            }
        }

        // TODO load current rgb show speed as soon it is queryable
        _currentRgbShowSpeed.value = MAX_RGB_SHOW_SPEED / 2
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val rgbRequestRepository: RgbRequestRepository,
        private val ipAddressStorage: IpAddressStorage
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RgbShowViewModel(rgbRequestRepository, ipAddressStorage) as T
    }
}




























