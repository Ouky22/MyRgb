package com.example.ledcontroller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ledcontroller.repository.RgbRequestRepository
import kotlinx.coroutines.launch

class RgbShowViewModel : ViewModel() {
    private val rgbRequestRepository = RgbRequestRepository()

    private val _rgbShowActive = MutableLiveData<Boolean>()
    val rgbShowActive: LiveData<Boolean> = _rgbShowActive

    private val _currentRgbShowSpeed = MutableLiveData<Int>()
    val currentRgbShowSpeed: LiveData<Int> = _currentRgbShowSpeed

    val MAX_RGB_SHOW_SPEED = 10


    init {
        viewModelScope.launch { loadCurrentSettings() }
    }

    fun onStartStopRgbShowImageViewClick() {
        _rgbShowActive.value?.let { active ->

            if (active) {
                viewModelScope.launch { rgbRequestRepository.stopRgbShow() }
            } else {
                viewModelScope.launch {
                    val defaultSpeed = MAX_RGB_SHOW_SPEED / 2
                    rgbRequestRepository.startRgbShow(currentRgbShowSpeed.value ?: defaultSpeed)
                }
            }

            _rgbShowActive.value = !active
        }
    }

    fun onSpeedSeekBarProgressChanged(progress: Int, fromUser: Boolean) {
        if (!fromUser)
            return

        viewModelScope.launch { rgbRequestRepository.setRgbShowSpeed(progress) }
        _currentRgbShowSpeed.value = progress
    }

    private suspend fun loadCurrentSettings() {
        val currentSettings = rgbRequestRepository.getCurrentSettings()
        _rgbShowActive.value = currentSettings.isRgbShowActive == 1

        // TODO load current rgb show speed as soon it is queryable
        _currentRgbShowSpeed.value = MAX_RGB_SHOW_SPEED / 2
    }
}




























