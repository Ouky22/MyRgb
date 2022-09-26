package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.persistence.rgbalarm.DefaultRgbAlarmRepository
import com.myrgb.ledcontroller.feature.rgbalarmclock.list.RgbAlarmListViewModel

class RgbAlarmContainer(private val defaultRgbAlarmRepository: DefaultRgbAlarmRepository) {
    val rgbAlarmListViewModelFactory = RgbAlarmListViewModel.Factory(defaultRgbAlarmRepository)
}