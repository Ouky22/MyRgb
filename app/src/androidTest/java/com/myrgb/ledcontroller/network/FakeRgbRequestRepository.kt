package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.Esp32Microcontroller
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet

class FakeRgbRequestRepository(
    private val rgbRequestService: RgbRequestService
) : RgbRequestRepository {

    override suspend fun loadCurrentRgbSettings(ipAddress: String): RgbSettingsResponse {
        return rgbRequestService.getCurrentSettings(ipAddress).body() ?: RgbSettingsResponse(
            RgbTriplet(0, 0, 0), 0, false, emptyList()
        )
    }

    override suspend fun turnStripOn(
        esp32Microcontroller: Esp32Microcontroller,
        rgbStrip: RgbStrip
    ) {
    }

    override suspend fun turnAllStripsOn(esp32Microcontroller: Esp32Microcontroller) {}

    override suspend fun turnStripOff(
        esp32Microcontroller: Esp32Microcontroller,
        rgbStrip: RgbStrip
    ) {
    }

    override suspend fun turnAllStripsOff(esp32Microcontroller: Esp32Microcontroller) {}

    override suspend fun setBrightness(
        esp32Microcontroller: Esp32Microcontroller,
        brightness: Int
    ) {
    }

    override suspend fun setColor(esp32Microcontroller: Esp32Microcontroller, color: RgbTriplet) {}

    override suspend fun startRgbShow(esp32Microcontroller: Esp32Microcontroller, speed: Int) {}

    override suspend fun setRgbShowSpeed(esp32Microcontroller: Esp32Microcontroller, speed: Int) {}
}