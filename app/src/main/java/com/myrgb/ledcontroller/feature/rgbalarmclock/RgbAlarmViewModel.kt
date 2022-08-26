package com.myrgb.ledcontroller.feature.rgbalarmclock

import androidx.lifecycle.*
import com.myrgb.ledcontroller.domain.RgbAlarm

class RgbAlarmViewModel(private val alarmRepository: RgbAlarmRepository) : ViewModel() {
    private val _alarms = MutableLiveData<List<RgbAlarm>>()
    val alarms: LiveData<List<RgbAlarm>>
        get() = _alarms

    init {
        _alarms.value = listOf(
            RgbAlarm(1, 221, false, 0, 0, 0),
            RgbAlarm(1, 1300, false, 0, 0, 0),
            RgbAlarm(1, 1233, true, 0, 0, 0),
            RgbAlarm(1, 1020, false, 0, 0, 0),
            RgbAlarm(1, 1324, true, 0, 0, 0),
            RgbAlarm(1, 1341, true, 0, 0, 0),
            RgbAlarm(1, 1214, false, 0, 0, 0),
        )
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val rgbAlarmRepository: RgbAlarmRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RgbAlarmViewModel(rgbAlarmRepository) as T
    }
}