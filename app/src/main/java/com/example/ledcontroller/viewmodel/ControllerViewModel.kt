package com.example.ledcontroller.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ledcontroller.model.RgbCircle
import com.example.ledcontroller.repository.RgbRequestRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.acos
import kotlin.math.sqrt

class ControllerViewModel : ViewModel() {

    private val rgbCircle = RgbCircle()

    private val rgbRequestRepository = RgbRequestRepository()

    private val rgbSetColorRequestTimer = Timer()
    private val rgbSetColorRequestTimerInterval = 200L
    private var readyForNextSetColorRgbRequest = true

    var rgbCircleCenterX = 0
    var rgbCircleCenterY = 0

    private val _currentlySelectedColor = MutableLiveData<RgbCircle.RgbTriplet>()
    val currentlySelectedColor = _currentlySelectedColor

    private val _currentlySelectedBrightness = MutableLiveData<Int>()
    val currentlySelectedBrightness = _currentlySelectedBrightness

    private val _isSofaLedStripOn = MutableLiveData(false)
    val isSofaLedStripOn = _isSofaLedStripOn
    private val _isBedLedStripOn = MutableLiveData(false)
    val isBedLedStripOn = _isBedLedStripOn
    private val _isDeskLedStripOn = MutableLiveData(false)
    val isDeskLedStripOn = _isDeskLedStripOn


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
        val newColor = rgbCircle.computeColorAtAngle(angle)
        _currentlySelectedColor.value = newColor

        if (readyForNextSetColorRgbRequest) {
            viewModelScope.launch {
                rgbRequestRepository.setColor(newColor)
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
                rgbRequestRepository.turnAllLedStripsOn()
            } else {
                _isSofaLedStripOn.value = false
                _isBedLedStripOn.value = false
                _isDeskLedStripOn.value = false
                rgbRequestRepository.turnAllLedStripsOff()
            }
        }
    }

    fun onButtonSofaClick() {
        _isSofaLedStripOn.value?.let { isOn ->
            _isSofaLedStripOn.value = !isOn

            viewModelScope.launch {
                if (isOn) rgbRequestRepository.turnSofaLedStripOff()
                else rgbRequestRepository.turnSofaLedStripOn()
            }
        }
    }

    fun onButtonBedClick() {
        _isBedLedStripOn.value?.let { isOn ->
            _isBedLedStripOn.value = !isOn

            viewModelScope.launch {
                if (isOn) rgbRequestRepository.turnBedLedStripOff()
                else rgbRequestRepository.turnBedLedStripOn()
            }
        }
    }

    fun onButtonDeskClick() {
        _isDeskLedStripOn.value?.let { isOn ->
            _isDeskLedStripOn.value = !isOn

            viewModelScope.launch {
                if (isOn) rgbRequestRepository.turnDeskLedStripOff()
                else rgbRequestRepository.turnDeskLedStripOn()
            }
        }
    }

    fun onBrightnessSeekBarProgressChanged(progress: Int, fromUser: Boolean) {
        if (!fromUser)
            return

        val newBrightness = (progress + 1) * 10
        _currentlySelectedBrightness.value = newBrightness

        viewModelScope.launch {
            rgbRequestRepository.setBrightness(newBrightness)
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
        val currentSettings = rgbRequestRepository.getCurrentSettings()

        _currentlySelectedColor.value = RgbCircle.RgbTriplet(
            currentSettings.redValue,
            currentSettings.greenValue,
            currentSettings.blueValue
        )

        _currentlySelectedBrightness.value = currentSettings.brightness

        currentSettings.strips.forEach { strip ->
            when (strip.name) {
                "desk" -> _isDeskLedStripOn.value = strip.isOn == 1
                "bed" -> _isBedLedStripOn.value = strip.isOn == 1
                "sofa" -> _isSofaLedStripOn.value = strip.isOn == 1
            }
        }
    }

    private fun allStripsAreOff() =
        !(_isBedLedStripOn.value ?: false || _isSofaLedStripOn.value ?: false || _isDeskLedStripOn.value ?: false)
}




































