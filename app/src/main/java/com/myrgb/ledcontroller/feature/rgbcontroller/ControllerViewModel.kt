package com.myrgb.ledcontroller.feature.rgbcontroller

import androidx.lifecycle.*
import com.myrgb.ledcontroller.IpAddressSettings
import com.myrgb.ledcontroller.domain.LedMicrocontroller
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.network.RgbSettingsResponse
import com.myrgb.ledcontroller.persistence.ipaddress.IpAddressSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

enum class SettingsLoadingStatus { LOADING, DONE }

class ControllerViewModel @Inject constructor(
    private val rgbRequestRepository: RgbRequestRepository,
    private val ipAddressSettingsRepository: IpAddressSettingsRepository
) : ViewModel() {

    private val _settingsLoadingStatus = MutableLiveData<SettingsLoadingStatus>()
    val settingsLoadingStatus: LiveData<SettingsLoadingStatus>
        get() = _settingsLoadingStatus

    private val ledMicrocontroller = mutableListOf<LedMicrocontroller>()

    private val _rgbStrips: MutableStateFlow<List<RgbStrip>> = MutableStateFlow(mutableListOf())
    val rgbStrips: StateFlow<List<RgbStrip>>
        get() = _rgbStrips

    private val _currentlySelectedColor = MutableLiveData<RgbTriplet>()
    val currentlySelectedColor: LiveData<RgbTriplet>
        get() = _currentlySelectedColor

    private val rgbSetColorRequestTimer = Timer()
    private val rgbSetColorRequestTimerInterval = 200L
    private var readyForNextSetColorRgbRequest = true


    init {
        rgbSetColorRequestTimer.schedule(object : TimerTask() {
            override fun run() {
                if (!readyForNextSetColorRgbRequest)
                    readyForNextSetColorRgbRequest = true
            }
        }, 0, rgbSetColorRequestTimerInterval)

        viewModelScope.launch {
            ipAddressSettingsRepository.ipAddressSettings.collect { loadCurrentSettings(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        rgbSetColorRequestTimer.cancel()
    }

    fun onColorChange(newColor: RgbTriplet) {
        _currentlySelectedColor.value = newColor

        if (readyForNextSetColorRgbRequest) {
            viewModelScope.launch {
                ledMicrocontroller.forEach { rgbRequestRepository.setColor(it, newColor) }
            }
            readyForNextSetColorRgbRequest = false
        }
    }

    fun onAllStripsOffOnButtonClick() {
        if (allStripsAreOff()) {
            setEnabledStatusOfAllStripsToTrue()
            viewModelScope.launch {
                ledMicrocontroller.forEach { controller ->
                    rgbRequestRepository.turnAllStripsOn(controller)
                }
            }
        } else {
            setEnabledStatusOfAllStripsToFalse()
            viewModelScope.launch {
                ledMicrocontroller.forEach { controller ->
                    rgbRequestRepository.turnAllStripsOff(controller)
                }
            }
        }
    }

    fun toggleEnabledStatusOf(rgbStrip: RgbStrip) {
        rgbStrip.enabled = !rgbStrip.enabled

        getLedMicrocontrollerOf(rgbStrip)?.let { microcontroller ->
            viewModelScope.launch {
                if (rgbStrip.enabled)
                    rgbRequestRepository.turnStripOn(microcontroller, rgbStrip)
                else
                    rgbRequestRepository.turnStripOff(microcontroller, rgbStrip)
            }
        }
    }

    private fun loadCurrentSettings(ipAddressSettings: IpAddressSettings) {
        viewModelScope.launch {
            _settingsLoadingStatus.value = SettingsLoadingStatus.LOADING

            val rgbSettingsResponses = mutableListOf<RgbSettingsResponse>()
            ledMicrocontroller.clear()

            for (ipAddressNamePair in ipAddressSettings.ipAddressNamePairsList) {
                val currentSettings =
                    rgbRequestRepository.loadCurrentRgbSettings(ipAddressNamePair.ipAddress)
                currentSettings?.let {
                    rgbSettingsResponses.add(it)
                    ledMicrocontroller.add(
                        LedMicrocontroller(ipAddressNamePair.ipAddress, it.strips)
                    )
                }
            }

            _rgbStrips.value = ledMicrocontroller.flatMap { it.rgbStrips }

            _currentlySelectedColor.value =
                rgbSettingsResponses.firstOrNull()?.color ?: RgbTriplet(0, 0, 0)

            _settingsLoadingStatus.value = SettingsLoadingStatus.DONE
        }
    }

    private fun getLedMicrocontrollerOf(strip: RgbStrip): LedMicrocontroller? {
        return ledMicrocontroller.firstOrNull { microcontroller ->
            microcontroller.rgbStrips.firstOrNull { s -> s === strip } != null
        }
    }

    private fun setEnabledStatusOfAllStripsToFalse() {
        ledMicrocontroller.forEach { controller ->
            controller.rgbStrips.forEach { it.enabled = false }
        }
    }

    private fun setEnabledStatusOfAllStripsToTrue() {
        ledMicrocontroller.forEach { controller ->
            controller.rgbStrips.forEach { it.enabled = true }
        }
    }

    private fun allStripsAreOff(): Boolean {
        ledMicrocontroller.forEach { microcontroller ->
            microcontroller.rgbStrips.forEach { strip ->
                if (strip.enabled)
                    return false
            }
        }
        return true
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: RgbRequestRepository,
        private val ipAddressSettingsRepository: IpAddressSettingsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (ControllerViewModel(repository, ipAddressSettingsRepository)) as T
    }
}




































