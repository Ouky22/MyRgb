package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.Esp32
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.feature.rgbcontroller.RgbSettingsResponse

interface RgbRequestRepository {
    suspend fun loadCurrentRgbSettings(esp32: Esp32): RgbSettingsResponse?

    suspend fun turnOn(esp32: Esp32, rgbStrip: RgbStrip)

    suspend fun turnAllOn(esp32: Esp32)

    suspend fun turnOff(esp32: Esp32, rgbStrip: RgbStrip)

    suspend fun turnAllOff(esp32: Esp32)

    suspend fun setBrightness(esp32: Esp32, brightness: Int)

    suspend fun setColor(esp32: Esp32, color: RgbTriplet)

    suspend fun startRgbShow(esp32: Esp32, speed: Int)

    suspend fun setRgbShowSpeed(esp32: Esp32, speed: Int)
}