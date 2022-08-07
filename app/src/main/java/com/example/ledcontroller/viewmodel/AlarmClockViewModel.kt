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
            Alarm(1, 2021L, 10, false),
            Alarm(1, 202231, 14, false),
            Alarm(1, 20233, 14, true),
            Alarm(1, 2020, 10, false),
            Alarm(1, 202324, 122340, true),
            Alarm(1, 2022341, 12340, true),
            Alarm(1, 2021234, 10, false),
        )
    }
}