package com.myrgb.ledcontroller.feature.rgbcontroller

import androidx.lifecycle.*
import com.myrgb.ledcontroller.domain.Esp32Microcontroller
import com.myrgb.ledcontroller.domain.RgbCircle
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.network.RgbRequestRepository
import com.myrgb.ledcontroller.network.RgbSettingsResponse
import com.myrgb.ledcontroller.network.esp32DeskIpAddress
import com.myrgb.ledcontroller.network.esp32SofaBedIpAddress
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.acos
import kotlin.math.sqrt

enum class SettingsLoadingStatus { LOADING, DONE }

class ControllerViewModel(private val rgbRequestRepository: RgbRequestRepository) : ViewModel() {
    var rgbCircleCenterX = 0
    var rgbCircleCenterY = 0

    val minBrightness = 10
    val maxBrightness = 230

    private val _settingsLoadingStatus = MutableLiveData<SettingsLoadingStatus>()
    val settingsLoadingStatus: LiveData<SettingsLoadingStatus>
        get() = _settingsLoadingStatus

    private val esp32Microcontroller = mutableListOf<Esp32Microcontroller>()

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
                esp32Microcontroller.forEach { rgbRequestRepository.setColor(it, newColor) }
            }
            readyForNextSetColorRgbRequest = false
        }
    }

    fun onRgbBulbButtonClick() {
        viewModelScope.launch {
            if (allStripsAreOff()) {
                esp32Microcontroller.forEach { esp32 ->
                    rgbRequestRepository.turnAllStripsOn(esp32)
                    esp32.rgbStrips.forEach { strip -> strip.enabled = true }
                }
            } else {
                esp32Microcontroller.forEach { esp32 ->
                    rgbRequestRepository.turnAllStripsOff(esp32)
                    esp32.rgbStrips.forEach { strip -> strip.enabled = false }
                }
            }
        }
    }

    fun toggleEnabledStatusOf(rgbStrip: RgbStrip) {
        getEsp32MicrocontrollerOf(rgbStrip)?.let { esp32 ->
            viewModelScope.launch {
                if (rgbStrip.enabled)
                    rgbRequestRepository.turnStripOff(esp32, rgbStrip)
                else
                    rgbRequestRepository.turnStripOn(esp32, rgbStrip)

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
            esp32Microcontroller.forEach { esp32 ->
                rgbRequestRepository.setBrightness(esp32, newBrightness)
            }
        }
    }

    fun loadCurrentSettings() {
        viewModelScope.launch {
            _settingsLoadingStatus.value = SettingsLoadingStatus.LOADING

            // TODO don't hardcode ip addresses and instead load them from a storage
            val esp32IpAddresses = mutableListOf(esp32DeskIpAddress, esp32SofaBedIpAddress)
            val rgbSettingsResponses = mutableListOf<RgbSettingsResponse>()

            _rgbStrips.value?.clear()
            esp32Microcontroller.clear()

            for (ipAddress in esp32IpAddresses) {
                val currentSettings = rgbRequestRepository.loadCurrentRgbSettings(ipAddress)
                currentSettings?.let {
                    rgbSettingsResponses.add(it)
                    esp32Microcontroller.add(Esp32Microcontroller(ipAddress, it.strips))
                    _rgbStrips.value?.addAll(it.strips)
                }
            }

            _currentlySelectedColor.value =
                rgbSettingsResponses.firstOrNull()?.color ?: RgbTriplet(0, 0, 0)

            _currentlySelectedBrightness.value = rgbSettingsResponses.firstOrNull()?.brightness ?: 0

            _settingsLoadingStatus.value = SettingsLoadingStatus.DONE
        }
    }

    private fun getEsp32MicrocontrollerOf(strip: RgbStrip): Esp32Microcontroller? {
        return esp32Microcontroller.firstOrNull { esp32 ->
            esp32.rgbStrips.firstOrNull { s -> s === strip } != null
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
        esp32Microcontroller.forEach { esp32 ->
            esp32.rgbStrips.forEach { strip ->
                if (strip.enabled)
                    return false
            }
        }
        return true
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: RgbRequestRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (ControllerViewModel(repository)) as T
    }
}




































