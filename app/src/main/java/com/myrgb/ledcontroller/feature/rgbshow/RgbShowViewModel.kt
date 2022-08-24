package com.myrgb.ledcontroller.feature.rgbshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrgb.ledcontroller.feature.rgbcontroller.ControllerRepository
import kotlinx.coroutines.launch

class RgbShowViewModel : ViewModel() {
    private val _rgbShowActive = MutableLiveData<Boolean>()
    val rgbShowActive: LiveData<Boolean>
        get() = _rgbShowActive

    private val _currentRgbShowSpeed = MutableLiveData<Int>()
    val currentRgbShowSpeed: LiveData<Int>
        get() = _currentRgbShowSpeed

    private val controllerRepository = ControllerRepository()

    val MAX_RGB_SHOW_SPEED = 10


    init {
        viewModelScope.launch { loadCurrentSettings() }
    }

    fun onStartStopRgbShowImageViewClick() {
        _rgbShowActive.value?.let { active ->

            if (active) {
                viewModelScope.launch { controllerRepository.stopRgbShow() }
            } else {
                viewModelScope.launch {
                    val defaultSpeed = MAX_RGB_SHOW_SPEED / 2
                    controllerRepository.startRgbShow(currentRgbShowSpeed.value ?: defaultSpeed)
                }
            }

            _rgbShowActive.value = !active
        }
    }

    fun onSpeedSeekBarProgressChanged(progress: Int, fromUser: Boolean) {
        if (!fromUser)
            return

        viewModelScope.launch { controllerRepository.setRgbShowSpeed(progress) }
        _currentRgbShowSpeed.value = progress
    }

    private suspend fun loadCurrentSettings() {
        val currentSettings = controllerRepository.getCurrentSettings()
        _rgbShowActive.value = currentSettings.isRgbShowActive == 1

        // TODO load current rgb show speed as soon it is queryable
        _currentRgbShowSpeed.value = MAX_RGB_SHOW_SPEED / 2
    }
}




























