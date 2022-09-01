package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmDao
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmRepository
import com.myrgb.ledcontroller.feature.rgbcontroller.DefaultControllerRepository
import com.myrgb.ledcontroller.network.RgbRequestService


class AppContainer(app: App) {

    private val rgbRequestServiceDesk: RgbRequestService by lazy {
        RgbRequestService.create()
    }
    private val rgbAlarmDao: RgbAlarmDao = app.ledControllerDatabase.rgbAlarmDao

    val defaultControllerRepository = DefaultControllerRepository(rgbRequestServiceDesk)
    val rgbAlarmRepository = RgbAlarmRepository(rgbAlarmDao)

    var controllerContainer: ControllerContainer? = null
    var rgbShowContainer: RgbShowContainer? = null
    var rgbAlarmContainer: RgbAlarmContainer? = null
}