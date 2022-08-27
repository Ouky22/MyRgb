package com.myrgb.ledcontroller.feature.rgbshow

import androidx.lifecycle.*
import com.myrgb.ledcontroller.feature.rgbcontroller.DefaultControllerRepository
import kotlinx.coroutines.launch

class RgbShowViewModel(private val defaultControllerRepository: DefaultControllerRepository) : ViewModel() {
    private val _rgbShowActive = MutableLiveData<Boolean>()
    val rgbShowActive: LiveData<Boolean>
        get() = _rgbShowActive

    private val _currentRgbShowSpeed = MutableLiveData<Int>()
    val currentRgbShowSpeed: LiveData<Int>
        get() = _currentRgbShowSpeed

    val MAX_RGB_SHOW_SPEED = 10


    init {
        viewModelScope.launch { loadCurrentSettings() }
    }

    fun onStartStopRgbShowImageViewClick() {
        _rgbShowActive.value?.let { active ->

            if (active) {
                viewModelScope.launch { defaultControllerRepository.stopRgbShow() }
            } else {
                viewModelScope.launch {
                    val defaultSpeed = MAX_RGB_SHOW_SPEED / 2
                    defaultControllerRepository.startRgbShow(currentRgbShowSpeed.value ?: defaultSpeed)
                }
            }

            _rgbShowActive.value = !active
        }
    }

    fun onSpeedSeekBarProgressChanged(progress: Int, fromUser: Boolean) {
        if (!fromUser)
            return

        viewModelScope.launch { defaultControllerRepository.setRgbShowSpeed(progress) }
        _currentRgbShowSpeed.value = progress
    }

    private suspend fun loadCurrentSettings() {
        val currentSettings = defaultControllerRepository.getCurrentSettings()
        _rgbShowActive.value = currentSettings.isRgbShowActive == 1

        // TODO load current rgb show speed as soon it is queryable
        _currentRgbShowSpeed.value = MAX_RGB_SHOW_SPEED / 2
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val defaultControllerRepository: DefaultControllerRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RgbShowViewModel(defaultControllerRepository) as T
    }
}




























