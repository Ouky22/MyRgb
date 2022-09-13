package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.LedMicrocontroller
import com.myrgb.ledcontroller.domain.RgbStrip
import com.myrgb.ledcontroller.domain.RgbTriplet

class FakeRgbRequestRepository(
    var ipAddressRgbSettingsMap: HashMap<String, RgbSettingsResponse> = hashMapOf()
) : RgbRequestRepository {

    override suspend fun loadCurrentRgbSettings(ipAddress: String): RgbSettingsResponse {
        return ipAddressRgbSettingsMap[ipAddress]!!
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