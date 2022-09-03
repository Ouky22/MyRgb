package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.Esp32Microcontroller
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet

interface RgbRequestRepository {
    suspend fun loadCurrentRgbSettings(ipAddress: String): RgbSettingsResponse?

    suspend fun turnStripOn(esp32Microcontroller: Esp32Microcontroller, rgbStrip: RgbStrip)

    suspend fun turnAllStripsOn(esp32Microcontroller: Esp32Microcontroller)

    suspend fun turnStripOff(esp32Microcontroller: Esp32Microcontroller, rgbStrip: RgbStrip)

    suspend fun turnAllStripsOff(esp32Microcontroller: Esp32Microcontroller)

    suspend fun setBrightness(esp32Microcontroller: Esp32Microcontroller, brightness: Int)

    suspend fun setColor(esp32Microcontroller: Esp32Microcontroller, color: RgbTriplet)

    suspend fun startRgbShow(esp32Microcontroller: Esp32Microcontroller, speed: Int)

    suspend fun setRgbShowSpeed(esp32Microcontroller: Esp32Microcontroller, speed: Int)
}