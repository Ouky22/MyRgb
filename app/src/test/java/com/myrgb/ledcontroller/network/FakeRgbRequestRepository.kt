package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.LedMicrocontroller
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
        ledMicrocontroller: LedMicrocontroller,
        rgbStrip: RgbStrip
    ) {
    }

    override suspend fun turnAllStripsOn(ledMicrocontroller: LedMicrocontroller) {}

    override suspend fun turnStripOff(
        ledMicrocontroller: LedMicrocontroller,
        rgbStrip: RgbStrip
    ) {
    }

    override suspend fun turnAllStripsOff(ledMicrocontroller: LedMicrocontroller) {}

    override suspend fun setBrightness(
        ledMicrocontroller: LedMicrocontroller,
        brightness: Int
    ) {
    }

    override suspend fun setColor(ledMicrocontroller: LedMicrocontroller, color: RgbTriplet) {}

    override suspend fun startRgbShow(ledMicrocontroller: LedMicrocontroller, speed: Int) {}

    override suspend fun setRgbShowSpeed(ledMicrocontroller: LedMicrocontroller, speed: Int) {}
}