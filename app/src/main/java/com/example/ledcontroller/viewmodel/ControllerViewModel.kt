package com.example.ledcontroller.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ledcontroller.model.RgbCircle
import com.example.ledcontroller.network.CurrentSettingsResponse
import com.example.ledcontroller.network.RgbRequest
import com.example.ledcontroller.network.RgbRequestServiceDesk
import com.example.ledcontroller.network.RgbRequestServiceSofaBed
import com.example.ledcontroller.repository.RgbRequestRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.acos
import kotlin.math.sqrt

class ControllerViewModel : ViewModel() {

    private val rgbCircle = RgbCircle()

    private val rgbRequestRepository = RgbRequestRepository()

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
    }

    fun onRgbCircleTouch(touchPositionX: Int, touchPositionY: Int) {
        val angle = computeAngleBetweenTouchAndRgbCircleCenter(touchPositionX, touchPositionY)
        val color = rgbCircle.computeColorAtAngle(angle)
        _currentlySelectedColor.value = color
        sendRgbRequest(RgbRequest(color.red, color.green, color.blue))
    }

    fun onButtonSofaClick() {
        _isSofaLedStripOn.value?.let { _isSofaLedStripOn.value = !it }
    }

    fun onButtonBedClick() {
        _isBedLedStripOn.value?.let { _isBedLedStripOn.value = !it }
    }

    fun onButtonDeskClick() {
        _isDeskLedStripOn.value?.let { _isDeskLedStripOn.value = !it }
    }

    fun onBrightnessSeekBarProgressChanged(progress: Int, fromUser: Boolean) {
        if (!fromUser)
            return
        _currentlySelectedBrightness.value = progress * 10
    }

    private fun sendRgbRequest(rgbRequest: RgbRequest) {
        viewModelScope.launch {
            try {
                rgbRequestRepository.sendRgbRequest(rgbRequest)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
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
        val currentSettings = try {
            rgbRequestRepository.getCurrentSettings()
        } catch (e: IOException) {
            e.printStackTrace()
            return
        } catch (e: HttpException) {
            e.printStackTrace()
            return
        }

        _currentlySelectedColor.value = RgbCircle.RgbTriplet(
            currentSettings.redValue,
            currentSettings.greenValue,
            currentSettings.blueValue
        )

        _currentlySelectedBrightness.value = currentSettings.brightness

        currentSettings.strips.forEach { strip ->
            when (strip.name) {
                "table" -> _isSofaLedStripOn.value = strip.isOn == 1
                "bed" -> _isBedLedStripOn.value = strip.isOn == 1
                "sofa" -> _isSofaLedStripOn.value = strip.isOn == 1
            }
        }
    }
}




































