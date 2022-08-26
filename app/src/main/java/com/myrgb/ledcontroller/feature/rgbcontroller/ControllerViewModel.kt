package com.myrgb.ledcontroller.feature.rgbcontroller

import androidx.lifecycle.*
import com.myrgb.ledcontroller.domain.RgbCircle
import com.myrgb.ledcontroller.domain.RgbTriplet
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.acos
import kotlin.math.sqrt

class ControllerViewModel(private val controllerRepository: ControllerRepository) : ViewModel() {
    var rgbCircleCenterX = 0
    var rgbCircleCenterY = 0

    private val _currentlySelectedColor = MutableLiveData<RgbTriplet>()
    val currentlySelectedColor: LiveData<RgbTriplet>
        get() = _currentlySelectedColor

    private val _currentlySelectedBrightness = MutableLiveData<Int>()
    val currentlySelectedBrightness: LiveData<Int>
        get() = _currentlySelectedBrightness

    private val _isSofaLedStripOn = MutableLiveData(false)
    val isSofaLedStripOn: LiveData<Boolean>
        get() = _isSofaLedStripOn

    private val _isBedLedStripOn = MutableLiveData(false)
    val isBedLedStripOn: LiveData<Boolean>
        get() = _isBedLedStripOn

    private val _isDeskLedStripOn = MutableLiveData(false)
    val isDeskLedStripOn: LiveData<Boolean>
        get() = _isDeskLedStripOn

    private val rgbCircle = RgbCircle()

    private val rgbSetColorRequestTimer = Timer()
    private val rgbSetColorRequestTimerInterval = 200L
    private var readyForNextSetColorRgbRequest = true


    init {
        viewModelScope.launch { loadCurrentSettings() }

        rgbSetColorRequestTimer.schedule(object : TimerTask() {
            override fun run() {
                if (!readyForNextSetColorRgbRequest)
                    readyForNextSetColorRgbRequest = true
            }
        }, 0, rgbSetColorRequestTimerInterval)
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
                controllerRepository.setColor(newColor)
            }
            readyForNextSetColorRgbRequest = false
        }
    }

    fun onRgbBulbButtonClick() {
        viewModelScope.launch {
            if (allStripsAreOff()) {
                _isSofaLedStripOn.value = true
                _isBedLedStripOn.value = true
                _isDeskLedStripOn.value = true
                controllerRepository.turnAllLedStripsOn()
            } else {
                _isSofaLedStripOn.value = false
                _isBedLedStripOn.value = false
                _isDeskLedStripOn.value = false
                controllerRepository.turnAllLedStripsOff()
            }
        }
    }

    fun onButtonSofaClick() {
        _isSofaLedStripOn.value?.let { isOn ->
            _isSofaLedStripOn.value = !isOn

            viewModelScope.launch {
                if (isOn) controllerRepository.turnSofaLedStripOff()
                else controllerRepository.turnSofaLedStripOn()
            }
        }
    }

    fun onButtonBedClick() {
        _isBedLedStripOn.value?.let { isOn ->
            _isBedLedStripOn.value = !isOn

            viewModelScope.launch {
                if (isOn) controllerRepository.turnBedLedStripOff()
                else controllerRepository.turnBedLedStripOn()
            }
        }
    }

    fun onButtonDeskClick() {
        _isDeskLedStripOn.value?.let { isOn ->
            _isDeskLedStripOn.value = !isOn

            viewModelScope.launch {
                if (isOn) controllerRepository.turnDeskLedStripOff()
                else controllerRepository.turnDeskLedStripOn()
            }
        }
    }

    fun onBrightnessSeekBarProgressChanged(progress: Int, fromUser: Boolean) {
        if (!fromUser)
            return

        val newBrightness = (progress + 1) * 10
        _currentlySelectedBrightness.value = newBrightness

        viewModelScope.launch {
            controllerRepository.setBrightness(newBrightness)
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

    private suspend fun loadCurrentSettings() {
        val rgbSettingsResponse: RgbSettingsResponse = controllerRepository.getCurrentSettings()

        _currentlySelectedColor.value = RgbTriplet(
            rgbSettingsResponse.redValue,
            rgbSettingsResponse.greenValue,
            rgbSettingsResponse.blueValue
        )

        _currentlySelectedBrightness.value = rgbSettingsResponse.brightness

        rgbSettingsResponse.strips.forEach { strip ->
            when (strip.name) {
                "desk" -> _isDeskLedStripOn.value = strip.isOn == 1
                "bed" -> _isBedLedStripOn.value = strip.isOn == 1
                "sofa" -> _isSofaLedStripOn.value = strip.isOn == 1
            }
        }
    }

    private fun allStripsAreOff() =
        !(_isBedLedStripOn.value ?: false || _isSofaLedStripOn.value ?: false || _isDeskLedStripOn.value ?: false)

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: ControllerRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            (ControllerViewModel(repository)) as T
    }
}




































