package com.myrgb.ledcontroller.feature.rgbalarmclock.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.myrgb.ledcontroller.domain.RgbAlarm
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmScheduler
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RgbAlarmListViewModel @Inject constructor(
    private val alarmRepository: RgbAlarmRepository,
    private val rgbAlarmScheduler: RgbAlarmScheduler
) : ViewModel() {

    private val _alarms = MutableStateFlow<List<RgbAlarm>>(emptyList())
    val alarms: StateFlow<List<RgbAlarm>>
        get() = _alarms

    init {
        viewModelScope.launch {
            alarmRepository.alarms.collectLatest {
                _alarms.value = it
            }
        }

//        _alarms.value = listOf(
//            RgbAlarm(221, false, RgbTriplet(0, 0, 0)),
//            RgbAlarm(1300, false, RgbTriplet(0, 0, 0)),
//            RgbAlarm(1233, true, RgbTriplet(0, 0, 0)),
//            RgbAlarm(1020, false, RgbTriplet(0, 0, 0)),
//            RgbAlarm(1324, true, RgbTriplet(0, 0, 0)),
//            RgbAlarm(1341, true, RgbTriplet(0, 0, 0)),
//            RgbAlarm(1214, false, RgbTriplet(0, 0, 0)),
//        )
//        runBlocking {
//            _alarms.value?.forEach {
//                alarmRepository.insertOrUpdate(it)
//            }
//        }
    }

    fun activateRgbAlarm(rgbAlarm: RgbAlarm) {
        viewModelScope.launch {
            alarmRepository.activateRgbAlarm(rgbAlarm)
            rgbAlarmScheduler.scheduleNextAlarm()
        }
    }

    fun deactivateRgbAlarm(rgbAlarm: RgbAlarm) {
        viewModelScope.launch {
            alarmRepository.deactivateRgbAlarm(rgbAlarm)
        }
    }

    fun deleteRgbAlarms(rgbAlarms: List<RgbAlarm>) {
        viewModelScope.launch {
            alarmRepository.delete(rgbAlarms)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val rgbAlarmRepository: RgbAlarmRepository,
        private val rgbAlarmScheduler: RgbAlarmScheduler
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RgbAlarmListViewModel(rgbAlarmRepository, rgbAlarmScheduler) as T
    }
}