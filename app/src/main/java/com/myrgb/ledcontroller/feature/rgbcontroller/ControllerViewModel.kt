package com.myrgb.ledcontroller.feature.rgbcontroller

import androidx.lifecycle.*
import com.myrgb.ledcontroller.domain.LedMicrocontroller
import com.myrgb.ledcontroller.domain.RgbCircle
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.network.RgbSettingsResponse
import com.myrgb.ledcontroller.persistence.IpAddressStorage
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.acos
import kotlin.math.sqrt

enum class SettingsLoadingStatus { LOADING, DONE }

class ControllerViewModel(
    private val rgbRequestRepository: RgbRequestRepository,
    private val ipAddressStorage: IpAddressStorage
) : ViewModel() {
    var rgbCircleCenterX = 0
    var rgbCircleCenterY = 0

    val minBrightness = 10
    val maxBrightness = 230

    private val _settingsLoadingStatus = MutableLiveData<SettingsLoadingStatus>()
    val settingsLoadingStatus: LiveData<SettingsLoadingStatus>
        get() = _settingsLoadingStatus

    private val ledMicrocontroller = mutableListOf<LedMicrocontroller>()

    private val _rgbStrips = MutableLiveData<MutableList<RgbStrip>>(mutableListOf())
    val rgbStrips: LiveData<MutableList<RgbStrip>>
        get() = _rgbStrips

    private val _currentlySelectedColor = MutableLiveData<RgbTriplet>()
    val currentlySelectedColor: LiveData<RgbTriplet>
        get() = _currentlySelectedColor

    private val _currentlySelectedBrightness = MutableLiveData<Int>()
    val currentlySelectedBrightness: LiveData<Int>
        get() = _currentlySelectedBrightness

    private val rgbCircle = RgbCircle()

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
            loadCurrentSettings()
        }
    }

    override fun onCleared() {
        super.onCleared()
        rgbSetColorRequestTimer.cancel()
    }

    fun onRgbCircleTouch(touchPositionX: Int, touchPositionY: Int) {
        val angle = computeAngleBetweenTouchAndRgbCircleCenter(touchPositionX, touchPositionY)
        val newColor = rgbCircle.calculateColorAtAngle(angle)
        _currentlySelectedColor.value = newColor

        if (readyForNextSetColorRgbRequest) {
            viewModelScope.launch {
                ledMicrocontroller.forEach { rgbRequestRepository.setColor(it, newColor) }
            }
            readyForNextSetColorRgbRequest = false
        }
    }

    fun onRgbBulbButtonClick() {
        viewModelScope.launch {
            if (allStripsAreOff()) {
                ledMicrocontroller.forEach { microcontroller ->
                    rgbRequestRepository.turnAllStripsOn(microcontroller)
                    microcontroller.rgbStrips.forEach { strip -> strip.enabled = true }
                }
            } else {
                ledMicrocontroller.forEach { microcontroller ->
                    rgbRequestRepository.turnAllStripsOff(microcontroller)
                    microcontroller.rgbStrips.forEach { strip -> strip.enabled = false }
                }
            }
        }
    }

    fun toggleEnabledStatusOf(rgbStrip: RgbStrip) {
        getLedMicrocontrollerOf(rgbStrip)?.let { microcontroller ->
            viewModelScope.launch {
                if (rgbStrip.enabled)
                    rgbRequestRepository.turnStripOff(microcontroller, rgbStrip)
                else
                    rgbRequestRepository.turnStripOn(microcontroller, rgbStrip)

                rgbStrip.enabled = !rgbStrip.enabled
            }
        }
    }

    fun onBrightnessSeekBarProgressChanged(progress: Int, fromUser: Boolean) {
        if (!fromUser)
            return

        val newBrightness = (progress * 10).coerceAtLeast(minBrightness).coerceAtMost(maxBrightness)
        _currentlySelectedBrightness.value = newBrightness

        viewModelScope.launch {
            ledMicrocontroller.forEach { microcontroller ->
                rgbRequestRepository.setBrightness(microcontroller, newBrightness)
            }
        }
    }

    fun loadCurrentSettings() {
        viewModelScope.launch {
            _settingsLoadingStatus.value = SettingsLoadingStatus.LOADING
            _rgbStrips.value?.clear()
            ledMicrocontroller.clear()

            val rgbSettingsResponses = mutableListOf<RgbSettingsResponse>()
            for (ipAddress in ipAddressStorage.getIpAddresses()) {
                val currentSettings = rgbRequestRepository.loadCurrentRgbSettings(ipAddress)
                currentSettings?.let {
                    rgbSettingsResponses.add(it)
                    ledMicrocontroller.add(LedMicrocontroller(ipAddress, it.strips))
                    _rgbStrips.value?.addAll(it.strips)
                }
            }

            _currentlySelectedColor.value =
                rgbSettingsResponses.firstOrNull()?.color ?: RgbTriplet(0, 0, 0)

            _currentlySelectedBrightness.value = rgbSettingsResponses.firstOrNull()?.brightness ?: 0

            _settingsLoadingStatus.value = SettingsLoadingStatus.DONE
        }
    }

    private fun getLedMicrocontrollerOf(strip: RgbStrip): LedMicrocontroller? {
        return ledMicrocontroller.firstOrNull { microcontroller ->
            microcontroller.rgbStrips.firstOrNull { s -> s === strip } != null
        }
    }

    private fun computeAngleBetweenTouchAndRgbCircleCenter(x: Int, y: Int): Int {
        // This method computes the angle (0-359) between a vertical vector (0,-1) starting in the rgb
        // center and a vector between the rgb circle center and the specified position

        // vector from the rgb circle center to the specified position
        val centerPointVectorX = x - rgbCircleCenterX
        val centerPointVectorY = y - rgbCircleCenterY

        // length of the vector from rgb circle center to the specified position
        val centerPointVectorLength =
            sqrt((centerPointVectorX * centerPointVectorX + centerPointVectorY * centerPointVectorY).toDouble())

        // compute the angle between the two vectors
        var angle = acos(-centerPointVectorY / centerPointVectorLength) / Math.PI * 180

        // if the position is left to the center, compute the greater angle
        if (x < rgbCircleCenterX)
            angle = 360 - angle

        return angle.toInt()
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
        private val ipAddressStorage: IpAddressStorage
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (ControllerViewModel(repository, ipAddressStorage)) as T
    }
}




































