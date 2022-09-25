package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.persistence.rgbalarm.DefaultRgbAlarmRepository
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmViewModel

class RgbAlarmContainer(private val defaultRgbAlarmRepository: DefaultRgbAlarmRepository) {
    val rgbAlarmViewModelFactory = RgbAlarmViewModel.Factory(defaultRgbAlarmRepository)
}