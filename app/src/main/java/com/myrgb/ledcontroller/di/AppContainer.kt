package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmDao
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmRepository
import com.myrgb.ledcontroller.network.DefaultRgbRequestRepository
import com.myrgb.ledcontroller.network.RgbRequestService
import com.myrgb.ledcontroller.persistence.DefaultIpAddressStorage


class AppContainer(app: App) {
    private val rgbAlarmDao: RgbAlarmDao = app.ledControllerDatabase.rgbAlarmDao

    val rgbAlarmRepository = RgbAlarmRepository(rgbAlarmDao)

    var rgbAlarmContainer: RgbAlarmContainer? = null
}