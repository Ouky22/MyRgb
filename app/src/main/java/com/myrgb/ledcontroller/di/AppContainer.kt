package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmDao
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmRepository
import com.myrgb.ledcontroller.network.DefaultRgbRequestRepository
import com.myrgb.ledcontroller.network.RgbRequestService
import com.myrgb.ledcontroller.persistence.IpAddressStorage


class AppContainer(app: App) {

    private val rgbRequestService: RgbRequestService by lazy {
        RgbRequestService.create()
    }
    private val rgbAlarmDao: RgbAlarmDao = app.ledControllerDatabase.rgbAlarmDao

    val ipAddressStorage = IpAddressStorage(app)

    val rgbRequestRepository = DefaultRgbRequestRepository(rgbRequestService)

    val rgbAlarmRepository = RgbAlarmRepository(rgbAlarmDao)

    var controllerContainer: ControllerContainer? = null
    var rgbShowContainer: RgbShowContainer? = null
    var rgbAlarmContainer: RgbAlarmContainer? = null
}