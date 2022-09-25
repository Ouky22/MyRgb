package com.myrgb.ledcontroller.feature.rgbalarmclock

import androidx.lifecycle.*
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.persistence.rgbalarm.DefaultRgbAlarmRepository
import javax.inject.Inject

class RgbAlarmViewModel @Inject constructor(
    private val alarmRepository: DefaultRgbAlarmRepository
) : ViewModel() {

    private val _alarms = MutableLiveData<List<RgbAlarm>>()
    val alarms: LiveData<List<RgbAlarm>>
        get() = _alarms

    init {
        _alarms.value = listOf(
            RgbAlarm(0, 221, false, RgbTriplet(0, 0, 0)),
            RgbAlarm(1, 1300, false, RgbTriplet(0, 0, 0)),
            RgbAlarm(2, 1233, true, RgbTriplet(0, 0, 0)),
            RgbAlarm(3, 1020, false, RgbTriplet(0, 0, 0)),
            RgbAlarm(4, 1324, true, RgbTriplet(0, 0, 0)),
            RgbAlarm(5, 1341, true, RgbTriplet(0, 0, 0)),
            RgbAlarm(6, 1214, false, RgbTriplet(0, 0, 0)),
        )
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val defaultRgbAlarmRepository: DefaultRgbAlarmRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RgbAlarmViewModel(defaultRgbAlarmRepository) as T
    }
}