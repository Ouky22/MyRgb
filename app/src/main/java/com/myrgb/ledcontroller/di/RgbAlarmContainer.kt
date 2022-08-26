package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmRepository
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmViewModel

class RgbAlarmContainer(private val rgbAlarmRepository: RgbAlarmRepository) {
    val rgbAlarmViewModelFactory = RgbAlarmViewModel.Factory(rgbAlarmRepository)
}