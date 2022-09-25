package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.persistence.rgbalarm.RgbAlarmDao
import com.myrgb.ledcontroller.persistence.rgbalarm.DefaultRgbAlarmRepository


class AppContainer(app: App) {
    private val rgbAlarmDao: RgbAlarmDao = app.ledControllerDatabase.rgbAlarmDao

    val defaultRgbAlarmRepository = DefaultRgbAlarmRepository(rgbAlarmDao)

    var rgbAlarmContainer: RgbAlarmContainer? = null
}