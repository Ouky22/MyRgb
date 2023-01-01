package com.myrgb.ledcontroller.feature.rgbalarmclock.list

import androidx.lifecycle.*
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
    private val alarmRepository: RgbAlarmRepository
) : ViewModel() {

    private val _alarms = MutableStateFlow<List<RgbAlarm>>(emptyList())
    val alarms: StateFlow<List<RgbAlarm>>
        get() = _alarms

    private val _nextActiveAlarm = MutableLiveData<RgbAlarm?>()
    val nextActiveAlarm: LiveData<RgbAlarm?>
        get() = _nextActiveAlarm

    init {
        viewModelScope.launch {
            alarmRepository.alarms.collectLatest {
                _alarms.value = it
                _nextActiveAlarm.value = alarmRepository.getNextActiveAlarm()
            }
        }
    }

    fun activateRgbAlarm(rgbAlarm: RgbAlarm) {
        viewModelScope.launch {
            alarmRepository.activateRgbAlarm(rgbAlarm)
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
    class Factory(private val rgbAlarmRepository: RgbAlarmRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            RgbAlarmListViewModel(rgbAlarmRepository) as T
    }
}