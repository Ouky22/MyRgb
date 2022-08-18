package com.example.ledcontroller.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.ledcontroller.model.RgbAlarm
import com.example.ledcontroller.repository.RgbAlarmRepository

class RgbAlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val _alarms = MutableLiveData<List<RgbAlarm>>()
    val alarms: LiveData<List<RgbAlarm>>
        get() = _alarms

    private val alarmRepository = RgbAlarmRepository(application)

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

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RgbAlarmViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RgbAlarmViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}