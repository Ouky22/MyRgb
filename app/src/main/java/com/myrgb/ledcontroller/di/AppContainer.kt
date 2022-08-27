package com.myrgb.ledcontroller.di

import com.myrgb.ledcontroller.App
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmDao
import com.myrgb.ledcontroller.feature.rgbalarmclock.RgbAlarmRepository
import com.myrgb.ledcontroller.feature.rgbcontroller.DefaultControllerRepository
import com.myrgb.ledcontroller.network.RgbRequestService


private const val esp32DeskBaseUrl = "http://192.168.1.249/"
private const val esp32SofaBedBaseUrl = "http://192.168.1.250/"

class AppContainer(app: App) {

    private val rgbRequestServiceDesk: RgbRequestService by lazy {
        RgbRequestService.create(esp32DeskBaseUrl)
    }
    private val rgbRequestServiceBedSofa: RgbRequestService by lazy {
        RgbRequestService.create(esp32SofaBedBaseUrl)
    }
    private val rgbAlarmDao: RgbAlarmDao = app.ledControllerDatabase.rgbAlarmDao

    val defaultControllerRepository = DefaultControllerRepository(rgbRequestServiceDesk, rgbRequestServiceBedSofa)
    val rgbAlarmRepository = RgbAlarmRepository(rgbAlarmDao)

    var controllerContainer: ControllerContainer? = null
    var rgbShowContainer: RgbShowContainer? = null
    var rgbAlarmContainer: RgbAlarmContainer? = null
}