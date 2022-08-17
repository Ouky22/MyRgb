package com.example.ledcontroller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ledcontroller.model.RgbAlarm

class AlarmClockViewModel : ViewModel() {
    private val _alarms = MutableLiveData<List<RgbAlarm>>()
    val alarms: LiveData<List<RgbAlarm>>
        get() = _alarms

    init {
        _alarms.value = listOf(
            RgbAlarm(1, 221, false),
            RgbAlarm(1, 1300, false),
            RgbAlarm(1, 1233, true),
            RgbAlarm(1, 1020, false),
            RgbAlarm(1, 1324, true),
            RgbAlarm(1, 1341, true),
            RgbAlarm(1, 1214, false),
        )
    }
}