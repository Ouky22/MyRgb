package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.Esp32
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet
import com.myrgb.ledcontroller.feature.rgbcontroller.RgbSettingsResponse

class FakeRgbRequestRepository(
    private val rgbRequestService: RgbRequestService
) : RgbRequestRepository {

    override suspend fun loadCurrentRgbSettings(esp32: Esp32): RgbSettingsResponse {
        return rgbRequestService.getCurrentSettings(esp32.ipAddress).body() ?: RgbSettingsResponse(
            0, 0, 0, 0, 0, listOf(), 0
        )
    }

    override suspend fun turnOn(esp32: Esp32, rgbStrip: RgbStrip) {}

    override suspend fun turnAllOn(esp32: Esp32) {}

    override suspend fun turnOff(esp32: Esp32, rgbStrip: RgbStrip) {}

    override suspend fun turnAllOff(esp32: Esp32) {}

    override suspend fun setBrightness(esp32: Esp32, brightness: Int) {}

    override suspend fun setColor(esp32: Esp32, color: RgbTriplet) {}

    override suspend fun startRgbShow(esp32: Esp32, speed: Int) {}
}