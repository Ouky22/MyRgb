package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmDao
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmRepository


class AppContainer(app: App) {
    private val rgbAlarmDao: RgbAlarmDao = app.ledControllerDatabase.rgbAlarmDao

    val rgbAlarmRepository = RgbAlarmRepository(rgbAlarmDao)

    var rgbAlarmContainer: RgbAlarmContainer? = null
}