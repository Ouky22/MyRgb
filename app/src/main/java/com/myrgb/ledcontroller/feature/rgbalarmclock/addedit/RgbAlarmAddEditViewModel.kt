package com.myrgb.ledcontroller.feature.rgbalarmclock.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbCircle
import com.myrgb.ledcontroller.domain.Weekday
import com.myrgb.ledcontroller.domain.util.computeAngleInCircle
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository
import com.myrgb.ledcontroller.persistence.util.asDomainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RgbAlarmAddEditViewModel @Inject constructor(
    private val rgbAlarmRepository: RgbAlarmRepository
) : ViewModel() {
    var rgbCircleCenterX = 0
    var rgbCircleCenterY = 0
    private val rgbCircle = RgbCircle()

    private val _rgbAlarmToAddOrEdit = MutableStateFlow(RgbAlarm.defaultRgbAlarmInstance)
    val rgbAlarmToAddOrEdit: StateFlow<RgbAlarm>
        get() = _rgbAlarmToAddOrEdit

    private var initialTimeOfEditedRgbAlarm = -1

    private val inEditingMode
        get() = initialTimeOfEditedRgbAlarm > -1

    fun setRgbAlarmForEditing(timeMinutesOfDay: Int) {
        viewModelScope.launch {
            try {
                _rgbAlarmToAddOrEdit.value =
                    rgbAlarmRepository.getByTime(timeMinutesOfDay).asDomainModel()
                initialTimeOfEditedRgbAlarm = _rgbAlarmToAddOrEdit.value.timeMinutesOfDay
            } catch (e: NoSuchElementException) {
                e.printStackTrace()
            }
        }
    }

    fun saveRgbAlarm() {
        viewModelScope.launch {
            val rgbAlarmTimeChanged =
                _rgbAlarmToAddOrEdit.value.timeMinutesOfDay != initialTimeOfEditedRgbAlarm

            if (inEditingMode && rgbAlarmTimeChanged) {
                rgbAlarmRepository.deleteByTime(initialTimeOfEditedRgbAlarm)
                rgbAlarmRepository.insertOrUpdate(_rgbAlarmToAddOrEdit.value)
            } else // when new alarm added or edited alarm has same time
                rgbAlarmRepository.insertOrUpdate(_rgbAlarmToAddOrEdit.value)
        }
    }

    fun setTime(timeMinutesOfDay: Int) {
        _rgbAlarmToAddOrEdit.value = _rgbAlarmToAddOrEdit.value.copy(
            timeMinutesOfDay = timeMinutesOfDay
        )
    }

    fun makeRepetitiveOn(weekday: Weekday) {
        _rgbAlarmToAddOrEdit.value = _rgbAlarmToAddOrEdit.value.apply {
            makeRepetitiveOn(weekday)
        }
    }

    fun makeNotRepetitiveOn(weekday: Weekday) {
        _rgbAlarmToAddOrEdit.value = _rgbAlarmToAddOrEdit.value.apply {
            makeNotRepetitiveOn(weekday)
        }
    }

    fun setAlarmColorOnRgbCircleTouch(touchPositionX: Int, touchPositionY: Int) {
        val angle =
            computeAngleInCircle(rgbCircleCenterX, rgbCircleCenterY, touchPositionX, touchPositionY)
        val newColor = rgbCircle.calculateColorAtAngle(angle)

        _rgbAlarmToAddOrEdit.value = _rgbAlarmToAddOrEdit.value.copy(
            color = newColor
        )
    }

}











































