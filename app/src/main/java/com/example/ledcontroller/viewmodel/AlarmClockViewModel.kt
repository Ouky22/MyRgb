package com.example.ledcontroller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ledcontroller.persistence.Alarm
import java.sql.Date
import java.sql.Time

class AlarmClockViewModel : ViewModel() {
    private val _alarms = MutableLiveData<List<Alarm>>()
    val alarms: LiveData<List<Alarm>>
        get() = _alarms

    init {
        _alarms.value = listOf(
            Alarm(1, 221, false),
            Alarm(1, 1300, false),
            Alarm(1, 1233, true),
            Alarm(1, 1020, false),
            Alarm(1, 1324, true),
            Alarm(1, 1341, true),
            Alarm(1, 1214, false),
        )
    }
}