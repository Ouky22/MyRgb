package com.example.ledcontroller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ledcontroller.model.Alarm
import java.sql.Date
import java.sql.Time

class AlarmClockViewModel : ViewModel() {
    private val _alarms = MutableLiveData<List<Alarm>>()
    val alarms: LiveData<List<Alarm>> = _alarms

    init {
        _alarms.value = listOf(
            Alarm(1, Date(2021), Time(10), false),
            Alarm(1, Date(202231), Time(14), false),
            Alarm(1, Date(202331), Time(14), true),
            Alarm(1, Date(2020), Time(10), false),
            Alarm(1, Date(202324), Time(122340), true),
            Alarm(1, Date(2022341), Time(12340), true),
            Alarm(1, Date(2021234), Time(10), false),
        )
    }
}