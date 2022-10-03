package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmRepository
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.RgbAlarmListViewModel

class RgbAlarmContainer(private val rgbAlarmRepository: RgbAlarmRepository) {
    val rgbAlarmListViewModelFactory = RgbAlarmListViewModel.Factory(rgbAlarmRepository)
}