package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.LedMicrocontroller
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet

interface RgbRequestRepository {
    suspend fun loadCurrentRgbSettings(ipAddress: String): RgbSettingsResponse?

    suspend fun turnStripOn(ledMicrocontroller: LedMicrocontroller, rgbStrip: RgbStrip)

    suspend fun turnAllStripsOn(ledMicrocontroller: LedMicrocontroller)

    suspend fun turnStripOff(ledMicrocontroller: LedMicrocontroller, rgbStrip: RgbStrip)

    suspend fun turnAllStripsOff(ledMicrocontroller: LedMicrocontroller)

    suspend fun setBrightness(ledMicrocontroller: LedMicrocontroller, brightness: Int)

    suspend fun setColor(ledMicrocontroller: LedMicrocontroller, color: RgbTriplet)

    suspend fun startRgbShow(ledMicrocontroller: LedMicrocontroller, speed: Int)

    suspend fun setRgbShowSpeed(ledMicrocontroller: LedMicrocontroller, speed: Int)
}